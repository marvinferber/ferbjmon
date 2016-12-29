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

package de.ubt.ferbjmon.callgraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LogCollector {

	private static LogCollector log = null;

	private Set<String> globalSet = null;
	private List<Set<String>> globalSetList = null;
	private BufferedWriter dotFile = null;

	private LogCollector() {
		globalSet = new HashSet<String>();
		globalSetList = new ArrayList<Set<String>>();
	}

	public static LogCollector getInstance() {
		if (log == null)
			log = new LogCollector();
		return log;

	}

	public void writeLog() throws IOException {
		// Cat all logs together
		for (Set<String> logentries : globalSetList)
			globalSet.addAll(logentries);
		// Write all logs to a file

		// replace .
		dotFile.write("digraph " + this.hashCode() + "{\n");
		for (String s : globalSet)
			dotFile.write(s);
		dotFile.write("}");
		dotFile.close();
	}

	public synchronized void registerLog(Set<String> set) {
		globalSetList.add(set);
	}

	public void setLogfile(String agentArgument) throws IOException {
		dotFile = new BufferedWriter(new FileWriter(agentArgument));

	}

}
