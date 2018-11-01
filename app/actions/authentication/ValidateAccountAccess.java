package actions.authentication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.mvc.With;

public interface ValidateAccountAccess {

	@With(AccountAccessValidator.class)
	@Target({ ElementType.FIELD, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface AccountAccessValidation {
		int[] value();
	}

}
