/**
 * 
 */
package com.fixit.bo.beans;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fixit.bo.maps.filters.MapAreaFilterDataLoader;
import com.fixit.bo.maps.filters.MapModelFilterer.FilterType;
import com.fixit.bo.maps.model.BaseMapModelWrapper;
import com.fixit.bo.maps.model.FilterableMapModelWrapper;
import com.fixit.bo.maps.model.MapModelWrapper;
import com.fixit.bo.views.MapAreaFilterView;
import com.fixit.bo.views.MapAreaFilterView.MapAreaFilter;
import com.fixit.core.dao.mongo.MapAreaDao;
import com.fixit.core.data.MapAreaType;
import com.fixit.core.data.mongo.MapArea;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/22 21:49:30 GMT+3
 */
@Component
@ManagedBean
@ViewScoped
public class MapAreaBean {

	private final static List<MapAreaFilter> FILTERS = Stream.of(
				new MapAreaFilter(FilterType.TRADESMEN_COUNT.getKey())
			).collect(Collectors.toList());
	
	@Autowired
	private MapAreaDao mapAreaDao;
	
	@Autowired
	private MapAreaFilterDataLoader mapAreaFilterDataLoader;
	
	private FilterableMapModelWrapper mapModel;
	private MapAreaFilterView mFilterView;
	
	private Polygon mSelectedPolygon;
		
	private void initMapModel() {
		List<MapArea> mapAreas = mapAreaDao.getAreasForType(MapAreaType.Ward);
		mapModel = new FilterableMapModelWrapper(
				new BaseMapModelWrapper(mapAreas, "mainForm:gmap", new LatLng(-26.1715046, 27.9699835)),
				mapAreaFilterDataLoader
		);
	}
	
	public MapModel getMapModel() {
		if(mapModel == null) {
			initMapModel();
		}
        return mapModel.getModel();
    }
	
	public MapModelWrapper getMapModelWrapper() {
		if (mapModel == null) {
			initMapModel();
		}
		return mapModel;
	}
	
	public MapAreaFilterView getFilterView() {
		if(mFilterView == null) {
			mFilterView = new MapAreaFilterView(FILTERS);
		}
		return mFilterView;
	}
	
	public Polygon getSelectedPolygon() {
		return mSelectedPolygon;
	}
	
	public void onPolygonSelect(OverlaySelectEvent event) {
		mSelectedPolygon = (Polygon) event.getOverlay();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('mapAreaDialog').show();");
		context.update("mainForm:mapAreaDialog");
	}
	
	public void clearSelectedPolygon() {
		mSelectedPolygon = null;
	}
	
	public void filter() {
		mFilterView.applyFilters(mapModel);
	}
	
	public void clearFilters() {
		mFilterView.reset();
		mapModel.clearFilters();
	}
	
}
