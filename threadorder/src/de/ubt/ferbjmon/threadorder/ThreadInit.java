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

public class ThreadInit {

	public static LogInterface getLogger(Thread t) {
		// return an Instance of the preferred Logger
		String logger = System.getProperty("myjavamon.logger");
		LogInterface logi = null;
		if (logger != null) {
			if (logger.equalsIgnoreCase("FastLogger")) {
				String flogsize = System.getProperty("myjavamon.logger.size");
				if (flogsize != null)
					logi = new FastLogger(Integer.parseInt(flogsize));
				else
					logi = new FastLogger();
			}
			if (logger.equalsIgnoreCase("StringBuilderLogger"))
				logi = new StringBuilderLogger();
			if (logger.equalsIgnoreCase("TmpFileLogger"))
				logi = new TmpFileLogger(new Long(t.getId()).toString());
		}
		//Falls kein oder kein gültiger Logger angegeben wurde benutze default-logger
		if (logi == null) {
			logi = new TmpFileLogger(new Long(t.getId()).toString());
		}
		// Add Thread ID to Shutdown Hook
		ShutdownHook.register(t);

		return logi;
	}

}
