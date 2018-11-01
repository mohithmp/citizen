package exceptions;

import play.api.Play;
import utils.Languages;

public class MyException extends Exception {

	private String message;

    private String systemMessage;

    private Languages languages = Play.current().injector().instanceOf(Languages.class);

	public MyException(String message) {
        this.message = languages.getLocalizedString(message);
	}

	public MyException(String message, Object... args) {
		this.message = languages.getLocalizedString(message, args);
	}

	public MyException(String message, String sysMsg) {
		this.message = languages.getLocalizedString(message);
        this.systemMessage = sysMsg;
	}

	public String getMessage() {
		return this.message;
	}

    public String getSystemMessage() {
        return this.systemMessage;
    }
}
