/**
 * Copyright 2016 Marvin Ferber
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.edu.ferbjmon.example.mergesort;

import java.util.Random;

public class Main {

	public static void main(String[] argv) {
		int numthreads = 1;
		if (argv.length > 0)
			numthreads = Integer.parseInt(argv[0]);
		System.out.println("Using " + numthreads + " Threads..");
		// LENGTH must be factor of 8 to generate meaningful output
		// LENGTH = 134217728 takes around 5s in a Core i7 3GHz (single thread)
		final int LENGTH = 134217728;
		// fill array with random numbers
		Random rand = new Random();
		int[] array = new int[LENGTH];
		for (int i = 0; i < LENGTH; ++i) {
			int nextInt = Math.abs(rand.nextInt()) % 100;
			array[i] = (nextInt);
		}

		// init data structure
		ParallelMergesort sort = new ParallelMergesort(array);
		// start time measurement
		System.out.println("Start sorting " + LENGTH + " values..");
		long ellapsedNanos = System.nanoTime();
		// sort
		sort.sort(0, array.length - 1, numthreads);
		// stop time measurement
		ellapsedNanos = System.nanoTime() - ellapsedNanos;
		System.out.println("Done sorting..");

		System.out.println("Sorting took " + ellapsedNanos + " nanoseconds.");
	}

}
