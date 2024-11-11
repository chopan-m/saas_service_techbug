package com.user_management.controller;

import com.user_management.dto.Base64File;
import com.user_management.dto.request.ChangePasswordRequest;
import com.user_management.dto.response.FileResponse;
import com.user_management.dto.response.GenericResponse;
import com.user_management.dto.UserDTO;
import com.user_management.dto.request.UserEditRequest;
import com.user_management.mapper.UserMapper;
import com.user_management.model.User;
import com.user_management.service.FileStorageService;
import com.user_management.service.UserService;
import com.user_management.utils.CommonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;

@Tag(
        name = "User APIs",
        description = "User Get, Edit, Delete APIs Controller"
)
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${file.upload-dir}")
    private String fileUploadDir;

    // MIME types for JPEG and PNG files
    private static final String JPEG_MIME_TYPE = "image/jpeg";
    private static final String PNG_MIME_TYPE = "image/png";

    @Operation(
            summary = "API to update a user",
            description = "PUT (/update/userid) API to update a user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200"
    )
    @PutMapping("/user/update/{userid}")
    public ResponseEntity<GenericResponse<UserDTO>> signup(@RequestBody UserEditRequest userEditRequest, @PathVariable Long userid){
        System.out.println(userEditRequest.toString() + " userEdit request for id: "+ userid);
        Optional<User> optionalUser = this.userService.findUserById(userid);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setName(userEditRequest.getName());
            user.setEmail(userEditRequest.getEmail());
            user.setOrganization(userEditRequest.getOrganization());

            User dbUser = this.userService.saveUser(user);

            String profileImgUrl = dbUser.getProfileImg() != null ? 
                "/api/v1/user/profile/download/" + dbUser.getProfileImg() : "";
            
            UserDTO dto = UserMapper.mapToUserDto(dbUser);
            dto.setProfileImg(profileImgUrl);
            
            GenericResponse<UserDTO> responseBody = GenericResponse.build(
                CommonUtils.getPath(), 
                HttpStatus.OK.value(),
                true, 
                "Success", 
                "", 
                dto
            );
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);

        } else {
            UserDTO dto = new UserDTO();
            GenericResponse<UserDTO> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.NOT_FOUND.value(),
                    false, "User Not Found!!", "", dto);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

    }


    @Operation(
            summary = "API to change password of a user",
            description = "PUT (/change/password/userid) API to update password of a user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200"
    )
    @PutMapping("/user/change/password/{userid}")
    public ResponseEntity<GenericResponse<String>> changePassword(@PathVariable Long userid,
                                                 @Valid @RequestBody ChangePasswordRequest request) {
        // Find the user by username
        System.out.println("ChangePassword Request for id: "+ userid);
        Optional<User> optionalUser = this.userService.findUserById(userid);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Check if the old password matches
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");

                String entity = "Old password is incorrect.";
                GenericResponse<String> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.NOT_MODIFIED.value(),
                        false, "Failure!!", "Old password is incorrect.", entity);
                return ResponseEntity.status(HttpStatus.OK).body(responseBody);
            }
            // Update the password with the new password
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            this.userService.saveUser(user);

            String entity = "Password changed successfully.";
            GenericResponse<String> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.OK.value(),
                    true, "Success", "", entity);
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } else {
            String entity = "User not found!!";
            GenericResponse<String> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.NOT_FOUND.value(),
                    false, "Error!!", "", entity);
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        }

    }



    @Operation(
            summary = "Upload Profile Picture API",
            description = "POST (/api/v1/upload/profile/photo/{userId}) for uploading a profile pic, size at max 5MB",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200"
    )

    @PostMapping(path ="/user/upload/profile/photo/{userid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GenericResponse<String>> uploadProfilePic(@RequestParam("file") MultipartFile file, @PathVariable("userid") Long userId) {

        // Check if the file is empty
        if (file.isEmpty()) {
            String entity = "Please select a file to upload.";
            GenericResponse<String> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.BAD_REQUEST.value(),
                    true, "Success", "", entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        }

        // Check the content type of the file
        String contentType = file.getContentType();

        if (!JPEG_MIME_TYPE.equals(contentType) && !PNG_MIME_TYPE.equals(contentType)) {
            String entity = "Only JPEG and PNG files are allowed.";
            GenericResponse<String> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                    true, "Success", "", entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        }

        // Process the file (e.g., save it to the server)
        try {
            // Save file to the server or perform other operations here
            // e.g., file.transferTo(new File("/path/to/save/" + file.getOriginalFilename()));

            //userId
            Optional<User> optionalUser = userService.findUserById(userId);
            User user = null;
            String currentProfilePicName = "";
            if(optionalUser.isPresent()) {
                user = optionalUser.get();
                currentProfilePicName = user.getProfileImg() != null ? user.getProfileImg() :  "";
//                System.out.println(user);
            } else {
                user = new User();
                user.setId(Long.parseLong("temp"));
            }

            Base64File fileInfo = fileStorageService.storeFile(file, currentProfilePicName);
//            String entity = "File uploaded successfully: " + fileInfo.getName();

            String fileDownloadUri = "/api/v1/user/profile/download/"+fileInfo.getName();
            if (!(user.getId().equals("temp"))) {
                System.out.println("Update ProfilePic for userid: " + userId);
                user.setProfileImg(fileInfo.getName());
                userService.saveUser(user);
            }


            GenericResponse<String> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.OK.value(),
                    true, "Success", "", fileDownloadUri);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } catch (Exception e) {
            String entity = "File upload failed.";
            GenericResponse<String> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    true, "Success", "", entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        }
    }

//    @Operation(
//            summary = "Get Profile Photo as Base64 String API",
//            description = "GET (/profile/photo/{userid}) for fetching profile photo as Base64",
//            security = @SecurityRequirement(name = "bearerAuth")
//    )
//    @ApiResponse(
//            responseCode = "200",
//            description = "HTTP Status 200"
//    )
//    @GetMapping("/user/profile/photo/base64/do-not-use-it")
    public ResponseEntity<GenericResponse<String>> getProfilePic(@RequestParam("userid") Long userId) throws IOException {

        String fileName = null;

        // fetch filename from db against the userid
        Optional<User> optionalUser = userService.findUserById(userId);
        if(optionalUser.isPresent()) {
            User user  = optionalUser.get();
            fileName = user.getProfileImg();

            // Define the path to the file
            Path imagePath = Paths.get(fileUploadDir, fileName);

            File imgFile = imagePath.toFile();
            // Check if the file exists
            if (!imgFile.exists() || !imgFile.isFile()) {
                String entity = "Image not found!";
                GenericResponse<String> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.NOT_FOUND.value(),
                        false, "Error", "", entity);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
            }

            // Read file as byte array
            byte[] imageBytes = Files.readAllBytes(imagePath);

            // Encode the byte array to Base64 string using java.util.Base64
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Return the encoded image as a string
            GenericResponse<String> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.OK.value(),
                    true, "Success", "", base64Image);
            return ResponseEntity.status(HttpStatus.OK).body(responseBody);
        } else {

            String entity = "User not found!";
            GenericResponse<String> responseBody =  GenericResponse.build(CommonUtils.getPath(), HttpStatus.NOT_FOUND.value(),
                    false, "Error", "", entity);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

    }



//    @GetMapping("/user/image")
    public ResponseEntity<byte[]> getImage() throws IOException {
        // Load image data
//        ClassPathResource resource = new ClassPathResource("static/images/image.png");
//        Path filePath = Paths.get(this.fileStorageService.getPath());
        Path filePath = Paths.get("upload/abc.jpg")
                .toAbsolutePath().normalize();

        byte[] imageData = Files.readAllBytes(filePath);

        // Set content type header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        // Return image data as ResponseEntity
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

//    @GetMapping("/user/images")
    public ResponseEntity<Resource> getImage2() {
        try {
            // Load the image from the resources directory or any other configured path
            Resource resource = resourceLoader.getResource("classpath:upload/" + "abc.jpg");

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Adjust based on image type
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(
            summary = "Get Profile Photo",
            description = "GET (/user/get/profile/photo?userid=xyz) for fetching profile photo as Base64",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200"
    )
    @GetMapping("/user/get/profile/photo")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("userid") Long userId) throws IOException {

       String fileName = null;

        // fetch filename from db against the userid
        Optional<User> optionalUser = userService.findUserById(userId);
        if(optionalUser.isPresent()) {
            User user  = optionalUser.get();
            fileName = user.getProfileImg();

            if (fileName != null) {
                // Define the path to the file
                Path imagePath = Paths.get(fileUploadDir, fileName);

                File imgFile = imagePath.toFile();
                // Check if the file exists
                if (!imgFile.exists() || !imgFile.isFile()) {
                    String entity = "Image not found!";
//                return (ResponseEntity<InputStreamResource>) ResponseEntity.notFound();

                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }

                System.out.println("fileUploadDir  " + fileUploadDir);

                // Define the path to the file
                Path filePath = Paths.get(fileUploadDir, fileName);

                // Check if the file exists
                if (!Files.exists(filePath)) {
                    return ResponseEntity.notFound().build();
                }

                // Get the file's content type
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                // Create headers
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

                // Create a ResponseEntity with the InputStreamResource
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentType(MediaType.parseMediaType(contentType))
                        .contentLength(Files.size(filePath))
                        .body(new InputStreamResource(Files.newInputStream(filePath)));

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }









    ///////////////// new apis

//    @PostMapping("/user/profile/upload")
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        // comment it cause we were routing through api gateway..
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("api/v1//user/profile/download/")
//                .path(fileName)
//                .toUriString();

        String fileDownloadUri = "/api/v1/user/profile/download/"+fileName;

        return new FileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @GetMapping("/user/profile/download/{fileName:.+}")
    public ResponseEntity <Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }



}
