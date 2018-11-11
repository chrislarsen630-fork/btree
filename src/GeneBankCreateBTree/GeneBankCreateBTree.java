/** GeneBankCreateBTree program driver class. Carries out the intended
  * purpose of the program.                                                   */
public class GeneBankCreateBTree{



/** Program entry point. Creates an instance of the GeneBankCreateBTree class
  * and executes it. Acts as a safety net for any unhandled exceptions thrown
  * elsewhere within the program. Do not add any code to this -- it goes in
  * the execute() method.
  * @param args Command-line arguments                                        */
public static void main(String[] args){
  try{
    (new GeneBankCreateBTree()).execute(args);
  }catch(Throwable e){
    System.out.println("==FATAL ERROR==");
    if(e.getMessage()!=null){
      System.out.println("Error message: \""+e.getMessage()+"\"");
    }
  }
}


/** Instance entry point. Carries out the intended purpose of the program.
  * @param args Command-line arguments                                        */
private void execute(String[] args){
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



} // class GeneBankCreateBTree
