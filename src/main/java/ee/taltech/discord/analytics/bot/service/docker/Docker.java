package ee.taltech.discord.analytics.bot.service.docker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.jaxrs.JerseyDockerHttpClient;
import ee.taltech.discord.analytics.bot.exception.DockerTimeoutException;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.util.concurrent.TimeUnit;

public abstract class Docker {
	protected final ObjectMapper mapper = new ObjectMapper();
	protected final Logger LOGGER = LoggerFactory.getLogger(Docker.class);
	protected DockerClient dockerClient;
	private CreateContainerResponse container;
	protected String tmpFolder = null;
	private boolean done = false;

	abstract void run();

	@SneakyThrows
	void run(CreateContainerResponse container, String containerName) {
		this.container = container;

		LOGGER.info("Created container with name: {}, id: {}", containerName, container.getId());

		dockerClient.startContainerCmd(container.getId()).exec();
		LOGGER.info("Started container with name: {}, id: {}", containerName, container.getId());

		StringBuilder readStd = new StringBuilder();

		dockerClient
				.logContainerCmd(containerName)
				.withStdErr(true)
				.withStdOut(true)
				.withFollowStream(true)
				.withSince(0)
				.exec(new ResultCallbackTemplate<LogContainerResultCallback, Frame>() {
					@Override
					public void onNext(Frame frame) {
						readStd.append(new String(frame.getPayload()));
					}

					@Override
					public void onComplete() {
						done = true;
						LOGGER.info("Finished container with name: {}, id: {}", containerName, container.getId());
						super.onComplete();
						LOGGER.debug(readStd.toString());
						readResult(readStd.toString());
					}
				});

		int seconds = 12000;
		while (!done) {
			TimeUnit.SECONDS.sleep(1);
			seconds--;
			if (seconds == 0) {
				throw new DockerTimeoutException("Timed out");
			}
		}
	}

	void cleanup() {
//		if (tmpFolder != null) {
//			try {
//				FileUtils.deleteDirectory(new File(tmpFolder));
//			} catch (Exception e) {
//				LOGGER.warn("Failed deleting tmp folder:" + tmpFolder);
//			}
//		}

		if (dockerClient != null && container != null) {

			try {
				dockerClient.stopContainerCmd(container.getId()).exec();
				LOGGER.info("Stopped container: {}", container.getId());
			} catch (Exception stop) {
				LOGGER.info("Container {} has already been stopped", container.getId());
			}

			try {
				dockerClient.killContainerCmd(container.getId()).exec();
				LOGGER.info("Killed container: {}", container.getId());
			} catch (Exception stop) {
				LOGGER.info("Container {} has already been killed", container.getId());
			}

			try {
				dockerClient.removeContainerCmd(container.getId()).exec();
				LOGGER.info("Removed container: {}", container.getId());
			} catch (Exception remove) {
				LOGGER.info("Container {} has already been removed", container.getId());
			}

			LOGGER.info("Cleaned up container with id: {}", container.getId());
		}
	}

	abstract void readResult(String consoleLogs);

	abstract Object getResult();

	@SneakyThrows
	protected void fetchClient() {
		String dockerHost = System.getenv().getOrDefault("DOCKER_HOST", "unix:///var/run/docker.sock");

		DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
				.withDockerHost(dockerHost)
				.withDockerTlsVerify(false)
				.build();

		dockerClient = DockerClientBuilder
				.getInstance(config)
				.withDockerHttpClient(
						new JerseyDockerHttpClient.Builder()
								.dockerHost(new URI(dockerHost))
								.sslConfig(config.getSSLConfig())
								.build())
				.build();
	}

	protected String getImage(String image) {
		fetchClient();
		ImageCheck imageCheck = new ImageCheck(dockerClient, image);
		return imageCheck.invoke();
	}

	@SneakyThrows
	static void ensureFoldersExist(File folder) {
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				ensureFoldersExist(folder.getParentFile());
			}
		}
	}
}
