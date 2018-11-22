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
public long getFrequency();


/** Increases the frequency count when a duplicate insertion is detected.     */
public void incrementFrequency();



} // interface TreeObjectInterface
