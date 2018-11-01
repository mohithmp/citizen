
package services;

import javax.inject.Inject;

import daos.AccountDAO;
import daos.AccountSessionDAO;
import dtos.request.AccountSignUpRequestDTO;
import dtos.response.AccountResponseDTO;
import exceptions.MyException;
import models.Account;
import models.AccountSession;
import utils.CustomObjectMapper;
import utils.MyConstants.ApiFailureMessages;
import utils.PasswordEncryptDecrypt;

public class AccountService {

	@Inject
	AccountDAO accountDAO;
	@Inject
	AccountSessionDAO accountSessionDAO;
	@Inject
	CustomObjectMapper mapper;
	@Inject
	PasswordEncryptDecrypt passwordEncrypt;

	public AccountResponseDTO signUpAccount(AccountSignUpRequestDTO payload) throws MyException {

		if (accountDAO.findByEmail(payload.email) != null) {
			throw new MyException(ApiFailureMessages.EMAIL_ALREADY_EXIST);
		}

		/* Create Account */
		Account newAccount = new Account();
		newAccount.setName(payload.name);
		newAccount.setEmail(payload.email);
		newAccount.setAccountType(payload.accountType);

		/* Hash the password */
		newAccount.setPassword(passwordEncrypt.generatePasswordHash(payload.password));
		newAccount = accountDAO.add(newAccount);

		/* Create Account Session */
		AccountSession accountSession = accountSessionDAO.create(newAccount, payload);

		return constructAccountResponse(newAccount, accountSession);
	}

	public AccountResponseDTO signInAccount(AccountSignUpRequestDTO payload) throws MyException {

		/* Find Account */
		Account account = accountDAO.findByEmail(payload.email);

		if (account == null) {
			throw new MyException(ApiFailureMessages.ACCOUNT_DOESNT_EXIST);
		}

		String encryptedPassword = passwordEncrypt.generatePasswordHash(payload.password);
		if (!passwordEncrypt.isPasswordSame(account.getPassword(), encryptedPassword)) {
			throw new MyException(ApiFailureMessages.INVALID_PASSWORD);
		}

		/* Create Account Session */
		AccountSession accountSession = accountSessionDAO.create(account, payload);

		return constructAccountResponse(account, accountSession);
	}

	private AccountResponseDTO constructAccountResponse(Account account, AccountSession accountSession) {

		AccountResponseDTO accountResponse = new AccountResponseDTO();

		accountResponse.accountId = account.getAccountId();
		accountResponse.name = account.getName();
		accountResponse.accountType = account.getAccountType();
		accountResponse.email = account.getEmail();

		if (accountSession != null) {
			accountResponse.token = accountSession.getToken();
		}

		return accountResponse;
	}

}
