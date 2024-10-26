package v1migrationtest;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class S3ObjectDateAppender {

    public static void main(String[] args) {
        try (S3Client s3Client = S3Client.create()) {
            String bucketName = "MY_BUCKET";
            String objectKey = "your-object-key";

            // Get the object content
            String content = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build())
                    .asUtf8String();

            // Append the current date
            String updatedContent = content + "\nCurrent Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            byte[] contentBytes = updatedContent.getBytes(StandardCharsets.UTF_8);

            // Put the updated object
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build(), 
                    RequestBody.fromBytes(contentBytes));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
