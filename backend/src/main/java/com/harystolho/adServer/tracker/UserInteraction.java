package com.harystolho.adserver.tracker;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("userInteractions")
public class UserInteraction {

	private String id;

	private String interactorId;
	private Set<String> interactions;

	public UserInteraction() {
		this.interactions = new HashSet<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInteractorId() {
		return interactorId;
	}

	public void setInteractorId(String interactorId) {
		this.interactorId = interactorId;
	}

	public Set<String> getInteractions() {
		return interactions;
	}

	public void setInteractions(Set<String> interactions) {
		this.interactions = interactions;
	}

	public void addInteraction(String interaction) {
		this.interactions.add(interaction);
	}

}
