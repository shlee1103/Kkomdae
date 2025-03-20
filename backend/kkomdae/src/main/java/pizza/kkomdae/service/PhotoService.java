package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pizza.kkomdae.dto.request.AiPhotoInfo;
import pizza.kkomdae.entity.LaptopTestResult;
import pizza.kkomdae.entity.Photo;
import pizza.kkomdae.repository.PhotoRepository;
import pizza.kkomdae.repository.laptopresult.LapTopTestResultRepository;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final LapTopTestResultRepository lapTopTestResultRepository;

    @Transactional
    public void uploadAiPhoto(AiPhotoInfo aiPhotoInfo) {
        LaptopTestResult laptopTestResult = lapTopTestResultRepository.getReferenceById(aiPhotoInfo.getTestId());
        Photo photo = photoRepository.findByLaptopTestResultAndPhotoId(laptopTestResult, aiPhotoInfo.getPhotoId());
        photo.setResultUrl(aiPhotoInfo.getS3PicUrl());
    }
}
