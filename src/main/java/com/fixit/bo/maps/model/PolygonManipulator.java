/**
 * 
 */
package com.fixit.bo.maps.model;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.Polygon;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/28 21:28:34 GMT+2
 */
public interface PolygonManipulator {

	void hide(Polygon polygon);
	void show(Polygon polygon);
	void select(Polygon polygon);
	void unselect(Polygon polygon);
	
	public void onPolygonSelect(OverlaySelectEvent event);

}
