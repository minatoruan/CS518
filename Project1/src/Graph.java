package project1;

import java.util.*;

public class Graph {
	private HashSet<String> _nodes;
	private HashMap<Edge, Integer> _edges;
	private HashMap<String, HashSet<String>> _adjacents;
	
	public static Graph parse(int[][] matrix) {
		Graph graph = new Graph();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == 0) continue; 
				graph.addEdge(String.format("%d", (i+1)), String.format("%d", (j+1)), matrix[i][j]);
			}
		}
		return graph;
	}
	
	public static Graph getGraph(boolean _20nodes) {
		if (!_20nodes) {
			Graph graph = new Graph();
			
			graph.addEdge("1", "2", 15);
			graph.addEdge("1", "3", 10);
			graph.addEdge("1", "5", 13);
			graph.addEdge("1", "6", 8);
			
			graph.addEdge("2", "4", 12);
			graph.addEdge("2", "8", 15);

			graph.addEdge("3", "4", 12);
			
			graph.addEdge("4", "8", 9);
			graph.addEdge("4", "7", 9);
			graph.addEdge("4", "6", 10);
			
			graph.addEdge("5", "6", 12);
			
			graph.addEdge("6", "7", 10);
			
			graph.addEdge("7", "8", 12);
			return graph;
		}
		return parse(new int[][]{
				{0,0,0,0,0,0,0,0,0,13,0,9,13,0,0,0,12,3,3,0},
				{0,0,8,13,9,0,0,0,0,0,3,2,0,0,4,13,9,6,8,2},
				{0,8,0,0,7,9,0,0,0,0,1,0,9,13,0,0,10,1,0,0},
				{0,13,0,0,0,15,13,16,10,0,0,0,1,0,0,12,0,6,0,0},
				{0,9,7,0,0,0,3,0,12,0,0,0,0,0,16,9,0,2,2,0},
				{0,0,9,15,0,0,8,14,0,0,0,15,0,9,2,3,5,0,0,7},
				{0,0,0,13,3,8,0,10,13,11,0,7,14,2,0,0,0,11,0,3},
				{0,0,0,16,0,14,10,0,0,9,0,6,0,12,6,5,15,0,0,0},
				{0,0,0,10,12,0,13,0,0,4,6,0,6,5,0,0,11,0,9,0},
				{13,0,0,0,0,0,11,9,4,0,0,11,0,0,2,2,0,8,1,13},
				{0,3,1,0,0,0,0,0,6,0,0,0,0,1,10,12,0,0,0,0},
				{9,2,0,0,0,15,7,6,0,11,0,0,11,1,7,0,5,0,0,0},
				{13,0,9,1,0,0,14,0,6,0,0,11,0,3,0,14,0,0,0,6},
				{0,0,13,0,0,9,2,12,5,1,1,1,3,0,0,0,0,0,12,10},
				{0,4,0,0,16,2,0,6,0,2,10,7,0,0,0,5,1,0,7,0},
				{0,13,0,12,9,3,0,5,0,2,12,0,14,0,5,0,7,0,8,0},
				{12,9,10,0,0,5,0,15,11,0,0,5,0,0,1,7,0,0,0,0},
				{3,6,1,6,2,0,11,0,0,8,0,0,0,0,0,0,0,0,5,6},
				{3,8,0,0,2,0,0,0,9,1,0,0,0,12,7,8,0,5,0,0},
				{0,2,0,0,0,7,3,0,0,13,0,0,6,10,0,0,0,6,0,0},
		});
	}
		
	public Graph() {
		_edges =  new HashMap<Edge, Integer>();
		_nodes = new HashSet<String>();
		_adjacents = new HashMap<String, HashSet<String>>();		
	}
	
	public void addEdge(String node1, String node2, int bandwidth) {
		if (_edges.containsKey(new Edge(node1, node2))) return;
		
		_edges.put(new Edge(node1, node2), bandwidth);
		_nodes.add(node1);
		_nodes.add(node2);
		
		if (!_adjacents.containsKey(node1))
			_adjacents.put(node1, new HashSet<String>());
		if (!_adjacents.containsKey(node2))
			_adjacents.put(node2, new HashSet<String>());
		
		_adjacents.get(node1).add(node2);
		_adjacents.get(node2).add(node1);
	}
	
	public int size() {
		return _nodes.size();
	}
	
	public String[] getAdjacent(String node, int minbandwidth) {
		ArrayList<String> array = new ArrayList<String>();
		for(String s : _adjacents.get(node)) {
			if (_edges.get(new Edge(node, s)) < minbandwidth) continue;
			array.add(s);
		}
		String[] adjacents = new String[array.size()];
		return array.toArray(adjacents);
	}
	
	public String[] getNodes() {
		String[] nodes = new String[_nodes.size()];
		return _nodes.toArray(nodes);
	}
	
	public boolean checkConnectivity(String[] path) {
		for(int i = 0; i < path.length - 1; i++) {
			if (path[i].equalsIgnoreCase(path[i+1])) continue;
			if (!_edges.containsKey(new Edge(path[i], path[i+1]))) return false;
		}
		return true;
	}
	
	public int minBandwidth(String[] path) {
		int minB = Integer.MAX_VALUE; 
		for(int i = 0; i < path.length - 1; i++) {
			if (path[i].equalsIgnoreCase(path[i+1])) continue;
			if (!_edges.containsKey(new Edge(path[i], path[i+1]))) return -1;
			minB = Math.min(minB, _edges.get(new Edge(path[i], path[i+1])));
		}
		return minB;
	}	
}
