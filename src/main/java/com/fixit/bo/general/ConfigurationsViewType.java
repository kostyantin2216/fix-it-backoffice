/**
 * 
 */
package com.fixit.bo.general;

import com.fixit.core.utils.Formatter;

/**
 * @author 		Kostyantin
 * @createdAt 	2018/01/15 21:15:57 GMT+2
 */
public enum ConfigurationsViewType {
	STORED_PROPERTIES,
	TRAFFIC_SOURCES;
	
	public String toDisplayName() {
		return Formatter.capitalize(name().replaceAll("_", " "));
	}
	
}
