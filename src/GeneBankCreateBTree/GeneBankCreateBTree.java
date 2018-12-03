import Common.*;

/** GeneBankCreateBTree program driver class. Carries out the intended
  * purpose of the program.                                                   */
public class GeneBankCreateBTree{



// STATE DATA ==================================================================
int    arg_useCache;
int    arg_btreeDegree;
String arg_gbkFile;
int    arg_sequenceLength;
int    arg_cacheSize  = 0;
int    arg_debugLevel = 0;
// STATE DATA ==================================================================


// main() ======================================================================
/** Program entry point. Creates an instance of the GeneBankCreateBTree class
  * and executes it. Acts as a safety net for any unhandled exceptions thrown
  * elsewhere within the program. Do not add any code to this -- it goes in
  * the execute() method.
  * @param args Command-line arguments                                        */
public static void main(String[] args){
  System.out.println();
  try{
    (new GeneBankCreateBTree()).execute(args);
  }catch(Throwable e){
    System.out.println();
    System.out.println("==FATAL ERROR==");
    if(e.getMessage()!=null){
      System.out.println("Error message: \""+e.getMessage()+"\"");
    }
  }
  System.out.println();
}
// main() ======================================================================


// execute() ===================================================================
/** Instance entry point. Carries out the intended purpose of the program.
  * @param args Command-line arguments                                        */
private void execute(String[] args){
  long performanceTimer = System.currentTimeMillis();
  
  // parse arguments
  if(!parseArguments(args)){
    displayUsage();
    return;
  }
  if(arg_btreeDegree==0)arg_btreeDegree = getOptimalBTreeDegree(4096);
  String btreeFileName     = arg_gbkFile+".btree.data."+arg_sequenceLength+"."+arg_btreeDegree;
  String btreeDumpFileName = arg_gbkFile+".btree.dump."+arg_sequenceLength;

  // display output info
  System.out.println("+ + + GeneBankCreateBTree + + +"                        );
  System.out.println(                                                         );
  System.out.print("A binary BTree file named "+btreeFileName+" of degree "   );
  System.out.print(arg_btreeDegree+" and sequence length "+arg_sequenceLength );
  System.out.print(" will be created from the GBK file "+arg_gbkFile+"."      );
  if(arg_useCache==1){
    System.out.print(" A "+arg_cacheSize+" entry cache will be used"           );
  }else{
    System.out.print(" No cache will be used"                                 );
  }
  System.out.print(", and "+(arg_debugLevel==1?"a":"no")+" debug file"        );
  if(arg_debugLevel==1)System.out.print(" named "+btreeDumpFileName         );
  System.out.println(" will be created."                                      );
  System.out.println(                                                         );

  // parse GBK file
  System.out.print("Parsing GBK file...");
  GeneBankFile geneFile = new GeneBankFile();
  try{
    geneFile.loadFromFile(arg_gbkFile,arg_sequenceLength);
  }catch(OmniException e){
    System.out.println("FAILED. Aborting.");
    return;
  }
  System.out.println("OK.");

  // initialize BTree
  System.out.print("Initializing BTree...");
  BTree tree = new BTree();
  try{
    tree.createNewFile(btreeFileName,arg_btreeDegree,arg_sequenceLength);
    tree.setCacheCapacity(arg_cacheSize);
  }catch(OmniException e){
    System.out.println("FAILED. Aborting.");
    return;
  }
  System.out.println("OK.");

  // populate BTree
  System.out.print("Populating BTree. Standby.");
  try{
    do{
      for(int i=0;!geneFile.isSubsequenceDone();i++){
        if((i%32768==0)&&(i>0))System.out.print(".");
        long data = geneFile.readData();
        tree.insertKey(new TreeObject(data));
      }
    }while(geneFile.nextSubsequence());
  }catch(OmniException e){
    System.out.println();
    System.out.println("Encountered an error. Aborting.");
    return;
  }
  System.out.println();
  System.out.println("Successfully populated BTree.");
  
  // write debug file
  if(arg_debugLevel==1){
    try{
      System.out.print("Writing debug dump file...");
      DebugFileDumper dumper = new DebugFileDumper();
      dumper.dumpBTreeToFile(tree,arg_gbkFile+".btree.dump."+arg_sequenceLength,arg_sequenceLength);
      System.out.println("OK.");
    }catch(OmniException e){System.out.println("FAILED.");}
  }
  
  // cleanup
  tree.dealloc();

  // display runtime
  long runtime = (System.currentTimeMillis() - performanceTimer) / 1000;
  System.out.println();
  System.out.println("Program complete in "+runtime+" seconds.");
}
// execute() ===================================================================


// displayUsage() ==============================================================
/** Prints the program usage to the standard output. Describes what the
    program does, lists expected parameters, and describes the purpose
    of each parameter.                                                        */
private void displayUsage(){
  System.out.println("Usage: java GeneBankCreateBTree <use_cache> <degree> <gbk_file> <sequence_length> [<cache_size>] [<debug_level>]");
  System.out.println("Creates a BTree from a given GeneBank file."         );
  System.out.println(                                                      );
  System.out.println("PARAMETERS"                                          );
  System.out.println("  use_cache        Use disk cache (0=no, 1=yes)"     );
  System.out.println("  degree           Degree of the BTree (0=auto)"     );
  System.out.println("  gbk_file         File path to GeneBank file"       );
  System.out.println("  sequence_length  Length of DNA sequences (1 to 31)");
  System.out.println("  cache_size       Size of the cache (in entries)"   );
  System.out.println("  debug_level      Create dump file (0=no, 1=yes)"   );
}
// displayUsage() ==============================================================


// parseArguments() ============================================================
/** Parses the command-line arguments. Returns false if insufficient or
  * invalid arguments are provided.
  * @param args Command-line arguments
  * @return Flag indicating whether the arguments are valid (true=yes)        */
private boolean parseArguments(String[] args){
  try{
    if( (args.length<4) || (args.length>6) )return false;
  
    // parse first argument (use cache)
    arg_useCache = new Integer(args[0]);
    if( (arg_useCache<0) || (arg_useCache>1) )return false;

    // parse second argument (BTree degree)
    arg_btreeDegree = new Integer(args[1]);
    if( (arg_btreeDegree==1) || (arg_btreeDegree<0) )return false;
    
    // parse third argument (GBK file)
    arg_gbkFile = args[2];

    // parse fourth argument (sequence length)
    arg_sequenceLength = new Integer(args[3]);
    if( (arg_sequenceLength<1) || (arg_sequenceLength>31) )return false;
    
    // parse fifth argument (cache size)
    if(args.length>4)arg_cacheSize = new Integer(args[4]);
    if(arg_cacheSize<0)return false;
    if(arg_useCache==0)arg_cacheSize = 0;

    // parse sixth argument (debug level)
    if(args.length>5)arg_debugLevel = new Integer(args[5]);
    if( (arg_debugLevel<0) || (arg_debugLevel>1) )return false;

    return true;
  }catch(Exception e){return false;}
}
// parseArguments() ============================================================


// getOptimalBTreeDegree() =====================================================
/** Calculates the optimal BTree degree, with the goal of finding a node size
  *  that fits in a single disk page with minimal waste.
  * @param pageSize Disk page size, in bytes.
  * @return Optimal BTree degree.                                             */
private int getOptimalBTreeDegree(int pageSize){
  int lastGoodDegree = 2;

  // Not the most elegant approach, but reliable.
  for(int i=3;i<=pageSize;i++){
    BTreeNode node = new BTreeNode(i);
    byte[] nodeBlob = node.convertToBinaryBlob();
    if(nodeBlob.length>pageSize)break;
    lastGoodDegree = i;
  }

  return lastGoodDegree;
}
// getOptimalBTreeDegree() =====================================================



} // class GeneBankCreateBTree
