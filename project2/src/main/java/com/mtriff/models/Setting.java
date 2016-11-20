package com.mtriff.models;

import java.sql.Date;

public class Setting {
	protected String aggregateBy;
	protected Date timeRangeStart;
	protected Date timeRangeEnd;
	public String getAggregateBy() {
		return aggregateBy;
	}
	public void setAggregateBy(String aggregateBy) {
		this.aggregateBy = aggregateBy;
	}
	public Date getTimeRangeStart() {
		return timeRangeStart;
	}
	public void setTimeRangeStart(Date timeRangeStart) {
		this.timeRangeStart = timeRangeStart;
	}
	public Date getTimeRangeEnd() {
		return timeRangeEnd;
	}
	public void setTimeRangeEnd(Date timeRangeEnd) {
		this.timeRangeEnd = timeRangeEnd;
	}
}
