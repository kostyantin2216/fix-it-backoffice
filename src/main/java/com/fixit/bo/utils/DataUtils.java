/**
 * 
 */
package com.fixit.bo.utils;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.map.LatLng;
import org.springframework.stereotype.Component;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/21 23:53:28 GMT+3
 */
@Component
@ManagedBean
@ApplicationScoped
public class DataUtils {

	public Integer[] boxArray(int[] array) {
		Integer[] result = new Integer[array.length];
		for(int i = 0; i < array.length; i++) {
			result[i] = array[i];
		}
		return result;
	}
	
	public String toString(LatLng latLng) {
		return latLng.getLat() + "," + latLng.getLng();
	}
	
}
