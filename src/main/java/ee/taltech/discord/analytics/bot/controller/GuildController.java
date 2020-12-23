package ee.taltech.discord.analytics.bot.controller;

import ee.taltech.discord.analytics.bot.configuration.ApplicationProperties;
import ee.taltech.discord.analytics.bot.exception.LockedResourceException;
import ee.taltech.discord.analytics.bot.model.entity.GuildEntity;
import ee.taltech.discord.analytics.bot.service.update.GuildUpdatingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/v2/guilds")
@AllArgsConstructor
public class GuildController {

	private final ApplicationProperties applicationProperties;
	private final GuildUpdatingService guildUpdatingService;

	@ResponseStatus(HttpStatus.ACCEPTED)
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GuildEntity> getGuilds() {
		return guildUpdatingService.getGuilds();
	}

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GuildEntity> updateGuilds() throws LockedResourceException {
		if (!applicationProperties.getInProgress()) {
			guildUpdatingService.updateGuilds();
			return guildUpdatingService.getGuilds();
		} else {
			throw new LockedResourceException();
		}
	}
}
