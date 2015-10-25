package project1;

import java.util.ArrayList;
import java.util.HashSet;

public class Program {
	public static String PrintPath(String[] path) {
		StringBuilder sb = new StringBuilder();
		String prev = "";
		HashSet<String> unique = new HashSet<String>(); 
		for (String s : path) {
			if (prev.equalsIgnoreCase(s)) continue;
			//if (unique.contains(s)) return "";
			sb.append(String.format(" %s ", s));
			unique.add(prev);
			prev = s;
		}
		return sb.toString();
	}
	
	public static String PrintMultiplePath(ArrayList<String[]> paths) {
		StringBuilder sb = new StringBuilder();
		HashSet<String> unique = new HashSet<String>();
		for (String[] path : paths) {
			String p = PrintPath(path);
			if (unique.contains(p)) continue;
			sb.append(String.format("%s\n", p));
			unique.add(p);
		}
		return sb.toString();
	}

	// args[0]: number of gen
	// args[1]: float Pc
	// args[2]: float pm
	public static void main(String[] args) {
		MyLogger.Init(args);
		
		/* the first test case scenario */ 
		Graph graph = Graph.getGraph(false);
		String[] destinations = new String[]{"4", "5", "7", "8"};
		
		/* the second test case scenario */
		//Graph graph = Graph.getGraph(true);
		//String[] destinations = new String[]{"9", "11", "12", "14", "16", "17", "19", "20"};
		
		int maxgen = args.length > 0 ? Integer.parseInt(args[0]) : 600;
		float pc = args.length > 0 ? Float.parseFloat(args[1]) : 0.9f;
		float pm = args.length > 0 ? Float.parseFloat(args[2]) : 0.2f;
		
		MulticastingRoutingPathFinder finder = new MulticastingRoutingPathFinder(graph, "1", destinations, 10);
		finder.find(25, maxgen, pc, pm);
		MyLogger.getLogger().info("*******Print obtained solutions******");
		for (String desNode : destinations) {
			MyLogger.getLogger().info(String.format("Node %s\n%s", desNode, PrintMultiplePath(finder.getPathsByEndNode(desNode))));
		}
		
		MyLogger.getLogger().info("Done");
	}

}






