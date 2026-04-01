package com.gamestore.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gamestore.exception.BadRequestException;

@Service
public class ImageUploadService {
    private final Cloudinary cloudinary;

    public ImageUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @SuppressWarnings("unchecked")
    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Image is empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("must be image (jpg, png, webp, etc.)");
        }
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder",          "gamestore/games",
                            "resource_type",   "image",
                            "transformation",  "f_auto,q_auto,w_600,h_800,c_fill"
                    )
            );
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new BadRequestException("Failed to upload image: " + e.getMessage());
        }
    }
    public void deleteImage(String publicId) {
        if (publicId == null || publicId.isBlank()){
            return;
        } 

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } 
        catch (IOException e) {
            System.err.println("Warning: could not delete Cloudinary image: " + e.getMessage());
        }
    }

    public String extractPublicId(String secureUrl) {
        if (secureUrl == null || !secureUrl.contains("/upload/")) return null;
        try {
            String afterUpload = secureUrl.split("/upload/")[1];
            if (afterUpload.matches("v\\d+/.*")) {
                afterUpload = afterUpload.replaceFirst("v\\d+/", "");
            }
            int dotIndex = afterUpload.lastIndexOf('.');
            return dotIndex > 0 ? afterUpload.substring(0, dotIndex) : afterUpload;
        } catch (Exception e) {
            return null;
        }
    }
}
