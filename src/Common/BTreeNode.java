package Common;

import java.util.*;

/** A node within a BTree containing one or more keys.
  * @author Christopher M. Larsen (chrislarsen630@u.boisestate.edu)
  * @version 20181202                                                         */
public class BTreeNode{



// STATE DATA ==================================================================
private int                nodeID;
private int                nKeys;
private boolean            leafFlag;
private final TreeObject[] keyArray;
private final int[]        childrenIDArray;
private final int          maxChildren;
private final int          maxKeys;
// STATE DATA ==================================================================


// BTreeNode() =================================================================
public BTreeNode(int degree){
  if(degree<2)throw new RuntimeException("Invalid BTree degree size.");
  
  maxChildren = degree*2;
  maxKeys     = maxChildren-1;

  childrenIDArray = new int[maxChildren];
  keyArray        = new TreeObject[maxKeys];
  //for(int i=0;i<maxKeys;i++)keyArray[i] = new TreeObject();  
}
// BTreeNode() =================================================================


// getID() =====================================================================
/** Returns the node's ID number.
  * @return Node ID number.                                                   */
public int getID(){return nodeID;}
// getID() =====================================================================


// setID() =====================================================================
/** Sets the node's ID number.
  * @param id ID number, as allocated by BTreeFileInterface.                  */
public void setID(int id){nodeID = id;}
// setID() =====================================================================


// isLeaf() ====================================================================
/** Indicates whether the node is a leaf on its tree.
  * @return True if the node is a leaf.                                       */
public boolean isLeaf(){return leafFlag;}
// isLeaf() ====================================================================


// setLeaf() ===================================================================
/** Sets whether this node is a leaf or not.
  * @param value Boolean value indicating whether the node is a leaf.         */
public void setLeaf(boolean value){leafFlag = value;}
// setLeaf() ===================================================================


// getNKeys() ==================================================================
/** Returns the number of active keys stored in this node.
  * @return Number of keys stored in node.                                    */
public int getNKeys(){return nKeys;}
// getNKeys() ==================================================================


// setNKeys() ==================================================================
/** Sets the number of active keys stored in this node.
  * @param value Number of keys stored in node.                               */
public void setNKeys(int value){nKeys = value;}
// setNKeys() ==================================================================


// getKeyArray() ===============================================================
/** Returns the array containing the keys. Note that the actual array size is
  * fixed and may be larger than the number of active keys; hence getNKeys()
  * should be used to determine the array size rather than the array's
  * .length member.
  * @return Array containing the keys stored in this node.                    */
public TreeObject[] getKeyArray(){return keyArray;}
// getKeyArray() ===============================================================


// getChildrenIDArray() ========================================================
/** Returns the array containing the IDs of this node's children. Note that
  * the actual array size is fixed and may be larger than the number of
  * active keys.
  * @return Array containing the IDs of this node's children.                 */
public int[] getChildrenIDArray(){return childrenIDArray;}
// getChildrenIDArray() ========================================================


// searchKey() =================================================================
/** Searches the node for a key with a matching data value. The returned key
  * is a direct reference to the key in the node and modification will update
  * the value inside the node. Null if not found.
  * @param key Key to search for.
  * @return Reference to key with matching data.                              */
public TreeObject searchKey(TreeObject key){
  int lo = -1;
  int hi = nKeys;
  
  while(true){
    int i = (hi+lo)/2;
    if(i==lo)i++;
    if(i==hi)return null;
    
    int comparison = keyArray[i].compareTo(key);
    if     (comparison<0)lo = i;
    else if(comparison>0)hi = i;
    else return keyArray[i];
  }
}
// searchKey() =================================================================


// convertToBinaryBlob() =======================================================
/** Returns a binary blob of the node.
  * @return Binary version of the node.                                       */
public byte[] convertToBinaryBlob(){
  int keyBlobSize = (new TreeObject()).convertToBinaryBlob().length;

  byte[] blob = new byte[4 + maxKeys*keyBlobSize + maxChildren*4 + 1];
  
  int offset = 0;
  
  for(int i=0;i<4;i++){
    blob[i+offset] = (byte)((nKeys>>((3-i)*8)) & 0xFF);
  }
  offset += 4;
  
  for(int i=0;i<nKeys;i++){
    byte[] keyBlob = keyArray[i].convertToBinaryBlob();
    System.arraycopy(keyBlob,0,blob,offset,keyBlobSize);
    offset += keyBlobSize;
  }
  offset += (maxKeys-nKeys) * keyBlobSize;
  
  for(int i=0,iS=nKeys+1;i<iS;i++){
    int childID = childrenIDArray[i];
    for(int j=0;j<4;j++)blob[j+offset] = (byte)((childID>>((3-j)*8)) & 0xFF);
    offset += 4;
  }
  offset += (maxChildren-(nKeys+1)) * 4;
  
  blob[offset] = (byte)(leafFlag ? 1 : 0);
  
  return blob;
}
// convertToBinaryBlob() =======================================================


// convertFromBinaryBlob() =====================================================
/** Loads this node from a binary blob.
  * @param blob Binary data containing node.                                  */
public void convertFromBinaryBlob(byte[] blob){
  int keyBlobSize = (new TreeObject()).convertToBinaryBlob().length;

  int offset = 0;
  
  nKeys = 0;
  for(int i=0;i<4;i++){
    nKeys |= (((int)blob[i+offset])&0xFF) << ((3-i)*8);
  }
  offset += 4;
  
  for(int i=0;i<nKeys;i++){
    byte[] keyBlob = Arrays.copyOfRange(blob,offset,offset+keyBlobSize);
    TreeObject keyObj = new TreeObject();
    keyObj.convertFromBinaryBlob(keyBlob);
    keyArray[i] = keyObj;
    offset += keyBlobSize;
  }
  offset += (maxKeys-nKeys) * keyBlobSize;
  
  for(int i=0,iS=nKeys+1;i<iS;i++){
    int childID = 0;
    for(int j=0;j<4;j++){
      childID |= (((int)blob[j+offset])&0xFF) << ((3-j)*8);
    }
    childrenIDArray[i] = childID;
    offset += 4;
  }
  offset += (maxChildren-(nKeys+1)) * 4;
  
  leafFlag = (blob[offset]==1);
}
// convertFromBinaryBlob() =====================================================



} // class BTreeNode
