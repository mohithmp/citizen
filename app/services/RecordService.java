package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import daos.AccountSessionDAO;
import daos.ObservationDAO;
import daos.RecordDAO;
import dtos.request.AddRecordRequestDTO;
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
		}

		// Categorize Records and distribute in multiple record tables
		switch (observation.getCategory()) {
		default:
			Record record = recordDAO.add(payload);
			break;
		}

	}

}
