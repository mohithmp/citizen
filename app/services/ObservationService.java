package services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.mongodb.morphia.query.Query;

import daos.ObservationDAO;
import dtos.response.GetObservationResponseDTO;
import models.Observation;
import pojo.ObservationResponse;
import utils.ConstructResponseUtils;

public class ObservationService {

	@Inject
	ObservationDAO observationDAO;
	@Inject
	ConstructResponseUtils constructResponseUtils;

	public GetObservationResponseDTO getObservations(String accountId, String searchText, int page, int limit) {

		Query<Observation> query = observationDAO.getBasicQuery();

		if (accountId != null) {
			query.filter("accountId", accountId);
		}

		// TODO Search based on Tags
		if (searchText != null) {
			query.filter("title", searchText);
		}

		// Get Paged list (Pagination)
		long totalCount = query.countAll();
		List<Observation> observations = query.offset(page * limit).limit(limit).asList();

		// Construct Response
		List<ObservationResponse> observationResponseList = new ArrayList<>();
		for (Observation observation : observations) {
			observationResponseList.add(constructResponseUtils.constructObservationResponse(observation));
		}

		GetObservationResponseDTO response = new GetObservationResponseDTO();
		response.observations = observationResponseList;
		response.totalCount = totalCount;

		return response;
	}

}
