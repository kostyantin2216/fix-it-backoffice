/**
 * 
 */
package com.fixit.bo.maps.model;

import java.util.List;
import java.util.Optional;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;

import com.fixit.core.data.mongo.MapArea;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/28 21:10:46 GMT+2
 */
public interface MapModelWrapper extends PolygonManipulator {

	String getMapElementId();
	LatLng getCenter();
	int getZoomLevel();
	
	MapModel getModel();
	List<MapArea> getMapAreas();
	
	Optional<MapModelWrapper> getParentWrapper();
	
	void onStateChange(StateChangeEvent event);
	
}
