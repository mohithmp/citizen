package daos;

import java.util.Date;
import java.util.UUID;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

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
		newAccount.setCreatedTime(new Date().getTime());
		newAccount.setUpdatedTime(new Date().getTime());
		ds.save(newAccount);
		return newAccount;
	}

	public void update(String accountId, String name) {
		Query<Account> account = ds.find(Account.class).filter("accountId", accountId);

		UpdateOperations<Account> ops = ds.createUpdateOperations(Account.class);
		if (name != null) {
			ops.set("researcher.name", name);
		}
		ops.set("updatedTime", new Date().getTime());

		ds.update(account, ops);
	}

}
