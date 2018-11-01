package actions.jsonrequestvalidation;

import controllers.BaseController;
import exceptions.MyException;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import utils.MyConstants.ApiFailureMessages;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public class JsonRequestValidation extends Action<ValidateJson> {

	@Inject
	FormFactory formFactory;

    @Inject
    BaseController baseController;

	@Override
	public CompletionStage<Result> call(Http.Context context) {
		CompletionStage<Result> result = validateRequest();
		if (result == null) {
			result = delegate.call(context);
		}
		return result;
	}

	public CompletionStage<Result> validateRequest() {
		Class<?> value = configuration.value();
		Form<?> form = formFactory.form(value).bind(Http.Context.current().request().body().asJson());

		if (form.hasErrors()) {
            Logger.info(form.errors().toString());
            Map<String, List<ValidationError>> errorMap = form.errors();
            String errorKey = errorMap.keySet().iterator().next();
            ValidationError validationError = errorMap.get(errorKey).iterator().next();
            String message = validationError.message();
            Long arguments = 0L;

            if (!validationError.arguments().isEmpty()) {
                arguments = (Long) validationError.arguments().get(0);
            }

            String errorMessage;
            MyException myException;
            errorKey = capitalizeFirstLetter(errorKey);

            switch (message) {
                case "error.required":
                    errorMessage = ApiFailureMessages.FIELD_MISSING;
                    myException = new MyException(errorMessage, errorKey, arguments);
					break;
				case "error.email":
					errorMessage = ApiFailureMessages.INVALID_EMAIL;
                    myException = new MyException(errorMessage, errorKey, arguments);
					break;
                case "error.minLength":
                    errorMessage = ApiFailureMessages.MIN_LENGTH_VALIDATION;
                    myException = new MyException(errorMessage, errorKey, arguments);
                    break;
                case "error.maxLength":
                    errorMessage = ApiFailureMessages.MAX_LENGTH_VALIDATION;
                    myException = new MyException(errorMessage, errorKey, arguments);
                    break;
                case "error.pattern":
                    errorMessage = ApiFailureMessages.PATTERN_VALIDATION;
                    myException = new MyException(errorMessage, errorKey, arguments);
                    break;
                default:
                    errorMessage = ApiFailureMessages.INVALID_INPUT;
                    myException = new MyException(errorMessage, errorKey, arguments);
                    break;
            }

            return baseController.failureResponsePromise(myException);
		}

		return null;
	}

	private String capitalizeFirstLetter(String str) {
	    return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
