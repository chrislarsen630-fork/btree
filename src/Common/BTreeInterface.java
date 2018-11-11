package Common;

/** BTree class. Represents a data heirarchy in the form of a rooted tree.    */
public interface BTreeInterface{



/** Associates the BTree with a BTreeFileInterface object. The file interface
  * is responsible for performing the heavy-lifting involved with file I/O;
  * it creates new BTree files, loads and parses existing files, and loads
  * and writes nodes to and from disk. The BTree object should not do any of
  * these tasks on its own -- it should perform all disk I/O through the
  * file interface.
  * @param target BTreeFileInterface to associate with BTree.                 */
public void setBTreeFileInterface(BTreeFileInterface target);


/** Associates the BTree with a BTreeCacheInterface object. The BTree should
  * query the cache to see if a node is in memory prior to reading the node
  * from file. If no cache is desired, call this method with a stub
  * implementation of the cache interface.
  * @param target BTreeCacheInterface to associate with BTree.                */
public void setBTreeCacheInterface(BTreeCacheInterface target);


/** Creates a new BTree and associates it with a target file name. This does
  * not save an in-memory BTree to file; instead, it creates a new empty file
  * and associates the object with the file. Methods such as insert() causes
  * the object to save new nodes to file, and methods such as search() causes
  * the object to load nodes from file as it traverses the tree. The object
  * should not perform disk I/O itself; it should use the BTreeFileInterface
  * specified by setBTreeFileInterface().
  * @param targetFile File path for the created file. If the file exists,
  *                   it will be overwritten.                                 */
public void createNewFile(String targetFile);


/** Associates BTree with an existing file and loads the root node to memory.
  * This does not load an entire BTree from file; instead, it associates the
  * object with the file and merely loads the root node. Methods such as
  * search() causes the object to load nodes from file as it traverses the
  * tree. The object should not perform disk I/O itself; it should use the
  * BTreeFileInterface specified by setBTreeFileInterface().
  * @param sourceFile File path to load the BTree from.                       */
public void loadFromFile(String sourceFile);


/** Searches for a key from the root node. Returns the node containing the
  * key, or null if it is not found.
  * @param key Key value to search for.
  * @return Node containing the key, or null if not found.                    */
public BTreeNodeInterface searchKey(TreeObjectInterface key);


/** Searches for a key within a subtree formed by a node. Returns the node
  * containing the key, or null if it is not found.
  * @param node Node to search for the key from.
  * @param key Key value to search for.
  * @return Node containing the key, or null if not found.                    */
public BTreeNodeInterface searchKey(BTreeNodeInterface node,TreeObjectInterface key);


/** Inserts a key into the tree. 
  * @param key Key value to insert.                                           */
public void insertKey(TreeObjectInterface key);



} // interface BTreeInterface
