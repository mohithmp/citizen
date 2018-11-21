package utils;

import java.util.Arrays;
import java.util.List;

public class MyConstants {

	public static final int DEFAULT_LIMIT = 10;

	public class ContextConstants {
		public static final String ACCOUNT_ID = "ACCOUNT_ID";
		public static final String ACCOUNT_SESSION = "ACCOUNT_SESSION";
		public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
	}

	public class DeviceType {
		public static final int BROWSER = 1;
		public static final int IOS = 2;
		public static final int ANDROID = 3;
		public static final int UNKNOWN = 4;
	}

	public class ApiRequestHeaders {
		public static final String SESSION_TOKEN_HEADER = "Session-Token";
	}

	public class ApiResponseKey {
		public static final String MESSAGE = "message";
	}

	public class ApiSuccessResponse {
		public static final String SUCCESS = "success";
	}

	public class ApiFailureMessages {
		public static final String INVALID_JSON_REQUEST = "invalid.json.request";
		public static final String INVALID_API_CALL = "invalid.api.call";
		public static final String ACCESS_FORBIDDEN = "access.forbidden";
		public static final String TECHNICAL_ERROR = "technical.error";
		public static final String INVALID_INPUT = "invalid.input";
		public static final String TYPE_MISMATCH = "type.mismatch";
		public static final String MIN_ITEMS_CONSTRAINT = "min.items";
		public static final String MIN_LENGTH_VALIDATION = "min.length.validation";
		public static final String MAX_LENGTH_VALIDATION = "max.length.validation";
		public static final String PATTERN_VALIDATION = "pattern.validation";
		public static final String FIELD_MISSING = "field.missing";
		public static final String INVALID_EMAIL = "invalid.email.format";
		public static final String SESSION_INVALID = "session.invalid";
		public static final String INVALID_PASSWORD = "password.invalid";

		public static final String ACCOUNT_DOESNT_EXIST = "account.doesnt.exist";
		public static final String EMAIL_ALREADY_EXIST = "email.already.exist";
		public static final String INVALID_ACCOUNT_TYPE = "invalid.account.type";
		public static final String OBSERVATION_DOESNT_EXIST = "observation.doesnt.exist";
		public static final String RECORD_INVALID_FIELD = "record.invalid.field";
		public static final String INVALID_FIELD = "invalid.field";
		public static final String CANNOT_IMPORT_WITH_EXISTING_RECORDS = "cannot.import.with.existing.records";
		public static final String INVALID_OBSERVATION_ID = "invalid.observation.id";
		public static final String OBSERVATION_ID_NOT_FOUND = "observation.id.not.found";
	}

	public class PushNotificationMessages {

	}

	public class ACCOUNT_TYPE {
		public static final int RESEARCHER = 1;
		public static final int USER = 2;
	}

	public static List<Integer> FIELD_TYPES = Arrays.asList(FIELD_TYPE.TEXT, FIELD_TYPE.NUMBER, FIELD_TYPE.IMAGE);

	public class FIELD_TYPE {
		public static final int UNKNOWN = 0;
		public static final int TEXT = 1;
		public static final int NUMBER = 2;
		public static final int IMAGE = 3;
	}

}
