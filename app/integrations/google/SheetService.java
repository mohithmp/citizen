package integrations.google;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import play.Environment;

public class SheetService {

	@Inject
	private Environment environment;

	private final String APPLICATION_NAME = "CitizenSciencePrototype";
	private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private final String TOKENS_DIRECTORY_PATH = "tokens";

	/**
	 * Global instance of the scopes required by this quickstart. If modifying
	 * these scopes, delete your previously saved tokens/ folder.
	 */
	private final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
	private final String CREDENTIALS_FILE_PATH = "google-credentials.json";

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT
	 *            The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException
	 *             If the credentials.json file cannot be found.
	 */
	private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = environment.resourceAsStream(CREDENTIALS_FILE_PATH);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	private List<HashMap<Object, Object>> getContent(String spreadsheetId, String SheetName, Credential c)
			throws GeneralSecurityException, IOException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, c).setApplicationName(APPLICATION_NAME)
				.build();
		ValueRange response = service.spreadsheets().values().get(spreadsheetId, SheetName).execute();
		List<List<Object>> values = response.getValues();

		List<HashMap<Object, Object>> output = new ArrayList<HashMap<Object, Object>>();
		if (values == null || values.isEmpty()) {
			System.out.println("No data found.");
		} else {
			List<?> keys = values.get(0);
			values.remove(0);
			for (List<?> row : values) {
				int cols = row.size();
				// Print columns A and E, which correspond to indices 0 and 4.
				HashMap<Object, Object> map = new HashMap<>();
				for (int i = 0; i < cols; i++) {
					map.put(keys.get(i), row.get(i));
				}
				output.add(map);
			}
		}
		return output;
	}

	/**
	 * Prints the names and majors of students in a sample spreadsheet:
	 * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
	 * 
	 * @return
	 */
	public List<HashMap<Object, Object>> getSheetData() throws IOException, GeneralSecurityException {
		// Build a new authorized API client service.
		final String spreadsheetId = "1TE0pcgim6ETurSNalMlHlAIKiS1gF60tcdfecOxWx_U";
		final String range = "Sheet1";
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		return getContent(spreadsheetId, range, getCredentials(HTTP_TRANSPORT));
	}
}
