package models;

import java.util.Date;

import org.mongodb.morphia.annotations.Id;

import pojo.ResearcherProfilePOJO;
import pojo.UserProfilePOJO;

public class Account {

	@Id
	private String id;

	private String accountId;

	private String email;

	private String password;

	private Integer accountType;

	private ResearcherProfilePOJO researcher;

	private UserProfilePOJO user;

	private Date createdTime;

	private Date updatedTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public ResearcherProfilePOJO getResearcher() {
		return researcher;
	}

	public void setResearcher(ResearcherProfilePOJO researcher) {
		this.researcher = researcher;
	}

	public UserProfilePOJO getUser() {
		return user;
	}

	public void setUser(UserProfilePOJO user) {
		this.user = user;
	}

}
