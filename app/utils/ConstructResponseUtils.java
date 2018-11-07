package utils;

import dtos.response.AccountResponseDTO;
import models.Account;
import models.AccountSession;
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

}
