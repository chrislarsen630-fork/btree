package Common;

/** Represents a data heirarchy in the form of a rooted tree.
  * @author Christopher M. Larsen (chrislarsen630@u.boisestate.edu)
  * @version 20181202                                                         */
public class BTree{



// STATE DATA ==================================================================
private BTreeFile  btreeFile;
private BTreeCache btreeCache;
private BTreeNode  rootNode;
private int        btreeDegree;
private int        cacheCapacity;
// STATE DATA ==================================================================



// setCacheCapacity() ==========================================================
/** Sets the capacity of the cache, or disables it if the size is set to 0.
  * Using the cache will speed up node accesses at the cost of increased
  * memory usage.
  * @param capacity Size of the cache in entries, or 0 to disable.            */
public void setCacheCapacity(int capacity){
  cacheCapacity = capacity;
  if(btreeCache!=null)btreeCache.setCapacity(cacheCapacity);
}
// setCacheCapacity() ==========================================================


// createNewFile() =============================================================
/** Creates a new BTree and associates it with a target file name. This does
  * not save an in-memory BTree to file; instead, it creates a new empty file
  * and associates the object with the file. Methods such as insert() causes
  * the object to save new nodes to file, and methods such as search() causes
  * the object to load nodes from file as it traverses the tree. The object
  * should not perform disk I/O itself; it should use the BTreeFileInterface
  * specified by setBTreeFileInterface().
  * @param targetFile File path for the created file. If the file exists,
  *                   it will be overwritten.
  * @param degree         Degree of the BTree.
  * @param sequenceLength Length of DNA sequences, in base pairs.
  * @throws OmniException on file creation error.                             */
public void createNewFile(String targetFile,int degree,int sequenceLength)
throws OmniException{
  btreeFile  = new BTreeFile();
  btreeCache = new BTreeCache();
  if(cacheCapacity>0)btreeCache.setCapacity(cacheCapacity);
  
  btreeDegree = degree;
  rootNode    = btreeFile.createNewFile(targetFile,degree,sequenceLength);
  rootNode.setLeaf(true);
  dispatchNode(rootNode);
}
// createNewFile() =============================================================


// loadFromFile() ==============================================================
/** Associates BTree with an existing file and loads the root node to memory.
  * This does not load an entire BTree from file; instead, it associates the
  * object with the file and merely loads the root node. Methods such as
  * search() causes the object to load nodes from file as it traverses the
  * tree. The object should not perform disk I/O itself; it should use the
  * BTreeFileInterface specified by setBTreeFileInterface().
  * @param sourceFile File path to load the BTree from.
  * @throws OmniException on file access error.                               */
public void loadFromFile(String sourceFile) throws OmniException{
  btreeFile  = new BTreeFile();
  btreeCache = new BTreeCache();
  if(cacheCapacity>0)btreeCache.setCapacity(cacheCapacity);

  rootNode = btreeFile.loadFromFile(sourceFile);
}
// loadFromFile() ==============================================================


// getSequenceLength() =========================================================
/** Returns the length of the DNA sequences.
  * @return DNA sequence length.                                              */
public int getSequenceLength(){return btreeFile.getSequenceLength();}
// getSequenceLength() =========================================================


// searchKey() =================================================================
/** Searches for a key from the root node. Returns the node containing the
  * key, or null if it is not found.
  * @param key Key value to search for.
  * @return Node containing the key, or null if not found.                    
  * @throws OmniException on file read error                                  */
public BTreeNode searchKey(TreeObject key) throws OmniException{
  return b_tree_search(rootNode,key);
}
// searchKey() =================================================================


// insertKey() =================================================================
/** Inserts a key into the tree. 
  * @param key Key value to insert.
  * @throws OmniException on file read error.                                 */
public void insertKey(TreeObject key) throws OmniException{
  BTreeNode node = searchKey(key);
  if(node!=null){
    node.searchKey(key).incrementFrequency();
    dispatchNode(node);
  }else b_tree_insert(key);
}
// insertKey() =================================================================


// getRootNode() ===============================================================
/** Returns the node at the root of the tree.
  * @return Root BTree node.                                                  */
public BTreeNode getRootNode(){return rootNode;}
// getRootNode() ===============================================================


// dealloc() ===================================================================
/** Ensures the BTree is written to disk and releases the resources held by
  * the BTree.                                                                */
public void dealloc(){btreeFile.close();}
// dealloc() ===================================================================


// fetchNode() =================================================================
/** Returns the node with the given ID, first checking the root node and
  * cache, then disk. Null if no node is found.
  * @param id ID of the node to fetch.
  * @throws OmniException on file read error.                              
  * @return Node matching the ID.                                             */
public BTreeNode fetchNode(int id) throws OmniException{
  if(rootNode.getID()==id)return rootNode;
  
  // check the cache
  if(cacheCapacity>0){
    BTreeNode ret = btreeCache.searchNode(id);
    if(ret!=null)return ret;
  }
  
  // not cached, read from file
  return btreeFile.readNode(id);
}
// fetchNode() =================================================================


// dispatchNode() ==============================================================
private void dispatchNode(BTreeNode node) throws OmniException{
  if(node.getID()==rootNode.getID())rootNode = node; // make sure root is current
  if(cacheCapacity>0)btreeCache.insertNode(node);
  btreeFile.writeNode(node);
}
// dispatchNode() ==============================================================


// b_tree_search() =============================================================
private BTreeNode b_tree_search(BTreeNode x,TreeObject k) throws OmniException{
  TreeObject[] xKey = x.getKeyArray();
  
  // perform binary search of node
  int lo = -1;
  int hi = x.getNKeys();
  while(true){
    int i = (hi+lo)/2;
    if(i==lo)i++;
    if(i==hi)break;  
    int comparison = k.compareTo(xKey[i]);
    if     (comparison>0)lo = i;
    else if(comparison<0)hi = i;
    else return x;
  }

  // check child nodes
  if(x.isLeaf())return null;
  int xCID = x.getChildrenIDArray()[hi];
  if(xCID<1)return null;
  return b_tree_search(fetchNode(xCID),k);
}
// b_tree_search() =============================================================


// b_tree_split_child() ========================================================
private void b_tree_split_child(BTreeNode x,int i,BTreeNode y) throws OmniException{
  int xN = x.getNKeys();
  BTreeNode z = btreeFile.allocateNode();
  z.setLeaf(y.isLeaf());
  z.setNKeys(btreeDegree-1);
  TreeObject[] xKey = x.getKeyArray();
  TreeObject[] yKey = y.getKeyArray();
  int[] xC = x.getChildrenIDArray();

  //for(int j=0;j<t-1;j++)zKey[j] = yKey[j+t];
  System.arraycopy(yKey,btreeDegree,z.getKeyArray(),0,btreeDegree-1);

  if(!y.isLeaf()){
    //for(int j=0;j<t;j++)zC[j] = yC[j+t];
    System.arraycopy(y.getChildrenIDArray(),btreeDegree,z.getChildrenIDArray(),0,btreeDegree);
  }
  y.setNKeys(btreeDegree-1);

  //for(int j=xN;j>=i+1;j--)xC[j+1] = xC[j];
  System.arraycopy(xC,i+1,xC,i+2,xN-i);
  
  xC[i+1] = z.getID();

  // for(int j=xN-1;j>=i;j--)xKey[j+1] = xKey[j];
  System.arraycopy(xKey,i,xKey,i+1,xN-i);

  xKey[i] = yKey[btreeDegree-1];
  x.setNKeys(xN+1);
  dispatchNode(y);
  dispatchNode(z);
  dispatchNode(x);
}
// b_tree_split_child() ========================================================


// b_tree_insert_nonfull() =====================================================
private void b_tree_insert_nonfull(BTreeNode x,TreeObject k)
throws OmniException{
  int xN = x.getNKeys();
  TreeObject[] xKey = x.getKeyArray();
  int[] xC = x.getChildrenIDArray();

  int i = xN-1;
  //for(;i>-1;i--)if(k.compareTo(xKey[i])>=0)break;
  //i++;
  int lo = -1;
  int hi = xN;
  while(true){
    i = (hi+lo)/2;
    if(i==lo)i++;
    if(i==hi)break;
    int comparison = k.compareTo(xKey[i]);
    if     (comparison>0)lo = i;
    else if(comparison<0)hi = i;
    else break;
  }

  if(x.isLeaf()){
    //for(;(i>=0)&&(k.compareTo(xKey[i])<0);i--)xKey[i+1] = xKey[i];
    //xKey[i+1] = k;
    System.arraycopy(xKey,i,xKey,i+1,xN-i);
    xKey[i] = k;
    
    x.setNKeys(xN+1);
    dispatchNode(x);
  }else{
    //for(;i>-1;i--)if(k.compareTo(xKey[i])>=0)break;
    //i++;

    BTreeNode xCi = fetchNode(xC[i]);
    if(xCi.getNKeys()==2*btreeDegree-1){
      b_tree_split_child(x,i,xCi);
      if(k.compareTo(xKey[i])>0)i++;
    }
    xCi = fetchNode(xC[i]);
    b_tree_insert_nonfull(xCi,k);
  }
}
// b_tree_insert_nonfull() =====================================================


// b_tree_insert() =============================================================
private void b_tree_insert(TreeObject k) throws OmniException{
  BTreeNode r = rootNode;
  int rN = r.getNKeys();
  if(rN==2*btreeDegree-1){
    rootNode = btreeFile.allocateNode();
    rootNode.setLeaf(false);
    rootNode.setNKeys(0);
    rootNode.getChildrenIDArray()[0] = r.getID();
    dispatchNode(rootNode);
    btreeFile.setRootNode(rootNode.getID());
    b_tree_split_child(rootNode,0,r);
    b_tree_insert_nonfull(rootNode,k);
  }else b_tree_insert_nonfull(r,k);
}
// b_tree_insert() =============================================================



} // class BTree
