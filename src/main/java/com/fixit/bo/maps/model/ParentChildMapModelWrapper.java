/**
 * 
 */
package com.fixit.bo.maps.model;

import java.util.List;
import java.util.Set;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

import com.fixit.core.data.mongo.MapArea;
import com.fixit.core.structure.TreeNode;

/**
 * @author 		Kostyantin
 * @createdAt 	2017/12/29 16:48:40 GMT+2
 */
public class ParentChildMapModelWrapper extends BaseMapModelWrapper {
	
	private TreeNode<MapArea> mCurrentNode;
	private Set<String> mSelectedAreas;
	
	private MapModel mModel;

	public ParentChildMapModelWrapper(TreeNode<MapArea> mapAreaTree, Set<String> selectedAreas, LatLng center) {
		super(new Options(), center);
		mSelectedAreas = selectedAreas;
		setCurrentNode(mapAreaTree);
	}
	
	private void setCurrentNode(TreeNode<MapArea> node) {
		mCurrentNode = node;
		mModel = new MapAreaModel();
		
		List<TreeNode<MapArea>> children = node.getChildren();
		for(TreeNode<MapArea> childNode : children) {
			MapArea mapArea = childNode.getData();
			Polygon polygon = createPolygon(mapArea);
			String mapAreaId = mapArea.get_id().toHexString();
			polygon.setId(mapAreaId);
			setData(polygon, "node", childNode);
			if(mSelectedAreas.contains(mapAreaId)) {
				select(polygon);
			}
			mModel.addOverlay(polygon);
		}
	}

	@Override
	public boolean onPolygonSelect(OverlaySelectEvent event) {
		Polygon polygon = (Polygon) event.getOverlay();
		TreeNode<MapArea> node = getData(polygon, "node");
		if(node.isLeaf()) {
			String id = event.getOverlay().getId();
			if(mSelectedAreas.contains(id)) {
				mSelectedAreas.remove(id);
				unselect(polygon);
			} else {
				mSelectedAreas.add(id);
				select(polygon);
			}
		} else {
			setCurrentNode(node);
		}
		return true;
	}
	
	public TreeNode<MapArea> getCurrentNode() {
		return mCurrentNode;
	}

	public Set<String> getSelectedAreas() {
		return mSelectedAreas;
	}
	
	public void showParent() {
		if(!mCurrentNode.isRoot()) {
			setCurrentNode(mCurrentNode.getParent());
		}
	}
	
	public void selectAllChildren() {
		for(Polygon polygon : mModel.getPolygons()) {
			select(polygon);
			mSelectedAreas.add(polygon.getId());
		}
	}
	
	public void unselectAllChildren() {
		for(Polygon polygon : mModel.getPolygons()) {
			unselect(polygon);
			mSelectedAreas.remove(polygon.getId());
		}
	}

	@Override
	public MapModel getModel() {
		return mModel;
	}
	
}
