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

public class TimeEvent {
	private Long timestamp;
	private short eventType;
	private String targetName;

	public TimeEvent(Long timestamp, short eventType, String targetName) {
		this.timestamp = timestamp;
		this.eventType = eventType;
		this.targetName = targetName;
	}

	/**
	 * @return the timestamp
	 */
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the eventType
	 */
	public short getEventType() {
		return eventType;
	}

	/**
	 * @return the targetName
	 */
	public String getTargetName() {
		return targetName;
	}
}