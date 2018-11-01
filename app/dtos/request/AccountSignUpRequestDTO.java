package dtos.request;

import play.data.validation.Constraints;

public class AccountSignUpRequestDTO {

	@Constraints.Required
	@Constraints.MinLength(2)
	@Constraints.MaxLength(50)
	public String name;

	@Constraints.Required
	@Constraints.MinLength(3)
	// @Constraints.Pattern("^[a-z]+[@][a-z.]$")
	public String email;

	@Constraints.Required
	@Constraints.MinLength(6)
	@Constraints.MaxLength(16)
	public String password;
	
	@Constraints.Required
	public Integer accountType;

	public String deviceId;

	@Constraints.Required
	public Integer deviceType;

	public String deviceToken;

}
