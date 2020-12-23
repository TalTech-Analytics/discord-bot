package ee.taltech.discord.analytics.bot.service.update;

import ee.taltech.discord.analytics.bot.configuration.ApplicationProperties;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CronService {

	private final Logger logger;
	private final ApplicationProperties applicationProperties;
	private final GuildUpdatingService guildUpdatingService;
	private final ChannelUpdatingService channelUpdatingService;
	private final MessageUpdatingService messageUpdatingService;
	private final ValenceService valenceService;
	private final AggregationService aggregationService;

	@Async
	@Scheduled(cron = "0 0 3 * * *") // 3 am
	public void run() {
		logger.info("Running cron task");

		if (applicationProperties.getInProgress()) {
			return;
		}

		applicationProperties.setInProgress(true);

		try {
			guildUpdatingService.updateGuilds();
			channelUpdatingService.updateChannels();
			messageUpdatingService.updateChannelMessages();
			valenceService.tagValence();
			aggregationService.aggregate();
		} catch (Exception e) {
			// TODO send mail when this fails
			logger.error("Failed running cron task with error: " + e.getMessage());
		}

		applicationProperties.setInProgress(false);
	}
}
