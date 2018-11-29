package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.mongodb.morphia.query.Query;

import daos.AccountSessionDAO;
import daos.ObservationDAO;
import daos.RecordDAO;
import dtos.request.AddRecordRequestDTO;
import dtos.request.EditRecordRequestDTO;
import dtos.response.GetRecordResponseDTO;
import exceptions.MyException;
import models.FieldPOJO;
import models.Observation;
import models.Record;
import utils.MyConstants.ApiFailureMessages;

public class RecordService {

	@Inject
	AccountSessionDAO accountSessionDAO;
	@Inject
	ObservationDAO observationDAO;
	@Inject
	RecordDAO recordDAO;

	public void addRecord(AddRecordRequestDTO payload) throws MyException {

		String accountId = accountSessionDAO.getAccountIdByContext();

		Observation observation = observationDAO.findAccountObservation(accountId, payload.observationId);

		if (observation == null) {
			throw new MyException(ApiFailureMessages.OBSERVATION_DOESNT_EXIST);
		}

		// Get field names in a list
		List<String> fieldNames = new ArrayList<>();
		for (FieldPOJO field : observation.getFields()) {
			fieldNames.add(field.getTitle());
		}

		// Validate field names
		for (Entry<String, Object> record : payload.data.entrySet()) {
			if (!fieldNames.contains(record.getKey())) {
				throw new MyException(ApiFailureMessages.RECORD_INVALID_FIELD);
			}
			// TODO Validate Field Type
		}

		// Categorize Records and distribute in multiple record tables
		switch (observation.getCategory()) {
		default:
			recordDAO.add(payload.observationId, payload.data, true);
			break;
		}

	}

	public void editRecord(EditRecordRequestDTO payload) throws MyException {
		String accountId = accountSessionDAO.getAccountIdByContext();

		Observation observation = observationDAO.findAccountObservation(accountId, payload.observationId);

		if (observation == null) {
			throw new MyException(ApiFailureMessages.OBSERVATION_DOESNT_EXIST);
		}

		// TODO Get field names in a list

		switch (observation.getCategory()) {

		default:
			Record rec = recordDAO.findRecord(payload.recordId, payload.observationId);

			if (rec == null) {
				throw new MyException(ApiFailureMessages.INVALID_RECORD);
			} else {
				recordDAO.edit(rec, payload.data);
			}
			break;
		}
	}

	public GetRecordResponseDTO getRecord(String observationId, int page, int limit) throws MyException {

		Observation observation = observationDAO.find(observationId);

		List<Record> records = new ArrayList<Record>();
		long totalCount = 0;
		// Categorize Records and get records
		switch (observation.getCategory()) {
		default:
			Query<Record> query = recordDAO.getBasicQuery();
			records = query.filter("observationId", observationId).order("-updatedTime").offset(page * limit)
					.limit(limit).asList();
			totalCount = query.countAll();
			break;
		}

		GetRecordResponseDTO response = new GetRecordResponseDTO();
		response.records = records;
		response.totalCount = totalCount;

		return response;
	}

}
