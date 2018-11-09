
package services;

import javax.inject.Inject;

import daos.AccountDAO;
import daos.AccountSessionDAO;
import dtos.request.AccountSignUpRequestDTO;
import dtos.request.UpdateResearcherRequestDTO;
import dtos.response.AccountResponseDTO;
import exceptions.MyException;
import models.Account;
import models.AccountSession;
import models.ResearcherProfilePOJO;
import models.UserProfilePOJO;
import utils.ConstructResponseUtils;
import utils.CustomObjectMapper;
import utils.MyConstants.ACCOUNT_TYPE;
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
	@Inject
	ConstructResponseUtils constructResponseUtils;

	public AccountResponseDTO signUpAccount(AccountSignUpRequestDTO payload) throws MyException {

		if (accountDAO.findByEmail(payload.email) != null) {
			throw new MyException(ApiFailureMessages.EMAIL_ALREADY_EXIST);
		}

		/* Create Account */
		Account newAccount = new Account();
		newAccount.setEmail(payload.email);
		newAccount.setAccountType(payload.accountType);

		// Set Profile Details for Researcher and User
		if (payload.accountType == ACCOUNT_TYPE.RESEARCHER) {
			ResearcherProfilePOJO researcher = new ResearcherProfilePOJO();
			researcher.setName(payload.name);
			researcher.setPoints(0);
			newAccount.setResearcher(researcher);
		} else if (payload.accountType == ACCOUNT_TYPE.USER) {
			UserProfilePOJO user = new UserProfilePOJO();
			user.setName(payload.name);
			user.setPoints(0);
			newAccount.setUser(user);
		} else {
			throw new MyException(ApiFailureMessages.INVALID_ACCOUNT_TYPE);
		}

		/* Hash the password */
		newAccount.setPassword(passwordEncrypt.generatePasswordHash(payload.password));
		newAccount = accountDAO.add(newAccount);

		/* Create Account Session */
		AccountSession accountSession = accountSessionDAO.create(newAccount, payload);

		return constructResponseUtils.constructAccountResponse(newAccount, accountSession);
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

		return constructResponseUtils.constructAccountResponse(account, accountSession);
	}

	public void updateResearcher(UpdateResearcherRequestDTO payload) throws MyException {

		String accountId = accountSessionDAO.getAccountIdByContext();

		accountDAO.update(accountId, payload.name, payload.email, payload.password);
		
	}

}
