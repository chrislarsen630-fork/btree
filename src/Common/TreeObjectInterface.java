package Common;

/** BTree object class. Data packet contained within a node.                  */
public interface TreeObjectInterface extends Comparable<TreeObjectInterface>{



/** Returns the data contained within the tree object.
  * @return Data contained in the object.                                     */
public long getData();


/** Sets the data contained within the tree object.
 * @param value Data value to set.                                            */
public void setData(long value);


/** Returns the number of duplicate insertions.
  * @return frequency value                                                   */
public int getFrequency();


/** Increases the frequency count when a duplicate insertion is detected.     */
public void incrementFrequency();


/** Returns a binary blob of the tree object.
  * @return Binary version of the tree object.                                */
public byte[] convertToBinaryBlob();


/** Loads this tree object from a binary blob.
  * @param blob Binary data containing tree object.                           */
public void convertFromBinaryBlob(byte[] blob);



} // interface TreeObjectInterface
