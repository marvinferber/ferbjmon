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

public class StringBuilderLogger extends Logger implements LogInterface {

	private StringBuilder builder = null;

	StringBuilderLogger() {

		this.builder = new StringBuilder();

	}

	public void log(String s) throws IOException {
		builder.append(System.nanoTime()+s);

	}

	public void finalizze() {
		System.out.println(this.toString() + " finalizze() aufgerufen!");

		try {
			FileWriter fstream = new FileWriter(globallog, true);
			BufferedWriter buf = new BufferedWriter(fstream);
			buf.write(builder.toString());

			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
