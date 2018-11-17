package controllers;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import actions.authentication.AuthenticateAccount;
import actions.authentication.ValidateAccountAccess.AccountAccessValidation;
import actions.jsonrequestvalidation.ValidateJson;
import dtos.request.ImportGoogleSheetRequestDTO;
import play.mvc.BodyParser;
import play.mvc.Result;
import pojo.ObservationResponse;
import services.ImportService;
import utils.CustomObjectMapper;
import utils.MyConstants.ACCOUNT_TYPE;
import utils.MyConstants.ApiSuccessResponse;

public class ImportController extends BaseController {

	@Inject
	private CustomObjectMapper customObjectMapper;
	@Inject
	private ImportService importService;

	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(ObservationResponse.class)
	@AuthenticateAccount()
	@AccountAccessValidation(ACCOUNT_TYPE.RESEARCHER)
	public CompletionStage<Result> importGoogleSheet() {
		JsonNode inputData = request().body().asJson();
		try {

			ImportGoogleSheetRequestDTO payload = customObjectMapper.getInstance().convertValue(inputData,
					ImportGoogleSheetRequestDTO.class);

			importService.importGoogleSheet(payload);

		} catch (Exception e) {
			return failureResponsePromise(e);
		}

		return successResponsePromise(ApiSuccessResponse.SUCCESS);
	}

}
