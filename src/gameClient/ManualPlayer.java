package gameClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import algorithms.Graph_Algo;
import dataStructure.edgeData;
import dataStructure.edge_data;
import utils.Point3D;
import utils.StdDraw;

public class ManualPlayer {

	
	public ManualPlayer(MyGameGUI gui) {
		setGUI(gui);
		init();
	}
	
	private void init() {
		RobotsStartPosition();
	}
	

	/**
	 * Moves each of the robots along the edge, in case the robot is on a node the
	 * next destination (next edge) is chosen (randomly).
	 * 
	 * @param game
	 * @param gg
	 * @param log
	 */
	public void moveRobotsGUI() {
		char c='0';
		//update fruit

		List<String> fruits = getGUI().getGame().getFruits();
		for (int i = 0; i < fruits.size(); i++) {
			getGUI().getFruitList().get(i).init(fruits.get(i));
		}


		List<String> log = getGUI().getGame().move();
		if (log != null) {
			for (int i = 0; i < log.size(); i++) {
				Robots r = getGUI().getRobList().get(i);
				r.init(log.get(i));

				//System.out.println(log.get(i));

				for(int j=0;j < getGUI().getRobList().size();j++) {
					c=(char) (j+'0');
					if(StdDraw.isKeyPressed(c))
						StdDraw.setPlayer(j);
				}
				if (r.getDest() == -1) {
					getGUI().getGame().chooseNextEdge(StdDraw.getPlayer(), nextNodeGUI(r.getSrc()));


				}
			}
		}
	}

	/**
	 * a very simple random walk implementation!
	 * 
	 * @param g
	 * @param src
	 * @return
	 */
	private int nextNodeGUI(int src) {
		int nextDest = -1;
		double x = 0, y = 0;


		if(StdDraw.isMousePressed()) {

			x = StdDraw.mouseX();
			y = StdDraw.mouseY();
		}
		Point3D p = new Point3D(x, y);
		Iterator<edge_data> iter = getGUI().getGraphAlgo().get_Dgraph().getE(src).iterator();
		edgeData edge;
		while (iter.hasNext()) {
			edge = (edgeData) iter.next();
			double check = p.distance2D(edge.getNodeDest().getLocation());
			if (check <= 0.0005) {
				return edge.getDest();
			}

		}
		return nextDest;
	}
	/**
	 * get number of robots
	 */
	private void RobotsStartPosition() {
		JSONObject GameJson;
		try {
			GameJson = new JSONObject(getGUI().getGame().toString()).getJSONObject("GameServer");
			int Robot_num = GameJson.getInt("robots");
			getGUI().setRobList(new ArrayList<Robots>(Robot_num));
			for (int a = 0; a < Robot_num; a++) {
				getGUI().getGame().addRobot(StdDraw.dialogRobots(a, getGUI().getGraphAlgo().get_Dgraph().nodeSize()));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// adding robots
		Iterator<String> iter = getGUI().getGame().getRobots().iterator();
		while (iter.hasNext()) {
			getGUI().getRobList().add(new Robots(iter.next()));
		}
	}
	

	public MyGameGUI getGUI() {
		return _gui;
	}

	private void setGUI(MyGameGUI g) {
		this._gui = g;
	}


	/**** private data ***/

	private MyGameGUI _gui;

}