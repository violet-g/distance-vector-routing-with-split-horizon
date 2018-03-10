import java.util.ArrayList;
import java.util.HashMap;

/* Representation of a single node in the network */
public class Node {

	/*
	 * Each node has a name following the convention N{num}, e.g. N1, N2, etc.,
	 * and a routing table that consists of a forwarding table providing the
	 * first hop to any destination and a distance vector providing the distance
	 * to any destination
	 */
	private String name;
	private HashMap<Node, Node> forwardingTable;
	private HashMap<Node, Integer> distanceVector;

	public Node(String name) {
		this.name = name;
		this.forwardingTable = new HashMap<>();
		this.distanceVector = new HashMap<>();
	}

	public String getName() {
		return name;
	}

	public HashMap<Node, Node> getForwardingTable() {
		return forwardingTable;
	}

	public HashMap<Node, Integer> getDistanceVector() {
		return distanceVector;
	}

	public void addNeighbour(Node node, int cost) {
		this.distanceVector.put(node, cost);
		this.forwardingTable.put(node, node);
	}

	/* Return all adjacent nodes */
	public ArrayList<Node> getNeighbours() {
		ArrayList<Node> neighbours = new ArrayList<>();
		for (Node node : this.getForwardingTable().keySet()) {
			if (this.getForwardingTable().get(node) != null
					&& node.getName().equals(this.getForwardingTable().get(node).getName())
					&& !node.getName().equals(this.getName())) {
				neighbours.add(node);
			}
		}
		return neighbours;
	}

	/*
	 * Finish the initialisation of the routing tables to include destinations
	 * for which no path has been found yet
	 */
	public void setUnknownPaths(Iterable<Node> nodes) {
		for (Node n : nodes) {
			if (!this.distanceVector.containsKey(n)) {
				// initialise to infinity
				if (!n.getName().equals(this.getName())) {
					this.distanceVector.put(n, Integer.MAX_VALUE);
					this.forwardingTable.put(n, null);
				} else { // initialise to 0 if this is the node itself
					this.forwardingTable.put(n, n);
					this.distanceVector.put(n, 0);

				}
			}
		}
	}

	/*
	 * Print whole formatted routing table of the node - this is a combination
	 * of it distance vector and forwarding table
	 */
	public void printRoutingTable() {
		for (Node n : this.getDistanceVector().keySet()) {
			System.out.print(n.getName() + " for distance ");
			if (this.getDistanceVector().get(n) != null) {
				System.out.print(this.getDistanceVector().get(n));
			} else {
				System.out.print("null");
			}

			if (this.getForwardingTable().get(n) != null) {
				System.out.print(" via node " + this.getForwardingTable().get(n).getName());
			} else {
				System.out.print(" via node null");
			}
			System.out.println();
		}
		System.out.println("------");
	}

	/*
	 * After any link failures or cost updates, check and update any links in
	 * the routing tables that are not accurate anymore
	 */
	public boolean checkLinks(HashMap<String, Node> nodes) {
		boolean linkUpdate = false;
		for (Node node : nodes.values()) {
			int currCost = this.getDistanceVector().get(node);
			// first hop on the path
			Node neighbour = this.getForwardingTable().get(node);
			if (neighbour != null && !neighbour.getName().equals(node.getName())) {
				int linkCost = this.getDistanceVector().get(neighbour);
				int neighbourCost = neighbour.getDistanceVector().get(node);
				if (linkCost != 0 && currCost < (neighbourCost + linkCost)) {
					this.getDistanceVector().put(node, neighbourCost + linkCost);
					linkUpdate = true;
				}
			}
		}
		return linkUpdate;
	}
}
