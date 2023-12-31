//package com.ssafy.trippy.config;
//
//import java.io.IOException;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.model.S3Object;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
////@Getter
//@RequiredArgsConstructor
//public class S3Service {
//	private AmazonS3 s3Client;
//
//    private final AmazonS3Client amazonS3Client;
//
//    @Value("${cloud.aws.credentials.accessKey}")
//    private String accessKey;
//
//    @Value("${cloud.aws.credentials.secretKey}")
//    private String secretKey;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//    @Value("${cloud.aws.region.static}")
//    private String region;
//
//    @PostConstruct
//    public void setS3Client() {
//        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
//
//        s3Client = AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withRegion(this.region)
//                .build();
//    }
//
//    public String upload(MultipartFile file) throws IOException {
//        return upload(file, "", file.getOriginalFilename());
//    }
//
//    public String upload(MultipartFile file, String dirName, String fileName) throws IOException {
//        String filePath = dirName+"/"+fileName;
//
//        s3Client.putObject(new PutObjectRequest(bucket, filePath, file.getInputStream(), null)
//                .withCannedAcl(CannedAccessControlList.PublicRead));
//        return s3Client.getUrl(bucket, filePath).toString();
//    }
//
//    public void delete(String filePath){
//        boolean isExistObject = s3Client.doesObjectExist(bucket, filePath);
//        if (isExistObject == true) {
//            s3Client.deleteObject(bucket, filePath);
//        }
//    }
//    
//   
//
//
//	@Override
//	public String toString() {
//		return "S3Service [s3Client=" + s3Client + ", amazonS3Client=" + amazonS3Client + ", accessKey=" + accessKey
//				+ ", secretKey=" + secretKey + ", bucket=" + bucket + ", region=" + region + "]";
//	}
//
//	public AmazonS3 getS3Client() {
//		return s3Client;
//	}
//
//	public void setS3Client(AmazonS3 s3Client) {
//		this.s3Client = s3Client;
//	}
//    
//    
//}
