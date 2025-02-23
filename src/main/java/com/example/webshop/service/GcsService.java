package com.example.webshop.service;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.IOException;

@Service
public class GcsService {
    @Autowired
    private Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket-name}")
    private String bucketName;

    public String uploadImage(MultipartFile file, String objectName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getBytes());
        return objectName;
    }

    public void deleteImage(String objectName) {
        BlobId blobId = BlobId.of(bucketName, objectName);
        storage.delete(blobId);
    }

    public String getPublicUrl(String objectName) {
        return "https://storage.googleapis.com/" + bucketName + "/" + objectName;
    }
}