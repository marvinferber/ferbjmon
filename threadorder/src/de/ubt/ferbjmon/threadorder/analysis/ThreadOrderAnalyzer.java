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

package de.ubt.ferbjmon.threadorder.analysis;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

public class ThreadOrderAnalyzer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String inputfilestr = null;
		String outputfolderstr = null;
		File inputfile;
		File outputfolder;

		try {
			inputfilestr = args[0];
			outputfolderstr = args[1];
		} catch (Exception e1) {
			System.out.println("Command line parameters are wrong or missing!");
			System.out.println("Usage: java -jar javamon2svg.jar <inputfile> <outputfolder>");
			e1.printStackTrace();
			System.exit(-1);
		}
		System.err.println("Analyzing output of thread order monitoring");
		System.err.println("Using inputfile: " + inputfilestr);
		System.err.println("Using outputfolder: " + outputfolderstr);
		inputfile = new File(inputfilestr);
		outputfolder = new File(outputfolderstr);

		Trace2SeqDiagram t2sd = new Trace2SeqDiagram();
		try {
			t2sd.readTrace(inputfile);
		} catch (IOException e) {
			System.err.println("ERROR: " + inputfile + " could not be opened for reading. "
					+ "This is likely due to a failed preceding ferbjmon monitoring module. "
					+ "Make sure the file path exists. (" + ThreadOrderAnalyzer.class.getName() + " : line "
					+ new Throwable().getStackTrace()[0].getLineNumber() + ")");
		}
		t2sd.createSVG();
		try {
			t2sd.writeSvgFile(outputfolder);
		} catch (IOException e) {
			System.err.println("ERROR: " + outputfolder + " could not be opened for writing. "
					+ "Make sure the file path exists. (" + ThreadOrderAnalyzer.class.getName() + " : line "
					+ new Throwable().getStackTrace()[0].getLineNumber() + ")");
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// // Display the document.
		// JSVGCanvas canvas = new JSVGCanvas();
		// JFrame f = new JFrame();
		// f.getContentPane().add(canvas);
		// canvas.setDocument(t2sd.getSVGDocument());
		// canvas.setDocumentState(canvas.ALWAYS_DYNAMIC);
		// f.pack();
		// f.setVisible(true);

	}

}
