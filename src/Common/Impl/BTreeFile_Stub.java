package Common.Impl;

import Common.*;

/** Stub implementation of BTreeFile. */
public class BTreeFile_Stub implements BTreeFileInterface{



@Override public BTreeNodeInterface createNewFile(
  String targetFile,int degree,int sequenceLength
) throws OmniException{return null;}

@Override public BTreeNodeInterface loadFromFile(String sourceFile)
throws OmniException{return null;}

@Override public int getSequenceLength(){return 0;}

@Override public BTreeNodeInterface readNode(int nodeID)
throws OmniException{return null;}

@Override public BTreeNodeInterface allocateNode()
throws OmniException{return null;}

@Override public void writeNode(BTreeNodeInterface node) throws OmniException{}

@Override public void setRootNode(int nodeID){}

@Override public void close(){}



} // class BTreeFile_Stub
