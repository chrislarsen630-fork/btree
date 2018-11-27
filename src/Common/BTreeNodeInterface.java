package Common;

/** A node within a BTree containing one or more keys.                        */
public interface BTreeNodeInterface{



/** Sets the BTree degree of the node.
  * @param degree Degree value.                                               */
public void setDegree(int degree);


/** Returns the node's ID number.
  * @return Node ID number.                                                   */
public int getID();


/** Sets the node's ID number.
  * @param id ID number, as allocated by BTreeFileInterface.                  */
public void setID(int id);


/** Indicates whether the node is a leaf on its tree.
  * @return True if the node is a leaf.                                       */
public boolean isLeaf();


/** Sets whether this node is a leaf or not.
  * @param value Boolean value indicating whether the node is a leaf.         */
public void setLeaf(boolean value);


/** Returns the number of active keys stored in this node.
  * @return Number of keys stored in node.                                    */
public int getNKeys();


/** Sets the number of active keys stored in this node.
  * @param value Number of keys stored in node.                               */
public void setNKeys(int value);


/** Returns the array containing the keys. Note that the actual array size is
  * fixed and may be larger than the number of active keys; hence getNKeys()
  * should be used to determine the array size rather than the array's
  * .length member.
  * @return Array containing the keys stored in this node.                    */
public TreeObjectInterface[] getKeyArray();


/** Returns the array containing the IDs of this node's children. Note that
  * the actual array size is fixed and may be larger than the number of
  * active keys.
  * @return Array containing the IDs of this node's children.                 */
public int[] getChildrenIDArray();


/** Searches the node for a key with a matching data value. The returned key
  * is a direct reference to the key in the node and modification will update
  * the value inside the node. Null if not found.
  * @param key Key to search for.
  * @return Reference to key with matching data.                              */
public TreeObjectInterface searchKey(TreeObjectInterface key);


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
