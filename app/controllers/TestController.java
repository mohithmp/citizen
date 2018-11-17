package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import integrations.google.GoogleSheetApiService;
import integrations.google.SheetService;
import play.mvc.Result;

public class TestController extends BaseController {

	@Inject
	private SheetService sheetService;
	@Inject
	private GoogleSheetApiService googleSheetApiService;

	public CompletionStage<Result> test() {

		List<HashMap<Object, Object>> response;
		try {

		} catch (Exception e) {
			return failureResponsePromise(e);
		}

		return successResponsePromise();
	}
}
