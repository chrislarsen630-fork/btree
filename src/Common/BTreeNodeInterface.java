package Common;

/** A node within a BTree containing one or more keys.                        */
public interface BTreeNodeInterface extends Comparable{



/** Returns the node's ID number.
  * @return Node ID number.                                                   */
public long getID();


/** Sets the node's ID number.
  * @param id ID number, as allocated by BTreeFileInterface.                  */
public void setID(long id);


/** Indicates whether the node is a leaf on its tree.
  * @return True if the node is a leaf.                                       */
public boolean isLeaf();


/** Returns a list of the node's keys.
  * @return List of keys contained in the node.                               */
public java.util.List<TreeObjectInterface> getKeys();


/** Returns a list of the IDs of the node's children.
  * @return List of children IDs.                                             */
public java.util.List<Long> getChildrenIDs();



} // interface BTreeNodeInterface
