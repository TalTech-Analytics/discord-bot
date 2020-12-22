package ee.taltech.discord.analytics.bot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageContainerDTO {

	private List<MessageDTO> messages;

}
