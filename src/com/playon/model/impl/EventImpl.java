package com.playon.model.impl;

import java.util.Date;
import java.util.List;

import com.playon.model.Activity;
import com.playon.model.Broadcast;
import com.playon.model.Event;
import com.playon.model.EventAvailability;
import com.playon.model.EventState;
import com.playon.model.Participant;
import com.playon.model.Publisher;
import com.playon.model.Venue;
import com.playon.model.Video;

public class EventImpl extends HasNameAndIdImpl implements Event {

	protected EventImpl() {
	}

	@Override
	public final native Activity getActivity() /*-{
		return this.activity;
	}-*/;

	@Override
	public final native Broadcast getBroadcast() /*-{
		return this.broadcast;
	}-*/;

	@Override
	public final native String getDescription() /*-{
		return this.description;
	}-*/;

	@Override
	public final native EventAvailability getEventAvailability() /*-{
		return this.availability;
	}-*/;

	@Override
	public final native EventState getEventState() /*-{
		return this.state;
	}-*/;

	@Override
	public final native Video getHighlight() /*-{
		return this.highlight;
	}-*/;

	@Override
	public final native List<Participant> getParticipants() /*-{
		return this.participants;
	}-*/;

	@Override
	public final native Publisher getPublisher() /*-{
		return this.publisher;
	}-*/;

	@Override
	public final native Date getScheduledDate() /*-{
		return this.scheduledDate;
	}-*/;

	@Override
	public final native String getURL() /*-{
		return this.url;
	}-*/;

	@Override
	public final native Venue getVenue() /*-{
		return this.venue;
	}-*/;

	@Override
	public final native Video getVideo() /*-{
		return this.video;
	}-*/;

	@Override
	public final native Boolean isPublished() /*-{
		return this.published;
	}-*/;

}
