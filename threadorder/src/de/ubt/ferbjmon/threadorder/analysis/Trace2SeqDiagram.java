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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class Trace2SeqDiagram {
	private SeqDiagramStructure dStruct = new SeqDiagramStructure();

	public void readTrace(File traceFile) throws IOException {

		BufferedReader buf = new BufferedReader(new FileReader(traceFile));
		String line;
		List<MonitoredEvent> s = new ArrayList<MonitoredEvent>();
		while ((line = buf.readLine()) != null) {
			String[] chunks = line.split(" ");
			try {
				MonitoredEvent item = new MonitoredEvent(chunks[0], chunks[1],
						chunks[2], chunks[3], chunks[4]);
				s.add(item);
			} catch (NoSuchEvenType e) {
				e.printStackTrace();
			}
		}
		buf.close();
		// Hier werden die einzelnen Einträge nach der Zeit sortiert
		// Die Reihenfolge könnte beim Schreiben in die Datei
		// durcheinandergekommen sein
		Collections.sort(s);
		// Nun werden die Einträge in die Datenstruktur eingefüllt und dabei
		// gleich richtig zusammensortiert

		for (MonitoredEvent evt : s) {
			dStruct.appendDataset(evt.getObjectId(), evt.getEntityId(),
					evt.getTimestamp(), evt.getEventType(), evt.getTargetName());
		}
	}

	public void createSVG() {

		SeqDiagram2SVGRenderer renderer = new SeqDiagram2SVGRenderer(dStruct);
		dStruct.populateSVGDiagramDocument("threadorder",
				renderer.makeSequenceDiagram(dStruct.getObjectEntities()));

	}

	public void writeSvgFile(File svgFile) throws IOException,
			TransformerFactoryConfigurationError, TransformerException {
		if (!svgFile.isDirectory())
			throw new FileNotFoundException(svgFile.getPath()
					+ "was not found!");
		for (String objectEntity : dStruct.getDiagrams()) {
			File f = new File(svgFile.getAbsolutePath() + File.separator
					+ objectEntity + ".svg");
			f.createNewFile();
			System.out.println("Writing outputfile: " + f.getAbsolutePath());
			FileOutputStream fstream = new FileOutputStream(f);

			DOMSource src = new DOMSource(
					dStruct.getObjectDiagramDocument(objectEntity));
			Result res = new StreamResult(fstream);
			// Write the DOM document to the file
			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.transform(src, res);
			fstream.close();
		}
	}

	public Document getSVGDocument() {
		return (Document) dStruct.getObjectDiagramDocument("threadorder");
	}

}
