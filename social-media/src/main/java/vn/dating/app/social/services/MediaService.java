package vn.dating.app.social.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.Media;
import vn.dating.app.social.models.Post;
import vn.dating.app.social.models.Reply;
import vn.dating.app.social.repositories.*;
import vn.dating.app.social.utils.RandomStringTime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;



@Service
@Slf4j
public class MediaService {
    @Autowired
    private UserRepository userRepository;

    @Value("${upload.directory}")
    private String UPLOAD_DIR;
    @Value("${upload.api-get-image}")
    private  String API_GET_IMAGE;



    @Autowired
    private MedialRepository medialRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReplyRepository replyRepository;

    public void saveImageToPost(Post newPost,String dirPath){
        Media media = new Media();
        media.setPost(newPost);
        media.setPath(dirPath);
        medialRepository.save(media);
    }
    public void saveImagesToPost(Post newPost, List<String> dirPaths){
        Media media = new Media();
        media.setPost(newPost);
        for (int index =0;index <dirPaths.size();index++){
            media.setPath(dirPaths.get(index));
            medialRepository.save(media);
        }
    }

    public Mono<String> onlySaveListFileToLocal(FilePart filePart) {

        if (!Files.exists(Path.of(UPLOAD_DIR))) {
            try {
                Files.createDirectories(Path.of(UPLOAD_DIR));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String extension = FilenameUtils.getExtension(filePart.filename()).toLowerCase();
        String nameFile = RandomStringTime.generateRandomStringByTime()+"."+extension;
        Path filePath = Paths.get(UPLOAD_DIR).resolve(nameFile);
        nameFile = API_GET_IMAGE+nameFile;


        return filePart.transferTo(filePath)
                .then(Mono.just(nameFile))
                .onErrorResume(error -> Mono.error(new RuntimeException("File saving failed.", error)));
    }

    public List<String> onlySaveListFileToLocal(List<MultipartFile> files, boolean insertUrl) {

        List<String> listFile = new ArrayList<>();

        if (!Files.exists(Path.of(UPLOAD_DIR))) {
            try {
                Files.createDirectories(Path.of(UPLOAD_DIR));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(filename).toLowerCase();
            String newFileName = RandomStringTime.generateRandomStringByTime()+"."+extension;


            Path destinationPath = Path.of(UPLOAD_DIR, newFileName);

            try {
                Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                if(insertUrl){
                    listFile.add(API_GET_IMAGE+newFileName);
                }else{
                    listFile.add(newFileName);
                }


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return listFile;
    }

    public String onlySaveFileToLocal(MultipartFile file, boolean insertUrl) {

        if (!Files.exists(Path.of(UPLOAD_DIR))) {
            try {
                Files.createDirectories(Path.of(UPLOAD_DIR));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        String filename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        String newFileName = RandomStringTime.generateRandomStringByTime()+"."+extension;


        Path destinationPath = Path.of(UPLOAD_DIR, newFileName);

        try {
            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            if(insertUrl){
                newFileName = API_GET_IMAGE+newFileName;
            }else{

            }


        } catch (IOException e) {

            e.printStackTrace();
        }

        return newFileName;
    }

    public void saveImageToComment(Comment newComment, String dirPath){
        Media media = new Media();
        media.setComment(newComment);
        media.setPath(dirPath);
        medialRepository.save(media);
    }

    public void saveMediaPost(long postId, List<String> paths){
        Post post = postRepository.getById(postId);
        List<Media> mediaList = new ArrayList<>();
        for (int index =0;index<paths.size();index++){
            Media media = new Media();
            media.setPost(post);
            media.setCreatedAt(Instant.now());
            media.setUpdatedAt(Instant.now());
            media.setPath(paths.get(index).replace(API_GET_IMAGE,""));

            mediaList.add(media);
        }
        medialRepository.saveAll(mediaList);

    }
    public void saveMediaComment(long commentId, String path){
        Comment comment = commentRepository.getById(commentId);

        Media media = new Media();
        media.setComment(comment);
        media.setCreatedAt(Instant.now());
        media.setUpdatedAt(Instant.now());
        media.setPath(path.replace(API_GET_IMAGE,""));

        medialRepository.save(media);
    }

    public void saveMediaReply(long replyId, String path){
        Reply reply = replyRepository.getById(replyId);

        Media media = new Media();
        media.setReply(reply);
        media.setCreatedAt(Instant.now());
        media.setUpdatedAt(Instant.now());
        media.setPath(path);

        medialRepository.save(media);
    }

    public void saveImageToReplay(Reply newReply, String dirPath){
        Media media = new Media();
        media.setReply(newReply);
        media.setPath(dirPath);
        medialRepository.save(media);
    }








}
