package services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import daos.AccountSessionDAO;
import daos.ObservationDAO;
import daos.RecordDAO;
import dtos.request.ImportGoogleSheetRequestDTO;
import exceptions.MyException;
import integrations.google.GoogleSheetApiService;
import integrations.google.ImportDataPOJO;
import models.FieldPOJO;
import models.Observation;
import play.libs.Json;
import utils.MyConstants.ApiFailureMessages;
import utils.MyConstants.FIELD_TYPE;

public class ImportService {

	@Inject
	private GoogleSheetApiService googleSheetApiService;
	@Inject
	ObservationDAO observationDAO;
	@Inject
	AccountSessionDAO accountSessionDAO;
	@Inject
	RecordDAO recordDAO;

	public void importGoogleSheet(ImportGoogleSheetRequestDTO payload)
			throws IOException, GeneralSecurityException, MyException {

		String accountId = accountSessionDAO.getAccountIdByContext();
		Observation observation = observationDAO.findAccountObservation(accountId, payload.observationId);
		if (observation == null) {
			throw new MyException(ApiFailureMessages.OBSERVATION_DOESNT_EXIST);
		}

		ImportDataPOJO googleSheetData = googleSheetApiService.importSheet(payload.spreadSheetId, payload.range);

		// Check Observation records are empty
		if (recordDAO.isRecordExists(payload.observationId)) {
			throw new MyException(ApiFailureMessages.CANNOT_IMPORT_WITH_EXISTING_RECORDS);
		}

		// Update Fields in Observation
		List<FieldPOJO> fields = new ArrayList<>();
		for (String key : googleSheetData.keys) {
			FieldPOJO field = new FieldPOJO();
			field.setId(UUID.randomUUID().toString());
			field.setTitle(key);
			field.setType(FIELD_TYPE.UNKNOWN);
			fields.add(field);
		}
		observationDAO.update(observation, fields);

		// Add data in MongoDB
		for (HashMap<String, Object> data : googleSheetData.data) {
			recordDAO.add(payload.observationId, data, true);
		}

	}

}
