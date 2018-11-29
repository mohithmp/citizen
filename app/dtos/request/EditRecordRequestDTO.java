package dtos.request;

import java.util.HashMap;

import play.data.validation.Constraints;

public class EditRecordRequestDTO {
	
	@Constraints.Required
	public String observationId;
	
	@Constraints.Required
	public String recordId;	
	
	@Constraints.Required
	public HashMap<String, Object> data;

}
