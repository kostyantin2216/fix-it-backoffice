/**
 * 
 */
package com.fixit.bo.beans.datatable;

import com.fixit.core.dao.CommonDao;
import com.fixit.core.dao.mongo.TradesmanDao;
import com.fixit.core.data.mongo.Tradesman;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/10/27 19:54:37 GMT+3
 */
public class TradesmanTableView extends DataTableView<Tradesman, TradesmanDao> {

	public TradesmanTableView(CommonDao<Tradesman, ?> dao) {
		super(dao);
	}

	
	
}
