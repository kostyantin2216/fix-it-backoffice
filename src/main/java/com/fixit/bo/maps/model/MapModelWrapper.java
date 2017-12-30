/**
 * 
 */
package com.fixit.bo.maps.model;

import java.util.Optional;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/28 21:10:46 GMT+2
 */
public interface MapModelWrapper extends PolygonManipulator {

	LatLng getCenter();
	int getZoomLevel();
	
	MapModel getModel();
	
	Optional<MapModelWrapper> getParentWrapper();
	
	void onStateChange(StateChangeEvent event);
	
}
