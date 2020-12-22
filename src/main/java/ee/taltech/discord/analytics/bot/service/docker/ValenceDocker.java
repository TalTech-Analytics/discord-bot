package ee.taltech.discord.analytics.bot.service.docker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.Volume;
import ee.taltech.discord.analytics.bot.model.dto.MessageContainerDTO;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;

import static com.github.dockerjava.api.model.AccessMode.ro;
import static com.github.dockerjava.api.model.AccessMode.rw;
import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

public class ValenceDocker extends Docker {

	private final ObjectMapper mapper = new ObjectMapper();

	private String tmpFolder;
	private final MessageContainerDTO valenceInput;
	private MessageContainerDTO valenceOutput;

	public ValenceDocker(MessageContainerDTO messageContainerDTO) {
		this.valenceInput = messageContainerDTO;
	}

	@Override
	@SneakyThrows
	public void run() {
		fetchClient();

		String imageId = getImage(dockerClient, "taltechanalytics/valence-analyzer");
		LOGGER.info("Got image with id: {}", imageId);

		Volume inputVolume = new Volume("/analyzer/valence-input");
		Volume outputVolume = new Volume("/analyzer/valence-output");

		long curTime = System.currentTimeMillis();
		tmpFolder = "valence/" + curTime;
		String inputFolder = String.format("%s/input", tmpFolder);
		String outputFolder = String.format("%s/output", tmpFolder);
		LOGGER.info("Making files to: {}", tmpFolder);
		ensureFoldersExist(new File(inputFolder));
		ensureFoldersExist(new File(outputFolder));

		LOGGER.info("Writing input to: {}/messages.json", inputFolder);
		mapper.writeValue(new File(inputFolder + "/messages.json"), valenceInput);

		containerName = "valence-analyzer-" + curTime;
		container = dockerClient.createContainerCmd(imageId)
				.withName(containerName)
				.withVolumes(inputVolume, outputVolume)
				.withAttachStdout(true)
				.withAttachStderr(true)
				.withHostConfig(newHostConfig()
						.withBinds(
								new Bind(new File(inputFolder).getAbsolutePath(), inputVolume, ro),
								new Bind(new File(outputFolder).getAbsolutePath(), outputVolume, rw))
						.withCpuCount(4L)
						.withMemory(8000000000L)
						.withMemorySwap(8000000000L)
						.withAutoRemove(true)
						.withPidsLimit(8192L)
						.withRestartPolicy(RestartPolicy.noRestart())
				).exec();

		super.run();
	}

	@Override
	void readResult(String consoleLogs) {
		try {
			valenceOutput = mapper.readValue(new File(tmpFolder + "/output/messages.json"), MessageContainerDTO.class);
		} catch (Exception e) {
			LOGGER.warn("Malformed output with message: " + e.getMessage());
		}
	}


	@Override
	public Object getResult() {
		return valenceOutput;
	}

	@Override
	void cleanup() {
		try {
			FileUtils.deleteDirectory(new File(tmpFolder));
		} catch (Exception e) {
			LOGGER.warn("Failed deleting tmp folder:" + tmpFolder);
		}
		super.cleanup();
	}
}
