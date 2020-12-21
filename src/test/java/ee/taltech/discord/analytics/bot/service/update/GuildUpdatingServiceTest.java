package ee.taltech.discord.analytics.bot.service.update;

import ee.taltech.discord.analytics.bot.model.entity.GuildEntity;
import ee.taltech.discord.analytics.bot.repository.GuildRepository;
import ee.taltech.discord.analytics.bot.service.factory.GuildFactory;
import ee.taltech.discord.analytics.bot.service.fetch.GuildFetchingService;
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
class GuildUpdatingServiceTest {

	@MockBean
	private JDA jda;

	@MockBean
	private GuildFetchingService guildFetchingService;

	@Autowired
	private GuildRepository guildRepository;

	@Autowired
	@InjectMocks
	private GuildUpdatingService guildUpdatingService;

	@Test
	void getGuilds() {
		guildRepository.save(GuildFactory.fullGuild());

		List<GuildEntity> guilds = guildUpdatingService.getGuilds();

		assertEquals(1, guilds.size());
		assertEquals("name", guilds.get(0).getName());
	}

	@Test
	void invalidGuild() {
		Throwable thrown = catchThrowable(GuildFactory::invalidGuild);

		assertNotNull(thrown);
	}

	@Test
	void saveGuild() {
		when(guildFetchingService.getGuilds()).thenReturn(List.of(GuildFactory.fullGuild()));

		guildUpdatingService.updateGuilds();

		List<GuildEntity> guilds = guildUpdatingService.getGuilds();

		assertEquals(1, guilds.size());
		assertEquals("name", guilds.get(0).getName());
	}

	@Test
	void updateGuild() {
		when(guildFetchingService.getGuilds()).thenReturn(List.of(GuildFactory.fullGuild()));

		guildUpdatingService.updateGuilds();

		when(guildFetchingService.getGuilds()).thenReturn(List.of(GuildFactory.newGuild()));

		guildUpdatingService.updateGuilds();

		List<GuildEntity> guilds = guildUpdatingService.getGuilds();

		assertEquals(1, guilds.size());
		assertEquals("new", guilds.get(0).getName());
	}
}