package vn.dating.app.social;



import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class MultipartUploadExample {

    public static void main(String[] args) {
        try {
            OkHttpClient client = new OkHttpClient();

            String token="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIyamRkUnhDa21RWE5iRkJBRWRVNnpaMnZxSGVPT3JmNFFfblZLeGs1MDFvIn0.eyJleHAiOjE2OTM1MjAyNTUsImlhdCI6MTY5MzUxODQ1NSwianRpIjoiNzA1MmQ5MzYtOGY2MC00NjhkLWJkM2MtNTY3YjdkMzU0ODNlIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgxL3JlYWxtcy9kYXRpbmctY2hhdC1vYXV0aCIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJlYTA2MTQ0NS02NDM0LTRiNzktOTY4Mi0zZWYwMjdiOWQ5N2MiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJkYXRpbmctY2hhdC1vYXV0aCIsInNlc3Npb25fc3RhdGUiOiJjNzIxZmUxZC1iNTFmLTRlNzgtYmNiYy0wM2IxM2FkMTBhMGQiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJ1c2VyIiwiZGVmYXVsdC1yb2xlcy1kYXRpbmctY2hhdC1vYXV0aCJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsInNpZCI6ImM3MjFmZTFkLWI1MWYtNGU3OC1iY2JjLTAzYjEzYWQxMGEwZCIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiYW5oIGFuaCIsInByZWZlcnJlZF91c2VybmFtZSI6ImFuaCIsImdpdmVuX25hbWUiOiJhbmgiLCJmYW1pbHlfbmFtZSI6ImFuaCIsImVtYWlsIjoiYW5oQGdtYWlsLmNvbSJ9.wmIX6uDlULSVMtJw6LfT3_LAbd9g528w3pAcMVoms9_dXw426PNX-jZizdZFn8ANFeYZQNzCn7fhK-b4jBbtzUaQivvnhQ0X0oxIlCvMSg9Ly8CXDl3TFf_rbSPt00lxz67Jg_XtCYjNuUHfWz8dg-j2ZiIfNmOAQAg2tkZsh2-DMVSiOdcZ0Gcdl8dyZEEd9Rzni2iY69rqyAVApJp5feFY7ED5wxBjaMwAT3h69jsunDqeQ1kOIARCL6eMEEE_my4X4xs0iq1G2z9tdeKJKtNgLGncT9v3b0gYhVKaF2pwHrnhOsoT-ymzz19eL1d4oq8fPAIRqrhxFpV09zBc2w";

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("title", "Test Title")
                    .addFormDataPart("content", "Test Content");

            File[] filesToUpload = {
                    new File("/Users/anhxthangdang/Desktop/a.jpg"),
                    new File("/Users/anhxthangdang/Desktop/a2.jpg")
                    // Add more file paths as needed
            };

            for (File file : filesToUpload) {
                String mediaType = getMediaType(file.getName());
                builder.addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse(mediaType), file));
            }

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url("http://localhost:1994/api/social/posts/create")
                    .addHeader("Authorization", "Bearer "+token)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    System.out.println("Response: " + responseBody);
                } else {
                    System.out.println("Error: " + response.code() + " - " + response.message());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getMediaType(String fileName) {
//        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
//            return "image/jpeg";
//        } else if (fileName.endsWith(".png")) {
//            return "image/png";
//        } else if (fileName.endsWith(".jpg")) {
//            return "image/jpg";
//        } else if (fileName.endsWith(".gif")) {
//            return "image/gif";
//        } else if (fileName.endsWith(".mp4")) {
//            return "video/mp4";
//        } else if (fileName.endsWith(".fpf")) {
//            return "application/fpf";
//        } else {
//            return "application/octet-stream";
//        }
        return "application/octet-stream";
    }
}

