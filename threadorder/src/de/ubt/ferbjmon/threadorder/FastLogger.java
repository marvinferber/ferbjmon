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

package de.ubt.ferbjmon.threadorder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FastLogger extends Logger implements LogInterface {

	final static int MAX_ENTRIES = 10000;

	private String[] logArray = null;

	private int index;

	public FastLogger() {
		logArray = new String[MAX_ENTRIES];
		index = 0;
	}

	public FastLogger(int parseInt) {
		logArray = new String[parseInt];
		index = 0;
	}

	@Override
	public void log(String s) throws IOException {
		if (index == logArray.length) {
			System.err.println("Log Capacity of exceeded..Logging stopped!");
		} else {
			logArray[index] =System.nanoTime()+ s;
			index++;
		}
	}

	@Override
	public void finalizze() {
		System.out.println(this.toString() + " finalizze() aufgerufen!");

		try {
			FileWriter fstream = new FileWriter(globallog, true);
			BufferedWriter buf = new BufferedWriter(fstream);
			for (int i = 0; i < index; i++)
				buf.write(logArray[i]);

			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
