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

import java.lang.instrument.ClassFileTransformer;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;

public class ClassLoderTransformer implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className, Class clazz,
			java.security.ProtectionDomain domain, byte[] bytes) {
		// System.out.println("Transformiere" + className);
		return enhanceClass(className, bytes);

	}

	private byte[] enhanceClass(String name, byte[] b) {
		ClassPool pool = ClassPool.getDefault();
		CtClass clazz = null;
		// System.out.println("Found: "+clazz+" "+pool);

		try {
			clazz = pool.makeClass(new java.io.ByteArrayInputStream(b));
			// System.out.println("Found: " + clazz);
			if (!clazz.isInterface()) {
				for (Object o : clazz.getAnnotations()) {
					// falls die Klasse als eine @Monitored Annotation hat
					if (o.toString().contains(
							"de.ubt.ferbjmon.annotation.Monitored"))
						processClass(clazz);
				}
				b = clazz.toBytecode();

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not instrument  " + name
					+ ",  exception : " + e.getMessage());
		} finally {
			if (clazz != null) {
				clazz.detach();
			}
		}
		return b;
	}

	private void processClass(CtClass clazz) throws NotFoundException,
			CannotCompileException, BadBytecode {
		System.err.println("Class " + clazz.getName()
				+ " will be instrumented and monitored!");
		CtMethod[] methods = clazz.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if (!methods[i].isEmpty()) {
				processMethod(methods[i], clazz.getName());
			}
		}
		CtConstructor[] constr = clazz.getDeclaredConstructors();
		for (int i = 0; i < constr.length; i++) {
			if (!constr[i].isEmpty()) {
				processConstructor(constr[i], clazz.getName());
			}
		}
	}

	private void processConstructor(CtConstructor ctConstructor, String name)
			throws CannotCompileException {
		ctConstructor.insertBeforeBody("Thread.currentThread().logi("
				+ "\" EnterConstructor: " + name + "_\" +  hashCode() + \""
				+ " " + ctConstructor.getName() + " " + "\");");
		ctConstructor.insertAfter("Thread.currentThread().logi("
				+ "\" ExitConstructor: " + name + "_\" +  hashCode() + \""
				+ " " + ctConstructor.getName() + " " + "\");");

	}

	private void processMethod(CtBehavior method, String className)
			throws NotFoundException, CannotCompileException, BadBytecode {
		if (Modifier.isStatic(method.getModifiers())) {
			// System.out.println("Achtung statische Methode!");
			method.insertBefore("Thread.currentThread().logi("
					+ "\" EnterMethod: " + className + " " + method.getName()
					+ " " + "\");");
			method.insertAfter("Thread.currentThread().logi("
					+ "\" ExitMethod: " + className + " " + method.getName()
					+ " " + "\");");
		} else {
			method.insertBefore("Thread.currentThread().logi("
					+ "\" EnterMethod: " + className + "_\" +  hashCode() + \""
					+ " " + method.getName() + " " + "\");");
			method.insertAfter("Thread.currentThread().logi("
					+ "\" ExitMethod: " + className + "_\" +  hashCode() + \""
					+ " " + method.getName() + " " + "\");");
		}
	}
}
