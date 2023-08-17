package vn.dating.app.gateway.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.dating.app.gateway.dto.CreateUserDto;
import vn.dating.app.gateway.services.KeycloakUserService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.time.Duration;

@RestController
@RequestMapping("/api/gateway")
@RequiredArgsConstructor
public class GatewayController {

    @Autowired
    private KeycloakUserService keycloakUserService;

//

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public String login(Principal principal){
//        System.out.println(principal.getName());
        return "gateway";
    }


    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String getToken(Principal principal){
        System.out.println("Handler");
        if(principal==null){
            return "principal is null";
        }
        System.out.println(principal.getName());
//        System.out.println(principal.getName());
        return principal.getName();
    }


    @PostMapping(path = "/upload")
    public String upload(@RequestPart("files") Flux<FilePart> files){
        System.out.println("upload file");
        files.subscribe(filePart -> {
            String originalFilename = filePart.filename();
            long size = filePart.headers().getContentLength();


            System.out.println("Uploaded file: " + originalFilename + " (Size: " + size + " bytes)");
        });

        return "Upload file";
    }
    @GetMapping(value = "/stream/flux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Student> streamJsonObjects() {
        return Flux.interval(Duration.ofSeconds(1)).map(i -> new Student("Name" + i, i.intValue()));
    }
    class Student{
        private String name;
        private int index;

        public Student(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", index=" + index +
                    '}';
        }
    }

    @GetMapping("stream/video")
    public ResponseEntity<Resource> streamVideo() throws IOException {
//        Resource videoResource = new ClassPathResource("static/video.MP4");
        Resource videoResource = new ClassPathResource("static/chubin.mp4");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            headers.setContentLength(videoResource.contentLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        headers.set("Accept-Ranges", "bytes"); // Enable byte-range requests

        return ResponseEntity.ok()
                .headers(headers)
                .body(videoResource);
    }






//    @PostMapping
//    public ResponseEntity createUser(@RequestBody CreateUserDto createUserDto) {
//
//        keycloakUserService.createUser(createUserDto);
//
//        return  ResponseEntity.ok("");
//    }
}
