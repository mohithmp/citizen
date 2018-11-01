package utils;

import controllers.BaseController;
import exceptions.MyException;
import play.http.HttpErrorHandler;
import play.mvc.Http.RequestHeader;
import play.mvc.Http.Status;
import utils.MyConstants.*;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ErrorHandler implements HttpErrorHandler {

    @Inject
    BaseController baseController;

    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {

        String msg = null;

        switch (statusCode) {
            case Status.NOT_FOUND:
                msg = ApiFailureMessages.INVALID_API_CALL;
                break;
            case Status.BAD_REQUEST:
            default:
                msg = ApiFailureMessages.INVALID_JSON_REQUEST;
                break;
        }

        return baseController.failureResponsePromise(new MyException(msg));
    }

    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        exception.printStackTrace();
        return CompletableFuture.completedFuture(
                Results.internalServerError("A server error occurred: " + exception.getMessage())
        );
    }
}
