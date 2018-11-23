package Common;

/** BTree cache interface. Stores recently accessed nodes in memory to
  * reduce disk I/O.                                                          */
public interface BTreeCacheInterface{



/** Searches the cache for a node, if it exists. Returns the
  * node if found, otherwise returns null.
  * @param nodeID ID of the node to search for.
  * @return Node containing the key, or null if not found.                    */
public BTreeNodeInterface searchNode(int nodeID);


/** Inserts a node into the cache.
  * @param node Node to insert.                                               */
public void insertNode(BTreeNodeInterface node);



} // interface BTreeCacheInterface
