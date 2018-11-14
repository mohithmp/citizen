package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import play.Environment;
import play.mvc.Result;
import services.ImportService;

public class ImportController extends BaseController {

	@Inject
	private Environment environment;
	@Inject
	private ImportService importService;

	public CompletionStage<Result> importGoogleSheet() {

		List<HashMap<Object, Object>> response;
		try {

			response = importService.importGoogleSheet();

		} catch (Exception e) {
			return failureResponsePromise(e);
		}

		return successResponsePromise(response);
	}

}
