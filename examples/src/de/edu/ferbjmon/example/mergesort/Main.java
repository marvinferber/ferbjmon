package de.edu.ferbjmon.example.mergesort;
import java.util.ArrayList;
import java.util.Random;

public class Main {

	public static void main(String[] argv) {

		final int LENGTH = 8;
		Random rand = new Random();
		ArrayList<Integer> array = new ArrayList<Integer>(LENGTH);

		for (int i = 0; i < LENGTH; ++i) {
			int nextInt = Math.abs(rand.nextInt()) % 100;
			array.add(nextInt);
			System.out.print(nextInt + " ");
		}
		System.out.println();

		ParallelMergesort sort = new ParallelMergesort(array);
		System.out.println("About to sort.");
		long ellapsedNanos = System.nanoTime();
		sort.sort(0, array.size() - 1);
		ellapsedNanos = System.nanoTime() - ellapsedNanos;
		System.out.println("Done sorting.");

		for (int i = 0; i < array.size(); i++) {
			System.out.print(array.get(i) + " ");
		}
		System.out.println();
		System.out.println("Success, sorting took " + ellapsedNanos + " nanoseconds.");
	}

}
