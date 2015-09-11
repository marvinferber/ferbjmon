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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class SeqDiagram2SVGRenderer {
	
	final float fontsize = (float) 10.0;
	final int scalefactor=1000;

	class EventTupel {
		public String targetName;
		public String threadEntity;
		Long start;
		Long end;
		Color col;
	}

	class Coord {
		public Coord(int startx, int starty) {
			this.x = startx;
			this.y = starty;
		}

		int x;
		int y;
	}

	public SeqDiagram2SVGRenderer(SeqDiagramStructure dStruct) {
		this.dStruct = dStruct;
	}

	private SeqDiagramStructure dStruct;

	public Document makeSequenceDiagram(String[] strings) {
		// Create an SVG document.
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);
		// Create a converter for this document.
		SVGGraphics2D g = new SVGGraphics2D(doc);

		int startx = 10;
		int starty = 10;
		//int scalefactor = 10000;
		for (String objectEntity : strings) {
			Font old = g.getFont();
			Font neu = old.deriveFont(Font.BOLD);
			g.setFont(neu);
			g.drawString(objectEntity, startx, starty);
			g.setFont(old);
			startx = paintSequenceDiagram(
					dStruct.getObjectEntity(objectEntity), g, startx,
					starty + 20, scalefactor) + 100;
		}
		// paintSequenceDiagramWithErrors(objectEntity, g);
		g.setSVGCanvasSize(new Dimension(startx, scalefactor + 100));

		// Populate the document root with the generated SVG content.
		Element root = doc.getDocumentElement();
		g.getRoot(root);

		return doc;
	}

	private int paintSequenceDiagram(ObjectEntity objectEntity,
			SVGGraphics2D svg2d, int startx, int starty, int scalefactor) {
		svg2d.setBackground(Color.white);
		svg2d.setPaint(Color.black);
		Map<String, Integer> threadEntities = new HashMap<String, Integer>();
		// Eventliste erstellen um später Zugriffsungereimtheiten zu finden
		List<EventTupel> eventList = new ArrayList<EventTupel>();
		for (String threadEntity : objectEntity.getThreadEntities()) {
			Stack<TimeEvent> teStack = new Stack<TimeEvent>();
			for (TimeEvent te : objectEntity.getTimeEvents(threadEntity)) {
				if (te.getEventType() == MonitoredEvent.ENTER_CONSTRUCTOR
						|| te.getEventType() == MonitoredEvent.ENTER_METHOD) {
					teStack.push(te);
				} else {
					EventTupel evt = new EventTupel();
					TimeEvent last = teStack.pop();
					evt.start = last.getTimestamp();
					evt.end = te.getTimestamp();
					evt.targetName = last.getTargetName();
					evt.threadEntity = threadEntity;
					evt.col=Color.lightGray;
					eventList.add(evt);
				}
			}
		}
		// Fehler finden
		findErrors(eventList);
		// Diagramm malen
		for (String threadEntity : objectEntity.getThreadEntities()) {
			int maxversatz = 0;
			int versatz = 0;
			int actual_y = starty * 2;
			threadEntities.put(threadEntity, startx);
			svg2d.drawString(threadEntity, startx, starty);
			int tmp = actual_y;
			// short lastevent = MonitoredEvent.EXIT_CONSTRUCTOR;
			Stack<TimeEvent> teStack = new Stack<TimeEvent>();
			for (TimeEvent te : objectEntity.getTimeEvents(threadEntity)) {
				// Falls es ein Enter-Event ist, auf den Stack legen
				if (te.getEventType() == MonitoredEvent.ENTER_CONSTRUCTOR
						|| te.getEventType() == MonitoredEvent.ENTER_METHOD) {
					// Falls der Stack leer war eine Linie malen
					if (teStack.isEmpty()) {
						actual_y = starty
								* 2
								+ between(scalefactor,
										dStruct.getNullTimeStamp(),
										dStruct.getMaxTimeStamp(),
										te.getTimestamp());
						svg2d.drawLine(startx + 10, tmp, startx + 10, actual_y);
					}
					// Auf den Stack damit
					teStack.push(te);
					versatz = versatz + 10;
					if (maxversatz < teStack.size() * 10)
						maxversatz = teStack.size() * 10;
				}
				// Falles ein Exit-Event ist, vom Stack nehmen und Kasten malen
				else {
					TimeEvent lastEvent = teStack.pop();
					versatz = versatz - 10;
					// Nun den Kasten malen
					actual_y = starty
							* 2
							+ between(scalefactor, dStruct.getNullTimeStamp(),
									dStruct.getMaxTimeStamp(),
									lastEvent.getTimestamp());
					tmp = starty
							* 2
							+ between(scalefactor, dStruct.getNullTimeStamp(),
									dStruct.getMaxTimeStamp(),
									te.getTimestamp());
					svg2d.setPaint(Color.black);
					Font old = svg2d.getFont();
					Font neu = old.deriveFont(fontsize);
					svg2d.setFont(neu);
					svg2d.drawString(lastEvent.getTargetName(), startx + 21
							+ versatz, actual_y + 1);
					svg2d.setFont(old);
					svg2d.drawRect(startx + versatz, actual_y, 20, tmp
							- actual_y);
					if (haserror(eventList, lastEvent.getTimestamp(),
							te.getTimestamp()))
						svg2d.setPaint(Color.red);
					else
						svg2d.setPaint(Color.lightGray);
					svg2d.fillRect(startx + versatz, actual_y, 20, tmp
							- actual_y);
					svg2d.setPaint(Color.black);

				}

			}
			startx = startx + maxversatz + 100;
		}
		return startx;
	}

	private boolean haserror(List<EventTupel> eventList, Long timestamp,
			Long timestamp2) {
		for (EventTupel evt : eventList) {
			if (evt.start == timestamp && evt.end == timestamp2) {
				if (evt.col.equals(Color.red))
					return true;
				else
					return false;
			}
		}
		return true;
	}

	private void findErrors(List<EventTupel> eventList) {
		for (int i = 0; i < eventList.size(); i++) {
			for (int j = 0; j < eventList.size(); j++) {
				if (!(i == j)) {
					EventTupel evt = eventList.get(i);
					EventTupel match = eventList.get(j);
					// die Methoden müssen die gleichen sein
					if (evt.targetName.equals(match.targetName)
					// start von evt liegt zw. start und end von match?
							&& (((evt.start >= match.start) && (evt.start <= match.end))
							// oder end von evt liegt zw start und end von match
							|| ((evt.end >= match.start) && (evt.end <= match.end)))) {
						evt.col = Color.red;
						match.col = Color.red;
					}
				}
			}
		}
	}

	private int between(int newMax, Long nullTimeStamp, Long maxTimeStamp,
			Long timestamp) {
		// maps the timestamp as time from between Null and Max to 0 and newMax
		Float normTS = new Float(timestamp - nullTimeStamp);
		Float normTime = new Float(maxTimeStamp - nullTimeStamp);
		Float div = normTS / normTime;
		Float max = new Float(newMax);
		return Math.round(div * max);
	}

}
