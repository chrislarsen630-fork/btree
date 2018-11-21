package CreateBTree.Impl;

import CreateBTree.GeneBankFileInterface;
import Common.*;

/** Stub implementation of GeneBankFile. */
public class GeneBankFile_Stub implements GeneBankFileInterface{



@Override public void loadFromFile(String file, int windowSize) throws OmniException{}

@Override public boolean isSubsequenceDone(){return false;}

@Override public long readData() throws OmniException{return 0;}

@Override public boolean nextSubsequence() throws OmniException{return false;}



} // class GeneBankFile_Stub
