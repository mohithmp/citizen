package services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import integrations.google.SheetService;

public class ImportService {

	@Inject
	private SheetService sheetService;

	public List<HashMap<Object, Object>> importGoogleSheet() throws IOException, GeneralSecurityException {

		List<HashMap<Object, Object>> response = sheetService.getSheetData();

		return response;
	}

}
