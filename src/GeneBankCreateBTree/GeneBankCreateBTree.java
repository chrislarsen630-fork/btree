import Common.*;
import CreateBTree.*;

/** GeneBankCreateBTree program driver class. Carries out the intended
  * purpose of the program.                                                   */
public class GeneBankCreateBTree{



private final boolean DEBUG_DEBUGTESTS                       = true;
private final boolean DEBUG_DEBUGTESTS_GENEBANKFILE          = true;
private final String  DEBUG_DEBUGTESTS_GENEBANKFILE_FILENAME = "test3.gbk";
private final boolean DEBUG_DEBUGTESTS_TREEOBJECT            = true;
private final boolean DEBUG_DEBUGTESTS_BTREENODE             = true;


/** Program entry point. Creates an instance of the GeneBankCreateBTree class
  * and executes it. Acts as a safety net for any unhandled exceptions thrown
  * elsewhere within the program. Do not add any code to this -- it goes in
  * the execute() method.
  * @param args Command-line arguments                                        */
public static void main(String[] args){
  try{
    (new GeneBankCreateBTree()).execute(args);
  }catch(Throwable e){
    System.out.println();
    System.out.println("==FATAL ERROR==");
    if(e.getMessage()!=null){
      System.out.println("Error message: \""+e.getMessage()+"\"");
    }
  }
}


/** Instance entry point. Carries out the intended purpose of the program.
  * @param args Command-line arguments                                        */
private void execute(String[] args){
  if(DEBUG_DEBUGTESTS){
    doDebugTests();
    return;
  }
    
  if(!parseArguments(args)){
    displayUsage();
    return;
  }
  
  // TODO: implement program functionality
}


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


/** Parses the command-line arguments. Returns false if insufficient or
  * invalid arguments are provided.
  * @param args Command-line arguments
  * @return Flag indicating whether the arguments are valid (true=yes)        */
private boolean parseArguments(String[] args){
  
  // TODO: implement argument parser
  
  return false;
}


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
    node.setDegree(4);
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
    eckas.setDegree(4);
    eckas.convertFromBinaryBlob(blob);
    System.out.println(""+eckas.getNKeys());
    for(int i=0;i<3;i++)System.out.print(""+eckas.getKeyArray()[i].getData()+" ");
    System.out.println();
    for(int i=0;i<4;i++)System.out.print(""+eckas.getChildrenIDArray()[i]+" ");
    System.out.println();
    System.out.println();
  }
}



} // class GeneBankCreateBTree
