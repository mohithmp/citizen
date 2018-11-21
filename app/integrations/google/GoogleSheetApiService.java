package integrations.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleSheetApiService {

	private String GOOGLE_API_KEY = "AIzaSyBZQTqZc49g5lm0XfYlCGCU5CvIm9P9BYs";

	private String READ_GOOGLE_SHEET_API = "https://sheets.googleapis.com/v4/spreadsheets/";

	/*
	 * String spreadSheetId = "1TE0pcgim6ETurSNalMlHlAIKiS1gF60tcdfecOxWx_U";
	 * String range = "Sheet1";
	 */

	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public ImportDataPOJO importSheet(String spreadSheetId, String range) throws IOException {

		if (range == null) {
			range = "Sheet1";
		}

		String url = READ_GOOGLE_SHEET_API + spreadSheetId + "/values/" + range + "?key=" + GOOGLE_API_KEY;

		InputStream is = new URL(url).openStream();
		List<HashMap<String, Object>> returnList = new ArrayList<HashMap<String, Object>>();
		
		ImportDataPOJO data = new ImportDataPOJO();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			JSONArray l = (JSONArray) json.get("values");
			JSONArray jsonKeys = (JSONArray) l.get(0);
			List<String> keys = new ArrayList<String>();
			
			for (int i = 0; i < jsonKeys.length(); i++) {
				keys.add(jsonKeys.getString(i));
			}

			for (int j = 1; j < l.length(); j++) {
				HashMap<String, Object> temphash = new HashMap<>();
				JSONArray temp = (JSONArray) l.get(j);
				for (int i = 0; i < temp.length(); i++) {
					temphash.put(keys.get(i), temp.get(i));
				}
				returnList.add(temphash);
			}
			
			
			data.data = returnList;
			data.keys = keys;

		} finally {
			is.close();
		}

	

		return data;
	}

}
