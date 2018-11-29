package controllers;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import actions.authentication.AuthenticateAccount;
import actions.authentication.ValidateAccountAccess.AccountAccessValidation;
import actions.jsonrequestvalidation.ValidateJson;
import dtos.request.AddRecordRequestDTO;
import dtos.request.EditRecordRequestDTO;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.RecordService;
import utils.CustomObjectMapper;
import utils.MyConstants.ACCOUNT_TYPE;
import utils.MyConstants.ApiSuccessResponse;

public class RecordController extends BaseController {

	@Inject
	private CustomObjectMapper customObjectMapper;
	@Inject
	private RecordService recordService;

	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(AddRecordRequestDTO.class)
	@AuthenticateAccount()
	@AccountAccessValidation(ACCOUNT_TYPE.RESEARCHER)
	public CompletionStage<Result> addRecord() {
		// Request Raw Json
		JsonNode inputData = request().body().asJson();

		try {
			AddRecordRequestDTO payload = customObjectMapper.getInstance().convertValue(inputData,
					AddRecordRequestDTO.class);
			recordService.addRecord(payload);

		} catch (Exception e) {
			return failureResponsePromise(e);
		}

		return successResponsePromise(ApiSuccessResponse.SUCCESS);
	}
	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(EditRecordRequestDTO.class)
	@AuthenticateAccount()
	@AccountAccessValidation(ACCOUNT_TYPE.RESEARCHER)
	public CompletionStage<Result> editRecord() {
		JsonNode inputData = request().body().asJson();
		try {
			EditRecordRequestDTO payload = customObjectMapper.getInstance().convertValue(inputData,
					EditRecordRequestDTO.class);
			recordService.editRecord(payload);
		} catch (Exception e) {
			return failureResponsePromise(e);
		}

		return successResponsePromise(ApiSuccessResponse.SUCCESS);
	}
	
}
