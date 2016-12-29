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

package de.ubt.ferbjmon.callgraph;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;

public class JavaAgent implements ClassFileTransformer {

	public static void premain(String agentArgument, Instrumentation instrumentation) {
		System.err.println("Using logfile: " + agentArgument);
		try {
			LogCollector.getInstance().setLogfile(agentArgument);
		} catch (IOException e) {
			// e.printStackTrace();
			System.err.println("ERROR: " + agentArgument
					+ " could not be opened for writing output! Please make sure the file path exists. "
					+ "Ferbjmon will not create any folder or file path for you. " + "Only files will be created.");
			System.exit(-1);
		}
		Runtime.getRuntime().addShutdownHook(new de.ubt.ferbjmon.callgraph.ShutdownHook());
		instrumentation.addTransformer(new JavaAgent());
	}

	public byte[] transform(ClassLoader loader, String className, @SuppressWarnings("rawtypes") Class clazz,
			java.security.ProtectionDomain domain, byte[] bytes) {

		boolean enhanceClass = !isNoTarget(className);

		if (enhanceClass) {

			return enhanceClass(className, bytes);
		} else {
			return bytes;
		}
	}

	private boolean isNoTarget(String className) {
		if (className.startsWith("java/"))
			return true;
		if (className.startsWith("javax/"))
			return true;
		if (className.startsWith("sun/"))
			return true;
		if (className.startsWith("com/sun/"))
			return true;
		if (className.startsWith("de/ubt/ferbjmon/"))
			return true;
		if (className.startsWith("javassist/"))
			return true;
		if (className.startsWith("java."))
			return true;
		if (className.startsWith("javax."))
			return true;
		if (className.startsWith("sun."))
			return true;
		if (className.startsWith("com.sun."))
			return true;
		if (className.startsWith("de.edu.ferbjmon."))
			return true;
		if (className.startsWith("javassist."))
			return true;
		return false;
	}

	private byte[] enhanceClass(String name, byte[] b) {
		// System.out.println(name);
		ClassPool pool = ClassPool.getDefault();
		CtClass clazz = null;
		try {
			clazz = pool.makeClass(new java.io.ByteArrayInputStream(b));
			if (!clazz.isInterface()) {
				for (Object o : clazz.getAnnotations()) {
					// falls die Klasse als eine @Monitored Annotation hat
					if (o.toString().contains("de.ubt.ferbjmon.annotation.Monitored"))
						Properties.setStartClass(clazz.getName());
				}
				// Versuch:Superklasse auch instrumentieren
				// Ist nicht nötig, da custom Klassen sowieso auch
				// instrumentiert werden und sich Systemklassen nicht
				// instrumentuieren lassen.

				// Falls die Klasse keine Konstruktoren hat, fügen wir einen
				// Initializer hinzu
				if (clazz.getClassInitializer() == null) {
					// System.err.println(name+" has no initializer");
					CtConstructor con = clazz.makeClassInitializer();
					enhanceMethod(con, clazz.getName());
					// clazz.addConstructor(con);
				}

				// Behaviors umfasst auch den Constructor mit
				CtBehavior[] methods = clazz.getDeclaredBehaviors();
				for (int i = 0; i < methods.length; i++) {
					if (!methods[i].isEmpty()) {
						enhanceMethod(methods[i], clazz.getName());
					}
				}

				b = clazz.toBytecode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not instrument  " + name + ",  exception : " + e.getMessage());
		} finally {
			if (clazz != null) {
				clazz.detach();
			}
		}
		return b;
	}

	private void enhanceMethod(CtBehavior method, String className) throws NotFoundException, CannotCompileException {
		// System.err.println(method+className);

		// Parameter auch pushen da es Abhängigkeiten sind
		StringBuilder parbuilder = new StringBuilder();
		for (CtClass parClass : method.getParameterTypes())
			if (!isNoTarget(parClass.getName()) && (!parClass.isPrimitive()))
				parbuilder.append("de.ubt.ferbjmon.callgraph.GraphCreator.pushParam(\"" + className + "\",\""
						+ parClass.getName() + "\");");
		// Natürlich auch den Rückgabewert
		if (method instanceof CtMethod) {
			CtMethod realmethod = (CtMethod) method;
			if (!isNoTarget(realmethod.getReturnType().getName()) && (!realmethod.getReturnType().isPrimitive())
					&& !isprimitiveArray(realmethod.getReturnType()))

				parbuilder.append("de.ubt.ferbjmon.callgraph.GraphCreator.pushParam(\"" + className + "\",\""
						+ realmethod.getReturnType().getName() + "\");");
		} else {
			// Falls wir einen default-Konstruktor haben
		}
		// Klassenabhängigkeiten einfügen
		CtClass superc = method.getDeclaringClass().getSuperclass();
		if (superc != null && !isNoTarget(superc.getName()))
			parbuilder.append("de.ubt.ferbjmon.callgraph.GraphCreator.pushParam(\"" + className + "\",\""
					+ superc.getName() + "\");");

		// Pushnode popnode einfügen
		method.insertBefore("de.ubt.ferbjmon.callgraph.GraphCreator.pushNode(\"" + className + "\",\""
				+ method.getName() + "\");" + parbuilder.toString());
		method.insertAfter("de.ubt.ferbjmon.callgraph.GraphCreator.popNode();");

	}

	private boolean isprimitiveArray(CtClass returnType) {
		try {
			if (returnType.isArray() && (returnType.getComponentType().isPrimitive()
					|| isNoTarget(returnType.getComponentType().getName())))
				return true;
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
