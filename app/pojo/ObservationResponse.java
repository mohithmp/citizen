package pojo;

import java.util.HashMap;
import java.util.List;

import models.FieldPOJO;

public class ObservationResponse {

	public String observationId;

	public String title;

	public String description;

	public List<FieldPOJO> fields;

	public Integer category;

	public List<String> tags;

	public HashMap<String, String> metadata;

	public Boolean isDeleted;

	public Long createdTime;

	public Long updatedTime;

}
