package Common.Impl;

import Common.*;

/** Chris's implementation of BTree. */
public class BTree_CML implements BTreeInterface{



// STATE DATA ==================================================================
private BTreeFileInterface  btreeFile;
private BTreeCacheInterface btreeCache;
private BTreeNodeInterface  rootNode;
private int     btreeDegree  = 0;
private int     cacheEntries = 0;
// STATE DATA ==================================================================



// setUseCache() ===============================================================
@Override public void setCacheSize(int cacheSize){
  cacheEntries = cacheSize;
  if(btreeCache!=null)btreeCache.setCacheSize(cacheSize);
}
// setUseCache() ===============================================================


// createNewFile() =============================================================
@Override public void createNewFile(String targetFile,int degree) throws OmniException{
  btreeFile  = AllocateC.new_BTreeFile();
  btreeCache = AllocateC.new_BTreeCache();
  if(cacheEntries>0)btreeCache.setCacheSize(cacheEntries);
  
  btreeDegree = degree;
  rootNode = btreeFile.createNewFile(targetFile,degree);
  rootNode.setLeaf(true);
  dispatchNode(rootNode);
}
// createNewFile() =============================================================


// loadFromFile() ==============================================================
@Override public void loadFromFile(String sourceFile) throws OmniException{
  btreeFile  = AllocateC.new_BTreeFile();
  btreeCache = AllocateC.new_BTreeCache();
  if(cacheEntries>0)btreeCache.setCacheSize(cacheEntries);

  rootNode = btreeFile.loadFromFile(sourceFile);
}
// loadFromFile() ==============================================================


// searchKey() =================================================================
@Override public BTreeNodeInterface searchKey(TreeObjectInterface key) throws OmniException{
  return b_tree_search(rootNode,key);
}
// searchKey() =================================================================


// insertKey() =================================================================
@Override public void insertKey(TreeObjectInterface key) throws OmniException{
  BTreeNodeInterface node = searchKey(key);
  if(node!=null){
    int i = 0;
    long data = key.getData();
    TreeObjectInterface[] nodeKeys = node.getKeyArray();
    for(int iS=node.getNKeys();i<iS;i++){
      if(nodeKeys[i].compareTo(key)==0)break;
    }
    nodeKeys[i].incrementFrequency();
    dispatchNode(node);
  }else b_tree_insert(key);
}
// insertKey() =================================================================


// getRootNode() ===============================================================
@Override public BTreeNodeInterface getRootNode(){return rootNode;}
// getRootNode() ===============================================================


// fetchNode() =================================================================
@Override public BTreeNodeInterface fetchNode(int id) throws OmniException{
  if(id==rootNode.getID())return rootNode;
  if(cacheEntries>0){
    BTreeNodeInterface ret = btreeCache.searchNode(id);
    if(ret!=null)return ret;
  }
  return btreeFile.readNode(id);
}
// fetchNode() =================================================================


// dispatchNode() ==============================================================
private void dispatchNode(BTreeNodeInterface node) throws OmniException{
  if(node.getID()==rootNode.getID())rootNode = node; // make sure root is current
  if(cacheEntries>0)btreeCache.insertNode(node);
  btreeFile.writeNode(node);
}
// dispatchNode() ==============================================================


// b_tree_search() =============================================================
private BTreeNodeInterface b_tree_search(BTreeNodeInterface x,TreeObjectInterface k) throws OmniException{
  int i  = 0;
  int xN = x.getNKeys();
  TreeObjectInterface[] xKey = x.getKeyArray();
  for(;(i<xN)&&(k.compareTo(xKey[i])>0);i++){}
  if( (i<xN) && (k.compareTo(xKey[i])==0) )return x;
  if(x.isLeaf())return null;
  int xCID = x.getChildrenIDArray()[i];
  if(xCID<1)return null;
  return b_tree_search(fetchNode(xCID),k);
}
// b_tree_search() =============================================================


// b_tree_split_child() ========================================================
private void b_tree_split_child(BTreeNodeInterface x,int i,BTreeNodeInterface y) throws OmniException{
  int t  = btreeDegree;
  int xN = x.getNKeys();
  BTreeNodeInterface z = fetchNode(btreeFile.allocateNode());
  z.setLeaf(y.isLeaf());
  z.setNKeys(t-1);
  TreeObjectInterface[] xKey = x.getKeyArray();
  TreeObjectInterface[] yKey = y.getKeyArray();
  TreeObjectInterface[] zKey = z.getKeyArray();
  int[] xC = x.getChildrenIDArray();
  int[] yC = y.getChildrenIDArray();
  int[] zC = z.getChildrenIDArray();
  for(int j=0;j<t-1;j++)zKey[j] = yKey[j+t];
  if(!y.isLeaf()){
    for(int j=0;j<t;j++)zC[j] = yC[j+t];
  }
  y.setNKeys(t-1);
  for(int j=xN;j>=i+1;j--)xC[j+1] = xC[j];
  xC[i+1] = z.getID();
  for(int j=xN-1;j>=i;j--)xKey[j+1] = xKey[j];
  xKey[i] = yKey[t-1];
  x.setNKeys(xN+1);
  dispatchNode(y);
  dispatchNode(z);
  dispatchNode(x);
}
// b_tree_split_child() ========================================================


// b_tree_insert_nonfull() =====================================================
private void b_tree_insert_nonfull(BTreeNodeInterface x,TreeObjectInterface k)
throws OmniException{
  int t  = btreeDegree;
  int xN = x.getNKeys();
  int i  = xN-1;
  TreeObjectInterface[] xKey = x.getKeyArray();
  int[] xC = x.getChildrenIDArray();
  if(x.isLeaf()){
    for(;(i>=0)&&(k.compareTo(xKey[i])<0);i--)xKey[i+1] = xKey[i];
    xKey[i+1] = k;
    x.setNKeys(xN+1);
    dispatchNode(x);
  }else{
    for(;(i>=0)&&(k.compareTo(xKey[i])<0);i--){}
    i++;
    BTreeNodeInterface xCi = fetchNode(xC[i]);
    if(xCi.getNKeys()==2*t-1){
      b_tree_split_child(x,i,xCi);
      if(k.compareTo(xKey[i])>0)i++;
    }
    xCi = fetchNode(xC[i]);
    b_tree_insert_nonfull(xCi,k);
  }
}
// b_tree_insert_nonfull() =====================================================


// b_tree_insert() =============================================================
private void b_tree_insert(TreeObjectInterface k) throws OmniException{
  int t = btreeDegree;
  BTreeNodeInterface r = rootNode;
  int rN = r.getNKeys();
  if(rN==2*t-1){
    BTreeNodeInterface s = fetchNode(btreeFile.allocateNode());
    rootNode = s;
    s.setLeaf(false);
    s.setNKeys(0);
    s.getChildrenIDArray()[0] = r.getID();
    dispatchNode(s);
    btreeFile.setRootNode(s.getID());
    b_tree_split_child(s,0,r);
    b_tree_insert_nonfull(s,k);
  }else b_tree_insert_nonfull(r,k);
}
// b_tree_insert() =============================================================



} // class BTree_Stub
