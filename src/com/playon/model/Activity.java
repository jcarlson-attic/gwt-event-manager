package com.playon.model;

import java.util.Set;

public interface Activity extends HasIdentity, HasId, HasName {

	Set<String> getTags();

}
