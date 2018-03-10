import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/* Read and process initial network information from an input file */
public class NetworkFileReader {

	/* Read the file and create all node instances */
	public void processInputFile(String networkFile, HashMap<String, Node> nodes) {
		String name1; // first node name from line
		String name2; // second node name from line
		Node node1 = null;
		Node node2 = null;
		int cost; // cost of edge
		String currLine;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(networkFile));
			while ((currLine = reader.readLine()) != null) {
				String[] line = currLine.split(",");
				name1 = line[0];
				name2 = line[1];
				cost = Integer.parseInt(line[2]);
				processEdge(nodes, name1, name2, node1, node2, cost);
			}
			reader.close();

			// set missing links to infinity
			for (Node n : nodes.values()) {
				n.setUnknownPaths(nodes.values());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Add the costs of links according to the file information */
	public void processEdge(HashMap<String, Node> nodes, String name1, String name2, Node node1, Node node2, int cost) {
		if (!nodes.containsKey(name1)) {
			node1 = new Node(name1);
			nodes.put(name1, node1);
		} else {
			node1 = nodes.get(name1);
		}

		if (!nodes.containsKey(name2)) {
			node2 = new Node(name2);
			nodes.put(name2, node2);
		} else {
			node2 = nodes.get(name2);
		}

		node1.addNeighbour(node2, cost);
		node2.addNeighbour(node1, cost);
	}
}
