package Common;

/** Stores recently accessed nodes in memory to reduce disk I/O. This
  * implementation runs in O(n) time and is significantly faster than the
  * O(n^2) LLDL implementation for low degree / large cache runs.
  * @author Christopher M. Larsen (chrislarsen630@u.boisestate.edu)
  * @version 20181202                                                         */
public class BTreeCache{



// STATE DATA ==================================================================
private final java.util.LinkedList<BTreeNode> cache = new java.util.LinkedList<>();
private int capacity = 0;
// STATE DATA ==================================================================


// setCapacity() ===============================================================
/** Sets the capacity of the cache, in entries. A size of 0 disables the
  * cache.
  * @param n Number of items to store in cache.                               */
public void setCapacity(int n){capacity = n;}
// setCapacity() ===============================================================


// searchNode() ================================================================
/** Searches the cache for a node, if it exists. Returns the
  * node if found, otherwise returns null.
  * @param nodeID ID of the node to search for.
  * @return Node containing the key, or null if not found.                    */
public BTreeNode searchNode(int nodeID){
  if(cache.size()<1)return null;
  java.util.ListIterator<BTreeNode> iter = cache.listIterator();

  // search for node  
  BTreeNode node = iter.next();
  while((node.getID()!=nodeID)&&(iter.hasNext()))node = iter.next();
  if(node.getID()!=nodeID)return null;

  // relocate node to beginning of list
  iter.remove();
  cache.addFirst(node);
  
  return node;
}
// searchNode() ================================================================


// insertNode() ================================================================
/** Inserts a node into the cache.
  * @param node Node to insert.                                               */
public void insertNode(BTreeNode node){
  cache.addFirst(node);
  if(cache.size()>capacity)cache.removeLast();
}
// insertNode() ================================================================



} // class BTreeCache
