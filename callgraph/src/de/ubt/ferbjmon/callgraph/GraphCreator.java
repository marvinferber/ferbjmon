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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class GraphCreator {

	
	private static ThreadLocal<Stack<String>> stack = new ThreadLocal<Stack<String>>();
	
	private static final String START = "_START_";
	
	private static String startCaptureAt = Properties.getStartCaptureAt();

	private static ThreadLocal<Boolean> captureStarted = new ThreadLocal<Boolean>();

	private static long threadIdGenerator = 1;
	private static ThreadLocal<Long> threadIdentifier = new ThreadLocal<Long>();

	private static ThreadLocal<Set<String>> alreadyvisited = new ThreadLocal<Set<String>>();

	public static void pushNode(String targetClass, String methodeName)
			throws IOException {
		// Evtl haben wir die markierte Klasse bereits geladen, dann hier
		// setzen!
		startCaptureAt = putinTicks(Properties.getStartCaptureAt());

		// Falls wir die Methode zum ersten mal aufrufen, die ThreadLocals
		// initialisieren
		if ((captureStarted.get() == null || !captureStarted.get()
				.booleanValue()))
			initThisThread();

		// Source und target Klasse in Hochkomma setzen
		String sourceClass = putinTicks(stack.get().peek());
		targetClass = putinTicks(targetClass);
		// Falls Source und Target identisch sind (Aufruf auf sich selbst) fügen
		// wir der Grafik nichts hinzu
		if (sourceClass.equals(targetClass)) {
			// stack.get().push(targetClass);
		} else {
			// Falls diese Klasse unsere Monitored Class ist
			if (targetClass.equals(startCaptureAt))
				alreadyvisited.get().add(targetClass + " [color=red]\n");

			// Falls statische Objetkte initialisiert werden
			// Auch wenn die Klasse initialisiert wird fügen wir die
			// Abhängigkeit
			// hinzu
			if (methodeName.equals("<clinit>"))
				pushParam(sourceClass, targetClass);
			else {
				StringBuffer buffer = new StringBuffer(sourceClass)
						.append("->").append(targetClass).append("[label=\"")
						.append(methodeName).append("()\"");
				if (targetClass.equals(startCaptureAt))
					buffer.append(",color=red");
				buffer.append("]\n");

				alreadyvisited.get().add(buffer.toString());
			}
		}
		stack.get().push(targetClass);
	}

	private static String putinTicks(String peek) {
		if (peek.startsWith("\""))
			return peek;
		else
			return "\"" + peek + "\"";
	}

	private static void initThisThread() throws IOException {

		if (threadIdentifier.get() == null) {
			threadIdentifier.set(threadIdGenerator++);
		}
		stack.set(new Stack<String>());
		captureStarted.set(Boolean.TRUE);
		alreadyvisited.set(new HashSet<String>());

		LogCollector.getInstance().registerLog(alreadyvisited.get());
		stack.get().push(START + threadIdentifier.get());
	}

	public static void popNode() {
		stack.get().pop();

	}

	public static void pushParam(String sourceClass, String targetClass)
			throws IOException {
		// Sourceclass und targetclass in Hochkomma setzen
		sourceClass = putinTicks(sourceClass);
		targetClass = putinTicks(targetClass);
		// Threadlocals initialisieren bei erstem Aufruf!
		if ((captureStarted.get() == null || !captureStarted.get()
				.booleanValue()))
			initThisThread();
		// Falls Source und Target identisch sind (Aufruf auf sich selbst) fügen
		// wir der Grafik nichts hinzu
		if (sourceClass.equals(targetClass)) {
			// Hier tun wir einfach nichts
		} else {
			StringBuffer buffer = new StringBuffer(sourceClass).append("->")
					.append(targetClass).append("[label=\"").append("depends")
					.append("\", style=dashed]\n");

			alreadyvisited.get().add(buffer.toString());
		}
	}
}
