import java.util.Random;
public class RouteFinder extends Thread {
	private int nTowns;
	private static double distanceMatrix[][];
	private int route[];
	private int ID;
	private static double bestDist = Integer.MAX_VALUE;
	private int jitter;
	private Random random;
	//private int gen;
	private Semaphore sem;
	private static long startTime = System.currentTimeMillis();

	public RouteFinder(int nTowns, double[][] distanceMatrix, int ID, int jitter, Semaphore sem) {
		this.nTowns = nTowns;
		RouteFinder.distanceMatrix = distanceMatrix;
		// this.route = route;
		this.ID = ID;
		this.jitter = jitter;
		random = new Random();
		this.sem =sem;
		
		
	}

	public void run() {
		// int jitter = 30;
		
		int[] bestRoute = new int[nTowns];
		route = new int[nTowns];
		double[][] tempDistMatrix = new double[nTowns][nTowns];
		while (true) {
			random.setSeed(System.currentTimeMillis()*ID);
			// double bestDist = Double.POSITIVE_INFINITY;

			// System.out.println(f);

			// + 4;//number between 1 and 11
			for (int i = 0; i < nTowns; i++) {
				for (int j = i; j < nTowns; j++) {
					tempDistMatrix[i][j] = distanceMatrix[i][j]
							+ ((random.nextInt(jitter)));
					tempDistMatrix[j][i] = tempDistMatrix[i][j];
				}
			}

			// for (int k = 0; k < nTowns; k++) {
			int current = (int) (random.nextInt(nTowns));
			route[0] = current; // k added to the start of the route array
			int next = 1; // tracks the next free index of the route array that
							// we can insert the next town number into
			// starting town
			boolean[] visited = new boolean[nTowns]; // boolean array that
														// mirrors the towns
														// array and keeps track
														// of whether we've
														// visited a town or not

			visited[current] = true; // we have visited the starting town
			double totalDistance = 0.0; // holds the distance travelled
			while (!allVisited(visited)) { // while there are towns left to
											// visit
				int nearest = -1; // number of the nearest neighbour.
									// initialised
									// as -1 as errors are thrown if it isnt
				for (int i = 0; i < nTowns; i++) { // iterate through the towns
					if (visited[i] == false) { // find one that hasn't been
												// visited
						nearest = i; // nearest is now an unvisited town
						break; // break to next segment
					}
				}
				for (int i = 0; i < nTowns; i++) {
					if (visited[i] == false
							&& tempDistMatrix[i][current] < tempDistMatrix[nearest][current]
							&& i != current) { // iterate, if a town hasnt been
												// visited, is closer to the
												// current than nearest and is
												// not the current twon
						nearest = i; // nearest is now that town
					} // find the nearest neighbour
				}

				route[next++] = nearest; // insert the nearest town into the
											// route array
				visited[nearest] = true; // the current town is now marked as
											// visited
				current = nearest; // the nearest neighbour is the new current
									// town
			}
			for (int i = 2; i < nTowns; i++) {
				twoOpt(i, distanceMatrix); // remove crossovers for substrings
											// of
											// size 2 to the
				// amount of towns
			}
			for (int i = 0; i < route.length; i++) {
				totalDistance += distanceMatrix[route[(i) % route.length]][route[(i + 1)
						% route.length]]; // compute total distance of the route
			}
			if (totalDistance < bestDist) { // if its the shortest distance
											// found so far, make it bestDist
				sem.grab();
				bestDist = totalDistance;
				
				System.out.println(bestDist + "km");
				for (int i = 0; i < route.length; i++) {
					bestRoute[i] = route[i];
					// bestJitter = jitter;
				}
				for (int i = 0; i < bestRoute.length; i++) {
					System.out.print(bestRoute[i] + 1 + "."); // Print route
																// numerically
				}
				System.out.println();
				long finishTime = System.currentTimeMillis() - startTime; // timer
				System.out.println(finishTime + "ms");
				System.out.println("Thread " + ID);
				System.out.println();
				sem.release();

			}
		}
	}

	public boolean allVisited(boolean[] in) { // Checks if all towns have
		// been visited using
		// the boolean array
		for (int i = 0; i < in.length; i++) {
			if (in[i] == false) {
				return false;
			}
		}
		return true;
	}

	public void twoOpt(int size, double tempDistMatrix[][]) { // Crossover
																// elimination

		for (int i = 0; i < route.length * 2; i++) { // Iterates over the route
														// array twice for good
														// optimisations
			double fDist = 0; // distance travelling forward along the
								// subsection of nodes 1 - (2 - 3 - 4) - 5
			double bDist = 0; // distance travelling forward along the
								// subsection of nodes 1 - (4 - 3 - 2) - 5
			for (int j = 0; j <= size; j++) {
				fDist += tempDistMatrix[route[(i + j) % route.length]][route[(i
						+ j + 1)
						% route.length]];
			}
			bDist += tempDistMatrix[route[(i) % route.length]][route[(i + size)
					% route.length]];
			for (int j = size; j > 1; j--) {
				bDist += tempDistMatrix[route[(i + j) % route.length]][route[(i
						+ j - 1)
						% route.length]]; // Magic *make hand wavey motion*
			}
			bDist += tempDistMatrix[route[(i + 1) % route.length]][route[(i
					+ size + 1)
					% route.length]];
			if (bDist < fDist) {
				int[] temp = new int[size];
				for (int j = 0; j < size; j++) {
					temp[j] = route[(i + size - j) % route.length];
				}
				for (int j = 0; j < temp.length; j++) {
					route[(i + 1 + j) % route.length] = temp[j];
				}
			}
		}
	}
}
