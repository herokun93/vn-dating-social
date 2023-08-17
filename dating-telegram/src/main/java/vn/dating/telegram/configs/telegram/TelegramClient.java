package vn.dating.telegram.configs.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
@Service
@Slf4j
public class TelegramClient {

    private String token = "5926354757:AAF_pzQxTZYgjRnFJ_WAqmrU4797jSAMnhI";
    private String telegramBaseUrl = "https://api.telegram.org/bot";
    private String apiUrl = telegramBaseUrl+token;

    private final RestTemplate restTemplate;


    @Autowired
    public TelegramClient(RestTemplate restTemplate) {
        this.restTemplate  = restTemplate;
    }

    public void sendMessage(String message, Object chatID) throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl+"/sendMessage")
                    .queryParam("chat_id", chatID)
                    .queryParam("text", message);
            ResponseEntity exchange = restTemplate.exchange(builder.toUriString().replaceAll("%20", " "), HttpMethod.GET, null, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error response : State code: {}, response: {} ", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception err) {
            log.error("Error: {} ", err.getMessage());
            throw new Exception("This service is not available at the moment!");
        }
    }

    public void sendPhotoFile(Object chatID, String caption, MultipartFile photo) throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap body = new LinkedMultiValueMap();
            ByteArrayResource fileAsResource = new ByteArrayResource(photo.getBytes()){
                @Override
                public String getFilename(){
                    return photo.getOriginalFilename();
                }
            };
            body.add("Content-Type", "image/png");
            body.add("photo", fileAsResource);
            HttpEntity<MultiValueMap> requestEntity = new HttpEntity(body, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl+"/sendPhoto")
                    .queryParam("chat_id", chatID)
                    .queryParam("caption", caption);
            System.out.println(requestEntity);
            String exchange = restTemplate.postForObject(
                    builder.toUriString().replaceAll("%20", " "),
                    requestEntity,
                    String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error response : State code: {}, response: {} ", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception err) {
            log.error("Error: {} ", err.getMessage());
            throw new Exception("This service is not available at the moment!");
        }
    }

    public void sendPhotoByPhotoId(Object chatID, String caption, String photo) throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl+"/sendPhoto")
                    .queryParam("chat_id", chatID)
                    .queryParam("photo", photo)
                    .queryParam("caption", caption);
            String exchange = restTemplate.postForObject(
                    builder.toUriString().replaceAll("%20", " "),
                    null,
                    String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error response : State code: {}, response: {} ", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception err) {
            log.error("Error: {} ", err.getMessage());
            throw new Exception("This service is not available at the moment!");
        }
    }
    public void sendVideoByVideoId(Object chatID, String caption, String video) throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl+"/sendVideo")
                    .queryParam("chat_id", chatID)
                    .queryParam("video", video)
                    .queryParam("caption", caption);
            String exchange = restTemplate.postForObject(
                    builder.toUriString().replaceAll("%20", " "),
                    null,
                    String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error response : State code: {}, response: {} ", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception err) {
            log.error("Error: {} ", err.getMessage());
            throw new Exception("This service is not available at the moment!");
        }
    }
}
