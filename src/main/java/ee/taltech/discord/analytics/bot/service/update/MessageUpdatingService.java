package ee.taltech.discord.analytics.bot.service.update;

import ee.taltech.discord.analytics.bot.model.entity.MessageEntity;
import ee.taltech.discord.analytics.bot.repository.ChannelRepository;
import ee.taltech.discord.analytics.bot.repository.MessageRepository;
import ee.taltech.discord.analytics.bot.service.fetch.MessageFetchingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageUpdatingService {

	private final MessageFetchingService channelFetchingService;
	private final MessageRepository messageRepository;
	private final ChannelRepository channelRepository;
	private final Logger logger;

	public Page<MessageEntity> getMessages(int page, int pageSize) {
		return messageRepository.findAll(PageRequest.of(page, pageSize));
	}

	public void updateChannelMessages() {
		channelRepository.findAll().forEach(channel -> {
			logger.info("Processing channel: " + channel.getCategory() + "/" + channel.getName());
			List<MessageEntity> messages = channelFetchingService.getNewerMessages(channel);
			while (messages.size() > 0) {
				String latest = messages.get(messages.size() - 1).getId();
				channel.setLatestMessageID(latest);
				channelRepository.save(channel);
				messageRepository.saveAll(messages);
				messages = channelFetchingService.getNewerMessages(channel);
			}
		});
	}
}
