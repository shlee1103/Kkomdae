from django.shortcuts import render

# Create your views here.
# s3app/views.py
import boto3
from django.http import JsonResponse
from django.conf import settings

def presigned_url_view(request):
    """
    ?file=파일명 으로 넘어온 파라미터에 대해
    AWS S3의 사전 서명(Presigned) URL을 생성하여 반환한다.
    """
    filename = request.GET.get('file')
    if not filename:
        return JsonResponse({'error': 'No filename provided'}, status=400)

    # boto3 s3 클라이언트 생성
    s3_client = boto3.client(
        's3',
        aws_access_key_id=settings.AWS_ACCESS_KEY_ID,
        aws_secret_access_key=settings.AWS_SECRET_ACCESS_KEY,
        region_name=settings.AWS_REGION
    )

    # 실제로 presigned url 생성
    try:
        key = f"{settings.S3_PREFIX}{filename}"
        url = s3_client.generate_presigned_url(
            ClientMethod='get_object',
            Params={
                'Bucket': settings.AWS_S3_BUCKET_NAME,
                'Key': key
            },
            ExpiresIn=600  # 600초(10분) 동안만 유효
        )
        return JsonResponse({'url': url})
    except Exception as e:
        return JsonResponse({'error': str(e)}, status=500)
