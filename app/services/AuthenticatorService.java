package services;

import javax.inject.Inject;

import daos.AccountSessionDAO;
import models.AccountSession;

public class AuthenticatorService {

	@Inject
	private AccountSessionDAO accountSessionDAO;

	public boolean isSessionValid(String accessToken, boolean isPublicApi) {
		return (isPublicApi && accessToken == null)
				|| (accessToken != null && accountSessionDAO.findByToken(accessToken) != null);
	}

	public AccountSession getAccountSession(String accessToken) {
		return accountSessionDAO.findByToken(accessToken);
	}

}
