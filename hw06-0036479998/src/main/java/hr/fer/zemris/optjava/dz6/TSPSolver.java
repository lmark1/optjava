package hr.fer.zemris.optjava.dz6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * TSP Solver implementation.
 * 
 * Accepts 4 command line arguments:
 * 1) file - TSP configuration file.
 * 2) k - candidate list size
 * 3) l - number of ants in colony
 * 4) maxiter - maximum number of generations
 * 
 * @author lmark
 *
 */
public class TSPSolver {

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 4) {
			System.out.println("Program accepts 4 command line arguments.");
			return;
		}
		
		Path filePath = Paths.get(args[0]);
		int maxAnts = Integer.valueOf(args[2]);
		int maxIter = Integer.valueOf(args[3]);
		List<City> cities = parseFile(filePath);
		System.out.println(cities);
		
		// Initialize node list
		int candidateCount = Integer.valueOf(args[1]);
		List<Node> nodes = new ArrayList<>();
		for (City city : cities) {
			City[] nearest = findKNearest(city, cities, candidateCount);
			if (nearest == null) {
				System.out.println();
			}
			nodes.add(new Node(city, nearest));
		}
		
		// K - nearest candidates
		for (Node node : nodes) {
			
			Node[] nodeCandidates = new Node[candidateCount];
			for (int i = 0; i < candidateCount; i++) {
				nodeCandidates[i] = nodes.get(node.candidates[i].ID - 1);
			}
			node.nodeCandidates = nodeCandidates;
		}
		
		System.out.println();
		
		MMAS mmas = new MMAS(nodes, maxIter, maxAnts, 0.05, 2, 4);
		mmas.run();
	}


	/**
	 * Returns K nearest cities.
	 * 
	 * @param city
	 * @param cities
	 * @param candidateCount
	 * @return
	 */
	private static City[] findKNearest(City root, List<City> cities,
			int candidateCount) {
		
		List<City> copy = new ArrayList<>();
		copy.addAll(cities);
		
		// Sort out the array distances
		Collections.sort(copy, new Comparator<City>() {

			@Override
			public int compare(City o1, City o2) {
				
				if (o1.equals(root)) {
					return 1;
				}
				
				if (o2.equals(root)) {
					return -1;
				}
				
				double d1 = Math.sqrt(
						Math.pow(root.x - o1.x, 2) +
						Math.pow(root.y - o1.y, 2));
				
				double d2 = Math.sqrt(
						Math.pow(root.x - o2.x, 2) +
						Math.pow(root.y - o2.y, 2));
				
				if (d1 > d2) {
					return 1;
				} else if (d1 < d2) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		copy = copy.subList(0, copy.size()-1);
		
		// Get 5 best
		City[] best = new City[candidateCount];
		for (int i = 0; i < candidateCount; i++) {
			best[i] = copy.remove(0);
		}
		
		//System.out.println(Arrays.asList(best));
		return best;
	}


	/**
	 * Parse tsp file.
	 * 
	 * @param filePath
	 */
	private static List<City> parseFile(Path filePath) {
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		List<City> cities = new ArrayList<>();
		boolean start = false;
		for (String line : lines) {
			System.out.println(line);
			
			if (line.isEmpty()) {
				continue;
			}
			
			if (line.equals("DISPLAY_DATA_SECTION") || line.equals("NODE_COORD_SECTION")) {
				start = true;
				continue;
			}
			
			if (!start) {
				continue;
			}
			
			if (line.equals("EOF")) {
				break;
			}
			
			String[] words = line.trim().split("\\s++");
			
			int ID = Integer.valueOf(words[0]);
			double x = Double.valueOf(words[1]);
			double y = Double.valueOf(words[2]);
			
			cities.add(new City(ID, x, y));
		}
		
		return cities;
	}	
}
