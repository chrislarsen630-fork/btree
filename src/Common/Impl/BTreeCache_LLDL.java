package Common.Impl;

import Common.*;

//** Landon and Dylan's implementation of BTreeCache. */
public class BTreeCache_LLDL implements BTreeCacheInterface{
    private BTreeNodeInterface [] cache;
    private int capacity    = 0;
    private int numElements = 0;

    /** Sets the size of the cache, in entries. A size of 0 disables the cache.
     * @param cacheSize Number of items to store in cache.                       */
    @Override public void setCacheSize(int cacheSize) {
        this.capacity = cacheSize;
        cache = new BTreeNodeInterface[cacheSize];
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
                return temp;
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
        if(numElements<capacity){
            numElements++;
        }
    }

}