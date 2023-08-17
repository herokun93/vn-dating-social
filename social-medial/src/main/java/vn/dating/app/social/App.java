package vn.dating.app.social;



import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
        String filePathString = "social-medial/src/main/resources/uploads/1690632562276-3c4fcb019e0341f08be492d4dee9b4df.jpg";

        Path filePath = Paths.get(filePathString);
        String fileNameWithExtension = filePath.getFileName().toString();


        System.out.println("File Name with Extension: " + fileNameWithExtension);
//        LocalDate localDate = java.time.LocalDate.now();
//
//        System.out.println(localDate.toString().substring(0,7).replace("-",""));
    }

}
