package ee.taltech.discord.analytics.bot.service.update;

import ee.taltech.discord.analytics.bot.model.entity.ChannelEntity;
import ee.taltech.discord.analytics.bot.repository.ChannelRepository;
import ee.taltech.discord.analytics.bot.service.fetch.ChannelFetchingService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChannelUpdatingService {
	private final ChannelFetchingService channelFetchingService;
	private final ChannelRepository channelRepository;
	private final Logger logger;

	public List<ChannelEntity> getChannels() {
		return channelRepository.findAll();
	}

	public void updateChannels() {
		List<ChannelEntity> existingChannels = channelRepository.findAll();
		List<ChannelEntity> newChannels = channelFetchingService.getChannels();

		newChannels.forEach(newChannel -> {

			Optional<ChannelEntity> existingChannel = existingChannels.stream()
					.filter(oldChannel -> oldChannel.getId().equals(newChannel.getId())).findFirst();

			if (existingChannel.isPresent()) {
				ChannelEntity channel = existingChannel.get();
				channel.setName(newChannel.getName());
				channel.setTopic(newChannel.getTopic());
				channel.setCategory(newChannel.getCategory());
				channelRepository.save(channel);
			} else {
				logger.info("Adding a new channel to DB with name: " + newChannel.getName());
				channelRepository.save(newChannel);
			}
		});
	}
}
