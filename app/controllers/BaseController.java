package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dtos.response.FailureResponseTemplate;
import exceptions.MyException;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.MyConstants.ApiFailureMessages;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class BaseController extends Controller {

	private int status;

	public BaseController() {

	}

	public Result successResponse(Object object) {
		return ok(json(object));
	}

	public Result successResponse() {
		return noContent();
	}

	public CompletionStage<Result> successResponsePromise(String message) {
		ObjectNode response = Json.newObject();
		response.put("message", message);
		return promise(successResponse(response));
	}

	public CompletionStage<Result> successResponsePromise(Object object) {
		return promise(successResponse(object));
	}

	public CompletionStage<Result> successResponsePromise() {
		return promise(successResponse());
	}

	public Result failureResponse(Exception e) {
		FailureResponseTemplate failureResponseTemplate = new FailureResponseTemplate();
		String message;
		String systemMessage;
		Result result;
		if (e instanceof MyException) {
			message = e.getMessage();
			systemMessage = ((MyException) e).getSystemMessage();
		} else {
			message = ApiFailureMessages.TECHNICAL_ERROR;
			systemMessage = e.getMessage();
		}

		classifyResponse(message);
		failureResponseTemplate.error = message;
		failureResponseTemplate.systemError = systemMessage;

		switch (status) {
		case Http.Status.UNAUTHORIZED:
			result = unauthorized(json(failureResponseTemplate));
			break;
		case Http.Status.FORBIDDEN:
			result = forbidden(json(failureResponseTemplate));
			break;
		case Http.Status.NOT_FOUND:
			result = notFound(json(failureResponseTemplate));
			break;
		default:
			result = badRequest(json(failureResponseTemplate));
		}

		return result;
	}

	public CompletionStage<Result> failureResponsePromise(Exception e) {
		return promise(failureResponse(e));
	}

	public CompletionStage<Result> promise(Result result) {
		return CompletableFuture.supplyAsync(() -> result);
	}

	private void classifyResponse(String message) {
		if (message.equals(ApiFailureMessages.ACCESS_FORBIDDEN)) {
			status = Http.Status.FORBIDDEN;
		} else if (message.equals(ApiFailureMessages.INVALID_API_CALL)) {
			status = Http.Status.NOT_FOUND;
		}
	}

	private JsonNode json(Object data) {
		return Json.toJson(data);
	}
}
