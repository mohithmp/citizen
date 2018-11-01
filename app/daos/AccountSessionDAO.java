package daos;

import java.util.Date;
import java.util.UUID;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import dtos.request.AccountSignUpRequestDTO;
import models.Account;
import models.AccountSession;
import play.Logger;
import play.mvc.Http;
import utils.MongoConnection;
import utils.MyConstants.ContextConstants;
import utils.MyConstants.DeviceType;

public class AccountSessionDAO {

	private static Datastore ds = MongoConnection.getDS();

	public AccountSession create(Account account, AccountSignUpRequestDTO payload) {

		if (payload.deviceId != null) {
			/* Delete session for duplicate device id */
			Query<AccountSession> query = ds.createQuery(AccountSession.class);
			query.field("deviceId").equal(payload.deviceId);
			ds.findAndDelete(query);
		}

		if (payload.deviceToken != null) {
			/* Delete session for duplicate device token */
			Query<AccountSession> query = ds.find(AccountSession.class);
			query.field("deviceToken").equal(payload.deviceToken);
			ds.findAndDelete(query);
		}

		if (payload.deviceType != DeviceType.ANDROID && payload.deviceType != DeviceType.IOS
				&& payload.deviceType != DeviceType.BROWSER) {
			payload.deviceType = DeviceType.UNKNOWN;
		}

		AccountSession accountSession = new AccountSession();

		accountSession.setAccountId(account.getAccountId());
		accountSession.setAccountType(account.getAccountType());
		String token = UUID.randomUUID().toString();
		accountSession.setToken(token);
		accountSession.setDeviceToken(payload.deviceToken);
		accountSession.setDeviceType(payload.deviceType);

		accountSession.setUpdatedTime(new Date());
		accountSession.setCreatedTime(new Date());

		if (payload.deviceType == DeviceType.BROWSER || payload.deviceType == DeviceType.UNKNOWN) {
			accountSession.setDeviceId(token);
		} else {
			/*
			 * registering device with push notification server
			 */
			if (payload.deviceToken != null) {
				try {
					// Register with GCM
				} catch (Exception e) {
					Logger.info(e.getMessage());
				}
			}
			accountSession.setDeviceId(payload.deviceId);
		}

		ds.save(accountSession);
		return accountSession;
	}

	public AccountSession findByDeviceToken(String deviceToken) {
		return ds.find(AccountSession.class).filter("deviceToken", deviceToken).get();
	}

	public AccountSession findByToken(String accessToken) {
		return ds.find(AccountSession.class).filter("token", accessToken).get();
	}

	public String getAccountIdByContext() {
		String accountId = (String) Http.Context.current().args.get(ContextConstants.ACCOUNT_ID);
		return accountId;
	}

	public String getAccountSessionByContext() {
		String accountSession = (String) Http.Context.current().args.get(ContextConstants.ACCOUNT_SESSION);
		return accountSession;
	}

}
