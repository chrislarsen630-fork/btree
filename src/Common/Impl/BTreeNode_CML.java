package Common.Impl;

import Common.*;
import java.util.*;

/** Chris's implementation of BTreeNode. */
public class BTreeNode_CML implements BTreeNodeInterface{



// STATE DATA ==================================================================
int  btreeDegree = 0;
long nodeID      = 0;
int  maxChildren = 0;
int  maxKeys     = 0;
// STATE DATA ==================================================================


// setDegree() =================================================================
@Override public void setDegree(int degree){
  if(degree<2)throw new RuntimeException("Invalid BTree degree size.");
  
  btreeDegree = degree;
  maxChildren = degree*2;
  maxKeys     = (degree*2)-1;
}
// setDegree() =================================================================


// getID() =====================================================================
@Override public long getID(){return nodeID;}
// getID() =====================================================================


// setID() =====================================================================
@Override public void setID(long id){nodeID = id;}
// setID() =====================================================================


// isLeaf() ====================================================================
@Override public boolean isLeaf(){
  return false; // TODO
}
// isLeaf() ====================================================================


// convertToBinaryBlob() =======================================================
@Override public byte[] convertToBinaryBlob(){
  return null; // TODO
}
// convertToBinaryBlob() =======================================================


// convertFromBinaryBlob() =====================================================
@Override public void convertFromBinaryBlob(byte[] blob){
  // TODO
}
// convertFromBinaryBlob() =====================================================


// inflateToMaximumSize() ======================================================
@Override public void inflateToMaximumSize(){
  // TODO
}
// inflateToMaximumSize() ======================================================



} // class BTreeNode_CML
