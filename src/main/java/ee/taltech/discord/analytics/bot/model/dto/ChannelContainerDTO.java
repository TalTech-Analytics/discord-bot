package ee.taltech.discord.analytics.bot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelContainerDTO {

	private List<ChannelDTO> channels;

}
