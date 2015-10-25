package project1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class MulticastingRoutingPathFinder {
	private Graph _graph;
	private String _source;
	private String[] _destinations;
	private int _minBandwidth;
	private ArrayList<String[]> _population;
	
	public MulticastingRoutingPathFinder(Graph graph, String source, String[] destinations, int minBandwidth) {
		this._graph = graph;
		this._source = source;
		this._destinations = destinations;
		this._minBandwidth = minBandwidth;
	}
	
	protected int[] selectChromosome(Random rnd, int populationSize) {
		return new int[] {rnd.nextInt(populationSize), rnd.nextInt(populationSize)};
	}
	
	//perform one-cut crossover at random point
	protected ArrayList<String[]> mate(Random rnd, ArrayList<String[]> population) {
		ArrayList<String[]> offspring = new ArrayList<String[]>(1);
		
		String[] s1 = population.get(0).clone();
		String[] s2 = population.get(1);
		
		int idx=rnd.nextInt(Math.min(s1.length, s2.length));
		if (idx == s2.length) return offspring;
		
		for (; idx < Math.min(s1.length, s2.length); idx++) s1[idx] = s2[idx];
		
		offspring.add(s1);
		return offspring; 
	}
		
	public void find(int popsize, int maxGen, float pc, float pm) {
		Random rnd = new Random();
		
		_population = initPopulation(rnd, popsize);
		for (int gen = 0; gen < maxGen; gen++) {
			MyLogger.getLogger().info(String.format("Processing gen %d", gen));
			for (int p = 0; p < popsize; ) {
				int[] selectedIndices = selectChromosome(rnd, _population.size());
				
				if (rnd.nextFloat() > pc) continue;
				ArrayList<String[]> selectedChromesome = getSelectedSchromosome(_population, selectedIndices);
				for(String[] chromosome : mate(rnd, selectedChromesome)) {
					if (rnd.nextFloat() < pm) chromosome = mutate(rnd, chromosome, _graph.getNodes());
					if (_graph.minBandwidth(chromosome) < _minBandwidth) continue;
					MyLogger.getLogger().info(String.format("Adding offspring %d", p));
					_population.add(chromosome);
					p++;
				}
			}
		}
	}
	
	public ArrayList<String[]> getPathsByEndNode(String node) {
		ArrayList<String[]> filter = new ArrayList<String[]>();
		if (_population == null) return filter;
		
		for (String[] s : _population) {
			if (!s[s.length - 1].equalsIgnoreCase(node) || _graph.minBandwidth(s) < _minBandwidth) continue;
			filter.add(s);
		}
		return filter;
	}
		
	private String[] removedVisistedNodes(String[] adjacents, ArrayList<String> visitedNodes) {
		HashSet<String> hash = new HashSet<String>();
		ArrayList<String> unvisitedNodes = new ArrayList<String>();
		for (String s : visitedNodes) hash.add(s);
		for (String s : adjacents) 
			if (!hash.contains(s)) unvisitedNodes.add(s);
		String[] unvisitedArray = new String[unvisitedNodes.size()];
		return unvisitedNodes.toArray(unvisitedArray);
	}
	
	private String[] generateChromesome(Random rnd, String[] nodes, String source, String destination) {
		MyLogger.getLogger().info(String.format("Generate new chromosome %s -> %s", source, destination));
		ArrayList<String> chromosome = new ArrayList<String>();
		chromosome.add(source);
		String currentNode = source;
		while (!currentNode.equalsIgnoreCase(destination)) {
			String[] adjacents = removedVisistedNodes(_graph.getAdjacent(currentNode, _minBandwidth), chromosome);
			MyLogger.getLogger().info(String.format("Current node: %s, Length of unvisited adjacents: %d", currentNode, adjacents.length));
			
			if (adjacents.length == 0) return new String[0];
			
			String chosenNode = adjacents[rnd.nextInt(adjacents.length)];
			MyLogger.getLogger().info(String.format("Chosen node: %s", chosenNode));
			
			if (chromosome.contains(chosenNode)) continue;
			currentNode = chosenNode;
			chromosome.add(currentNode);
		}
		String[] array = new String[chromosome.size()];	
		return chromosome.toArray(array);
	}
	
	/*
	private String[] generateChromesome(Random rnd, String[] nodes, String source, String destination) {
		MyLogger.getLogger().info(String.format("Generate new chromosome %s -> %s", source, destination));
		ArrayList<String> chromosome = new ArrayList<String>();
		chromosome.add(source);
		String currentNode = source;
		while (chromosome.size() < nodes.length - 1) {
			String chosenNode = nodes[rnd.nextInt(nodes.length)];
			if (chosenNode.equalsIgnoreCase(source) || chosenNode.equalsIgnoreCase(destination)) continue;
			currentNode = chosenNode;
			chromosome.add(currentNode);
		}
		chromosome.add(destination);
		String[] array = new String[chromosome.size()];	
		return chromosome.toArray(array);
	}
	*/
	private ArrayList<String[]> initPopulation(Random rnd, int popsize) {
		MyLogger.getLogger().info(String.format("Init population - size %d", popsize));
		int des_pos = 0;
		ArrayList<String[]> population = new ArrayList<String[]>();
		String[] nodes = _graph.getNodes();
		for(int i = 0; i < popsize * _destinations.length; ) {
			String[] chromosome = generateChromesome(rnd, nodes, _source, _destinations[des_pos]);
			if (chromosome.length > 0 && _graph.checkConnectivity(chromosome)) {
				MyLogger.getLogger().info(String.format("Add chromosome - size %d", population.size()));
				population.add(chromosome);
				i++;
				des_pos++; 
				if (des_pos == _destinations.length) des_pos = 0;
			}
		}
		return population;
	}

	private ArrayList<String[]> getSelectedSchromosome(ArrayList<String[]> population, int[] selectedIndices) {
		ArrayList<String[]> selectedChromosomes = new ArrayList<String[]>(selectedIndices.length);
		for (int index : selectedIndices) selectedChromosomes.add(population.get(index));
		return selectedChromosomes;
	}
	
	private String[] mutate(Random rnd, String[] chromosome, String[] nodes) {
		if (chromosome.length == 2) {
			MyLogger.getLogger().info(String.format("Skip mutate a chromosome %d", chromosome.length));
			return chromosome;
		}
		
		MyLogger.getLogger().info(String.format("Mutate a chromosome %d", chromosome.length));
		int i = rnd.nextInt(chromosome.length-2) + 1;
		int j = 0;
		do {
			j= rnd.nextInt(nodes.length);
		} while(!nodes[j].equalsIgnoreCase(chromosome[0]) && !nodes[j].equalsIgnoreCase(chromosome[chromosome.length-1]));
		chromosome[i] = nodes[j];
		return chromosome;
	}
}