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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.ubt.ferbjmon.annotation.Monitored;

@Monitored
public class ParallelMergesort {

	private ArrayList<Integer> array;
	private boolean first = true;

	public ParallelMergesort(ArrayList<Integer> array) {
		try {
			Thread.sleep(5);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.array = array;

	}

	public void sort(final int start, final int end) {
		if ((end - start) + 1 < 2)
			return;
		try {
			Thread.sleep(5);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (first) {
			first = false;
			Thread t1 = new Thread(new Runnable() {

				@Override
				public void run() {

					int newend = start + ((end - start) / 2);
					if ((newend - start) + 1 >= 2)
						sort(start, newend);
				}
			});
			t1.setName(start + "<-->" + (start + ((end - start) / 2)));
			t1.start();

			Thread t2 = new Thread(new Runnable() {

				@Override
				public void run() {
					int newstart = start + ((end - start) / 2) + 1;
					if ((end - newstart) + 1 >= 2)
						sort(newstart, end);
				}
			});
			t2.setName((start + ((end - start) / 2) + 1) + "<-->" + end);
			t2.start();

			try {
				t1.join();
				t2.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else {
			int newend = start + ((end - start) / 2);
			if ((newend - start) + 1 >= 2)
				sort(start, newend);
			int newstart = start + ((end - start) / 2) + 1;
			if ((end - newstart) + 1 >= 2)
				sort(newstart, end);

		}
		try {
			Thread.sleep(5);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		merge(start, end);
	}

	public void merge(int start, int end) {
		try {
			Thread.sleep(5);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Integer[] neu = new Integer[end - start + 1];
		array.subList(start, end + 1).toArray(neu);
		Integer[] neuer = new Integer[neu.length];
		System.arraycopy(neu, 0, neuer, 0, neu.length);
		List<Integer> tmp = Arrays.asList(neuer);

		int i = 0, j = 0, k = tmp.size() / 2;
		for (i = start; i <= end; i++) {
			if (j == tmp.size() / 2) {
				array.set(i, tmp.get(k));
				k++;
				continue;
			}
			if (k == tmp.size()) {
				array.set(i, tmp.get(j));
				j++;
				continue;
			}
			if (tmp.get(j) < tmp.get(k)) {
				array.set(i, tmp.get(j));
				j++;
			} else {
				array.set(i, tmp.get(k));
				k++;
			}
		}

	}

}
