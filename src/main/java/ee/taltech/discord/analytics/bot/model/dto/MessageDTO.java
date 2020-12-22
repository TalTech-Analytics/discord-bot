package ee.taltech.discord.analytics.bot.model.dto;

import ee.taltech.discord.analytics.bot.model.entity.MessageEntity;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

	private String id;
	private String content;
	private String valence;

	public static MessageDTO from(MessageEntity x) {
		return MessageDTO.builder()
				.id(x.getId())
				.content(x.getContent())
				.build();
	}
}
