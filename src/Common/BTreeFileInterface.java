package Common;

/** BTree file I/O interface. This interface is intended for internal use by
  * BTreeInterface. No other code should touch it, beyond instantiating a new
  * object via BTreeInterface.setBTreeFileInterface(new BTreeFileInterface()) */
public interface BTreeFileInterface{



/** Creates a new BTree file. Called by BTreeInterface.createNewFile().
  * @param targetFile File path for the created file. If the file exists,
  *                   it will be overwritten.                                 
  * @return Root node of the BTree.                                           */
public BTreeNodeInterface createNewFile(String targetFile);


/** Loads an existing BTree file from disk. Called by
  * BTreeInterface.loadFromFile().
  * @param sourceFile File path to load the BTree from.
  * @return Root node of the BTree.                                           */
public BTreeNodeInterface loadFromFile(String sourceFile);


/** Loads the node at the specified index from disk.
  * @param key Key of the node to read.
  * @return BTree node.                                                       */
public BTreeNodeInterface readNode(TreeObjectInterface key);


/** Saves a node at the specified index to disk.
 * @param node Node to write.                                                 */
public void writeNode(BTreeNodeInterface node);



} // interface BTreeFileInterface
