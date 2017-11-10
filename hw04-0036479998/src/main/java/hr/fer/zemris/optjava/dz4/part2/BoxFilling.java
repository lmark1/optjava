package hr.fer.zemris.optjava.dz4.part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * This class implements a steady state genetic algorithm used
 * for solving a boxfilling problem.
 * 
 * Program accepts following arguments:
 * 1. Path to the proeblem definition file
 * 2. population size
 * 3. n (n>=2)
 * 4. m (m>=2)
 * 5. false: Children are added to population regardless of fitness
 * 	  true: Children are added to population only if they have higher fitness
 * 6. maximum number of iterations
 * 7. accepted maximal container length
 * 
 * @author lmark
 *
 */
public class BoxFilling {

	private static final Random random = new Random();
	
	private static int boxWidth;
	private static int populationSize;
	private static int n;
	private static int m;
	private static boolean flag;
	private static int maximumIterations;
	
	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 7) {
			System.out.println("7. arguments expected");
			return;
		}
		
		Path newPath = Paths.get(args[0]);
		List<Stick> sticks = parseFile(newPath);
		System.out.println("Initial seed: " + sticks);
		
		String[] fileArgs =newPath.getFileName().toString().split("-");
		
		boxWidth = Integer.parseInt(fileArgs[1]);
		populationSize = Integer.valueOf(args[1]);
		n = Integer.valueOf(args[2]);
		m = Integer.valueOf(args[3]);
		maximumIterations = Integer.valueOf(args[5]);
		
		flag = false;
		if ("true".equals(args[4].toLowerCase())) {
			flag = true;
		}
		
		run(sticks);
	}
	
	private static void run(List<Stick> seed) {
		
		// Generate new population based on given seed
		List<Container> currentPopulation = new ArrayList<>();
		for (int i = 0; i < populationSize; i++) {
			currentPopulation.add(new Container(seed, boxWidth, random));
			System.out.println(currentPopulation.get(i));
		}
		
		// Initialize ga components
		ContainerTournament parentSelection = new ContainerTournament(n, true);
		ContainerTournament childReplacement = new ContainerTournament(m, false);
		ContainerCrossover crossover = new ContainerCrossover(boxWidth);
		
		// Sort population
		Collections.sort(currentPopulation, new Comparator<Container>() {

			@Override
			public int compare(Container o1, Container o2) {
				if (o1.fitness > o2.fitness) {
					return -1;
				} else if (o1.fitness < o2.fitness) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		
		System.err.println("Starting: ");
		for (int i = 0; i < maximumIterations; i++) {
			
			System.out.println("Iter: " + i + ", " + currentPopulation.get(0));
			
			// Select 2 parents
			Container parent1 = parentSelection.selectParent(currentPopulation, random);
			Container parent2 = parentSelection.selectParent(currentPopulation, random);
			//System.out.println("Parent1: " + parent1);
			//System.out.println("Parent2: " + parent2);
			
			// Generate children
			Container[] children = crossover.cross(parent1, parent2, random);
			Container bestChild = children[0].fitness > children[1].fitness ? children[0] : children[1];
			//System.out.println("Best child: " + bestChild);
			
			// Get replacement
			Container replacement = childReplacement.selectParent(currentPopulation, random);
			//System.out.println("Replacement: " + replacement);
			
			if (flag) {
				if (bestChild.fitness >= replacement.fitness) {
					if(!currentPopulation.remove(replacement)) {
						throw new IllegalArgumentException("Unable to remove worst");
					}
					currentPopulation.add(bestChild);
				}
			} else {
				if(!currentPopulation.remove(replacement)) {
					throw new IllegalArgumentException("Unable to remove worst");
				}
				currentPopulation.add(bestChild);
			}
			
		}
	}

	/**
	 * Parse box file.
	 * 
	 * @param file
	 * @return
	 */
	private static List<Stick> parseFile(Path file) {
		
		String line = null;
		try {
			line = Files.readAllLines(file).get(0);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		String[] numbers = line.substring(1, line.length()-1).split(",");
		List<Stick> sticks = new ArrayList<>();
		
		int index = 0;
		for (String num: numbers) {
			sticks.add(new Stick(index, Integer.valueOf(num.trim())));
			index++;
		}
		
		return sticks;
	}

}
