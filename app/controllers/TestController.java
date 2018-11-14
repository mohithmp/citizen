package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import integrations.google.SheetService;
import play.mvc.Result;

public class TestController extends BaseController {

	@Inject
	private SheetService sheetService;

	public CompletionStage<Result> test() {

		List<HashMap<Object, Object>> response;
		try {

			response = sheetService.getSheetData();

		} catch (Exception e) {
			return failureResponsePromise(e);
		}

		return successResponsePromise(response);
	}
}
