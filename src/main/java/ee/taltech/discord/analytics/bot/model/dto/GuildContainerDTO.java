package ee.taltech.discord.analytics.bot.model.dto;

import ee.taltech.discord.analytics.bot.model.entity.GuildEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GuildContainerDTO {

	private List<GuildEntity> guilds;

}
