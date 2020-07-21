package com.bkrmalick.awsimageupload.profile;

import com.amazonaws.services.s3.AmazonS3;
import com.bkrmalick.awsimageupload.bucket.BucketName;
import com.bkrmalick.awsimageupload.filestore.FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UserProfileService
{
	private final UserProfileDataAccessService userProfileDataAccessService;
	private final UserProfileDynamoDataAccessService userProfileDynamoDataAccessService;
	private final AmazonS3 s3;
	private final FileStore fileStore;

	@Autowired
	public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, UserProfileDynamoDataAccessService userProfileDynamoDataAccessService, AmazonS3 s3, FileStore fileStore)
	{
		this.userProfileDataAccessService = userProfileDataAccessService;
		this.userProfileDynamoDataAccessService = userProfileDynamoDataAccessService;
		this.s3 = s3;
		this.fileStore = fileStore;
	}

	List<UserProfile> getUserProfiles()
	{
		//return userProfileDataAccessService.getUserProfiles();
		return userProfileDynamoDataAccessService.getUserProfiles();
	}

	void uploadUserProfileImage(String userProfileID, MultipartFile file)
	{
		//1. Check if image is not empty
		if (file.getSize() == 0)
		{
			throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + " bytes]");
		}
		//2. ensure file is an image
		else if (file.getContentType() == null || !file.getContentType().split("/")[0].equalsIgnoreCase("image"))
		{
			throw new IllegalStateException("File not image type [" + file.getContentType().split("/")[0] + "]");
		}

		//3. ensure user exists in db
		UserProfile userProfile = userProfileDynamoDataAccessService.getUserProfileFromIDOrThrow(userProfileID);


		//4. grab metadata from file if any
		Map<String, String> metadata = new HashMap<>();
		metadata.put("Content-Type", file.getContentType());
		metadata.put("Content-Length", String.valueOf(file.getSize()));

		//5. Store the image in s3 and update database (userProfileImageLink) with s3 image link
		log("Checks passed, uploading image to bucket...");

		String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), userProfile.getUsername());
		String filename = String.format("%s_%s", file.getOriginalFilename(), UUID.randomUUID());

		try
		{
			fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());

			//delete old one if exists
			if(userProfile.getUserProfileImageLink()!=null && userProfile.getUserProfileImageLink().length()>0)
				fileStore.delete(path,userProfile.getUserProfileImageLink());

			//update new one
			userProfile.setUserProfileImageLink(filename);
			userProfileDynamoDataAccessService.updateUserProfileDetails(userProfile);

		} catch (Exception e)
		{
			throw new IllegalStateException(e);
		}

			/*
			File localImage=null;
			try
			{
				localImage = multipartToFile(file,file.getOriginalFilename());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			String bucket=AmazonConfig.getBucket();

			if(localImage!=null)
			{
				s3.putObject(
						new PutObjectRequest(
							bucket,
							file.getOriginalFilename(),
							localImage).withCannedAcl(CannedAccessControlList.PublicRead)
				);

				String url=AmazonConfig.getURL(file.getOriginalFilename());
				log("uploaded image to :"+ url );

				localImage.delete();

				userProfileDataAccessService.getUserProfileFromID(userProfileID).setUserProfileImageLink(url);
			}*/

	}

	private static void log(String msg)
	{
		System.out.println("LOG: " + msg);
	}

	private static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException
	{
		File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
		log("Temporarily saving to: " + convFile.getAbsolutePath());
		multipart.transferTo(convFile);
		return convFile;
	}

	public byte[] downloadUserProfileImage(String userProfileID)
	{
		UserProfile userProfile = userProfileDynamoDataAccessService.getUserProfileFromIDOrThrow(userProfileID);

		String path = String.format("%s/%s",
				BucketName.PROFILE_IMAGE.getBucketName(),
				userProfile.getUsername());

		return Optional.ofNullable(userProfile.getUserProfileImageLink())
				.filter(key->key.length()>0) //filter out any links which are empty
				.map(key -> fileStore.download(path,key))
				.orElse(new byte[0]);
	}

	public void updateOrAddUserAndScore(String username, int score, MultipartFile file)
	{
		UserProfile user=userProfileDynamoDataAccessService.getUserProfiles()
				.stream()
				.filter((p)->p.getUsername().equalsIgnoreCase(username))
				.findFirst()
				.orElse(null);


		if(user!=null)
		{
			//userProfileDataAccessService.setUserScore(user, score);
			throw new IllegalStateException("Cannot add user. User already exists ["+username+"]");
		}
		else
		{

			UserProfile profile=new UserProfile( username,"", score);

			userProfileDynamoDataAccessService.addUserProfile(profile); //add the user
			uploadUserProfileImage(profile.getUserProfileID(),file); 	//add the image to the user
		}
	}
}
