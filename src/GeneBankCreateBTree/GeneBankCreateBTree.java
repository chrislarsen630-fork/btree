import Common.*;
import CreateBTree.*;

/** GeneBankCreateBTree program driver class. Carries out the intended
  * purpose of the program.                                                   */
public class GeneBankCreateBTree{



// DEBUG SETTINGS ==============================================================
private final boolean DEBUG_DEBUGTESTS                       = false;
private final int     DEBUG_DEBUGTESTS_BTREEDEGREE           = 64;
private final int     DEBUG_DEBUGTESTS_SEQUENCELENGTH        = 6;
private final boolean DEBUG_DEBUGTESTS_GENEBANKFILE          = false;
private final String  DEBUG_DEBUGTESTS_GENEBANKFILE_FILENAME = "test3.gbk";
private final boolean DEBUG_DEBUGTESTS_TREEOBJECT            = false;
private final boolean DEBUG_DEBUGTESTS_BTREENODE             = false;
private final boolean DEBUG_DEBUGTESTS_BTREE                 = false;
private final boolean DEBUG_DEBUGTESTS_GENEBANKFILEPLUSBTREE = false;
// DEBUG SETTINGS ==============================================================


// STATE DATA ==================================================================
int    arg_useCache;
int    arg_btreeDegree;
String arg_gbkFile;
int    arg_sequenceLength;
int    arg_cacheSize = 0;
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
      System.out.println("Stack trace: ");
      e.printStackTrace();
    }
  }
  System.out.println();
}
// main() ======================================================================


// execute() ===================================================================
/** Instance entry point. Carries out the intended purpose of the program.
  * @param args Command-line arguments                                        */
private void execute(String[] args){
  if(DEBUG_DEBUGTESTS){
    doDebugTests();
    return;
  }

  long performanceTimer = System.currentTimeMillis();
  
  if(!parseArguments(args)){
    displayUsage();
    return;
  }
  String btreeFileName     = arg_gbkFile+".btree."+arg_sequenceLength;
  String btreeDumpFileName = arg_gbkFile+".btree.dump."+arg_sequenceLength;
  if(arg_btreeDegree==0)arg_btreeDegree = getOptimalBTreeDegree(4096);

  System.out.println("+ + + GeneBankCreateBTree + + +"                        );
  System.out.println(                                                         );
  System.out.print("A binary BTree file named "+btreeFileName+" of degree "   );
  System.out.print(arg_btreeDegree+" and sequence length "+arg_sequenceLength );
  System.out.print(" will be created from the GBK file "+arg_gbkFile+"."      );
  if(arg_useCache==1){
    System.out.print(" A "+arg_cacheSize+" byte cache will be used"           );
  }else{
    System.out.print(" No cache will be used"                                 );
  }
  System.out.print(", and "+(arg_debugLevel==1?"a":"no")+" debug file"        );
  if(arg_debugLevel==1)System.out.print(" named "+btreeDumpFileName         );
  System.out.println(" will be created."                                      );
  System.out.println(                                                         );

  System.out.print("Parsing GBK file...");
  GeneBankFileInterface geneFile = AllocateB.new_GeneBankFile();
  try{
    geneFile.loadFromFile(arg_gbkFile,arg_sequenceLength);
  }catch(OmniException e){
    System.out.println("FAILED. Aborting.");
    return;
  }
  System.out.println("OK.");

  System.out.print("Initializing BTree...");
  BTreeInterface tree = AllocateC.new_BTree();
  try{
    tree.createNewFile(btreeFileName,arg_btreeDegree);
    tree.setCacheSize(arg_cacheSize);
  }catch(OmniException e){
    System.out.println("FAILED. Aborting.");
    return;
  }
  System.out.println("OK.");

  System.out.print("Populating BTree. Standby.");
  int count = 0;
  try{
    do{
      for(int i=0;!geneFile.isSubsequenceDone();i++){
        if((i%300==0)&&(i>0))System.out.print(".");
        long data = geneFile.readData();
        TreeObjectInterface key = AllocateC.new_TreeObject();
        key.setData(data);
        tree.insertKey(key);
        count++;
      }
    }while(geneFile.nextSubsequence());
  }catch(OmniException e){
    System.out.println();
    System.out.println("Encountered an error while parsing GBK file. Aborting,");
    return;
  }
  System.out.println();
  System.out.println("Successfully populated BTree.");
  
  if(arg_debugLevel==1){
    try{
      System.out.print("Writing debug dump file...");
      DebugFileDumperInterface dumper = AllocateB.new_DebugFileDumper();
      dumper.dumpBTreeToFile(tree,arg_gbkFile+".btree.dump."+arg_sequenceLength,arg_sequenceLength);
      System.out.println("OK.");
    }catch(OmniException e){System.out.println("FAILED.");}
  }
  
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
  System.out.println(                                                      );
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
  
  // Not the most elegant approach, but the most reliable.
  BTreeNodeInterface node = AllocateC.new_BTreeNode();
  for(int i=3;i<=pageSize;i++){
    node.setDegree(i);
    node.inflateToMaximumSize();
    byte[] nodeBlob = node.convertToBinaryBlob();
    if(nodeBlob.length>pageSize)break;
    lastGoodDegree = i;
  }
  
  return lastGoodDegree;
}
// getOptimalBTreeDegree() =====================================================


// doDebugTest() ===============================================================
/** Temporary test routines to check that the classes are functioning
  * correctly. Ideally these should be unit tests, but I'm lazy.
  * Delete prior to submission.                                               */
private void doDebugTests(){
  if(DEBUG_DEBUGTESTS_GENEBANKFILE){
    System.out.println("GENEBANKFILE_________________________________________");
    GeneBankFileInterface geneFile = AllocateB.new_GeneBankFile();
    try{
      geneFile.loadFromFile(DEBUG_DEBUGTESTS_GENEBANKFILE_FILENAME,1);
      do{
        for(int i=0;!geneFile.isSubsequenceDone();i++){
          if(i%60==0&&i>0)System.out.println();
          System.out.print(geneFile.readData()+" ");
        }
        System.out.println();
        System.out.println();
      }while(geneFile.nextSubsequence());
    }catch(Exception e){throw new RuntimeException(e.getMessage());}
  }

  if(DEBUG_DEBUGTESTS_TREEOBJECT){
    System.out.println("TREEOBJECT___________________________________________");
    TreeObjectInterface obj = AllocateC.new_TreeObject();
    obj.setData(0x12345678);
    byte[] blob = obj.convertToBinaryBlob();
    for(int i=0;i<blob.length;i++)System.out.print(String.format("0x%02X ",blob[i]));
    System.out.println();
    
    TreeObjectInterface obj2 = AllocateC.new_TreeObject();
    obj2.convertFromBinaryBlob(blob);
    System.out.println(String.format("0x%08X",obj2.getData()));
    System.out.println(obj2.getFrequency());
    
    System.out.println();
    System.out.println();
  }
  
  if(DEBUG_DEBUGTESTS_BTREENODE){
    System.out.println("BTREENODE____________________________________________");
    BTreeNodeInterface node = AllocateC.new_BTreeNode();
    node.setDegree(DEBUG_DEBUGTESTS_BTREEDEGREE);
    node.setNKeys(4);
    node.getKeyArray()[0].setData(11111111);
    node.getKeyArray()[1].setData(22222222);
    node.getKeyArray()[2].setData(33333333);
    node.getChildrenIDArray()[0] = 44444444;
    node.getChildrenIDArray()[1] = 55555555;
    node.getChildrenIDArray()[2] = 66666666;
    node.getChildrenIDArray()[3] = 77777777;
    System.out.println(""+node.getNKeys());
    for(int i=0;i<3;i++)System.out.print(""+node.getKeyArray()[i].getData()+" ");
    System.out.println();
    for(int i=0;i<4;i++)System.out.print(""+node.getChildrenIDArray()[i]+" ");
    System.out.println();
    System.out.println();
  
    byte[] blob = node.convertToBinaryBlob();

    BTreeNodeInterface eckas = AllocateC.new_BTreeNode();
    eckas.setDegree(DEBUG_DEBUGTESTS_BTREEDEGREE);
    eckas.convertFromBinaryBlob(blob);
    System.out.println(""+eckas.getNKeys());
    for(int i=0;i<3;i++)System.out.print(""+eckas.getKeyArray()[i].getData()+" ");
    System.out.println();
    for(int i=0;i<4;i++)System.out.print(""+eckas.getChildrenIDArray()[i]+" ");
    System.out.println();
    System.out.println();
  }
  
  if(DEBUG_DEBUGTESTS_BTREE){
    try{
      BTreeInterface tree = AllocateC.new_BTree();
      tree.createNewFile("TestFile.bin",DEBUG_DEBUGTESTS_BTREEDEGREE);
      char[] keyArray = {'a','j','i','b','c','h','g','d','e','f'};
      for(int i=0;i<keyArray.length;i++){
        TreeObjectInterface key = AllocateC.new_TreeObject();
        key.setData(keyArray[i]);
        tree.insertKey(key);
      }
      for(int i=0;i<keyArray.length;i++){
        TreeObjectInterface key = AllocateC.new_TreeObject();
        key.setData(keyArray[i]);
        BTreeNodeInterface n = tree.searchKey(key);
        System.out.print(n.getID()+" : ");
        for(int j=0,jS=n.getNKeys();j<jS;j++)System.out.print(""+(char)n.getKeyArray()[j].getData()+" ");
        System.out.println(n.isLeaf()?" leaf":" nonleaf");
      }
    }catch(OmniException e){System.out.println("THREW EXCEPTION");}
  }
  
  if(DEBUG_DEBUGTESTS_GENEBANKFILEPLUSBTREE){
    try{
      BTreeInterface tree = AllocateC.new_BTree();
      tree.createNewFile("TestFile.bin",DEBUG_DEBUGTESTS_BTREEDEGREE);
      
      GeneBankFileInterface geneFile = AllocateB.new_GeneBankFile();
      geneFile.loadFromFile(DEBUG_DEBUGTESTS_GENEBANKFILE_FILENAME,DEBUG_DEBUGTESTS_SEQUENCELENGTH);
      do{
        for(int i=0;!geneFile.isSubsequenceDone();i++){
          if(i%60==0&&i>0)System.out.println();
          long data = geneFile.readData();
          System.out.print(data+" ");
          TreeObjectInterface key = AllocateC.new_TreeObject();
          key.setData(data);
          tree.insertKey(key);          
        }
        System.out.println();
        System.out.println();
      }while(geneFile.nextSubsequence());
      DebugFileDumperInterface dump = AllocateB.new_DebugFileDumper();
      dump.dumpBTreeToFile(tree,"blah.txt",DEBUG_DEBUGTESTS_SEQUENCELENGTH);
    }catch(OmniException e){System.out.println("THREW EXCEPTION");}
  }
}
// doDebugTest() ===============================================================



} // class GeneBankCreateBTree
