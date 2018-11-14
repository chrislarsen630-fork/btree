package Search;

import Search.Impl.*;

/** Class-to-interface allocator (Search). Creates a new instance of a class
  * which does the best job of implementing the desired interface. This allows
  * code to create instances of classes that are not yet implemented -- the
  * allocator supplies a do-nothing stub class. Later on when the class is
  * implemented, all that needs to be changed is a single line of code here
  * to return the proper implementation,                                      */
public class AllocateS{



// Search
public static QueryFileReaderInterface new_QueryFileReader(){return new QueryFileReader_Stub();}



} // class Interface
