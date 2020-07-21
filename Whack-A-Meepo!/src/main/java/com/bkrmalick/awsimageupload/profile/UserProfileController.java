package com.bkrmalick.awsimageupload.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user-profile")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3000/*"}) //need to change for production
public class UserProfileController
{
	private final UserProfileService userProfileService;

	@Autowired
	public UserProfileController(UserProfileService userProfileService)
	{
		this.userProfileService=userProfileService;
	}

	@GetMapping
	public List<UserProfile> getUserProfiles()
	{
		return userProfileService.getUserProfiles();
	}

	/*
	@RequestParam is use for query parameter(static values) like: http://localhost:8080/calculation/pow?base=2&ext=4

	@PathVariable is use for dynamic values like : http://localhost:8080/calculation/sqrt/8
	 */

	@PostMapping
			(
			path="{userProfileId}/image/upload",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public void uploadUserProfileImage(@PathVariable("userProfileId") String userProfileID,
									   @RequestParam("file") MultipartFile file)
	{
		userProfileService.uploadUserProfileImage(userProfileID,file);
	}

	@PostMapping
			(
					path="score/upload",
					consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE
			)
	public void uploadUserAndScore(@RequestParam("name") String name,
									   @RequestParam("score") int score,@RequestParam("file") MultipartFile file)
	{
		System.out.println(name+":"+score);
		userProfileService.updateOrAddUserAndScore(name,score, file);
	}

	@GetMapping
			(
					path="{userProfileId}/image/download"
			)
	public byte[] downloadUserProfileImage(@PathVariable("userProfileId") String userProfileID)
	{
		return userProfileService.downloadUserProfileImage(userProfileID);
	}
}
