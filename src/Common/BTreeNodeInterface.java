package Common;

/** A node within a BTree containing one or more keys.                        */
public interface BTreeNodeInterface{



/** Sets the BTree degree of the node.
  * @param degree Degree value.                                               */
public void setDegree(int degree);


/** Returns the node's ID number.
  * @return Node ID number.                                                   */
public long getID();


/** Sets the node's ID number.
  * @param id ID number, as allocated by BTreeFileInterface.                  */
public void setID(long id);


/** Indicates whether the node is a leaf on its tree.
  * @return True if the node is a leaf.                                       */
public boolean isLeaf();


/** Returns a binary blob of the node.
  * @return Binary version of the node.                                       */
public byte[] convertToBinaryBlob();


/** Loads this node from a binary blob.
  * @param blob Binary data containing node.                                  */
public void convertFromBinaryBlob(byte[] blob);


/** Makes this node as large as possible for its given degree. Used by
  * BTreeFile to determine how much space to allocate for nodes.              */
public void inflateToMaximumSize();



} // interface BTreeNodeInterface
