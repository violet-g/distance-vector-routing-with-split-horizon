import java.util.ArrayList;
import java.util.HashMap;

/* Distance Vector Routing Algorithm Runner */
public class DVRRunner {

	public static void main(String[] args) {
		int iterations = 0;
		boolean splitHorizon = false;
		String command = "";

		// default input file
		String networkFile = "input-medium.txt";
		HashMap<String, Node> nodes = new HashMap<>();

		Network network = new Network();
		NetworkFileReader reader = new NetworkFileReader();
		reader.processInputFile(networkFile, nodes);

		// process command line arguments
		try {
			command = args[0];

			/* Run the algorithm for a specific number of iterations */
			if (command.equals("iterate")) {
				iterations = Integer.parseInt(args[1]);
				network.iterate(nodes, iterations, false, null);
				network.printRoutingTables(nodes);
			}

			/*
			 * Run the algorithm until convergence with an option to use split
			 * horizon
			 */
			else if (command.equals("converge")) {
				if (args.length == 2 && args[1].equals("split-horizon")) {
					splitHorizon = true;
				}
				network.converge(nodes, splitHorizon);
				network.printRoutingTables(nodes);
			}

			/*
			 * Find best route found between two nodes after a given number of
			 * iterations
			 */
			else if (command.equals("route")) {
				Node src = nodes.get(args[1]);
				Node dst = nodes.get(args[2]);
				iterations = Integer.parseInt(args[3]);
				network.iterate(nodes, iterations, false, null);
				network.findBestRoute(src, dst);
			}

			/*
			 * Trace the changes in the routing tables of a chosen set of nodes
			 * through a set number of iterations
			 */
			else if (command.equals("trace")) {
				ArrayList<Node> nodesSet = new ArrayList<>();
				iterations = Integer.parseInt(args[1]);
				for (int i = 2; i < args.length; i++) {
					nodesSet.add(nodes.get(args[i]));
				}
				for (Node node : nodesSet) {
					System.out.println(node.getName());
				}
				network.iterate(nodes, iterations, true, nodesSet);
			}

			/*
			 * Run the algorithm for a specific number of iterations, fail one
			 * link and continue executing until convergence with the option to
			 * use split horizon
			 */
			else if (command.equals("fail")) {
				if (args.length == 5 && args[4].equals("split-horizon")) {
					splitHorizon = true;
				}
				Node node1 = nodes.get(args[1]);
				Node node2 = nodes.get(args[2]);
				iterations = Integer.parseInt(args[3]);
				network.iterate(nodes, iterations, false, null);
				network.failLink(node1, node2);
				network.converge(nodes, splitHorizon);
				network.printRoutingTables(nodes);
			}

			/*
			 * Run the algorithm for a specific number of iterations, change the
			 * cost of one link and continue executing until convergence with
			 * the option to use split horizon
			 */
			else if (command.equals("change-cost")) {
				if (args.length == 6 && args[5].equals("split-horizon")) {
					splitHorizon = true;
				}
				Node node1 = nodes.get(args[1]);
				Node node2 = nodes.get(args[2]);
				int cost = Integer.parseInt(args[3]);
				iterations = Integer.parseInt(args[4]);
				network.iterate(nodes, iterations, false, null);
				network.changeCost(node1, node2, cost);
				network.converge(nodes, splitHorizon);
				network.printRoutingTables(nodes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
