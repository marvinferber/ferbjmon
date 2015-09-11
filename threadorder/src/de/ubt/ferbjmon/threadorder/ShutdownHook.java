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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ShutdownHook extends Thread {

	static List<Thread> threadList = new ArrayList<Thread>();

	public void run() {
		System.err.println(threadList.size() + " Threads have been monitored!");
		for (Thread t : threadList) {
			// for (Method m : t.getClass().getMethods())
			// System.out.println(m.getName());
			Class<?> clazzt = t.getClass();
			try {
				Method method = null;
				for (Method m : clazzt.getMethods())
					if (m.getName().contains("getlogi"))
						method = m;

				// Object o = m.invoke(t, null);

				LogInterface log = (LogInterface) method.invoke(t, null);
				log.finalizze();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void register(Thread t) {
		threadList.add(t);
	}
}