package Common.Impl;

import Common.*;

/** Chris's implementation of TreeObject.                                     */
public class TreeObject_CML implements TreeObjectInterface{



// STATE DATA ==================================================================
private long data;
private long frequency = 0;
// STATE DATA ==================================================================


// getData() ===================================================================
@Override public long getData(){return data;}
// getData() ===================================================================


// setData() ===================================================================
@Override public void setData(long value){data = value;}
// setData() ===================================================================


// getFrequency() ==============================================================
@Override public long getFrequency(){return frequency;}
// getFrequency() ==============================================================


// incrementFrequency() ========================================================
@Override public void incrementFrequency(){frequency++;}
// incrementFrequency() ========================================================


// compareTo() =================================================================
@Override public int compareTo(TreeObjectInterface obj){
  long targetData = obj.getData();
  if(data < targetData)return -1;
  if(data > targetData)return  1;
  return 0;
}
// compareTo() =================================================================



} // class TreeObject_CML
