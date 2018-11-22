package Common.Impl;

import Common.*;

/** Stub implementation of BTreeFile. */
public class BTreeFile_Stub implements BTreeFileInterface{



@Override public BTreeNodeInterface createNewFile(String targetFile,int degree) throws OmniException{return null;}

@Override public BTreeNodeInterface loadFromFile(String sourceFile) throws OmniException{return null;}

@Override public BTreeNodeInterface readNode(long nodeID) throws OmniException{return null;}

@Override public long allocateNode() throws OmniException{return 0;}

@Override public void writeNode(BTreeNodeInterface node) throws OmniException{}



} // class BTreeFile_Stub
