import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Drawdown {
	public static void main(String args[]) throws FileNotFoundException {

		File apple = new File("X:\\CS211\\Lab9\\apple.txt");
		File facebook = new File("X:\\CS211\\Lab9\\facebook.txt");
		File dates = new File("X:\\CS211\\Lab9\\dates.txt");

		Scanner s = new Scanner(apple);
		double[] appleArray = new double[252];
		int i = 251;
		while (s.hasNext()) {
			appleArray[i--] = s.nextDouble();
		}
		s.close();
		s = new Scanner(facebook);
		double[] facebookArray = new double[252];
		i = 251;
		while (s.hasNext()) {
			facebookArray[i--] = s.nextDouble();
		}
		s.close();
		s = new Scanner(dates);
		String[] datesArray = new String[252];
		i = 251;
		while (s.hasNext()) {
			datesArray[i--] = s.nextLine();
		}
		s.close();
		

		
		
		
		
		int bestMin = 0;
		int bestMax = 0;
		int min = 0;
		int max = 0;
		double bestDrop = 0;
		double drop = 0;

		for (i = 0; i < 252; i++) {
			if (appleArray[i] > appleArray[max]) {
				max = i;
			}
			if (appleArray[i] < appleArray[min]) {
				min = i;
			}
		}
		if (max < min) {
			bestMax = max;
			bestMin = min;
			bestDrop = appleArray[max] - appleArray[min];
		} 
		else {
			int localMin = max;
			for (i = max + 1; i < 252; i++) {
				if (appleArray[i] < appleArray[localMin]) {
					localMin = i;
				}
			}
			int localMax = min;
			for (i = 0; i < min; i++) {
				if (appleArray[i] > appleArray[localMax]) {
					localMax = i;
				}
			}
			if (((appleArray[max] - appleArray[localMin])/appleArray[max]) > (appleArray[localMax] - appleArray[min])/appleArray[localMax]) {
				bestDrop = appleArray[max] - appleArray[localMin];
				bestMax = max;
				bestMin = localMin;
			} else {
				bestDrop = appleArray[localMax] - appleArray[min];
				bestMax = localMax;
				bestMin = min;
			}

		}
		System.out.println(bestDrop);
		System.out.println(datesArray[bestMax] + " - " + datesArray[bestMin]);
	}
}
