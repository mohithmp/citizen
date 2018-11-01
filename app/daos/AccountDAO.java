package daos;

import java.util.Date;
import java.util.UUID;

import org.mongodb.morphia.Datastore;

import exceptions.MyException;
import models.Account;
import utils.MongoConnection;
import utils.MyConstants.ApiFailureMessages;

public class AccountDAO {

	private static Datastore ds = MongoConnection.getDS();

	public Account find(String accountId) throws MyException {
		Account account = ds.find(Account.class).filter("accountId", accountId).get();
		if (account == null) {
			throw new MyException(ApiFailureMessages.ACCOUNT_DOESNT_EXIST);
		}
		return account;
	}

	public Account findByEmail(String email) throws MyException {
		Account account = ds.find(Account.class).filter("email", email).get();
		return account;
	}

	public Account add(Account newAccount) {
		newAccount.setAccountId(UUID.randomUUID().toString());
		newAccount.setCreatedTime(new Date());
		newAccount.setUpdatedTime(new Date());
		ds.save(newAccount);
		return newAccount;
	}

}
