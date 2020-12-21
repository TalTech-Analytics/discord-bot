package ee.taltech.discord.analytics.bot.model.entity;

import lombok.*;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {

	@Id
	@NonNull
	private String id;

	@NonNull
	private String authorID;

	@NonNull
	private String channelID;

	@NonNull
	private String guildID;

	@NonNull
	private OffsetDateTime timeCreated;

	@NonNull
	@Column(columnDefinition = "TEXT")
	private String content;


	public static MessageEntity from(Message x) {
		return MessageEntity.builder()
				.id(x.getId())
				.authorID(x.getAuthor().getId())
				.channelID(x.getChannel().getId())
				.guildID(x.getGuild().getId())
				.timeCreated(x.getTimeCreated())
				.content(x.getContentStripped())
				.build();
	}
}
