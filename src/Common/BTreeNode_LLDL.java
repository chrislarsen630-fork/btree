package Common;

import java.util.ArrayList;
import java.util.List;

/** A node within a BTree containing one or more keys.
  * @author Landon Lemieux (landonlemieux@u.boisestate.edu)
  * @author Dylan Leman    (dylanleman@u.boisestate.edu)                      */
public class BTreeNode_LLDL{

	long id;
	boolean isLeaf;
	List<TreeObject> keys;
	List<Long> children;
	long parentID;
	
	public BTreeNode_LLDL() {
		keys = new ArrayList<TreeObject>();
		children = new ArrayList<Long>();
		id = 0;
		isLeaf = true;
	}
	
	public int compareTo(Object o) {
		if(id <  ((BTreeNode) o).getID()) {
			return -1;
		}else if(id > ((BTreeNode) o).getID()) {
			return 1;
		}
		return 0;
	}

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

	public void setID(long id) {
		this.id = id;
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}
	
	public List<TreeObject> getKeys() {
		return keys;
	}

	public List<Long> getChildrenIDs() {
		return children;
	}


}
