package actions.authentication;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import actions.authentication.ValidateAccountAccess.AccountAccessValidation;
import controllers.BaseController;
import exceptions.MyException;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import utils.MyConstants.ApiFailureMessages;
import utils.MyConstants.ContextConstants;

public class AccountAccessValidator extends Action<AccountAccessValidation> {

	@Inject
	private BaseController baseController;

	/**
	 * Delegates the call further if its valid input
	 */
	@Override
	public CompletionStage<Result> call(Http.Context context) {

		int[] givenAccountTypes = configuration.value();

		if (context.args.containsKey(ContextConstants.ACCOUNT_TYPE)) {
			int currentAccountType = Integer.parseInt(context.args.get(ContextConstants.ACCOUNT_TYPE).toString());

			boolean isValid = false;
			for (int givenAccountType : givenAccountTypes) {
				if (currentAccountType == givenAccountType) {
					isValid = true;
				}
			}

			if (!isValid) {
				return baseController.failureResponsePromise(new MyException(ApiFailureMessages.ACCESS_FORBIDDEN));
			}
		}
		return delegate.call(context);
	}

}
