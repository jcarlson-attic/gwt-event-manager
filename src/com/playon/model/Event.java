package com.playon.model;

import com.google.gwt.core.client.JsArray;

public class Event extends HasNameAndId {

	protected Event() {
	}

	public final native Activity getActivity() /*-{
		return this.activity;
	}-*/;

	public final native Broadcast getBroadcast() /*-{
		return this.broadcast;
	}-*/;

	public final native String getDescription() /*-{
		return this.description;
	}-*/;

	public final native EventAvailability getEventAvailability() /*-{
		return this.availability;
	}-*/;

	public final native EventState getEventState() /*-{
		return this.state;
	}-*/;

	public final native Video getHighlight() /*-{
		return this.highlight;
	}-*/;

	public final native JsArray<Participant> getParticipants() /*-{
		return this.participants;
	}-*/;

	public final native Publisher getPublisher() /*-{
		return this.publisher;
	}-*/;

	public final native String getScheduledDate() /*-{
		return this.scheduledDate;
	}-*/;

	public final native String getURL() /*-{
		return this.url;
	}-*/;

	public final native Venue getVenue() /*-{
		return this.venue;
	}-*/;

	public final native Video getVideo() /*-{
		return this.video;
	}-*/;

	public final native Boolean isPublished() /*-{
		return this.published;
	}-*/;

}
