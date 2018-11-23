package Common;

import Common.Impl.*;

/** Class-to-interface allocator (Common). Creates a new instance of a class
  * which does the best job of implementing the desired interface. This allows
  * code to create instances of classes that are not yet implemented -- the
  * allocator supplies a do-nothing stub class. Later on when the class is
  * implemented, all that needs to be changed is a single line of code here
  * to return the proper implementation,                                      */
public class AllocateC{



// Common
public static BTreeCacheInterface new_BTreeCache(){return new BTreeCache_Stub();}
public static BTreeFileInterface  new_BTreeFile (){return new BTreeFile_CML  ();}
public static BTreeInterface      new_BTree     (){return new BTree_Stub     ();}
public static BTreeNodeInterface  new_BTreeNode (){return new BTreeNode_CML  ();}
public static TreeObjectInterface new_TreeObject(){return new TreeObject_CML ();}



} // class Interface
