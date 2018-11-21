package CreateBTree;

import CreateBTree.Impl.*;

/** Class-to-interface allocator (CreateBTree). Creates a new instance of a
  * class which does the best job of implementing the desired interface. This
  * allows code to create instances of classes that are not yet implemented
  * -- the allocator supplies a do-nothing stub class. Later on when the
  * class is implemented, all that needs to be changed is a single line of
  * code here to return the proper implementation,                                      */
public class AllocateB{



// CreateBTree
public static DebugFileDumpInterface new_DebugFileDump(){return new DebugFileDump_Stub();}
public static GeneBankFileInterface  new_GeneBankFile (){return new GeneBankFile_CML  ();}



} // class Interface
