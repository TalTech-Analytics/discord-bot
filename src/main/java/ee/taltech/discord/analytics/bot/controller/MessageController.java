package ee.taltech.discord.analytics.bot.controller;

import ee.taltech.discord.analytics.bot.configuration.ApplicationProperties;
import ee.taltech.discord.analytics.bot.exception.LockedResourceException;
import ee.taltech.discord.analytics.bot.model.entity.MessageEntity;
import ee.taltech.discord.analytics.bot.service.update.MessageUpdatingService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/v2/messages")
@AllArgsConstructor
public class MessageController {

	private final ApplicationProperties applicationProperties;
	private final MessageUpdatingService messageUpdatingService;

	@ResponseStatus(HttpStatus.ACCEPTED)
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, params = {"page", "size"})
	public Page<MessageEntity> getGuilds(@RequestParam("page") int page, @RequestParam("size") int size) {
		return messageUpdatingService.getMessages(page, size);
	}

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateMessages() throws LockedResourceException {
		if (!applicationProperties.getInProgress()) {
			messageUpdatingService.updateChannelMessages();
		} else {
			throw new LockedResourceException();
		}
	}
}
