package ee.taltech.discord.analytics.bot.service.fetch;

import ee.taltech.discord.analytics.bot.model.entity.GuildEntity;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GuildFetchingService {

	private final JDA bot;

	public List<GuildEntity> getGuilds() {
		return bot.getGuilds().stream().map(GuildEntity::from).collect(Collectors.toList());
	}

}
