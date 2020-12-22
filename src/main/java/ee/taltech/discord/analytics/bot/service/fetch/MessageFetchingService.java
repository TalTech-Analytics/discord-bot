package ee.taltech.discord.analytics.bot.service.fetch;

import ee.taltech.discord.analytics.bot.model.entity.ChannelEntity;
import ee.taltech.discord.analytics.bot.model.entity.MessageEntity;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageFetchingService {

	private final JDA bot;

	public List<MessageEntity> getNewerMessages(ChannelEntity channelEntity) {
		TextChannel channel = bot.getTextChannelById(channelEntity.getId());
		if (channel == null) {
			return new ArrayList<>();
		}

		List<Message> messages;

		if (channelEntity.getLatestMessageID() == null) {
			messages = channel.getHistoryFromBeginning(100).complete().getRetrievedHistory();
		} else {
			messages = channel.getHistoryAfter(channelEntity.getLatestMessageID(), 100).complete().getRetrievedHistory();
		}

		return messages.stream().map(MessageEntity::from).collect(Collectors.toList());
	}
}
