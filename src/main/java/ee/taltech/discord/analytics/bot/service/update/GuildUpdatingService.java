package ee.taltech.discord.analytics.bot.service.update;

import ee.taltech.discord.analytics.bot.model.entity.GuildEntity;
import ee.taltech.discord.analytics.bot.repository.GuildRepository;
import ee.taltech.discord.analytics.bot.service.fetch.GuildFetchingService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GuildUpdatingService {

	private final GuildFetchingService guildFetchingService;
	private final GuildRepository guildRepository;
	private final Logger logger;

	public List<GuildEntity> getGuilds() {
		return guildRepository.findAll();
	}

	@Async
	@Scheduled(cron = "0 0 2 * * *") // 2 am
	public void updateGuilds() {
		List<GuildEntity> existingGuilds = guildRepository.findAll();
		List<GuildEntity> newGuilds = guildFetchingService.getGuilds();

		newGuilds.forEach(newGuild -> {

			Optional<GuildEntity> existingChannel = existingGuilds.stream()
					.filter(oldGuild -> oldGuild.getId().equals(newGuild.getId())).findFirst();

			if (existingChannel.isPresent()) {
				GuildEntity guild = existingChannel.get();
				guild.setName(newGuild.getName());
				guild.setDescription(newGuild.getDescription());
				guildRepository.save(guild);
			} else {
				logger.info("Adding a new guild to DB with name: " + newGuild.getName());
				guildRepository.save(newGuild);
			}
		});
	}

}
