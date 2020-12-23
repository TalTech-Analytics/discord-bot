package ee.taltech.discord.analytics.bot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LockedResourceException extends RuntimeException {
	public LockedResourceException() {
		super("Resource needed for this task is already in use");
	}

	public LockedResourceException(String s) {
		super(s);
	}
}