package ee.taltech.discord.analytics.bot.service.update;

import ee.taltech.discord.analytics.bot.configuration.ApplicationProperties;
import ee.taltech.discord.analytics.bot.model.dto.MessageContainerDTO;
import ee.taltech.discord.analytics.bot.model.dto.MessageDTO;
import ee.taltech.discord.analytics.bot.model.entity.MessageEntity;
import ee.taltech.discord.analytics.bot.model.entity.Valence;
import ee.taltech.discord.analytics.bot.repository.MessageRepository;
import ee.taltech.discord.analytics.bot.service.docker.DockerRunningService;
import ee.taltech.discord.analytics.bot.service.docker.ValenceDocker;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ValenceService {

	private final ApplicationProperties properties;
	private final DockerRunningService dockerRunningService;
	private final MessageRepository messageRepository;
	private final Logger logger;

	@Async
	@Scheduled(cron = "0 0 */1 * * *") // every hour
	public void tagValence() {
		if (properties.getDatabaseLocked()) {
			return;
		}
		properties.setDatabaseLocked(true);

		try {
			runDocker();
		} catch (Exception e) {
			logger.error("Running valence container resulted in an error: {}", e.getMessage());
		}

		logger.info("Finished running valence tagger");
		properties.setDatabaseLocked(false);

	}

	private void runDocker() {
		HashMap<String, MessageEntity> entities = new HashMap<>();

		MessageContainerDTO container = new MessageContainerDTO(messageRepository.findAllByValenceNull().stream()
				.peek(x -> entities.put(x.getId(), x))
				.map(MessageDTO::from).collect(Collectors.toList()));

		Object result = dockerRunningService.runDocker(new ValenceDocker(container));

		if (result instanceof MessageContainerDTO) {
			((MessageContainerDTO) result).getMessages().forEach(x -> {
				try {
					MessageEntity entity = entities.get(x.getId());
					try {
						entity.setValence(Valence.fromValue(x.getValence()));
					} catch (Exception e) {
						entity.setValence(Valence.ONLY_MIXED);
					}
				} catch (Exception e) {
					logger.warn("Failed updating message: {} with error: {}", x, e.getMessage());
				}
			});
			messageRepository.saveAll(entities.values());
		} else {
			logger.error("result is of wrong type: {}", result == null ? null : result.getClass());
		}
	}
}
