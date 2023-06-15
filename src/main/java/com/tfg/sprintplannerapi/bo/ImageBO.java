package com.tfg.sprintplannerapi.bo;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageBO {

    /**
     * Almacena la imagen en local y nombrandola con un nombre único generado
     * @param image
     * @return devuelve el nombre de la imagen
     */
    public String uploadImage( MultipartFile image){
        if(!image.isEmpty()){
            Path directoryImage = Paths.get("target//classes//static//uploads"); //"src//main//resources//static//uploads"
            String absoluteRoute = directoryImage.toFile().getAbsolutePath();
            try{
                byte[] byteImg = image.getBytes();
                String uniqueFileName = generateUniqueFileName(image.getOriginalFilename());

                Path imageRoute = Paths.get(absoluteRoute + "//" + uniqueFileName);
                Files.write(imageRoute,byteImg );

                return uniqueFileName;
            }catch (IOException e){
                return null;
            }
        }
        return null;
    }

    /**
     * @param avatarName
     * @return Devuelve una imagen almacenada en local en formato Resource
     */
    public Resource loadImage(String avatarName) {
        if(avatarName != null) {
            Path imagePath = Paths.get("target//classes//static//uploads//" + avatarName); //"src//main//resources//static//uploads//"
            Resource imageResource;
            imageResource = new FileSystemResource(imagePath);

            return imageResource;
        }
        return null;
    }

    /**
     * Genera un nombre único para cada imagen
     * @param originalFilename
     * @return
     */
    private String generateUniqueFileName(String originalFilename) {
        long timestamp = System.currentTimeMillis();
        String uniqueId = UUID.randomUUID().toString();   // Generar un identificador único
        String fileExtension = extractFileExtension(originalFilename);

        String uniqueFileName = timestamp + "-" + uniqueId + fileExtension;

        return uniqueFileName;
    }

    private String extractFileExtension(String filename) {
        int extensionIndex = filename.lastIndexOf('.');
        if (extensionIndex > 0 && extensionIndex < filename.length() - 1) {
            return filename.substring(extensionIndex);
        }
        return "";
    }


}
