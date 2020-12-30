package ee.taltech.discord.analytics.bot.service.docker;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.Volume;
import ee.taltech.discord.analytics.bot.model.dto.*;
import lombok.Builder;
import lombok.SneakyThrows;

import java.io.File;
import java.util.stream.Collectors;

import static com.github.dockerjava.api.model.AccessMode.ro;
import static com.github.dockerjava.api.model.AccessMode.rw;
import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

@Builder
public class AggregatorDocker extends Docker {

	private final GuildContainerDTO guilds;
	private final ChannelContainerDTO channels;
	private final MessageContainerDTO messages;

	@Override
	@SneakyThrows
	public void run() {
		String imageId = getImage("taltechanalytics/discord-data-aggregator");
		LOGGER.info("Got image with id: {}", imageId);

		Volume inputVolume = new Volume("/analyzer/input");
		Volume outputVolume = new Volume("/analyzer/output");

		long curTime = System.currentTimeMillis();
		tmpFolder = System.getProperty("DISCORD_BOT_HOME", "data/") + "aggregator/" + curTime;
		String inputFolder = String.format("%s/input", tmpFolder);
		String outputFolder = String.format("%s/output", tmpFolder);

		LOGGER.info("Making files to: {}", tmpFolder);
		ensureFoldersExist(new File(inputFolder));
		ensureFoldersExist(new File(outputFolder));
		createDiscordTree(inputFolder);

		String containerName = "aggregator-" + curTime;

		CreateContainerResponse container = dockerClient.createContainerCmd(imageId)
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
		super.run(container, containerName);
	}

	@SneakyThrows
	private void createDiscordTree(String inputFolder) {
		mapper.writeValue(new File(inputFolder + "/guilds.json"), guilds);

		for (GuildDTO guild : guilds.getGuilds()) {
			String guildFolder = inputFolder + "/" + guild.getId();
			ensureFoldersExist(new File(guildFolder));
			ChannelContainerDTO guildChannels = new ChannelContainerDTO(channels.getChannels()
					.stream().filter(x -> x.getGuildID().equals(guild.getId())).collect(Collectors.toList()));
			mapper.writeValue(new File(guildFolder + "/channels.json"), guildChannels);

			for (ChannelDTO channel : channels.getChannels()) {
				String channelFolder = guildFolder + "/" + channel.getId();
				ensureFoldersExist(new File(channelFolder));
				MessageContainerDTO channelMessages = new MessageContainerDTO(messages.getMessages()
						.stream().filter(x -> x.getChannelID().equals(channel.getId())).collect(Collectors.toList()));
				mapper.writeValue(new File(channelFolder + "/channel.json"), channelMessages);
			}
		}
	}

	@Override
	void readResult(String consoleLogs) {

	}

	@Override
	Object getResult() {
		return null;
	}
}
