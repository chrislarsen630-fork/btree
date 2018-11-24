package Common;

/** BTree cache interface. Stores recently accessed nodes in memory to
  * reduce disk I/O.                                                          */
public interface BTreeCacheInterface{



/** Sets the size of the cache, in entries. A size of 0 disables the cache.
  * @param cacheSize Number of items to store in cache.                       */
public void setCacheSize(int cacheSize);


/** Searches the cache for a node, if it exists. Returns the
  * node if found, otherwise returns null.
  * @param nodeID ID of the node to search for.
  * @return Node containing the key, or null if not found.                    */
public BTreeNodeInterface searchNode(int nodeID);


/** Inserts a node into the cache.
  * @param node Node to insert.                                               */
public void insertNode(BTreeNodeInterface node);



} // interface BTreeCacheInterface
