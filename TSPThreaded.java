import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TSPThreaded {
	public static double[][] distanceMatrix = null;
	public static int[] route = null; // global variables cause yolo

	public static void main(String args[]) throws InterruptedException {
		// long startTime = System.currentTimeMillis(); // Start timer
		File towns = new File("X:\\CS211\\Lab9\\src\\100towns.txt");
		double[] lats = null; // latitude
		double[] longs = null; // longitude
		String[] townNames = null; // translates the towns name into a number,
									// much easier for iteration and final
									// printout
		int nTowns = 0; // Number of towns
		Scanner s = null;
		//System.out.println(System.currentTimeMillis());

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
				// distanceMatrix[j][i] = distanceMatrix[i][j]; // xy is the
				// same
				// as yx, just
				// for handiness
			}
		}
		s = new Scanner(System.in);
		System.out.println("Number of threads:");
		int nThreads = s.nextInt();
		Semaphore sem = new Semaphore();
		RouteFinder[] finderArray = new RouteFinder[nThreads];// = new
																// RouteFinder(nTowns,
																// route,
																// distanceMatrix);
			for (int i = 0; i < nThreads; i++) {
				finderArray[i] = new RouteFinder(nTowns, distanceMatrix,
						(i + 1), 20, sem);
				finderArray[i].start();
			}

	}

	public static double haversine(double x1, double y1, double x2, double y2) {

		x1 *= (Math.PI / 180.0);
		y1 *= (Math.PI / 180.0);
		x2 *= (Math.PI / 180.0);
		y2 *= (Math.PI / 180.0);

		return (2.0 * 6371.0 * Math.asin(Math.sqrt(Math.pow(
				Math.sin((x1 - x2) / 2.0), 2.0)
				+ (Math.cos(x1) * Math.cos(x2) * (Math.pow(
						Math.sin((y1 - y2) / 2.0), 2.0))))));
	}
}