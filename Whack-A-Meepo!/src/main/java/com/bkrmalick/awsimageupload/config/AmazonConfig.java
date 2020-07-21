package com.bkrmalick.awsimageupload.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.bkrmalick.awsimageupload.bucket.BucketName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig
{
	private final static BucketName bucket=BucketName.PROFILE_IMAGE;
	private final static String region="eu-west-2";

	@Bean
	public AmazonS3 s3()
	{
		AWSCredentials awsCredentials= new BasicAWSCredentials(
				"XXXXXXXXXXXXXXXXXXXXXXXXXXX",
				"XXXXXXXXXXXXXXXXXXXXXXXXXXX"
		);

		return AmazonS3ClientBuilder
				.standard()
				.withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.build();
	}

	public static String getBucket()
	{
		return bucket.getBucketName();
	}

	public static String getRegion()
	{
		return region;
	}

	public static String getURL(String filename)
	{
		return "https://"+getBucket()+".s3."+getRegion()+".amazonaws.com/"+filename;
	}
}
