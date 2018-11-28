package Common.Impl;

import Common.*;
import java.util.*;

/** Chris's O(n) implementation of BTreeCache. Significantly faster than
  * the O(n^2) LLDL implementation for low degree / large cache runs.         */
public class BTreeCache_CML implements Common.BTreeCacheInterface{



// STATE DATA ==================================================================
private final LinkedList<BTreeNodeInterface> cache = new LinkedList<>();
private int capacity = 0;
// STATE DATA ==================================================================


// setCacheSize() ==============================================================
@Override public void setCacheSize(int cacheSize){capacity = cacheSize;}
// setCacheSize() ==============================================================


// searchNode() ================================================================
@Override public BTreeNodeInterface searchNode(int nodeID){
  if(cache.size()<1)return null;
  ListIterator<BTreeNodeInterface> iter = cache.listIterator();

  // search for node  
  BTreeNodeInterface node = iter.next();
  while((node.getID()!=nodeID)&&(iter.hasNext()))node = iter.next();
  if(node.getID()!=nodeID)return null;
  
  // relocate node to beginning of list
  iter.remove();
  cache.addFirst(node);
  
  return node;
}
// searchNode() ================================================================


// insertNode() ================================================================
@Override public void insertNode(BTreeNodeInterface node){
  cache.addFirst(node);
  if(cache.size()>=capacity)cache.removeLast();
}
// insertNode() ================================================================



} // class BTreeCache_CML
