package ee.taltech.discord.analytics.bot.service.update;

import ee.taltech.discord.analytics.bot.configuration.ApplicationProperties;
import ee.taltech.discord.analytics.bot.model.dto.*;
import ee.taltech.discord.analytics.bot.repository.ChannelRepository;
import ee.taltech.discord.analytics.bot.repository.GuildRepository;
import ee.taltech.discord.analytics.bot.repository.MessageRepository;
import ee.taltech.discord.analytics.bot.service.docker.AggregatorDocker;
import ee.taltech.discord.analytics.bot.service.docker.DockerRunningService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AggregationService {

	private final ApplicationProperties applicationProperties;
	private final GuildRepository guildRepository;
	private final ChannelRepository channelRepository;
	private final MessageRepository messageRepository;
	private final DockerRunningService dockerRunningService;
	private final Logger logger;

	@Async
	public void aggregate() {
		try {
			runDocker();
		} catch (Exception e) {
			logger.error("Running aggregator container resulted in an error: {}", e.getMessage());
			throw e;
		}

		logger.info("Finished running discord data aggregator");
	}

	private void runDocker() {
		dockerRunningService.runDocker(AggregatorDocker.builder()
				.home(applicationProperties.getHome())
				.guilds(new GuildContainerDTO(guildRepository.findAll()
						.stream().map(GuildDTO::from).collect(Collectors.toList())))
				.channels(new ChannelContainerDTO(channelRepository.findAll()
						.stream().map(ChannelDTO::from).collect(Collectors.toList())))
				.messages(new MessageContainerDTO(messageRepository.findAll()
						.stream().map(MessageDTO::from).collect(Collectors.toList())))
				.build());
	}

}
