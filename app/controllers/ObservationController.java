package controllers;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import actions.authentication.AuthenticateAccount;
import actions.authentication.ValidateAccountAccess.AccountAccessValidation;
import actions.jsonrequestvalidation.ValidateJson;
import dtos.request.AccountSignUpRequestDTO;
import dtos.request.CreateObservationRequestDTO;
import dtos.request.UpdateResearcherRequestDTO;
import dtos.response.CreateObservationresponseDTO;
import dtos.response.GetObservationResponseDTO;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.ObservationService;
import utils.CustomObjectMapper;
import utils.MyConstants;
import utils.MyConstants.ACCOUNT_TYPE;
import pojo.ObservationResponse;

public class ObservationController extends BaseController {

	@Inject
	private CustomObjectMapper customObjectMapper;
	@Inject
	private ObservationService observationService;

	@AuthenticateAccount(isPublicApi = true)
	public CompletionStage<Result> getObservations(String accountId, String searchText, int page, int limit) {

		GetObservationResponseDTO response = new GetObservationResponseDTO();
		try {

			if (limit == 0) {
				limit = MyConstants.DEFAULT_LIMIT;
			}

			response = observationService.getObservations(accountId, searchText, page, limit);
			response.page = page;
			response.limit = limit;

		} catch (Exception e) {
			return failureResponsePromise(e);
		}

		return successResponsePromise(response);
	}
	

	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(ObservationResponse.class)
	@AuthenticateAccount()
	@AccountAccessValidation(ACCOUNT_TYPE.RESEARCHER)
	public CompletionStage<Result> createObservation(){
		JsonNode inputData = request().body().asJson();
		CreateObservationresponseDTO response = new CreateObservationresponseDTO();
		try {
			
			CreateObservationRequestDTO payload = customObjectMapper.getInstance().convertValue(inputData,
					CreateObservationRequestDTO.class);
			response = observationService.createObervation(payload);
		}
		catch (Exception e) {

			return failureResponsePromise(e);
		}

		return successResponsePromise(response);
	}
		

}
