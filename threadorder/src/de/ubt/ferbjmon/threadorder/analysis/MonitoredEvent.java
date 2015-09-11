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

public class MonitoredEvent implements Comparable<MonitoredEvent> {

	public final static short ENTER_CONSTRUCTOR = 1;
	public final static short EXIT_CONSTRUCTOR = 2;
	public final static short ENTER_METHOD = 3;
	public final static short EXIT_METHOD = 4;

	private String objectId;// the object
	private String entityId;// the caller
	private Long timestamp;// access time
	private short eventType;//
	private String targetName;// name of the method

	public MonitoredEvent(String string, String string2, String string3,
			String string4, String string5) throws NoSuchEvenType {
		// timestamp
		this.timestamp = Long.parseLong(string);
		// eventtype
		if (string2.equalsIgnoreCase("EnterMethod:"))
			this.eventType = ENTER_METHOD;
		else if (string2.equalsIgnoreCase("ExitMethod:"))
			this.eventType = EXIT_METHOD;
		else if (string2.equalsIgnoreCase("EnterConstructor:"))
			this.eventType = ENTER_CONSTRUCTOR;
		else if (string2.equalsIgnoreCase("ExitConstructor:"))
			this.eventType = EXIT_CONSTRUCTOR;
		else
			throw new NoSuchEvenType(string2);
		// objectId
		this.objectId = string3;
		// the method
		this.targetName = string4;
		// the caller
		this.entityId = string5;

	}

	public String getObjectId() {
		return objectId;
	}

	public String getEntityId() {
		return entityId;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public short getEventType() {
		return eventType;
	}

	public String getTargetName() {
		return targetName;
	}

	@Override
	public int compareTo(MonitoredEvent arg0) {
		if (timestamp < arg0.getTimestamp())
			return -1;
		else if (timestamp == arg0.getTimestamp())
			return 0;
		else
			return 1;
	}

}
