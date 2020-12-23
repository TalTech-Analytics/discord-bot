package ee.taltech.discord.analytics.bot.controller;

import ee.taltech.discord.analytics.bot.configuration.ApplicationProperties;
import ee.taltech.discord.analytics.bot.exception.LockedResourceException;
import ee.taltech.discord.analytics.bot.model.entity.ChannelEntity;
import ee.taltech.discord.analytics.bot.service.update.ChannelUpdatingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController()
@RequestMapping("api/v2/channels")
@AllArgsConstructor
public class ChannelController {

	private final ApplicationProperties applicationProperties;
	private final ChannelUpdatingService channelUpdatingService;

	@ResponseStatus(HttpStatus.ACCEPTED)
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ChannelEntity> getChannels() {
		return channelUpdatingService.getChannels();
	}

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ChannelEntity> updateChannels() throws LockedResourceException {
		if (!applicationProperties.getInProgress()) {
			channelUpdatingService.updateChannels();
			return channelUpdatingService.getChannels();
		} else {
			throw new LockedResourceException();
		}
	}
}
