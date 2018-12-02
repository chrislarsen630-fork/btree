package Common;

/** Data packet contained within a BTree node.
  * @author Christopher M. Larsen (chrislarsen630@u.boisestate.edu)
  * @version 20181202                                                         */
public class TreeObject{



// STATE DATA ==================================================================
private long data;
private int  frequency = 0;
// STATE DATA ==================================================================


// TreeObject() ================================================================
public TreeObject(){}

public TreeObject(long value){
  data      = value;
  frequency = 1;
}
// TreeObject() ================================================================


// getData() ===================================================================
/** Returns the data contained within the tree object.
  * @return Data contained in the object.                                     */
public long getData(){return data;}
// getData() ===================================================================


// setData() ===================================================================
/** Sets the data contained within the tree object.
 * @param value Data value to set.                                            */
public void setData(long value){
  data      = value;
  frequency = 1;
}
// setData() ===================================================================


// getFrequency() ==============================================================
/** Returns the number of duplicate insertions.
  * @return frequency value                                                   */
public int getFrequency(){return frequency;}
// getFrequency() ==============================================================


// incrementFrequency() ========================================================
/** Increases the frequency count when a duplicate insertion is detected.     */
public void incrementFrequency(){frequency++;}
// incrementFrequency() ========================================================


// compareTo() =================================================================
public int compareTo(TreeObject obj){
  if(obj==null)return 1;

  long targetData = obj.getData();
  if(data < targetData)return -1;
  if(data > targetData)return  1;
  return 0;
}
// compareTo() =================================================================


// convertToBinaryBlob() =======================================================
/** Returns a binary blob of the tree object.
  * @return Binary version of the tree object.                                */
public byte[] convertToBinaryBlob(){
  byte[] blob = new byte[12];
  for(int i=0;i<8;i++)blob[i  ] = (byte)((data     >>((7-i)*8)) & 0xFF);
  for(int i=0;i<4;i++)blob[i+8] = (byte)((frequency>>((3-i)*8)) & 0xFF);
  return blob;
}
// convertToBinaryBlob() =======================================================


// convertFromBinaryBlob() =====================================================
/** Loads this tree object from a binary blob.
  * @param blob Binary data containing tree object.                           */
public void convertFromBinaryBlob(byte[] blob){
  data      = 0;
  frequency = 0;

  for(int i=0;i<8;i++)data      |= (((long)blob[i  ])&0xFF) << ((7-i)*8);
  for(int i=0;i<4;i++)frequency |= (((int )blob[i+8])&0xFF) << ((3-i)*8);

}
// convertFromBinaryBlob() =====================================================



} // class TreeObject
