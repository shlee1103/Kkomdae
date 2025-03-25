# flask_app.py

# 필요한 라이브러리 임포트
from flask import Flask, request, jsonify   # Flask 웹 프레임워크 및 JSON 응답을 위한 모듈
import os
from dotenv import load_dotenv              # 환경 변수 로딩
import boto3                                # AWS S3 연동을 위한 boto3 라이브러리 (최신 버전)
from io import BytesIO
from PIL import Image
import tempfile
load_dotenv()

# AWS 관련 설정 로드
aws_access_key_id = os.getenv('AWS_ACCESS_KEY_ID')
aws_secret_access_key = os.getenv('AWS_SECRET_ACCESS_KEY')
region_name = os.getenv('AWS_REGION')
bucket_name = os.getenv('BUCKET_NAME')
folder = os.getenv('S3_PREFIX')

# Flask 애플리케이션 생성
app = Flask(__name__)

# S3 클라이언트 생성
s3_client = boto3.client(
    's3',
    aws_access_key_id=aws_access_key_id,
    aws_secret_access_key=aws_secret_access_key,
    region_name=region_name
)

@app.route('/analyze', methods=['POST'])
def analyze():
    """
    클라이언트로부터 JSON 형태의 데이터를 수신하여:
      1) s3_key (예: "lighting.png")를 읽는다.
      2) s3_key 로 S3에서 직접 해당 파일을 다운로드해 로컬에 저장한다.
      3) 다운로드된 로컬 파일을 Pillow로 열어 분석(예: 흑백 변환).
      4) 분석 결과물(analyzed_{원본파일명}.png)을 S3에 업로드.
      5) 업로드된 파일의 presigned URL을 생성해 JSON 형태로 반환.
    """
    data = request.get_json()
    if not data:
        return jsonify({"error": "JSON 데이터가 제공되지 않았습니다."}), 400
    
    try:
        # JSON 데이터는 { "<원본파일이름>": "<이미지URL or 다른 메타정보>" } 형태라고 가정.
        # 예) { "lighting.png": "https://..."} 처럼 key와 value가 한 쌍이라고 가정
        s3_key, image_url = list(data.items())[0]
    except Exception as e:
        return jsonify({"error": "JSON 형식이 올바르지 않습니다.", "detail": str(e)}), 400

    # 1) S3에서 파일을 로컬로 다운로드
    temp_dir = tempfile.gettempdir()
    local_download_path = os.path.join(temp_dir, s3_key)
    try:
        s3_client.download_file(
            Bucket=bucket_name,
            Key=folder + s3_key,   # 폴더 + 파일명
            Filename=local_download_path
        )
    except Exception as e:
        return jsonify({"error": "S3 파일 다운로드 실패", "detail": str(e)}), 400

    # 2) 다운로드된 로컬 파일을 Pillow로 열기
    try:
        original_image = Image.open(local_download_path)
    except Exception as e:
        return jsonify({"error": "이미지를 열 수 없습니다.", "detail": str(e)}), 400

    # --- 이미지 분석 로직 예시 ---
    new_image = original_image.convert("L")  # 그레이스케일 변환
    # 실제로는 OpenCV나 딥러닝 모델 등을 적용 가능
    # ---------------------------------

    # 3) 새로운 이미지 S3 키 만들기
    new_key = f"{folder}analyzed_{s3_key}"  
    # => folder + analyzed_{...} 형태로 업로드할 때 폴더까지 포함

    # 4) 메모리에 PNG로 저장 후 S3 업로드
    new_image_bytes = BytesIO()
    new_image.save(new_image_bytes, format="PNG")
    new_image_bytes.seek(0)  # 버퍼 포인터 초기화
    
    try:
        s3_client.upload_fileobj(
            Fileobj=new_image_bytes,
            Bucket=bucket_name,
            Key=new_key
        )
    except Exception as e:
        return jsonify({"error": "S3 업로드 실패", "detail": str(e)}), 500

    # 5) 임시파일 삭제 
    try:
        os.remove(local_download_path)
    except Exception as e:
        print(f"임시 파일 삭제 실패: {e}")
        return jsonify({"error": "임시 파일 삭제 실패", "detail": str(e)}), 500
        

    # 5) 업로드된 파일의 presigned URL 생성
    try:
        new_image_url = s3_client.generate_presigned_url(
            'get_object',
            Params={'Bucket': bucket_name, 'Key': new_key},
            ExpiresIn=3600  # 1시간 유효
        )
    except Exception as e:
        return jsonify({"error": "사전 서명 URL 생성 실패", "detail": str(e)}), 500

    # 최종 반환 
    result = {
        "분석결과": {
            "손상" : 0, # 적용 결과과
            "원본 이름" : s3_key,
            "원본이미지URL": image_url,  # 클라이언트가 넘긴 URL or 메타정보
            "업로드 이름" : f"analyzed_{s3_key}",
            "새로운이미지URL": new_image_url
        }
    }
    return jsonify(result)

# Flask 앱 실행
if __name__ == '__main__':
    app.run(debug=True)
