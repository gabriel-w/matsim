/* *********************************************************************** *
 * project: org.matsim.*
 * OnTheFlyClientQuadSwing.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.vis.otfvis;


import java.awt.BorderLayout;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.matsim.core.gbl.Gbl;
import org.matsim.vis.otfvis.data.OTFConnectionManager;
import org.matsim.vis.otfvis.data.fileio.queuesim.OTFQueueSimLinkAgentsWriter;
import org.matsim.vis.otfvis.gui.NetJComponent;
import org.matsim.vis.otfvis.gui.OTFVisConfig;
import org.matsim.vis.otfvis.handler.OTFLinkLanesAgentsNoParkingHandler;
import org.matsim.vis.otfvis.opengl.gui.OTFFileSettingsSaver;
import org.matsim.vis.otfvis.opengl.gui.OTFTimeLine;



/**
 * This Client is capable of running on SWING only computers. It does not need OpenGL acceleration.
 * But it does not feature the whole set of operations possible with the OpenGL client.
 * It is also very slow, but for small networks it should work.
 * 
 * @author dstrippgen
 * @author dgrether
 *
 */
public class OTFClientSwing extends OTFClient {

	private static final Logger log = Logger.getLogger(OTFClientSwing.class);
	
	private OTFConnectionManager connect2 = new OTFConnectionManager();

	public OTFClientSwing(String url) {
		super("file:" + url);
		connect2.add(OTFLinkLanesAgentsNoParkingHandler.Writer.class, OTFLinkLanesAgentsNoParkingHandler.class);
		connect2.add(OTFLinkLanesAgentsNoParkingHandler.class, NetJComponent.SimpleQuadDrawer.class);
		connect2.add(OTFLinkLanesAgentsNoParkingHandler.class,  NetJComponent.AgentDrawer.class);
		/*
		 * This entry is needed to couple the org.matsim.core.queuesim to the visualizer
		 */
		this.connect2.add(OTFQueueSimLinkAgentsWriter.class, OTFLinkLanesAgentsNoParkingHandler.class);
		
	}
		

	@Override
	protected void createDrawer(){
		try {
			if(!hostControlBar.getOTFHostControl().isLiveHost()) {
				frame.getContentPane().add(new OTFTimeLine("time", hostControlBar), BorderLayout.SOUTH);
			} else  {
				throw new IllegalStateException("Server in live mode!");
			}
			mainDrawer = new NetJComponent(frame, createNewView("swing", connect2, hostControlBar.getOTFHostControl()));

		}catch (RemoteException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		hostControlBar.finishedInitialisition();
	}
	
	@Override
	protected void getOTFVisConfig() {
		if (Gbl.getConfig() != null) {
			visconf = (OTFVisConfig) Gbl.getConfig().otfVis();
		}
		else {
			Gbl.createConfig(null);
		}
		saver = new OTFFileSettingsSaver(visconf, this.url);
		if (visconf == null) {
			visconf = ((OTFFileSettingsSaver)saver).openAndReadConfig();
		} 
		else {
			log.warn("OTFVisConfig already defined, cant read settings from file");
		}
	}



	public static void main(String[] args) {
		// FIXME hard-coded filenames
		String arg0;
		if(args.length == 0)arg0 = "file:./otfvis.mvi";
		else arg0 = args[0];

		new OTFClientSwing("file:" + arg0).run();
		
	}		




}
