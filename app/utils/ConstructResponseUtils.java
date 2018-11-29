package utils;

import dtos.response.AccountResponseDTO;
import models.Account;
import models.AccountSession;
import models.Observation;
import models.Record;
import pojo.ObservationResponse;
import pojo.RecordResponse;
import utils.MyConstants.ACCOUNT_TYPE;

public class ConstructResponseUtils {

	public AccountResponseDTO constructAccountResponse(Account account, AccountSession accountSession) {

		AccountResponseDTO accountResponse = new AccountResponseDTO();

		accountResponse.accountId = account.getAccountId();
		accountResponse.accountType = account.getAccountType();
		accountResponse.email = account.getEmail();

		// Set Profile Details
		if (account.getAccountType() == ACCOUNT_TYPE.RESEARCHER) {
			accountResponse.name = account.getResearcher().getName();
			accountResponse.points = account.getResearcher().getPoints();
		} else if (account.getAccountType() == ACCOUNT_TYPE.USER) {
			accountResponse.name = account.getUser().getName();
			accountResponse.points = account.getUser().getPoints();
		}

		if (accountSession != null) {
			accountResponse.token = accountSession.getToken();
		}

		return accountResponse;
	}

	public ObservationResponse constructObservationResponse(Observation observation) {

		ObservationResponse response = new ObservationResponse();

		response.observationId = observation.getObservationId();
		response.title = observation.getTitle();
		response.description = observation.getDescription();
		response.fields = observation.getFields();
		response.category = observation.getCategory();
		response.tags = observation.getTags();
		response.metadata = observation.getMetadata();
		response.isDeleted = observation.getIsDeleted();
		response.createdTime = observation.getCreatedTime();
		response.updatedTime = observation.getUpdatedTime();

		return response;
	}
	
	public RecordResponse constructRecordResponse(Record record) {
		
		RecordResponse response = new RecordResponse();
		
		response.recordId = record.getRecordId();
		response.observationId = record.getObservationId();
		response.data = record.getData();
		response.createdTime = record.getCreatedTime();
		response.updatedTime = record.getUpdatedTime();
		return response;
		
	}

}
