package ee.taltech.discord.analytics.bot.service.factory;

import ee.taltech.discord.analytics.bot.model.entity.GuildEntity;

public class GuildFactory {

	public static GuildEntity invalidGuild() {
		return GuildEntity.builder()
				.name("Missing discord ID")
				.build();
	}

	public static GuildEntity fullGuild() {
		return GuildEntity.builder()
				.name("name")
				.id("discordID")
				.description("description")
				.build();
	}

	public static GuildEntity newGuild() {
		return GuildEntity.builder()
				.name("new")
				.id("discordID")
				.description("description")
				.build();
	}

}
