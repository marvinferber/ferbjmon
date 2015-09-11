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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectEntity {

	private HashMap<String, List<TimeEvent>> entityMap = new HashMap<String, List<TimeEvent>>();

	public ObjectEntity(String uuid) {
		this.uuid = uuid;
	}

	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void appendDataset(String entityId, Long timestamp, short eventType,
			String targetName) {
		// Zu jeder Entität/Thread wird die Aufrufreihenfolge gespeichert
		TimeEvent t = new TimeEvent(timestamp, eventType, targetName);
		// Falls die entityId noch nicht existiert -> erstellen
		if (!entityMap.containsKey(entityId))
			entityMap.put(entityId, new ArrayList<TimeEvent>());
		// Datensatz einfügen
		entityMap.get(entityId).add(t);
	}

	public String[] getThreadEntities() {
		return entityMap.keySet()
				.toArray(new String[entityMap.keySet().size()]);
	}

	public TimeEvent[] getTimeEvents(String entity) {
		return entityMap.get(entity).toArray(
				new TimeEvent[entityMap.get(entity).size()]);
	}

}
