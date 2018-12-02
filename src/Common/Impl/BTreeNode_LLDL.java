package Common;

import java.util.ArrayList;
import java.util.List;

public class BTreeNode implements BTreeNodeInterface {

	long id;
	boolean isLeaf;
	List<TreeObjectInterface> keys;
	List<Long> children;
	long parentID;
	
	public BTreeNode() {
		keys = new ArrayList<TreeObjectInterface>();
		children = new ArrayList<Long>();
		id = 0;
		isLeaf = true;
	}
	
	@Override
	public int compareTo(Object o) {
		if(id <  ((BTreeNode) o).getID()) {
			return -1;
		}else if(id > ((BTreeNode) o).getID()) {
			return 1;
		}
		return 0;
	}

	@Override
	public long getID() {
		return id;
	}
	
	/**
	 * Returns the ID of the parent node
	 * @return parent ID
	 */
	public long getParent() {
		return parentID;
	}
	
	/**
	 * Sets the ID of the parent node
	 * @param parent ID                   
	 */
	public void setParent(long parent) {
		parentID = parent;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}
	
	@Override
	public List<TreeObjectInterface> getKeys() {
		return keys;
	}

	@Override
	public List<Long> getChildrenIDs() {
		return children;
	}


}
