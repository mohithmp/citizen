package services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.mongodb.morphia.query.Query;

import daos.AccountSessionDAO;
import daos.ObservationDAO;
import dtos.request.CreateObservationRequestDTO;
import dtos.request.UpdateObservationRequestDTO;
import dtos.response.CreateObservationresponseDTO;
import dtos.response.GetObservationResponseDTO;
import exceptions.MyException;
import models.FieldPOJO;
import models.Observation;
import pojo.FieldRequestPOJO;
import pojo.ObservationResponse;
import utils.ConstructResponseUtils;
import utils.MyConstants;
import utils.MyConstants.ApiFailureMessages;

public class ObservationService {

	@Inject
	ObservationDAO observationDAO;
	@Inject
	ConstructResponseUtils constructResponseUtils;
	@Inject
	AccountSessionDAO accountSessionDAO;

	public GetObservationResponseDTO getObservations(String accountId, String searchText, int page, int limit) {

		Query<Observation> query = observationDAO.getBasicQuery();

		if (accountId != null) {
			query.filter("accountId", accountId);
		}

		// TODO Search based on Tags
		if (searchText != null) {
			query.search("title", searchText);
		}

		// Get Paged list (Pagination)
		long totalCount = query.countAll();
		List<Observation> observations = query.order("-updatedTime").offset(page * limit).limit(limit).asList();

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

	public CreateObservationresponseDTO createObervation(CreateObservationRequestDTO payload) throws MyException {
		// TODO Auto-generated method stub

		for (FieldRequestPOJO field : payload.fields) {
			if (field.fieldTitle == null) {
				throw new MyException(ApiFailureMessages.INVALID_FIELD);
			}
			if (field.fieldType == null || !MyConstants.FIELD_TYPES.contains(field.fieldType)) {
				throw new MyException(ApiFailureMessages.INVALID_FIELD);
			}
		}
		Observation newObservation = new Observation();
		newObservation.setAccountId(accountSessionDAO.getAccountIdByContext());
		newObservation.setTitle(payload.title);
		newObservation.setDescription(payload.description);
		List<FieldPOJO> fd = new ArrayList<>();
		for (FieldRequestPOJO field : payload.fields) {
			FieldPOJO fp = new FieldPOJO();
			fp.setId(UUID.randomUUID().toString());
			fp.setTitle(field.fieldTitle);
			fp.setType(field.fieldType);
			fd.add(fp);
		}
		newObservation.setFields(fd);
		newObservation.setCategory(payload.category);
		newObservation.setTags(payload.tags);
		Observation observation = observationDAO.add(newObservation);

		return null;
	}

	public void updateObervation(UpdateObservationRequestDTO payload) throws MyException {
		String accountid = accountSessionDAO.getAccountIdByContext();

		Observation observation = observationDAO.findAccountObservation(accountid, payload.observationId);

		if (observation != null) {

			observationDAO.update(observation, payload.title, payload.description, payload.tags);
			
		} else {
			throw new MyException(ApiFailureMessages.INVALID_OBSERVATION_ID);
		}

	}
}
