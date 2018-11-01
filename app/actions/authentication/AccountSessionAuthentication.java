package actions.authentication;

import controllers.BaseController;
import exceptions.MyException;
import models.AccountSession;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.AuthenticatorService;
import utils.MyConstants.*;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class AccountSessionAuthentication extends Action<AuthenticateAccount> {

	@Inject
	private BaseController baseController;

	@Inject
	private AuthenticatorService authenticatorService;

	@Override
	public CompletionStage<Result> call(Http.Context ctx) {
		try {
			if (isSessionValid(ctx)) {
				// Set custom attributes in Http
				String accessToken = ctx.request().getHeader(ApiRequestHeaders.SESSION_TOKEN_HEADER);

				if (accessToken != null) {
					AccountSession accountSession = authenticatorService.getAccountSession(accessToken);
					ctx.args.put(ContextConstants.ACCOUNT_ID, accountSession.getAccountId());
					ctx.args.put(ContextConstants.ACCOUNT_TYPE, accountSession.getAccountType());
					ctx.args.put(ContextConstants.ACCOUNT_SESSION, accountSession.getToken());
				}
				return delegate.call(ctx);
			}
		} catch (Exception e) {
			Logger.info("Exception while authenticating user " + e.getMessage());
		}

		return baseController.failureResponsePromise(new MyException(ApiFailureMessages.SESSION_INVALID));
	}

	public boolean isSessionValid(Http.Context ctx) {
		boolean isPublicApi = configuration.isPublicApi();
		String accessToken = ctx.request().getHeader(ApiRequestHeaders.SESSION_TOKEN_HEADER);

		return (isPublicApi && accessToken == null)
				|| (accessToken != null && authenticatorService.getAccountSession(accessToken) != null);
	}

}