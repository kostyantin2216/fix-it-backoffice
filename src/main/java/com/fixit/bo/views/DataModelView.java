/**
 * 
 */
package com.fixit.bo.views;

import com.fixit.core.data.DataModelObject;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/24 16:18:42 GMT+2
 */
public interface DataModelView<E extends DataModelObject<?>> {
	
	E toDMO();
	boolean updateDMO(E entity);
	
}
