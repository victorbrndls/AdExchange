package com.harystolho.adServer.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Spot;

/**
 * Caches the contracts the belong to an user and the spots that are bound to
 * these contracts
 * 
 * @author Harystolho
 *
 */
public class AdModelData {
	private Set<Contract> contracts;
	private Set<Spot> spots;

	public AdModelData() {
		contracts = new HashSet<>();
		spots = new HashSet<>();
	}

	public Set<Contract> getContracts() {
		return contracts;
	}

	public void addContract(Contract contract) {
		this.contracts.add(contract);
	}

	public void removeContracts(Collection<Contract> contracts) {
		this.contracts.removeAll(contracts);
	}

	public Set<Spot> getSpots() {
		return spots;
	}

	public void addSpot(Spot spot) {
		this.spots.add(spot);
	}

	public void removeSpots(Collection<Spot> spots) {
		this.spots.removeAll(spots);
	}

	public boolean isEmpty() {
		return contracts.isEmpty() && spots.isEmpty();
	}

}