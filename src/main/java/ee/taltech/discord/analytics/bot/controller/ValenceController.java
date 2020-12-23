package ee.taltech.discord.analytics.bot.controller;

import ee.taltech.discord.analytics.bot.configuration.ApplicationProperties;
import ee.taltech.discord.analytics.bot.exception.LockedResourceException;
import ee.taltech.discord.analytics.bot.service.update.ValenceService;
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
public class ValenceController {

	private final ApplicationProperties applicationProperties;
	private final ValenceService valenceService;

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping(path = "valence:run", produces = MediaType.APPLICATION_JSON_VALUE)
	public void tagMessages() throws LockedResourceException {
		if (!applicationProperties.getInProgress()) {
			valenceService.tagValence();
		} else {
			throw new LockedResourceException();
		}
	}
}
