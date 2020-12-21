package ee.taltech.discord.analytics.bot.controller;

import ee.taltech.discord.analytics.bot.model.entity.GuildEntity;
import ee.taltech.discord.analytics.bot.service.update.GuildUpdatingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/v2")
@AllArgsConstructor
public class GuildController {

	private final GuildUpdatingService guildUpdatingService;

	@ResponseStatus(HttpStatus.ACCEPTED)
	@GetMapping(path = "/guilds", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GuildEntity> getGuilds() {
		return guildUpdatingService.getGuilds();
	}

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping(path = "/guilds", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GuildEntity> updateGuilds() {
		guildUpdatingService.updateGuilds();
		return guildUpdatingService.getGuilds();
	}
}
