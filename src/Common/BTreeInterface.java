package Common;

/** BTree class. Represents a data heirarchy in the form of a rooted tree.    */
public interface BTreeInterface{



/** Sets the size of the cache, or disables it if the size is set to 0. Using
  * the cache will speed up node accesses at the cost of increased memory
  * usage.
  * @param cacheSize Size of the cache in entries, or 0 to disable.           */
public void setCacheSize(int cacheSize);


/** Creates a new BTree and associates it with a target file name. This does
  * not save an in-memory BTree to file; instead, it creates a new empty file
  * and associates the object with the file. Methods such as insert() causes
  * the object to save new nodes to file, and methods such as search() causes
  * the object to load nodes from file as it traverses the tree. The object
  * should not perform disk I/O itself; it should use the BTreeFileInterface
  * specified by setBTreeFileInterface().
  * @param targetFile File path for the created file. If the file exists,
  *                   it will be overwritten.
  * @param degree Degree of the BTree.
  * @throws OmniException on file creation error.                             */
public void createNewFile(String targetFile,int degree) throws OmniException;


/** Associates BTree with an existing file and loads the root node to memory.
  * This does not load an entire BTree from file; instead, it associates the
  * object with the file and merely loads the root node. Methods such as
  * search() causes the object to load nodes from file as it traverses the
  * tree. The object should not perform disk I/O itself; it should use the
  * BTreeFileInterface specified by setBTreeFileInterface().
  * @param sourceFile File path to load the BTree from.
  * @throws OmniException on file access error.                               */
public void loadFromFile(String sourceFile) throws OmniException;


/** Searches for a key from the root node. Returns the node containing the
  * key, or null if it is not found.
  * @param key Key value to search for.
  * @return Node containing the key, or null if not found.                    */
public BTreeNodeInterface searchKey(TreeObjectInterface key) throws OmniException;


/** Inserts a key into the tree. 
  * @param key Key value to insert.                                           */
public void insertKey(TreeObjectInterface key) throws OmniException;



} // interface BTreeInterface
