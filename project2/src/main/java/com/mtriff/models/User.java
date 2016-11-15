package com.mtriff.models;

public class User {
	
	private String email;
	private BarChartSetting earthquakeMagnitudeSettings;
	private MapChartSetting earthquakeLocationSettings;
	private BarChartSetting tsunamiOccurrenceSettings;
	private MapChartSetting tsunamiLocationSettings;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public BarChartSetting getEarthquakeMagnitudeSettings() {
		return earthquakeMagnitudeSettings;
	}
	public void setEarthquakeMagnitudeSettings(BarChartSetting earthquakeMagnitudeSettings) {
		this.earthquakeMagnitudeSettings = earthquakeMagnitudeSettings;
	}
	public MapChartSetting getEarthquakeLocationSettings() {
		return earthquakeLocationSettings;
	}
	public void setEarthquakeLocationSettings(MapChartSetting earthquakeLocationSettings) {
		this.earthquakeLocationSettings = earthquakeLocationSettings;
	}
	public BarChartSetting getTsunamiOccurrenceSettings() {
		return tsunamiOccurrenceSettings;
	}
	public void setTsunamiOccurrenceSettings(BarChartSetting tsunamiOccurrenceSettings) {
		this.tsunamiOccurrenceSettings = tsunamiOccurrenceSettings;
	}
	public MapChartSetting getTsunamiLocationSettings() {
		return tsunamiLocationSettings;
	}
	public void setTsunamiLocationSettings(MapChartSetting tsunamiLocationSettings) {
		this.tsunamiLocationSettings = tsunamiLocationSettings;
	}
}
