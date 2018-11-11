package dtos.response;

import java.util.ArrayList;
import java.util.List;

import pojo.ObservationResponse;
import utils.MyConstants;

public class GetObservationResponseDTO {

	public List<ObservationResponse> observations = new ArrayList<ObservationResponse>();

	public long totalCount = 0;
	
	public long page = 0;
	
	public long limit = MyConstants.DEFAULT_LIMIT;
}
