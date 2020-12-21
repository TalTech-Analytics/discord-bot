package ee.taltech.discord.analytics.bot.model.entity;

import lombok.*;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@Table(name = "guild")
@NoArgsConstructor
@AllArgsConstructor
public class GuildEntity {

	@Id
	@NonNull
	private String id;

	@NonNull
	private String name;

	private String description;

	public static GuildEntity from(Guild x) {
		return GuildEntity.builder()
				.id(x.getId())
				.name(x.getName())
				.description(x.getDescription())
				.build();
	}
}
