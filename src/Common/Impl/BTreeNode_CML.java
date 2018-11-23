package Common.Impl;

import Common.*;
import java.util.*;

/** Chris's implementation of BTreeNode. */
public class BTreeNode_CML implements BTreeNodeInterface{



// STATE DATA ==================================================================
int btreeDegree = 0;
int nodeID      = 0;
int maxChildren = 0;
int maxKeys     = 0;
int nKeys       = 0;
boolean               leafFlag = true;
TreeObjectInterface[] keyArray;
int[]                 childrenIDArray;
// STATE DATA ==================================================================


// setDegree() =================================================================
@Override public void setDegree(int degree){
  if(degree<2)throw new RuntimeException("Invalid BTree degree size.");
  
  btreeDegree = degree;
  maxChildren = degree*2;
  maxKeys     = (degree*2)-1;
  
  childrenIDArray = new int[maxChildren];
  keyArray        = new TreeObjectInterface[maxKeys];
  for(int i=0;i<maxKeys;i++)keyArray[i] = AllocateC.new_TreeObject();
}
// setDegree() =================================================================


// getID() =====================================================================
@Override public int getID(){return nodeID;}
// getID() =====================================================================


// setID() =====================================================================
@Override public void setID(int id){nodeID = id;}
// setID() =====================================================================


// isLeaf() ====================================================================
@Override public boolean isLeaf(){return leafFlag;}
// isLeaf() ====================================================================


// setLeaf() ===================================================================
@Override public void setLeaf(boolean value){leafFlag = value;}
// setLeaf() ===================================================================


// getNKeys() ==================================================================
@Override public int getNKeys(){return nKeys;}
// getNKeys() ==================================================================


// setNKeys() ==================================================================
@Override public void setNKeys(int value){nKeys = value;}
// setNKeys() ==================================================================


// getKeyArray() ===============================================================
@Override public TreeObjectInterface[] getKeyArray(){return keyArray;}
// getKeyArray() ===============================================================


// getChildrenIDArray() ========================================================
@Override public int[] getChildrenIDArray(){return childrenIDArray;}
// getChildrenIDArray() ========================================================


// convertToBinaryBlob() =======================================================
@Override public byte[] convertToBinaryBlob(){
  int keyBlobSize = AllocateC.new_TreeObject().convertToBinaryBlob().length;
  
  byte[] blob = new byte[4 + maxKeys*keyBlobSize + maxChildren*4];
  
  int offset = 0;
  
  for(int i=0;i<4;i++)blob[i+offset] = (byte)((nKeys>>(i*8)) & 0xFF);
  offset += 4;
  
  for(int j=0;j<maxKeys;j++){
    TreeObjectInterface key = keyArray[j];
    byte[] keyBlob = key.convertToBinaryBlob();
    System.arraycopy(keyBlob,0,blob,offset,keyBlobSize);
    offset += keyBlobSize;
  }
  
  for(int j=0;j<maxChildren;j++){
    int childID = childrenIDArray[j];
    for(int i=0;i<4;i++)blob[i+offset] = (byte)((childID>>(i*8)) & 0xFF);
    offset += 4;
  }
  
  return blob;
}
// convertToBinaryBlob() =======================================================


// convertFromBinaryBlob() =====================================================
@Override public void convertFromBinaryBlob(byte[] blob){
  int keyBlobSize = AllocateC.new_TreeObject().convertToBinaryBlob().length;

  int offset = 0;
  
  nKeys = 0;
  for(int i=0;i<4;i++)nKeys |= ((int)blob[i+offset])<<(i*8);
  offset += 4;
  
  for(int j=0;j<maxKeys;j++){
    byte[] keyBlob = Arrays.copyOfRange(blob,offset,offset+keyBlobSize);
    TreeObjectInterface keyObj = AllocateC.new_TreeObject();
    keyObj.convertFromBinaryBlob(keyBlob);
    keyArray[j] = keyObj;
    offset += keyBlobSize;
  }
  
  for(int j=0;j<maxChildren;j++){
    int childID = 0;
    for(int i=0;i<4;i++){
      int v = blob[i+offset];
      if(v<0)v = 256+v;
      childID |= v<<(i*8);
    }
    childrenIDArray[j] = childID;
    offset += 4;
  }
}
// convertFromBinaryBlob() =====================================================


// inflateToMaximumSize() ======================================================
@Override public void inflateToMaximumSize(){
  for(int i=0;i<keyArray.length;i++)keyArray[i] = AllocateC.new_TreeObject();
  for(int i=0;i<childrenIDArray.length;i++)childrenIDArray[i] = i;
}
// inflateToMaximumSize() ======================================================



} // class BTreeNode_CML
