package ee.taltech.discord.analytics.bot.controller;

import ee.taltech.discord.analytics.bot.service.update.AggregationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/v2")
@AllArgsConstructor
public class AggregationController {

	private final AggregationService aggregationService;

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping(path = "/aggregate", produces = MediaType.APPLICATION_JSON_VALUE)
	public void aggregateData() {
		aggregationService.aggregate();
	}
}