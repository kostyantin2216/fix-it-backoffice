/**
 * 
 */
package com.fixit.bo.beans;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

import com.fixit.bo.maps.filters.MapAreaFilterDataLoader;
import com.fixit.bo.maps.filters.MapModelFilterer.FilterType;
import com.fixit.bo.maps.model.FilterableMapModelWrapper;
import com.fixit.bo.maps.model.MapModelWrapper;
import com.fixit.bo.maps.model.SimpleMapModelWrapper;
import com.fixit.bo.views.MapAreaFilterView;
import com.fixit.bo.views.MapAreaFilterView.MapAreaFilter;
import com.fixit.core.dao.mongo.MapAreaDao;
import com.fixit.core.data.MapAreaType;
import com.fixit.core.data.mongo.MapArea;
import com.fixit.core.logging.FILog;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.PlaceAutocompleteType;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/22 21:49:30 GMT+3
 */
@ManagedBean
@ViewScoped
public class MapAreaBean {

	private final static List<MapAreaFilter> FILTERS = Stream.of(
				new MapAreaFilter(FilterType.TRADESMEN_COUNT.getKey())
			).collect(Collectors.toList());

	@ManagedProperty(value = "#{mapAreaDao}")
	private MapAreaDao mMapAreaDao;
	
	@ManagedProperty(value = "#{geoApiContext}")
	private transient GeoApiContext mGeoApiContext;

	@ManagedProperty(value = "#{mapAreaFilterDataLoader}")
	private MapAreaFilterDataLoader mMapAreaFilterDataLoader;
	
	private FilterableMapModelWrapper mMapModel;
	private MapAreaFilterView mFilterView;
	
	private Polygon mSelectedPolygon;
		
	private void initMapModel() {
		List<MapArea> mapAreas = mMapAreaDao.getAreasForType(MapAreaType.Ward);
		mMapModel = new FilterableMapModelWrapper(
				new SimpleMapModelWrapper(mapAreas, new LatLng(-26.1715046, 27.9699835)),
				mMapAreaFilterDataLoader
		);
	}
	
	public void setmMapAreaDao(MapAreaDao mMapAreaDao) {
		this.mMapAreaDao = mMapAreaDao;
	}

	public void setmGeoApiContext(GeoApiContext mGeoApiContext) {
		this.mGeoApiContext = mGeoApiContext;
	}

	public void setmMapAreaFilterDataLoader(MapAreaFilterDataLoader mMapAreaFilterDataLoader) {
		this.mMapAreaFilterDataLoader = mMapAreaFilterDataLoader;
	}

	public MapModel getMapModel() {
		if(mMapModel == null) {
			initMapModel();
		}
        return mMapModel.getModel();
    }
	
	public MapModelWrapper getMapModelWrapper() {
		if (mMapModel == null) {
			initMapModel();
		}
		return mMapModel;
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
		mFilterView.applyFilters(mMapModel);
	}
	
	public void clearFilters() {
		mFilterView.reset();
		mMapModel.clearFilters();
	}
	
	public List<String> completeAddress(String query) {
		try {
			AutocompletePrediction[] prediction = PlacesApi.placeAutocomplete(mGeoApiContext, query)
				.types(PlaceAutocompleteType.ADDRESS)
				.components(ComponentFilter.country("ZA"))
				.await();
			
			return Arrays.stream(prediction)
						.map(p -> p.description)
						.collect(Collectors.toList());
		} catch (ApiException | InterruptedException | IOException e) {
			FILog.e("Order address autocomplete", e.getMessage(), e);
		}
		
		return Collections.emptyList();
	}
	
}
