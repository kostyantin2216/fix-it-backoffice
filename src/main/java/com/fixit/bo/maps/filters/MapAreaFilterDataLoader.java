/**
 * 
 */
package com.fixit.bo.maps.filters;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fixit.bo.maps.filters.MapModelFilterer.FilterType;
import com.fixit.bo.maps.model.FilterableMapModelWrapper.FilterDataLoader;
import com.fixit.components.maps.MapAreaController;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/11/17 15:11:38 GMT+2
 */
@Component
@ManagedBean
@SessionScoped
public class MapAreaFilterDataLoader implements FilterDataLoader {

	@Autowired
	private MapAreaController mapAreaController;
	
	@Override
	public Map<String, Object> loadFilterData(FilterType filterType) {
		Map<String, Object> data = new HashMap<>();
		switch (filterType) {
			case TRADESMEN_COUNT:
				data.putAll(mapAreaController.getTradesmenCountPerArea());
				break;
			case ORDER_COUNT:
				break;
		}
		return data;
	}
	
	

}
