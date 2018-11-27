package Common.Impl;

import Common.*;
import java.util.List;

/** Stub implementation of BTreeNode. */
public class BTreeNode_Stub implements BTreeNodeInterface{



@Override public void setDegree(int degree){}

@Override public int getID(){return 0;}

@Override public void setID(int id){}

@Override public boolean isLeaf(){return false;}

@Override public void setLeaf(boolean value){}

@Override public int getNKeys(){return 0;}

@Override public void setNKeys(int value){}

@Override public TreeObjectInterface[] getKeyArray(){return null;}

@Override public int[] getChildrenIDArray(){return null;}

@Override public TreeObjectInterface searchKey(TreeObjectInterface key){return null;}

@Override public byte[] convertToBinaryBlob(){return null;}

@Override public void convertFromBinaryBlob(byte[] blob){}

@Override public void inflateToMaximumSize(){}



} // class BTreeNode_Stub
