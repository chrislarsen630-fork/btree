import Common.*;

/** GeneBankSearch program driver class. Carries out the intended
  * purpose of the program.
  * @author Christopher M. Larsen (chrislarsen630@u.boisestate.edu)
  * @author Landon Lemieux (landonlemieux@u.boisestate.edu)
  * @author Dylan Leman    (dylanleman@u.boisestate.edu)
  * @version 20181202                                                         */
public class GeneBankSearch{



// STATE DATA ==================================================================
private int    arg_useCache;
private String arg_btreeFile;
private String arg_queryFile;
private int    arg_cacheSize  = 0;
private int    arg_debugLevel = 0;
// STATE DATA ==================================================================


// main() ======================================================================
/** Program entry point. Creates an instance of the GeneBankSearch class
  * and executes it. Acts as a safety net for any unhandled exceptions thrown
  * elsewhere within the program. Do not add any code to this -- it goes in
  * the execute() method.
  * @param args Command-line arguments                                        */
public static void main(String[] args){
  try{
    (new GeneBankSearch()).execute(args);
  }catch(Throwable e){
    System.out.println("==FATAL ERROR==");
    if(e.getMessage()!=null){
      System.out.println("Error message: \""+e.getMessage()+"\"");
    }
  }
}
// main() ======================================================================


// execute() ===================================================================
/** Instance entry point. Carries out the intended purpose of the program.
  * @param args Command-line arguments                                        */
private void execute(String[] args){
  if(!parseArguments(args)){
    System.out.println();
    displayUsage();
    System.out.println();
    return;
  }
  try {
    BTree tree = new BTree();
    tree.loadFromFile(arg_btreeFile);
    queryFileRead(tree); //inside of this method, tree objects are created and inserted into tree
  }catch(OmniException e){
    System.out.println("AN ERROR HAS OCCURRED. ABORTING.");
  }
}
// execute() ===================================================================


// queryFileRead() =============================================================
private void queryFileRead(BTree tree){
  try{
    java.io.File queryFile = new java.io.File(arg_queryFile);
    java.util.Scanner scan = new java.util.Scanner(queryFile);
    BTreeNode node;
    while (scan.hasNextLine()) {
      String s = scan.nextLine();
      if(s.length()>0){
        TreeObject treeObj = createTreeObject(s);
        node = tree.searchKey(treeObj);
        if(node != null){
          int freq = node.searchKey(treeObj).getFrequency();
          System.out.println(s.toLowerCase() + ": " + freq);
        }
      }
    }
  }catch(java.io.FileNotFoundException | OmniException e){
    System.out.println(e.getMessage());
  }
}
// queryFileRead() =============================================================


// createTreeObject() ==========================================================
private TreeObject createTreeObject(String line){
  long data = 0;
  for(int i=0;i<line.length();i++){
    char c  = line.charAt(i);
    int val = 0;
    switch(c){
      case 'A': val = 0; break;
      case 'C': val = 1; break;
      case 'G': val = 2; break;
      case 'T': val = 3; break;
    }
    data = (data<<2) | val;
  }
  return new TreeObject(data);
}
// createTreeObject() ==========================================================


// displayUsage() ==============================================================
/** Prints the program usage to the standard output. Describes what the
    program does, lists expected parameters, and describes the purpose
    of each parameter.                                                        */
private void displayUsage(){
  System.out.println("Usage: java GeneBankSearch <use_cache> <btree_file> <query_file> [<cache_size>] [<debug_level>]");
  System.out.println("Searches a BTree file for sequences of a given length." );
  System.out.println(                                                         );
  System.out.println("PARAMETERS"                                             );
  System.out.println("  use_cache    Use disk cache (0=no, 1=yes)"            );
  System.out.println("  btree_file   File path to BTree file"                 );
  System.out.println("  query_file   File path to DNA sequence file"          );
  System.out.println("  cache_size   Size of the cache (in entries)"          );
  System.out.println("  debug_level  Create dump file (0=no, 1=yes)"          );
}
// displayUsage() ==============================================================


// parseArguments() ============================================================
/** Parses the command-line arguments. Returns false if insufficient or
  * invalid arguments are provided.
  * @param args Command-line arguments
  * @return Flag indicating whether the arguments are valid (true=yes)        */
private boolean parseArguments(String[] args){
  try{
    if( (args.length<3) || (args.length>5) )return false;

    // parse first argument (use cache)
    arg_useCache = new Integer(args[0]);
    if( (arg_useCache<0) || (arg_useCache>1) )return false;

    // parse second argument (BTree file)
    arg_btreeFile = args[1];

    // parse third argument (query file)
    arg_queryFile = args[2];

    // parse fourth argument (cache size)
    if(args.length>3)arg_cacheSize = new Integer(args[3]);
    if(arg_cacheSize<0)return false;
    if(arg_useCache==0)arg_cacheSize = 0;

    // parse fifth argument (debug level)
    if(args.length>4)arg_debugLevel = new Integer(args[4]);
    if( (arg_debugLevel<0) || (arg_debugLevel>1) )return false;

    return true;
  }catch(Exception e){return false;}
}
// parseArguments() ============================================================



} // class GeneBankSearch
