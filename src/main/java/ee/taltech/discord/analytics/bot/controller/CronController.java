package ee.taltech.discord.analytics.bot.controller;

import ee.taltech.discord.analytics.bot.configuration.ApplicationProperties;
import ee.taltech.discord.analytics.bot.exception.LockedResourceException;
import ee.taltech.discord.analytics.bot.service.update.CronService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/v2/")
@AllArgsConstructor
public class CronController {

	private final ApplicationProperties applicationProperties;
	private final CronService cronService;

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping(path = "cron:run", produces = MediaType.APPLICATION_JSON_VALUE)
	public void runCronTasks() throws LockedResourceException {
		if (!applicationProperties.getInProgress()) {
			cronService.run();
		} else {
			throw new LockedResourceException();
		}
	}
}
