package ee.taltech.discord.analytics.bot.service.update;

import ee.taltech.discord.analytics.bot.model.dto.ChannelContainerDTO;
import ee.taltech.discord.analytics.bot.model.dto.GuildContainerDTO;
import ee.taltech.discord.analytics.bot.model.dto.MessageContainerDTO;
import ee.taltech.discord.analytics.bot.model.dto.MessageDTO;
import ee.taltech.discord.analytics.bot.repository.ChannelRepository;
import ee.taltech.discord.analytics.bot.repository.GuildRepository;
import ee.taltech.discord.analytics.bot.repository.MessageRepository;
import ee.taltech.discord.analytics.bot.service.docker.AggregatorDocker;
import ee.taltech.discord.analytics.bot.service.docker.DockerRunningService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AggregationService {

	private final GuildRepository guildRepository;
	private final ChannelRepository channelRepository;
	private final MessageRepository messageRepository;
	private final DockerRunningService dockerRunningService;
	private final Logger logger;

	@Async
//	@Scheduled(cron = "0 0 0 */1 * *") // every day
	public void aggregate() {
		try {
			runDocker();
		} catch (Exception e) {
			logger.error("Running aggregator container resulted in an error: {}", e.getMessage());
		}

		logger.info("Finished running discord data aggregator");
	}

	private void runDocker() {
		dockerRunningService.runDocker(AggregatorDocker.builder()
				.guilds(new GuildContainerDTO(guildRepository.findAll()))
				.channels(new ChannelContainerDTO(channelRepository.findAll()))
				.messages(new MessageContainerDTO(messageRepository.findAll()
						.stream().map(MessageDTO::from).collect(Collectors.toList())))
				.build());
	}

}
