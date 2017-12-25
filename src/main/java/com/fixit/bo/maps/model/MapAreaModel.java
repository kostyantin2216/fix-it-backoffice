/**
 * 
 */
package com.fixit.bo.maps.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.primefaces.model.map.Circle;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;
import org.primefaces.model.map.Polyline;
import org.primefaces.model.map.Rectangle;

import com.fixit.core.logging.FILog;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/17 17:09:26 GMT+2
 */
public class MapAreaModel implements MapModel {

	private List<Polygon> polygons;
	
	public MapAreaModel() {
		polygons = new ArrayList<>();
	}

	@Override
	public void addOverlay(Overlay overlay) {
		if(overlay instanceof Polygon) {
			polygons.add((Polygon) overlay);
		} else {
			FILog.w("MapAreaModel only supports overlays of type Polygon");
		}
	}

	@Override
	public List<Polygon> getPolygons() {
		return polygons;
	}

	@Override
	public Polygon findOverlay(String id) {
		for(Iterator<Polygon> iterator = polygons.iterator(); iterator.hasNext();) {
			Polygon overlay = iterator.next();
			
			if(overlay.getId().equals(id))
				return overlay;
		}
		return null;
	}
	
	@Override
	public List<Marker> getMarkers() {
		return Collections.emptyList();
	}

	@Override
	public List<Polyline> getPolylines() {
		return Collections.emptyList();
	}

	@Override
	public List<Circle> getCircles() {
		return Collections.emptyList();
	}

	@Override
	public List<Rectangle> getRectangles() {
		return Collections.emptyList();
	}

	
}
