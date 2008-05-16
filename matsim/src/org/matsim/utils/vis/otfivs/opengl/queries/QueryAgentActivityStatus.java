package org.matsim.utils.vis.otfivs.opengl.queries;

import java.util.Collection;

import org.matsim.events.Events;
import org.matsim.mobsim.QueueLink;
import org.matsim.mobsim.QueueNetworkLayer;
import org.matsim.mobsim.Vehicle;
import org.matsim.plans.Act;
import org.matsim.plans.Person;
import org.matsim.plans.Plan;
import org.matsim.plans.Plans;
import org.matsim.utils.vis.otfivs.interfaces.OTFDrawer;
import org.matsim.utils.vis.otfivs.interfaces.OTFQuery;


public class QueryAgentActivityStatus implements OTFQuery{
	public final String agentID;

	boolean calcOffset = true;

	double now;
	//out
	int activityNr = -1;
	double finished = 0;

	public QueryAgentActivityStatus(String agentID, double d) {
		this.agentID = agentID;
		this.now = d;
	}

	public void query(QueueNetworkLayer net, Plans plans, Events events) {
		Person person = plans.getPerson(this.agentID);
		if (person == null) return;

		Plan plan = person.getSelectedPlan();

		// find the acual activity by searchin all activity links
		// for a vehicle with this agent id

		for (int i=0;i< plan.getActsLegs().size(); i+=2) {
			Act act = (Act)plan.getActsLegs().get(i);
			QueueLink link = net.getQueueLink(act.getLinkId());
			Collection<Vehicle> vehs = link.getAllVehicles();
			for (Vehicle info : vehs) {
				if (info.getDriver().getId().toString().compareTo(this.agentID) == 0) {
					// we found the little nutty, now lets reason about the lngth of ist activity
					double departure = info.getDepartureTime_s();
					double diff =  departure - info.getLastMovedTime();
					this.finished = (this.now - info.getLastMovedTime()) / diff;
					this.activityNr = i/2;
				}
			}
		}

	}

	public void remove() {
		// TODO Auto-generated method stub

	}

	public void draw(OTFDrawer drawer) {
		// TODO Auto-generated method stub

	}

}
