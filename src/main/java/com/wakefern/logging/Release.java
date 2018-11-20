package com.wakefern.logging;

public class Release {

	private String releaseDate;
	private String releaseLevel;
	private String releaseDescription;

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getReleaseLevel() {
		return releaseLevel;
	}

	public void setReleaseLevel(String releaseLevel) {
		this.releaseLevel = releaseLevel;
	}

	public String getReleaseDescription() {
		return releaseDescription;
	}

	public void setReleaseDescription(String releaseDescription) {
		this.releaseDescription = releaseDescription;
	}

	@Override
	public String toString() {
		return "Release [releaseDate=" + releaseDate + ", releaseLevel="
				+ releaseLevel + ", releaseDescription=" + releaseDescription
				+ "]";
	}

}
