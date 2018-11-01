package dtos.request;

import play.data.validation.Constraints;

public class AccountSignInRequestDTO {

	@Constraints.Required
	@Constraints.MinLength(10)
	@Constraints.MaxLength(10)
	// @Constraints.Pattern("^[a-z]+[@][a-z.]$")
	public String email;

	@Constraints.Required
	public String password;

	@Constraints.Required
	public String deviceId;

	@Constraints.Required
	public Integer deviceTypeId;

	@Constraints.Required
	public String deviceToken;

}
