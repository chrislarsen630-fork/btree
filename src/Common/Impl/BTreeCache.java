package Common.Impl;

import Common.*;

/** BTree cache interface. Stores recently accessed nodes in memory to
 * reduce disk I/O.                                                          */
public class BTreeCache implements BTreeCacheInterface{
    private BTreeNodeInterface [] cache;
    private int capacity;
    private int numElements;

    public BTreeCache(int size){
    this.capacity = size;
    cache = new BTreeNodeInterface[capacity];
    }

    /** Sets the size of the cache, in entries. A size of 0 disables the cache.
     * @param cacheSize Number of items to store in cache.                       */
    @Override public void setCacheSize(int cacheSize) {
        cache = java.util.Arrays.copyOf(cache,cacheSize);
    }

    /** Searches the cache for a node, if it exists. Returns the
     * node if found, otherwise returns null.
     * @param nodeID ID of the node to search for.
     * @return Node containing the key, or null if not found.                    */
    @Override public BTreeNodeInterface searchNode(int nodeID){
        for (int i = 0; i < numElements; i++){
            if (nodeID == cache[i].getID()){
                BTreeNodeInterface temp = cache[i];
                for(int j = i; j > 0; j--){
                    cache[j] = cache[j-1];
                }
                cache[0] = temp;
                return cache[i];
            }
        }
        return null;
    }


    /** Inserts a node into the cache.
     * @param node Node to insert.                                               */
    @Override public void insertNode(BTreeNodeInterface node) {
        for(int i = numElements-1; i > 0; i--){
            cache[i] = cache[i-1];
        }
        cache[0] = node;
        if(numElements<=capacity){
            numElements++;
        }
    }

}