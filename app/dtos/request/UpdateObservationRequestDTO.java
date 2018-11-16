package dtos.request;

import java.util.List;

import play.data.validation.Constraints;

public class UpdateObservationRequestDTO {
	
	@Constraints.Required
	public String observationId;
	
	public String title;
	
	public String description;
	
	public List<String> tags;

}
