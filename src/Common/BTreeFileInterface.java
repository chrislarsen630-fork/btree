package Common;

/** BTree file I/O interface. This interface is intended for internal use by
  * BTreeInterface. No other code should touch it, beyond instantiating a new
  * object via BTreeInterface.setBTreeFileInterface(new BTreeFileInterface()) */
public interface BTreeFileInterface{



/** Creates a new BTree file. Called by BTreeInterface.createNewFile().
  * @param targetFile File path for the created file. If the file exists,
  *                   it will be overwritten.
  * @param degree Degree of the BTree.
  * @return Root node of the BTree.
  * @throws OmniException on file creation error.                             */
public BTreeNodeInterface createNewFile(String targetFile,int degree) throws OmniException;


/** Loads an existing BTree file from disk. Called by
  * BTreeInterface.loadFromFile().
  * @param sourceFile File path to load the BTree from.
  * @return Root node of the BTree.
  * @throws OmniException on file access or read error.                       */
public BTreeNodeInterface loadFromFile(String sourceFile) throws OmniException;


/** Loads the node with the specified ID from disk.
  * @param nodeID ID of the node to read.
  * @return BTree node.
  * @throws OmniException if node does not exist or on read error.            */
public BTreeNodeInterface readNode(int nodeID) throws OmniException;


/** Allocates space for a new BTree node and returns its ID number.
  * @return ID of the newly allocated node.
  * @throws OmniException if a new node cannot be allocated.                  */
public int allocateNode() throws OmniException;


/** Saves a node to disk.
 * @param node Node to write.
 * @throws OmniException on file write error.                                 */
public void writeNode(BTreeNodeInterface node) throws OmniException;


/** Sets the ID of the root node.
  * @param nodeID ID of the root node.                                        */
public void setRootNode(int nodeID);



} // interface BTreeFileInterface
