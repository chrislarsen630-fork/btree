package Common.Impl;

import Common.*;
import java.util.List;

/** Stub implementation of BTreeNode. */
public class BTreeNode_Stub implements BTreeNodeInterface{



@Override public long getID(){return 0;}

@Override public void setID(long id){}

@Override public boolean isLeaf(){return false;}

@Override public List<TreeObjectInterface> getKeys(){return null;}

@Override public List<Long> getChildrenIDs(){return null;}

@Override public int compareTo(Object t){return 0;}



} // class BTreeNode_Stub
