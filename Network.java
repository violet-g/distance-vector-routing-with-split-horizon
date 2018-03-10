import java.util.ArrayList;
import java.util.HashMap;

/* Representation of the network the nodes form */
public class Network {

	/*
	 * A neighbour sending its routing table to a node in order to share its
	 * current knowledge of the network
	 */
	public boolean exchangeDistances(Node node, Node neighbour, boolean hasUpdate, boolean splitHorizon) {
		int cost = node.getDistanceVector().get(neighbour);
		int currentBest; // current best distance to a node in the network
		int offeredBest; // best distance offered by the neighbour
		for (Node n : neighbour.getDistanceVector().keySet()) {
			currentBest = node.getDistanceVector().get(n);
			offeredBest = neighbour.getDistanceVector().get(n) + cost;
			if (currentBest > offeredBest && offeredBest > 0) {
				// split horizon execution
				if (splitHorizon && !neighbour.getForwardingTable().get(n).getName().equals(node.getName())) {
					node.getDistanceVector().put(n, offeredBest);
					node.getForwardingTable().put(n, neighbour);
					hasUpdate = true;
				} else { // standard DVR
					node.getDistanceVector().put(n, offeredBest);
					node.getForwardingTable().put(n, neighbour);
					hasUpdate = true;
					// System.out.println(node.getName() + " updated its
					// distance to " + n.getName() + " to " + offeredBest + "
					// via " + neighbour.getName());
				}
			}
		}
		return hasUpdate;

	}

	/* Apply the algorithm for a specific number of iterations */
	public void iterate(HashMap<String, Node> nodes, int iterations, boolean trace, ArrayList<Node> nodesSet) {
		for (int i = 1; i <= iterations; i++) {
			for (Node n : nodes.values()) {
				ArrayList<Node> neighbours = n.getNeighbours();
				for (Node node : neighbours) {
					this.exchangeDistances(n, node, true, false);
				}
			}

			// print details for a custom node set if tracing is on
			if (trace && nodesSet != null) {
				for (Node node : nodesSet) {
					System.out.println("Node " + node.getName() + " at iteration " + i + ": ");
					node.printRoutingTable();
				}
			}
		}
	}

	/* Execute the DVR algorithm until convergence */
	public void converge(HashMap<String, Node> nodes, boolean splitHorizon) {
		int i = 1;
		boolean linkUpdate = false;
		boolean hasUpdate = false;
		while (true) {
			// System.out.println("Iteration " + i);
			// update any incorrect links
			for (Node n : nodes.values()) {
				linkUpdate = n.checkLinks(nodes);
			}
			// exchange information from neighbour to neighbour
			for (Node n : nodes.values()) {
				ArrayList<Node> neighbours = n.getNeighbours();
				for (Node node : neighbours) {
					hasUpdate = this.exchangeDistances(n, node, hasUpdate, splitHorizon);
				}
			}

			// if no new updates, the network has converged
			if (!hasUpdate && !linkUpdate) {
				break;
			}

			// avoid count to infinity problem if split horizon has been
			// activated
			if (!hasUpdate && linkUpdate && splitHorizon) {
				break;
			}

			hasUpdate = false;

			// stop execution if the count to infinity problem has been
			// encountered
			if (!splitHorizon && i > 10 && !hasUpdate) {
				System.out.println("Taking too long to converge...");
				break;
			}
			i++;
		}
	}

	/* Track the best route from a source to a destination */
	public void findBestRoute(Node src, Node dst) {
		ArrayList<Node> route = new ArrayList<>();
		Node curr = src;
		while (!curr.getName().equals(dst.getName())) {
			curr = curr.getForwardingTable().get(dst);
			route.add(curr);
		}

		System.out.print("Best route so far is: ");
		for (Node node : route) {
			System.out.print(node.getName() + " ");
		}
	}

	/* Fail a specific link between two nodes in the network */
	public void failLink(Node n1, Node n2) {
		n1.getDistanceVector().put(n2, Integer.MAX_VALUE);
		n2.getDistanceVector().put(n1, Integer.MAX_VALUE);
		n1.getForwardingTable().put(n2, null);
		n2.getForwardingTable().put(n1, null);
	}

	/* Change the cost of a certain link in the network */
	public void changeCost(Node n1, Node n2, int cost) {
		n1.getDistanceVector().put(n2, cost);
		n2.getDistanceVector().put(n1, cost);
	}

	/* Print the routing tables of all nodes in the network */
	public void printRoutingTables(HashMap<String, Node> nodes) {
		for (Node node : nodes.values()) {
			System.out.println("Node " + node.getName() + ":");
			node.printRoutingTable();
		}
	}
}
