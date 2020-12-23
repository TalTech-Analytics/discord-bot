package ee.taltech.discord.analytics.bot.model.dto;

import ee.taltech.discord.analytics.bot.model.entity.ChannelEntity;
import lombok.*;
import org.springframework.lang.NonNull;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDTO {

	@NonNull
	private String id;
	@NonNull
	private String guildID;
	@NonNull
	private String name;

	public static ChannelDTO from(ChannelEntity x) {
		return ChannelDTO.builder()
				.id(x.getId())
				.guildID(x.getGuildID())
				.name(x.getCategory() != null ? x.getCategory() + "/" + x.getName() : x.getName())
				.build();
	}
}
