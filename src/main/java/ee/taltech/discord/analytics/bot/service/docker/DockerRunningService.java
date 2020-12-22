package ee.taltech.discord.analytics.bot.service.docker;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DockerRunningService {

	private final Logger logger;

	/**
	 * @param docker : Docker to be ran.
	 * @return test job result
	 */
	@SneakyThrows
	public Object runDocker(Docker docker) {

		Exception exception = null;

		try {
			docker.run();
		} catch (Exception e) {
			logger.error("Failed running docker: {}", e.getMessage());
			exception = e;
		} finally {
			docker.cleanup();
		}

		if (exception == null) {
			return docker.getResult();
		} else {
			throw exception;
		}

	}

}