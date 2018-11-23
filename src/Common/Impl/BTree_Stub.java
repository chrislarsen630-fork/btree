package Common.Impl;

import Common.*;

/** Stub implementation of BTree. */
public class BTree_Stub implements BTreeInterface{



@Override public void setBTreeFileInterface(BTreeFileInterface target){}

@Override public void setBTreeCacheInterface(BTreeCacheInterface target){}

@Override public void createNewFile(String targetFile,int degree)
throws OmniException{}

@Override public void loadFromFile(String sourceFile) throws OmniException{}

@Override public BTreeNodeInterface searchKey(TreeObjectInterface key)
throws OmniException{return null;}

@Override public void insertKey(TreeObjectInterface key) throws OmniException{}



} // class BTree_Stub
