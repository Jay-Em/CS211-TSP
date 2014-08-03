import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TSP {
	public static double[][] distanceMatrix = null;
	public static int[] route = null; // global variables cause yolo

	public static void main(String args[]) {
		long startTime = System.currentTimeMillis(); // Start timer
		File towns = new File("X:\\CS211\\Lab9\\src\\towns.txt");
		double[] lats = null; // latitude
		double[] longs = null; // longitude
		String[] townNames = null; // translates the towns name into a number,
									// much easier for iteration and final
									// printout
		int nTowns = 0; // Number of towns
		Scanner s = null;

		// Scan in the list of towns and co-ordinates
		// The format is "latitude longitude townname"

		try {
			s = new Scanner(towns);
			while (s.hasNextLine()) {
				nTowns = s.nextInt(); // First thing in the document should be
										// the number of towns on its own line
				s.nextLine();
				townNames = new String[nTowns];
				lats = new double[nTowns];
				longs = new double[nTowns];
				route = new int[nTowns];
				for (int i = 0; i < nTowns; i++) {
					lats[i] = s.nextDouble();
					longs[i] = s.nextDouble();
					townNames[i] = s.nextLine().trim();
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace(); // housekeeping
		}

		distanceMatrix = new double[nTowns][nTowns]; // An n*n distance matrix
														// that holds the
														// distances between
														// each town
		

		for (int i = 0; i < nTowns; i++) {
			for (int j = 0; j < nTowns; j++) {
				distanceMatrix[i][j] = haversine(lats[i], longs[i], lats[j],
						longs[j]); // holds the haversine value
				//distanceMatrix[j][i] = distanceMatrix[i][j]; // xy is the same
																// as yx, just
																// for handiness
			}
		}

		 // Just a high number as a
													// default best distance.
													// bestDist holds the best
													// total distance found
		 // bestRoute is an int array that
											// holds the path in the form of
											// town numbers
		//int bestJitter = 100;
		//for(int f = 0; f < 1000; f++){
		while(true){
			double bestDist = Double.POSITIVE_INFINITY;
			int[] bestRoute = new int[nTowns];
			//System.out.println(f);
		
		double[][] tempDistMatrix = new double[nTowns][nTowns];
		int jitter = 20;// + 4;//number between 1 and 11
		for(int i = 0; i < nTowns; i++){
			for(int j = 0; j < nTowns; j++){
				tempDistMatrix[i][j] = distanceMatrix[i][j] + ((Math.random()*jitter));
			}
		}
		
		//for (int k = 0; k < nTowns; k++) {
		int current = (int)(Math.random()*nTowns);
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
				int nearest = -1; // number of the nearest neighbour. initialised
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
				twoOpt(i, distanceMatrix); // remove crossovers for substrings of size 2 to the
							// amount of towns
			}
			for (int i = 0; i < route.length; i++) {
				totalDistance += distanceMatrix[route[(i) % route.length]][route[(i + 1)
						% route.length]]; // compute total distance of the route
			}
			if (totalDistance < bestDist) { // if its the shortest distance
											// found so far, make it bestDist
				bestDist = totalDistance;
				System.out.println(bestDist + "km");
				for (int i = 0; i < route.length; i++) {
					bestRoute[i] = route[i];
					//bestJitter = jitter;
				}
				for (int i = 0; i < bestRoute.length; i++) {
					System.out.print(bestRoute[i] + 1 + "."); // Print route numerically
				}
				System.out.println();
			}
		}
		

		//long finishTime = System.currentTimeMillis() - startTime; // timer
		//System.out.println("Time taken: " + finishTime + "ms");
		//System.out.println("Total distance: " + bestDist + "km"); // Print
																	// distance

		//for (int i = 0; i < bestRoute.length; i++) {
		//	System.out.print(bestRoute[i] + 1 + "."); // Print route numerically
		//}
		//System.out.println();
		//System.out.println("Jitter: " + bestJitter);
		//System.out.println(bestDist/nTowns);
		//for (int i = 0; i <= bestRoute.length; i++) {
			//System.out.println(townNames[bestRoute[i % bestRoute.length]]); // Print
																			// town
																			// names
		//}
	}

	public static boolean allVisited(boolean[] in) { // Checks if all towns have
														// been visited using
														// the boolean array
		for (int i = 0; i < in.length; i++) {
			if (in[i] == false) {
				return false;
			}
		}
		return true;
	}

	public static double haversine(double x1, double y1, double x2, double y2) { // Computes
																					// the
																					// haversine
																					// distance
																					// given
																					// GPS
																					// co-ords

		x1 *= (Math.PI / 180.0);
		y1 *= (Math.PI / 180.0);
		x2 *= (Math.PI / 180.0);
		y2 *= (Math.PI / 180.0);

		return (2.0 * 6371.0 * Math.asin(Math.sqrt(Math.pow(
				Math.sin((x1 - x2) / 2.0), 2.0)
				+ (Math.cos(x1) * Math.cos(x2) * (Math.pow(
						Math.sin((y1 - y2) / 2.0), 2.0))))));
	}

	public static void twoOpt(int size, double tempDistMatrix[][]) { // Crossover elimination

		for (int i = 0; i < route.length*2; i++) { // Iterates over the route
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
