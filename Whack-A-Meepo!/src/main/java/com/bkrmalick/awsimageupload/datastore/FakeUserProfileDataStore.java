package com.bkrmalick.awsimageupload.datastore;

import com.bkrmalick.awsimageupload.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore
{
	private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

	/*static
	{
		USER_PROFILES.add(new UserProfile( "283fd8d6-ad01-412e-95c1-ef17c5a8af84", "hadimalik", null,0));
		USER_PROFILES.add(new UserProfile("790b5f78-cd10-466b-bfb8-af272bc37f33", "maleehanaseer", null,0));
		USER_PROFILES.add(new UserProfile("ebf0661a-3e0f-4f73-a850-7cd1bdc1d27d", "bkrmalick", null,0));
		USER_PROFILES.add(new UserProfile("4c3f0af0-c2bd-436b-9c5a-bc7c00e8c915", "sarahnaseer", null,0));
	}*/

	public List<UserProfile> getUserProfiles()
	{
		return USER_PROFILES;
	}


	public void addUserProfile(String name, int score)
	{
//		USER_PROFILES.add(new UserProfile( UUID.randomUUID().toString(), name, null, score));
		USER_PROFILES.add(new UserProfile( name, null, score));
	}

	public void setUserScore(UserProfile user, int score)
	{
		user.setScore(score);
	}
}
