package ee.taltech.discord.analytics.bot.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfiguration {

	@Bean
	public OpenAPI customOpenAPI() {
		List<Server> servers = new ArrayList<>();
		servers.add(new Server().url("http://localhost:8070/").description("Local Dev"));

		return new OpenAPI()
				.info(new Info()
						.title("Arete API")
						.version("2.0")
						.description("This is an API reference for Aretes that talks with automated testing service. use /auth endpoint to get a token which can be used to authorise one's self. You can use username: `admin`, password: `admin` to log into dev environment to test endpoints. Alternatively when endpoint supports an alternative authentication method, then that can be used instead.")
						.license(new License().name("GitHub").url("https://github.com/envomp?tab=repositories&q=arete&type=&language="))
						.contact(new Contact().email("enrico.vompa@gmail.com").name("Enrico Vompa"))
				)
				.servers(servers);
	}

}
