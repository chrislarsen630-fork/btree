package Common.Impl;

import Common.*;

/** Chris's implementation of TreeObject.                                     */
public class TreeObject_CML implements TreeObjectInterface{



// STATE DATA ==================================================================
private long data;
private int  frequency = 1;
// STATE DATA ==================================================================


// getData() ===================================================================
@Override public long getData(){return data;}
// getData() ===================================================================


// setData() ===================================================================
@Override public void setData(long value){data = value;}
// setData() ===================================================================


// getFrequency() ==============================================================
@Override public int getFrequency(){return frequency;}
// getFrequency() ==============================================================


// incrementFrequency() ========================================================
@Override public void incrementFrequency(){frequency++;}
// incrementFrequency() ========================================================


// compareTo() =================================================================
@Override public int compareTo(TreeObjectInterface obj){
  if(obj==null)return 1;

  long targetData = obj.getData();
  if(data < targetData)return -1;
  if(data > targetData)return  1;
  return 0;
}
// compareTo() =================================================================


// convertToBinaryBlob() =======================================================
@Override public byte[] convertToBinaryBlob(){
  byte[] blob = new byte[12];
  for(int i=0;i<8;i++)blob[i  ] = (byte)((data     >>(i*8)) & 0xFF);
  for(int i=0;i<4;i++)blob[i+8] = (byte)((frequency>>(i*8)) & 0xFF);
  return blob;
}
// convertToBinaryBlob() =======================================================


// convertFromBinaryBlob() =====================================================
@Override public void convertFromBinaryBlob(byte[] blob){
  data      = 0;
  frequency = 0;
  for(int i=0;i<8;i++)data      |= ((long)blob[  i])<<(i*8);
  for(int i=0;i<4;i++)frequency |= ((int )blob[i+8])<<(i*8);
}
// convertFromBinaryBlob() =====================================================



} // class TreeObject_CML
