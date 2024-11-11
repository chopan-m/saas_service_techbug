package com.user_management.service;

import com.user_management.dto.Base64File;
import com.user_management.exception.FileNotFoundException;
import com.user_management.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Objects;

import org.springframework.core.io.Resource;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
//            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

//    public Path getPath(@Value("${file.upload-dir}") String uploadDir) {
//        return Paths.get(uploadDir+"/abc.jpg")
//                .toAbsolutePath().normalize();
//    }

    public Base64File storeFile(MultipartFile file, String currentProfilePicName) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Get the filename with extension
//       String fileName = this.fileStorageLocation.getFileName().toString();

        try {

            // Extract the filename and extension
//            String filenameWithoutExtension = getFilenameWithoutExtension(fileName);
            String newProfilePicExtension = getFileExtension(fileName);
            String existingProfilePicExtension = getFileExtension(currentProfilePicName);

            // if profile pic exists, delete it first..
            if (currentProfilePicName.length() > 1)
//                if (!(existingProfilePicExtension.equals(newProfilePicExtension)))
                this.deleteFile(currentProfilePicName);
        //           else // it will get replaced by new file if extension is same..


            // compression
            /*
          // Read the uploaded image into a BufferedImage
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            Path filePath = Paths.get(this.fileStorageLocation.toString(), fileName);
            // Define the path to save the compressed image
            File compressedImageFile = new File(filePath.toString());
            // Compress and save the image
            compressImage(originalImage, compressedImageFile, newProfilePicExtension);
             */




//            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//            long size = multipartFile.getSize();

//            String filecode = FileUploadUtil.saveFile(fileName, multipartFile);

//            FileUploadResponse response = new FileUploadResponse();
//            response.setFileName(fileName);
//            response.setSize(size);
//            response.setDownloadUri("/downloadFile/" + filecode);


            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            // Files.copy(file.getInputStream(), targetLocation);


            // Read file as byte array
//            byte[] imageBytes = Files.readAllBytes(filePath);
            // Encode the byte array to Base64 string using java.util.Base64
//            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

//            System.out.println(base64Image);
            Base64File fileInfo = new Base64File();
            fileInfo.setName(fileName);
//            fileInfo.setContent(base64Image);
            return fileInfo;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public boolean deleteFile(String fileName) {
        Path filePath = Paths.get(this.fileStorageLocation.toString(), fileName);
        // Check if the file exists
        if (!Files.exists(filePath)) {
            System.out.println("File not found! " + filePath);
            return true;
        } else {
            System.out.println("File found! " + filePath);
        }
        try {
            // Delete the file
            Files.delete(filePath);
            System.out.println("File deleted successfully:");

        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Error deleting file: " + fileName);
        }
        return true;
    }

    // Method to extract the filename without extension
    private String getFilenameWithoutExtension(String fullFilename) {
        int dotIndex = fullFilename.lastIndexOf('.');
        return (dotIndex == -1) ? fullFilename : fullFilename.substring(0, dotIndex);
    }

    // Method to extract the extension of the file
    private String getFileExtension(String fullFilename) {
        int dotIndex = fullFilename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fullFilename.substring(dotIndex + 1);
    }


    // Method to compress the image
    private void compressImage(BufferedImage image, File output, String extension) throws IOException {
        // Get all image writers for JPG format
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(extension);
        if (!writers.hasNext()) {
            throw new IllegalStateException("No writers found for "+ extension + "format.");
        }

        // Select the first writer
        ImageWriter writer = writers.next();

        // Create an output stream for the compressed image
        try (FileOutputStream fos = new FileOutputStream(output);
             ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {

            // Set the writer output
            writer.setOutput(ios);

            // Create a compression parameter
            ImageWriteParam param = writer.getDefaultWriteParam();

            // Enable compression mode
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.3f); // Set compression quality (0.0 - 1.0, 1.0 is highest quality)
            }

            // Write the compressed image to file
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            // Dispose the writer to free resources
            writer.dispose();
        }
    }



    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        }catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
}