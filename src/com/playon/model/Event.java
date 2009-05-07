package com.playon.model;

import java.util.Date;
import java.util.List;

public interface Event extends HasIdentity, HasId, HasName {

	Broadcast getBroadcast();

	Video getHighlight();

	String getDescription();

	Venue getVenue();

	Boolean isPublished();

	EventAvailability getEventAvailability();

	EventState getEventState();

	Video getVideo();

	List<Participant> getParticipants();

	Date getScheduledDate();

	String getURL();

	Publisher getPublisher();

	Activity getActivity();

}
