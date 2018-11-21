package utils;

import javax.inject.Inject;

import daos.AccountDAO;
import dtos.response.AccountResponseDTO;
import exceptions.MyException;
import models.Account;
import models.AccountSession;
import models.Observation;
import pojo.ObservationResponse;
import utils.MyConstants.ACCOUNT_TYPE;

public class ConstructResponseUtils {

	@Inject
	AccountDAO accountDAO;

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

	public ObservationResponse constructObservationResponse(Observation observation) throws MyException {

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

		Account account = accountDAO.find(observation.getAccountId());
		AccountResponseDTO accountResponse = new AccountResponseDTO();
		accountResponse.accountId = account.getAccountId();
		accountResponse.email = account.getEmail();
		accountResponse.accountType = account.getAccountType();

		if (account.getAccountType() == ACCOUNT_TYPE.RESEARCHER) {
			accountResponse.name = account.getResearcher().getName();
			accountResponse.points = account.getResearcher().getPoints();
		} else if (account.getAccountType() == ACCOUNT_TYPE.USER) {
			accountResponse.name = account.getUser().getName();
			accountResponse.points = account.getUser().getPoints();
		}

		response.account = accountResponse;

		return response;
	}

}
