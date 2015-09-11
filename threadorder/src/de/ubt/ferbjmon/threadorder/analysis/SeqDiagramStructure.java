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

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class SeqDiagramStructure {
	Map<String, ObjectEntity> entitiesMap = new HashMap<String, ObjectEntity>();

	Map<String, Document> diagramMap = new HashMap<String, Document>();

	Long minTimeStamp = null;

	Long maxTimeStamp = null;

	private boolean hasEntity(String entity) {
		return entitiesMap.containsKey(entity);
	}

	public void appendDataset(String objectId, String entityId, Long timestamp,
			short eventType, String targetName) {
		// Neue Objektentität erstellen, falls noch nicht vorhanden
		if (!hasEntity(objectId))
			entitiesMap.put(objectId, new ObjectEntity(objectId));
		// Datensatz zur Objektentität hinzufügen
		entitiesMap.get(objectId).appendDataset(entityId, timestamp, eventType,
				targetName);
		if (minTimeStamp == null)
			minTimeStamp = timestamp;
		maxTimeStamp = timestamp;
	}

	public String[] getObjectEntities() {
		return (String[]) entitiesMap.keySet().toArray(
				new String[entitiesMap.keySet().size()]);
	}

	public ObjectEntity getObjectEntity(String key) {
		return entitiesMap.get(key);
	}

	public void populateSVGDiagramDocument(String uuid, Document doc) {
		diagramMap.put(uuid, doc);
	}

	public Node getObjectDiagramDocument(String objectEntity) {
		return diagramMap.get(objectEntity);
	}

	public Long getNullTimeStamp() {
		return minTimeStamp;
	}

	public Long getMaxTimeStamp() {
		return maxTimeStamp;
	}

	public String[] getDiagrams() {
		return diagramMap.keySet().toArray(
				new String[diagramMap.keySet().size()]);
	}
}
