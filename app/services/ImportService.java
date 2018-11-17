package services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import daos.AccountSessionDAO;
import daos.ObservationDAO;
import daos.RecordDAO;
import dtos.request.ImportGoogleSheetRequestDTO;
import exceptions.MyException;
import integrations.google.GoogleSheetApiService;
import models.Observation;
import utils.MyConstants.ApiFailureMessages;

public class ImportService {

	@Inject
	private GoogleSheetApiService googleSheetApiService;
	@Inject
	ObservationDAO observationDAO;
	@Inject
	AccountSessionDAO accountSessionDAO;
	@Inject
	RecordDAO recordDAO;

	public List<HashMap<String, Object>> importGoogleSheet(ImportGoogleSheetRequestDTO payload)
			throws IOException, GeneralSecurityException, MyException {

		String accountId = accountSessionDAO.getAccountIdByContext();
		Observation observation = observationDAO.findAccountObservation(accountId, payload.observationId);
		if (observation == null) {
			throw new MyException(ApiFailureMessages.OBSERVATION_DOESNT_EXIST);
		}

		List<HashMap<String, Object>> googleSheetData = googleSheetApiService.importSheet(payload.spreadSheetId,
				payload.range);

		// Check Observation records are empty
		if (recordDAO.isRecordExists(payload.observationId)) {
			throw new MyException(ApiFailureMessages.CANNOT_IMPORT_WITH_EXISTING_RECORDS);
		}

		// Add data in MongoDB
		for (HashMap<String, Object> data : googleSheetData) {
			recordDAO.add(payload.observationId, data);
		}

		return googleSheetData;
	}

}
