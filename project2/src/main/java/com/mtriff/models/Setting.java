package com.mtriff.models;

public class Setting {
	protected String aggregateBy;
	protected String timeRangeStart;
	protected String timeRangeEnd;
	public String getAggregateBy() {
		return aggregateBy;
	}
	public void setAggregateBy(String aggregateBy) {
		this.aggregateBy = aggregateBy;
	}
	public String getTimeRangeStart() {
		return timeRangeStart;
	}
	public void setTimeRangeStart(String timeRangeStart) {
		this.timeRangeStart = timeRangeStart;
	}
	public String getTimeRangeEnd() {
		return timeRangeEnd;
	}
	public void setTimeRangeEnd(String timeRangeEnd) {
		this.timeRangeEnd = timeRangeEnd;
	}
}
