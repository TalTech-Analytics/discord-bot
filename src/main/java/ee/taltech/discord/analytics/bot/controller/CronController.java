package ee.taltech.discord.analytics.bot.controller;

import ee.taltech.discord.analytics.bot.service.update.CronService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/v2/cron")
@AllArgsConstructor
public class CronController {

	private final CronService cronService;

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping(path = ":run", produces = MediaType.APPLICATION_JSON_VALUE)
	public void runCronTasks() {
		cronService.run();
	}
}
