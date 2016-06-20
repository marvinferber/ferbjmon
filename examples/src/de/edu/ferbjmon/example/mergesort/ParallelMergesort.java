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

import java.util.Arrays;

import de.ubt.ferbjmon.annotation.Monitored;

@Monitored
public class ParallelMergesort {

	private int[] array;
	int threadid = 1;

	/**
	 * @param array
	 */
	public ParallelMergesort(int[] array) {
		// some little sleep to make this constructor call visible in a timeline
		// diagram
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		this.array = array;

	}

	/**
	 * @param start
	 * @param end
	 * @param numthreads
	 */
	public void sort(final int start, final int end, final int numthreads) {
		// sort rest of array directly if array.length / 4 to avoid confusing
		// recursion depth in meregsort output
		if ((end - start) + 1 == array.length / 4) {
			Arrays.sort(array, start, end + 1);
			return;
		}
		// short sleep to make recursion visible in timeline diagram
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		}
		// start threads depending on needs
		boolean createThreads;
		switch (numthreads) {
		case 1:
			// single thread
			createThreads = (end - start) + 1 > array.length;
			break;
		case 2:
			// two threads
			createThreads = (end - start) + 1 == array.length;
			break;
		case 4:
			// four threads
			createThreads = (end - start) + 1 <= array.length;
			break;

		default:
			// bad value falling back to single thread
			System.out.println("bad value falling back to single thread: 1");
			createThreads = (end - start) + 1 > array.length;
			break;
		}

		if (createThreads) {
			Thread t1 = new Thread(new Runnable() {

				@Override
				public void run() {
					int newend = start + ((end - start) / 2);
					// avoid sort if current array.length =1
					if ((newend - start) + 1 >= 2)
						sort(start, newend, numthreads);
				}
			});
			t1.setName("T" + threadid);
			t1.start();
			threadid++;

			Thread t2 = new Thread(new Runnable() {

				@Override
				public void run() {
					int newstart = start + ((end - start) / 2) + 1;
					// avoid sort if current array.length =1
					if ((end - newstart) + 1 >= 2)
						sort(newstart, end, numthreads);
				}
			});
			t2.setName("T" + threadid);
			t2.start();
			threadid++;
			// join threads before merge
			try {
				t1.join();
				t2.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else {
			// if no threads were created do recursion in current thread
			int newend = start + ((end - start) / 2);
			if ((newend - start) + 1 >= 2)
				sort(start, newend, numthreads);
			int newstart = start + ((end - start) / 2) + 1;
			if ((end - newstart) + 1 >= 2)
				sort(newstart, end, numthreads);

		}
		// merge partially sorted chunks
		merge(start, end);
	}

	/**
	 * @param start
	 * @param end
	 */
	public void merge(int start, int end) {
		// add a very short delay to make merge better visible in timeline
		// diagram
		try {
			Thread.sleep(5);
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		}
		// perform merge using a temporary array
		int length = end - start + 1;
		int[] tmp = new int[length];
		System.arraycopy(array, start, tmp, 0, length);

		int i = 0, j = 0, k = tmp.length / 2;
		for (i = start; i <= end; i++) {
			if (j == tmp.length / 2) {
				array[i] = tmp[k];
				k++;
				continue;
			}
			if (k == tmp.length) {
				array[i] = tmp[j];
				j++;
				continue;
			}
			if (tmp[j] < tmp[k]) {
				array[i] = tmp[j];
				j++;
			} else {
				array[i] = tmp[k];
				k++;
			}
		}

	}

}
