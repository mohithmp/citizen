package controllers;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import actions.authentication.AuthenticateAccount;
import dtos.response.GetObservationResponseDTO;
import play.mvc.Result;
import services.ObservationService;
import utils.CustomObjectMapper;
import utils.MyConstants;

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

}
