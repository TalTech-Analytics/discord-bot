package ee.taltech.discord.analytics.bot.service.factory;

import ee.taltech.discord.analytics.bot.model.entity.ChannelEntity;

public class ChannelFactory {

	public static ChannelEntity invalidChannel() {
		return ChannelEntity.builder()
				.name("Missing discord ID and guild ID")
				.build();
	}

	public static ChannelEntity fullChannel() {
		return ChannelEntity.builder()
				.name("name")
				.guildID("guildID")
				.id("discordID")
				.topic("topic")
				.build();
	}

	public static ChannelEntity newChannel() {
		return ChannelEntity.builder()
				.name("new")
				.guildID("guildID")
				.id("discordID")
				.topic("topic")
				.build();
	}

}
