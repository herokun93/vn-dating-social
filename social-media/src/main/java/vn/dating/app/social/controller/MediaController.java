package vn.dating.app.social.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dating.app.social.services.MediaService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/social/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;
    @Value("${upload.directory}")
    private String UPLOAD_DIR;

    @GetMapping("/{filename}")
    public ResponseEntity<FileSystemResource> getMedia(@PathVariable String filename) {

        Path imagePath = Paths.get(UPLOAD_DIR + filename);

        FileSystemResource resource = new FileSystemResource(imagePath);

        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        MediaType mediaType;
        switch (extension) {
            case "png":
                mediaType = MediaType.IMAGE_PNG;
                break;
            case "jpg":
            case "jpeg":
                mediaType = MediaType.IMAGE_JPEG;
                break;
            case "gif":
                mediaType = MediaType.IMAGE_GIF;
                break;
            case "mp4":
                mediaType = MediaType.parseMediaType("video/mp4");
                break;
            case "avi":
                mediaType = MediaType.parseMediaType("video/avi");
                break;
            default:
                // Set a default media type for other media types
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
                break;
        }
        return ResponseEntity.ok()
                .contentType(mediaType) // Set the appropriate media type based on your image format
                .body(resource);
    }

    @GetMapping("/stream/{filename}")
    public ResponseEntity<Resource> stream(@PathVariable String filename) throws IOException {
        Path imagePath = Paths.get(UPLOAD_DIR + filename);

        System.out.println("client access");
        if (!Files.exists(imagePath)) {
            // Return a 404 Not Found response if the file does not exist
            System.out.println("Return a 404 Not Found response if the file does not exist");
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(imagePath);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        System.out.println("play video");

        byte[] imageBytes = Files.readAllBytes(imagePath);
        Resource imageResource = new ByteArrayResource(imageBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(imageBytes.length);
        headers.set("Accept-Ranges", "bytes");

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageResource);
    }
}
