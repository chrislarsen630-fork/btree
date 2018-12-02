package Common;

/** Stores recently accessed nodes in memory to reduce disk I/O. This
  * implementation runs in O(n^2) time.
  * @author Landon Lemieux (landonlemieux@u.boisestate.edu)
  * @author Dylan Leman    (dylanleman@u.boisestate.edu)
  * @version 20181202                                                         */
public class BTreeCache_LLDL{
    private BTreeNode[] cache;
    private int capacity    = 0;
    private int numElements = 0;

    /** Sets the size of the cache, in entries. A size of 0 disables the cache.
     * @param n Number of items to store in cache.                            */
    public void setCapacity(int n) {
        this.capacity = n;
        cache = new BTreeNode[n];
    }

    /** Searches the cache for a node, if it exists. Returns the
     * node if found, otherwise returns null.
     * @param nodeID ID of the node to search for.
     * @return Node containing the key, or null if not found.                 */
    public BTreeNode searchNode(int nodeID){
        for (int i = 0; i < numElements; i++){
            if (nodeID == cache[i].getID()){
                BTreeNode temp = cache[i];
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
     * @param node Node to insert.                                            */
    public void insertNode(BTreeNode node) {
        for(int i = numElements-1; i > 0; i--){
            cache[i] = cache[i-1];
        }
        cache[0] = node;
        if(numElements<capacity){
            numElements++;
        }
    }

}
