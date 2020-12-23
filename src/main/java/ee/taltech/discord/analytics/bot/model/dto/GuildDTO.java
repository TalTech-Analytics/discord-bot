package ee.taltech.discord.analytics.bot.model.dto;

import ee.taltech.discord.analytics.bot.model.entity.GuildEntity;
import lombok.*;
import org.springframework.lang.NonNull;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GuildDTO {

	@NonNull
	private String id;
	@NonNull
	private String name;

	public static GuildDTO from(GuildEntity x) {
		return GuildDTO.builder()
				.id(x.getId())
				.name(x.getName())
				.build();
	}
}
