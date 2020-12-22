package ee.taltech.discord.analytics.bot.model.entity;

public enum Valence {
	ONLY_NEGATIVE("ONLY_NEGATIVE"),
	ONLY_NEUTRAL("ONLY_NEUTRAL"),
	ONLY_POSITIVE("ONLY_POSITIVE"),
	ONLY_MIXED("ONLY_MIXED"),
	MOSTLY_NEGATIVE("MOSTLY_NEGATIVE"),
	MOSTLY_NEUTRAL("MOSTLY_NEUTRAL"),
	MOSTLY_POSITIVE("MOSTLY_POSITIVE"),
	MOSTLY_MIXED("MOSTLY_MIXED");

	private final String value;

	Valence(String value) {
		this.value = value;
	}

	public static Valence fromValue(String value) {

		value = value.replace(" ", "_").toUpperCase();

		for (Valence role : values()) {
			if (role.value.equals(value)) {
				return role;
			}
		}

		throw new IllegalArgumentException("Invalid valence: " + value);
	}

	public String toValue() {
		return value;
	}
}