package ee.taltech.discord.analytics.bot.model.entity;

import lombok.*;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@Builder
@Table(name = "channel")
@NoArgsConstructor
@AllArgsConstructor
public class ChannelEntity {

	@Id
	@NonNull
	private String id;

	@NonNull
	private String guildID;

	@NonNull
	private String name;

	private String category;

	private String topic;

	private String latestMessageID;

	public static ChannelEntity from(TextChannel x) {
		return ChannelEntity.builder()
				.id(x.getId())
				.guildID(x.getGuild().getId())
				.name(x.getName())
				.category(x.getParent() == null ? null : x.getParent().getName())
				.topic(x.getTopic())
				.build();
	}
}
