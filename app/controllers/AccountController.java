package controllers;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import actions.authentication.AuthenticateAccount;
import actions.authentication.ValidateAccountAccess.AccountAccessValidation;
import actions.jsonrequestvalidation.ValidateJson;
import dtos.request.AccountSignInRequestDTO;
import dtos.request.AccountSignUpRequestDTO;
import dtos.request.UpdateResearcherRequestDTO;
import dtos.response.AccountResponseDTO;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.AccountService;
import utils.CustomObjectMapper;
import utils.MyConstants.ACCOUNT_TYPE;
import utils.MyConstants.ApiSuccessResponse;

public class AccountController extends BaseController {

	@Inject
	private CustomObjectMapper customObjectMapper;
	@Inject
	private AccountService accountService;

	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(AccountSignUpRequestDTO.class)
	public CompletionStage<Result> signUp() {
		JsonNode inputData = request().body().asJson();

		AccountResponseDTO response = new AccountResponseDTO();
		try {
			AccountSignUpRequestDTO payload = customObjectMapper.getInstance().convertValue(inputData,
					AccountSignUpRequestDTO.class);
			response = accountService.signUpAccount(payload);

		} catch (Exception e) {
			return failureResponsePromise(e);
		}

		return successResponsePromise(response);
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(AccountSignInRequestDTO.class)
	public CompletionStage<Result> signIn() {
		JsonNode inputData = request().body().asJson();
		AccountSignUpRequestDTO payload = new AccountSignUpRequestDTO();

		AccountResponseDTO response = new AccountResponseDTO();
		try {
			payload = customObjectMapper.getInstance().convertValue(inputData, AccountSignUpRequestDTO.class);
			response = accountService.signInAccount(payload);

		} catch (Exception e) {
			return failureResponsePromise(e);
		}

		return successResponsePromise(response);
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ValidateJson(UpdateResearcherRequestDTO.class)
	@AuthenticateAccount(isPublicApi = true)
	@AccountAccessValidation(ACCOUNT_TYPE.RESEARCHER)
	public CompletionStage<Result> updateResearcher() {
		JsonNode inputData = request().body().asJson();
		UpdateResearcherRequestDTO payload = new UpdateResearcherRequestDTO();

		try {
			payload = customObjectMapper.getInstance().convertValue(inputData, UpdateResearcherRequestDTO.class);
			accountService.updateResearcher(payload);

		} catch (Exception e) {
			return failureResponsePromise(e);
		}

		return successResponsePromise(ApiSuccessResponse.SUCCESS);
	}

}
