package ee.taltech.discord.analytics.bot.service.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.Image;
import ee.taltech.discord.analytics.bot.exception.ImageNotFoundException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ImageCheck {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageCheck.class);

	private final DockerClient dockerClient;

	private final String image;

	public ImageCheck(DockerClient dockerClient, String image) {
		this.dockerClient = dockerClient;
		this.image = image;
	}

	public String invoke() {
		pull();
		List<Image> images = dockerClient.listImagesCmd().withShowAll(true).exec();

		for (Image analyzer : images) {
			if (analyzer.getRepoTags() != null) {
				for (String tag : analyzer.getRepoTags()) {
					if (tag.contains(image)) {
						return analyzer.getId();
					}
				}
			}
		}

		throw new ImageNotFoundException("Image with name " + image + " was not found!");
	}

	@SneakyThrows
	public void pull() {
		LOGGER.info("Pulling new image: {}", image);
		dockerClient.pullImageCmd(image)
				.exec(new PullImageResultCallback())
				.awaitCompletion(300, TimeUnit.SECONDS);
	}
}