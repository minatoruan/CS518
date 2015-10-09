package hw1;
import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HireAssistantTest {
	
	public static int NumberOfRuns = 5;
	public static String PrimeText = "10000.txt";
	
	// option 0: order list, n = 2000
	// option 1: in-order list, n = 2000
	// option 2: uniformly distributed random number, n = 2000
	// option 3: uniformly distributed random number, n = 5000
	// option 4: list of ordered 10000 primes
	public static int[] getList(int option) {
		int length = 0;
		switch (option) {
		case 0:
		case 1:
		case 2:
			length = 2000;
			break;
		case 3:
			length = 5000;
			break;
		case 4:
			length = 10000;
			break;			
		}
		
		if (option == 4) {
			return getListFromFile("bin/p10000.txt", length);
		}		
		
		int[] array = new int[length];
		Random r = new Random();
		for (int i = 0; i < length; i++)
			switch (option) {
			case 0: 
				array[i] = i+1;
				break;
			case 1:
				array[i] = length - i - 1;
				break;
			case 2:
			case 3:
				array[i] = r.nextInt();
				break;
			}
		return array;
	}
	
	public static int[] PermuteInPlace(int[] array) {
		Random r = new Random();
		int length = array.length;
		int swap_index = 0;
		for (int i = 0; i < length; i++) {
			swap_index = r.nextInt(length - i) + i;
			if (swap_index == i) continue;
			array[i] ^= array[swap_index];
			array[swap_index] ^= array[i];
			array[i] ^= array[swap_index];
		}
		return array;
	}
	
	public static int[] getListFromFile(String filepath, int length) {
		try {
			FileInputStream fis = new FileInputStream(new File(filepath));
			int[] array = new int[length];
			String line = null;
			int index = 0;
			
			try (BufferedReader br = 
					new BufferedReader(new InputStreamReader(fis))) {
				while ((line = br.readLine()) != null || index == length){
					if (line== null) break;
					if (line.equalsIgnoreCase("")) continue;
					
					for (String item : line.replace("  ", " ").split(" ")) {
						if (item.equalsIgnoreCase("")) continue;
						array[index++] = Integer.parseInt(item);
					}
				}
				return array;
			}
		} catch (IOException exception) {
			System.out.printf("Exception %s", exception.getMessage());
		}
		return null;
	}
	
	public static int Randomized_Hire_Assistant(int[] array) {
		int[] a = PermuteInPlace(array);
		int best = 0;
		int n_of_hires = 1;
		
		for(int i = 1; i < a.length; i++)
			if (a[i] > a[best]) {
				n_of_hires++; 
				best = i;
			}
		
		return n_of_hires;
	}
	
	public static void main(String[] args) {
		for (int option = 0; option < 5; option++) {
			System.out.printf("Input %d\n", option+1);
			int[] array = getList(option);
			int sum_of_hires = 0;
			int hires = 0;
			for (int idx = 0; idx < NumberOfRuns; idx++) {
				hires = Randomized_Hire_Assistant(array);
				System.out.printf("\tTime %d: %d\n", idx+1, hires);
				sum_of_hires += hires;
			}
			System.out.printf("\tAvg: %.1f\n", sum_of_hires/5.0);
		}
	}

}
