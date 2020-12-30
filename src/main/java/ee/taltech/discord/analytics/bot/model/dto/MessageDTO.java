package ee.taltech.discord.analytics.bot.model.dto;

import ee.taltech.discord.analytics.bot.model.entity.MessageEntity;
import lombok.*;
import org.springframework.lang.NonNull;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

	@NonNull
	private String id;
	@NonNull
	private String channelID;
	@NonNull
	private String guildID;
	@NonNull
	private String content;
	private String valence;

	public static MessageDTO from(MessageEntity x) {
		return MessageDTO.builder()
				.id(x.getId())
				.channelID(x.getChannelID())
				.guildID(x.getGuildID())
				.content(x.getContent())
				.valence(x.getValence() == null ? null : x.getValence().toValue())
				.build();
	}
}
