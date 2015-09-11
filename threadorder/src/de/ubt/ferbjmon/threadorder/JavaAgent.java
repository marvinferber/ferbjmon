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

import java.io.IOException;
import java.lang.instrument.Instrumentation;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class JavaAgent {
	
	public static void premain(String agentArgs, Instrumentation inst) {
		//System.err.println(System.getProperty("java.class.path"));
		//System.err.println(System.getProperty("sun.boot.class.path"));
		System.err.println("Using logfile: " + agentArgs);
		try {
			Logger.setLogfile(agentArgs);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		System.err.println("ShutdownHook added!");
		ClassPool pool = ClassPool.getDefault();
		try {
			CtClass clazz = pool.get("java.lang.Thread");
			clazz.getDeclaredMethod("logi");
		} catch (NotFoundException e) {
			modifyThreadClass(pool);
		}

		inst.addTransformer(new ClassLoderTransformer());
	}

	private static void modifyThreadClass(ClassPool pool) {
		System.err.println("Modifying Thread class!");
		CtClass clazz;
		try {
			clazz = pool.get("java.lang.Thread");
			// Append the Logger Field
			// java.lang.StringBuilder sbLogger = new java.lang.StringBuilder();
			//
			CtClass loggerclass = pool
					.get("de.ubt.ferbjmon.threadorder.LogInterface");
			CtField logger = new CtField(loggerclass, "sbLogger", clazz);
			// clazz.addField(logger, "new java.lang.StringBuilder()");
			clazz.addField(logger);
			// Append a boolean to signal if the Thread is regsitered for
			// collecting the monitored Data
			// java.lang.Boolean registered = new java.lang.Boolean();
			CtClass registeredclass = pool.get("java.lang.Boolean");
			CtField registered = new CtField(registeredclass, "registered",
					clazz);
			clazz.addField(registered, "new java.lang.Boolean(false)");
			// Append a boolean to signal if the Thread is regsitered for
			// collecting the monitored Data
			// java.lang.Boolean registered = new java.lang.Boolean();
			// Append the method logi()
			//	
			CtMethod m = CtNewMethod
					.make(
							"public void logi(java.lang.String logstring){"
									// +
									// "System.out.println(Thread.currentThread().hashCode()+logstring);"
									+ "if(! $0.registered.booleanValue()){"
									+ "$0.sbLogger=de.ubt.ferbjmon.threadorder.ThreadInit.getLogger($0);"
									+ "$0.registered=new java.lang.Boolean(true);"
									+ "}"
									+ "$0.sbLogger.log(logstring + $0.getName() + \"\\n\");"
									+ "}", clazz);
			clazz.addMethod(m);
			// Append a method getlogi that returns the Logger used after
			// execution
			CtMethod m1 = CtNewMethod.make(
					"public de.ubt.ferbjmon.threadorder.LogInterface getlogi(){"
							+ "return $0.sbLogger;" + "}", clazz);
			clazz.addMethod(m1);
			System.err.println("java.lang.Thread modified");
			// Write the newly generated Class file to disk
			// System classes cannot be modfied at runtime
			// Load it the time by appending -Xbootclasspath/p:.
			clazz.writeFile(".");
		} catch (NotFoundException e1) {
			System.out
					.println("Faild to modify Thread class! NotFoundException");
			e1.printStackTrace();
		} catch (CannotCompileException e) {
			System.out
					.println("Faild to modify Thread class! CannotCompileException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Faild to modify Thread class! IOException");
			e.printStackTrace();
		}
	}

}
