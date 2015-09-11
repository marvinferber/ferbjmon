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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TmpFileLogger extends Logger implements LogInterface {

	private BufferedWriter fWriter = null;
	private String tmpFileName;

	TmpFileLogger(String threadid) {

		new StringBuilder();
		File tmpFile = null;
		try {
			tmpFile = File.createTempFile("myjavamon.log", threadid);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		tmpFileName = tmpFile.getAbsolutePath();
		try {
			this.fWriter = new BufferedWriter(new FileWriter(tmpFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void log(String s) throws IOException {
		// builder.append(s);

		fWriter.write(System.nanoTime() + s);
	}

	public void finalizze() {
		System.err.println(this.toString() + " finalizze() called!");
		try {
			fWriter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader fReader = null;
		try {
			fReader = new BufferedReader(new FileReader(tmpFileName));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			FileWriter fstream = new FileWriter(globallog, true);
			BufferedWriter buf = new BufferedWriter(fstream);
			// buf.write(builder.toString());
			String line;
			while ((line = fReader.readLine()) != null)
				buf.write(line + "\n");
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
