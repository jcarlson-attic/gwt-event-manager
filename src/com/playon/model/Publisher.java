package com.playon.model;

import java.util.List;

public interface Publisher extends HasIdentity, HasId, HasName {

	List<Activity> getActivities();

	List<Organization> getOrganizations();

}
