package ee.taltech.discord.analytics.bot.service.fetch;

import ee.taltech.discord.analytics.bot.model.entity.ChannelEntity;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChannelFetchingService {

	private final JDA bot;

	public List<ChannelEntity> getChannels() {
		return bot.getTextChannels().stream().map(ChannelEntity::from).collect(Collectors.toList());
	}

}
