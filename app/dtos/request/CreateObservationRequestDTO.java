package dtos.request;

import pojo.FieldRequestPOJO;

import java.util.List;

import play.data.validation.Constraints;

public class CreateObservationRequestDTO {
	
	@Constraints.Required
	public String title;
	
	public String description;
	
	public List<FieldRequestPOJO> fields;
	
	public Integer category;
	
	public List<String> tags;

}
