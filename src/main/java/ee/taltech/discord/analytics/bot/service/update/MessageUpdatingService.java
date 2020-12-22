package ee.taltech.discord.analytics.bot.service.update;

import ee.taltech.discord.analytics.bot.configuration.ApplicationProperties;
import ee.taltech.discord.analytics.bot.model.entity.ChannelEntity;
import ee.taltech.discord.analytics.bot.model.entity.MessageEntity;
import ee.taltech.discord.analytics.bot.repository.ChannelRepository;
import ee.taltech.discord.analytics.bot.repository.MessageRepository;
import ee.taltech.discord.analytics.bot.service.fetch.MessageFetchingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class MessageUpdatingService {

	private final MessageFetchingService channelFetchingService;
	private final MessageRepository messageRepository;
	private final ChannelRepository channelRepository;
	private final Logger logger;
	private final ApplicationProperties applicationProperties;

	private static final ConcurrentLinkedQueue<ChannelEntity> channelQueue = new ConcurrentLinkedQueue<>();
	private static Integer threadsRemaining = -1;

	public Page<MessageEntity> getMessages(int page, int pageSize) {
		return messageRepository.findAll(PageRequest.of(page, pageSize));
	}

	public void updateChannelMessages() {
		if (threadsRemaining < 0) {
			threadsRemaining = applicationProperties.getMaxConcurrentApiRequests();
		}

		if (channelQueue.isEmpty() && threadsRemaining.equals(applicationProperties.getMaxConcurrentApiRequests())) {
			channelQueue.addAll(channelRepository.findAll());
		}
	}

	@Async
	@Scheduled(fixedRate = 1000)
	public void queueUpdate() {
		if (threadsRemaining != 0) {
			try {
				threadsRemaining -= 1;
				ChannelEntity channel = channelQueue.poll();
				if (channel != null) {
					updateMessages(channel);
				}
			} catch (Exception e) {
				logger.warn("Fetching API failed with message: {}", e.getMessage());
			} finally {
				threadsRemaining += 1;
			}
		}
	}

	private void updateMessages(ChannelEntity channel) {
		logger.info("Processing channel: " + channel.getCategory() + "/" + channel.getName());
		List<MessageEntity> messages = channelFetchingService.getNewerMessages(channel);
		while (messages.size() > 0) {
			messages.forEach(message -> {
				messageRepository.save(message);
				channel.setLatestMessageID(message.getId());
			});
			channelRepository.save(channel);
			messages = channelFetchingService.getNewerMessages(channel);
		}
	}
}
