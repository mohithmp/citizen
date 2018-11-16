package dtos.request;

import java.util.HashMap;

import play.data.validation.Constraints;

public class AddRecordRequestDTO {

	@Constraints.Required
	public String observationId;

	@Constraints.Required
	public HashMap<String, Object> data;

}
