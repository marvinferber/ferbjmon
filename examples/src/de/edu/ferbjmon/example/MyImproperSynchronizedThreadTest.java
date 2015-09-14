/**
 * Copyright 2015 Marvin Ferber
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

package de.edu.ferbjmon.example;

import de.edu.ferbjmon.example.improper.MyEvenThread;
import de.edu.ferbjmon.example.improper.MyOddThread;
import de.edu.ferbjmon.example.structure.MyStructure;


public class MyImproperSynchronizedThreadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int threshold = 10;

		int threads = 1;

		MyOddThread[] oddArray = new MyOddThread[threads];
		MyEvenThread[] evenArray = new MyEvenThread[threads];

		MyStructure my = new MyStructure();
		for (int i = 0; i < threads; i++) {
			oddArray[i] = new MyOddThread(my, threshold,i);
			evenArray[i] = new MyEvenThread(my, threshold,i);
		}

		Long start = System.currentTimeMillis();

		for (int i = 0; i < threads; i++) {
			oddArray[i].start();
			evenArray[i].start();
		}

		for (int i = 0; i < threads; i++) {
			try {
				oddArray[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				evenArray[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Long end = System.currentTimeMillis();
		System.out.println("Time elapsed: " + (end - start) + "ms");
	}
}
