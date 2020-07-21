package com.bkrmalick.awsimageupload.profile;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.bkrmalick.awsimageupload.config.DynamoDBConfig;
import com.bkrmalick.awsimageupload.datastore.FakeUserProfileDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class UserProfileDynamoDataAccessService
{
	private DynamoDBMapper mapper;

	@Autowired
	public UserProfileDynamoDataAccessService(DynamoDBMapper mapper)
	{
		this.mapper = mapper;
	}

	List<UserProfile> getUserProfiles()
	{
		return mapper.scan(UserProfile.class, new DynamoDBScanExpression());
	}

	public UserProfile getUserProfile(UUID uuid)
	{
		return mapper.load(UserProfile.class,uuid);
	}

	public void deleteUserProfile(UserProfile profile)
	{
		//need to check if it exists in the database before this method called
		mapper.delete(profile);
	}

	void addUserProfile(UserProfile profile)
	{
		//fakeUserProfileDataStore.addUserProfile(name,score);
		mapper.save(profile);
	}

	public UserProfile getUserProfileFromIDOrThrow(String tocheck)
	{
		return getUserProfiles()
				.stream()
				.filter(profile -> profile.getUserProfileID().equals(tocheck))
				.findFirst()
				.orElseThrow(()-> new IllegalStateException("User not found [" + tocheck+"]"));
	}

	public void updateUserProfileDetails(UserProfile profile)
	{
		//fakeUserProfileDataStore.setUserScore(user, score);
		try
		{
			mapper.save(profile,buildDynamoDBSaveExpression(profile));
		}
		catch(ConditionalCheckFailedException e)
		{
			throw new IllegalStateException("Failed to update user ",e);
		}
	}

	public DynamoDBSaveExpression buildDynamoDBSaveExpression(UserProfile profile)
	{
		DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();

		Map<String, ExpectedAttributeValue> expected = new HashMap<>();

		expected.put("userProfileID",
						new ExpectedAttributeValue(new AttributeValue(profile.getUserProfileID()+"")).withComparisonOperator(ComparisonOperator.EQ)
		);

		saveExpression.setExpected(expected);

		return saveExpression;
	}

}
