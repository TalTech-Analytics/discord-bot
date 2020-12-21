package ee.taltech.discord.analytics.bot.service.update;

import ee.taltech.discord.analytics.bot.model.entity.ChannelEntity;
import ee.taltech.discord.analytics.bot.repository.ChannelRepository;
import ee.taltech.discord.analytics.bot.service.factory.ChannelFactory;
import ee.taltech.discord.analytics.bot.service.fetch.ChannelFetchingService;
import net.dv8tion.jda.api.JDA;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class ChannelUpdatingServiceTest {

	@MockBean
	private JDA jda;

	@MockBean
	private ChannelFetchingService channelFetchingService;

	@Autowired(required = false)
	private ChannelRepository channelRepository;

	@Autowired(required = false)
	@InjectMocks
	private ChannelUpdatingService channelUpdatingService;

	@Test
	void getChannels() {
		channelRepository.save(ChannelFactory.fullChannel());

		List<ChannelEntity> channels = channelUpdatingService.getChannels();

		assertEquals(1, channels.size());
		assertEquals("name", channels.get(0).getName());
	}

	@Test
	void invalidChannel() {
		Throwable thrown = catchThrowable(ChannelFactory::invalidChannel);

		assertNotNull(thrown);
	}

	@Test
	void saveChannel() {
		when(channelFetchingService.getChannels()).thenReturn(List.of(ChannelFactory.fullChannel()));

		channelUpdatingService.updateChannels();

		List<ChannelEntity> channels = channelUpdatingService.getChannels();

		assertEquals(1, channels.size());
		assertEquals("name", channels.get(0).getName());
	}

	@Test
	void updateChannel() {
		when(channelFetchingService.getChannels()).thenReturn(List.of(ChannelFactory.fullChannel()));

		channelUpdatingService.updateChannels();

		when(channelFetchingService.getChannels()).thenReturn(List.of(ChannelFactory.newChannel()));

		channelUpdatingService.updateChannels();

		List<ChannelEntity> channels = channelUpdatingService.getChannels();

		assertEquals(1, channels.size());
		assertEquals("new", channels.get(0).getName());
	}
}