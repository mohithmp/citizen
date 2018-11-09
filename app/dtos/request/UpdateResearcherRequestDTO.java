package dtos.request;

import play.data.validation.Constraints;

public class UpdateResearcherRequestDTO {

	@Constraints.MinLength(2)
	@Constraints.MaxLength(50)
	public String name;
	
	@Constraints.MinLength(3)
	// @Constraints.Pattern("^[a-z]+[@][a-z.]$")
	public String email;
	
	@Constraints.MinLength(6)
	@Constraints.MaxLength(16)
	public String password;
}
