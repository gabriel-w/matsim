/*******************************************************************************
 * Copyright (c) 2011 Stefan Schroeder.
 * eMail: stefan.schroeder@kit.edu
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Stefan Schroeder - initial API and implementation
 ******************************************************************************/
package org.matsim.contrib.freight.vrp.basics;



public class Pickup implements TourActivity, JobActivity, TourState{

	private Shipment shipment;
	
	private double practical_earliestOperationStartTime;
	
	private double practical_latestOperationStartTime;

	private int currentLoad;

	private double currentCost;

	private TourStateSnapshot tourStateSnapshot = new TourStateSnapshot();
	
	public Pickup(Shipment shipment) {
		super();
		this.shipment = shipment;
		practical_earliestOperationStartTime = shipment.getPickupTW().getStart();
		practical_latestOperationStartTime = shipment.getPickupTW().getEnd();
	}
	
	public Pickup(Pickup pickup){
		this.shipment = (Shipment) pickup.getJob();
		practical_earliestOperationStartTime = pickup.getEarliestOperationStartTime();
		practical_latestOperationStartTime = pickup.getLatestOperationStartTime();
		this.currentLoad = pickup.getCurrentLoad();
		this.currentCost = pickup.getCurrentCost();
		this.tourStateSnapshot = new TourStateSnapshot(pickup.getTourStateSnapshot());
	}

	public int getCapacityDemand(){
		return shipment.getSize();
	}

	@Override
	public double getOperationTime() {
		return shipment.getPickupServiceTime();
	}

	public String getLocationId() {
		return shipment.getFromId();
	}
	
	@Override
	public void setEarliestOperationStartTime(double early) {
		practical_earliestOperationStartTime = early;
	}

	@Override
	public double getEarliestOperationStartTime() {
		return practical_earliestOperationStartTime;
	}

	@Override
	public double getLatestOperationStartTime() {
		return practical_latestOperationStartTime;
	}

	@Override
	public void setLatestOperationStartTime(double late) {
		practical_latestOperationStartTime = late;
	}

	@Override
	public Job getJob() {
		return shipment;
	}

	@Override
	public int getCurrentLoad() {
		return this.currentLoad;
	}

	@Override
	public void setCurrentLoad(int load) {
		this.currentLoad = load;
	}
	
	public String toString(){
		return "Pickup" + " of " + shipment.getSize() + " units @ "+ getLocationId()  + " @ practTW(" + round(practical_earliestOperationStartTime) + "," +
			round(practical_latestOperationStartTime) + ") theoreticalTW(" + round(shipment.getPickupTW().getStart()) + "," + round(shipment.getPickupTW().getEnd()) + ")";
	}

	private String round(double time) {
		if(time == Double.MAX_VALUE){
			return "oo";
		}
		return "" + Math.round(time);
	}

	@Override
	public double getCurrentCost() {
		return currentCost;
	}

	@Override
	public void setCurrentCost(double cost) {
		this.currentCost = cost;
	}

	@Override
	public TourStateSnapshot getTourStateSnapshot() {
		return this.tourStateSnapshot;
	}

	@Override
	public void setTourStateSnapshot(TourStateSnapshot snapshot) {
		this.tourStateSnapshot = snapshot;
	}

	@Override
	public TourActivity duplicate() {
		return new Pickup(this);
	}
}
