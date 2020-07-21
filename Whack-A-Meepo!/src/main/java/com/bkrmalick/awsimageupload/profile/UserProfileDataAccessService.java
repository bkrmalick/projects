package com.bkrmalick.awsimageupload.profile;

import com.bkrmalick.awsimageupload.datastore.FakeUserProfileDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserProfileDataAccessService
{
	private final FakeUserProfileDataStore fakeUserProfileDataStore;

	@Autowired //automatically injects FakeUserProfileDataStore object when UserProfileDataAccessService is created
	public UserProfileDataAccessService(FakeUserProfileDataStore fakeUserProfileDataStore)
	{
		this.fakeUserProfileDataStore=fakeUserProfileDataStore;
	}

	List<UserProfile> getUserProfiles()
	{
		return fakeUserProfileDataStore.getUserProfiles();
	}

	void addUserProfile(String name, int score)
	{
		fakeUserProfileDataStore.addUserProfile(name,score);
	}

	public UserProfile getUserProfileFromIDOrThrow(String tocheck)
	{
		return getUserProfiles()
				.stream()
				.filter(profile -> profile.getUserProfileID().equals(tocheck))
				.findFirst()
				.orElseThrow(()-> new IllegalStateException("User not found [" + tocheck+"]"));
	}

	public void setUserScore(UserProfile user, int score)
	{
		fakeUserProfileDataStore.setUserScore(user, score);

	}
}
