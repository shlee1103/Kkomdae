# flask_app.py

# 필요한 라이브러리 임포트
from flask import Flask, request, jsonify   # Flask 웹 프레임워크 및 JSON 응답을 위한 모듈
import os
from dotenv import load_dotenv              # 환경 변수 로딩
import boto3                                # AWS S3 연동을 위한 boto3 라이브러리 (최신 버전)
from io import BytesIO
from PIL import Image
import tempfile
from loguru import logger
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
    logger.debug("Request received: %s", request.get_json()) # 요청 데이터 로깅
    data = request.get_json()
    if not data:
        logger.error("No JSON data provided.")
        return jsonify({"error": "JSON 데이터가 제공되지 않았습니다."}), 400

    try:
        # Spring에서 보내는 형식에 맞춰 처리
        s3_key = data.get('s3Key')
        
        if not s3_key:
            return jsonify({"error": "필수 필드가 누락되었습니다."}), 400
            
        logger.debug(f"s3_key: {s3_key}")
 
    except Exception as e:
        logger.error(f"Invalid JSON format: {e}")
        return jsonify({"error": "JSON 형식이 올바르지 않습니다.", "detail": str(e)}), 400

    # 1) S3에서 파일을 로컬로 다운로드
    temp_dir = tempfile.gettempdir()
    local_download_path = os.path.join(temp_dir, s3_key)
    logger.debug(f"local_download_path: {local_download_path}")
    try:
        # 디버깅을 위해 folder 변수와 s3_key 변수의 값을 출력합니다.
        logger.debug(f"Bucket: {bucket_name}, Key: {folder + s3_key}")
        s3_client.download_file(
            Bucket=bucket_name,
            Key=folder + s3_key,
            Filename=local_download_path
        )
        logger.debug(f"File downloaded successfully to {local_download_path}")
    except Exception as e:
        logger.error(f"S3 file download failed: {e}")
        return jsonify({"error": "S3 파일 다운로드 실패", "detail": str(e)}), 400

    # 2) 다운로드된 로컬 파일을 Pillow로 열기
    try:
        original_image = Image.open(local_download_path)
        logger.debug("Image opened successfully.")
    except Exception as e:
        logger.error(f"Failed to open image: {e}")
        return jsonify({"error": "이미지를 열 수 없습니다.", "detail": str(e)}), 400

    # --- 이미지 분석 로직 예시 ---
    new_image = original_image.convert("L")  # 그레이스케일 변환
    # 실제로는 OpenCV나 딥러닝 모델 등을 적용 가능
    # ---------------------------------

    # 3) 새로운 이미지 S3 키 만들기
    new_key = f"{folder}analyzed_{s3_key}"
    
    # => folder + analyzed_{...} 형태로 업로드할 때 폴더까지 포함
    logger.debug(f"New S3 key: {new_key}")
    

    # 4) 메모리에 PNG로 저장 후 S3 업로드
    new_image_bytes = BytesIO()
    new_image.save(new_image_bytes, format="PNG")
    new_image_bytes.seek(0)  # 버퍼 포인터 초기화

    try:
        s3_client.upload_fileobj(
            Fileobj=new_image_bytes,
            Bucket=bucket_name,
            Key=new_key,
            ExtraArgs={'ContentType': 'image/png'}
        )
        logger.debug(f"File uploaded successfully to S3: {new_key}")
    except Exception as e:
        logger.error(f"S3 upload failed: {e}")
        return jsonify({"error": "S3 업로드 실패", "detail": str(e)}), 500

    # 5) 임시파일 삭제
    try:
        os.remove(local_download_path)
        logger.debug(f"Temporary file removed: {local_download_path}")
    except Exception as e:
        logger.warning(f"Failed to remove temporary file: {e}")
        return jsonify({"error": "임시 파일 삭제 실패", "detail": str(e)}), 500


    # 5) 업로드된 파일의 presigned URL 생성
    try:
        new_image_url = s3_client.generate_presigned_url(
            'get_object',
            Params={'Bucket': bucket_name, 'Key': new_key},
            ExpiresIn=3600  # 1시간 유효
        )
        logger.debug(f"Presigned URL generated: {new_image_url}")
    except Exception as e:
        logger.error(f"Failed to generate presigned URL: {e}")
        return jsonify({"error": "사전 서명 URL 생성 실패", "detail": str(e)}), 500

    # 최종 반환
    result = {
        "damage" : 0, # 적용 결과
        "uploadName" : f"analyzed_{s3_key}",
    }
    logger.debug(f"Response: {result}")
    return jsonify(result)

# Flask 앱 실행
if __name__ == '__main__':
    app.run(debug=True)


# 요청 예시 (Request Examples)
# postman 주소창에 붙여넣으면 됨

"""
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"lighting.png": "https://your-bucket.s3.your-region.amazonaws.com/lighting.png"}' \
  http://127.0.0.1:5000/analyze

"""