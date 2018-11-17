package dtos.request;

import play.data.validation.Constraints;

public class ImportGoogleSheetRequestDTO {

	@Constraints.Required
	public String observationId;

	@Constraints.Required
	public String spreadSheetId;

	public String range;

}
