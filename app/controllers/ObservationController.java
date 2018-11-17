package controllers;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import actions.authentication.AuthenticateAccount;
import actions.authentication.ValidateAccountAccess.AccountAccessValidation;
import actions.jsonrequestvalidation.ValidateJson;
import dtos.request.CreateObservationRequestDTO;
import dtos.request.UpdateObservationRequestDTO;

import dtos.response.GetObservationResponseDTO;
import play.mvc.BodyParser;
import play.mvc.Result;
import pojo.ObservationResponse;
import services.ObservationService;
import utils.CustomObjectMapper;
import utils.MyConstants;
import utils.MyConstants.ACCOUNT_TYPE;

import utils.MyConstants.ApiSuccessResponse;

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
	@ValidateJson(CreateObservationRequestDTO.class)
	@AuthenticateAccount()
	@AccountAccessValidation(ACCOUNT_TYPE.RESEARCHER)
	public CompletionStage<Result> createObservation() {
		JsonNode inputData = request().body().asJson();

		ObservationResponse response = new ObservationResponse();
		try {

			CreateObservationRequestDTO payload = customObjectMapper.getInstance().convertValue(inputData,
					CreateObservationRequestDTO.class);
			response = observationService.createObervation(payload);

		} catch (Exception e) {
			return failureResponsePromise(e);
		}

		return successResponsePromise(response);
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(UpdateObservationRequestDTO.class)
	@AuthenticateAccount()
	@AccountAccessValidation(ACCOUNT_TYPE.RESEARCHER)
	public CompletionStage<Result> updateObervation() {
		JsonNode inputData = request().body().asJson();
		ObservationResponse response;
		try {
			
			UpdateObservationRequestDTO payload = customObjectMapper.getInstance().convertValue(inputData,
					UpdateObservationRequestDTO.class);
			response = observationService.updateObervation(payload);
			
		} catch (Exception e) {
			return failureResponsePromise(e);
		}

		return successResponsePromise(response);

	}

}
