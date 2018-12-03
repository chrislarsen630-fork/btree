import java.util.*;
import java.io.*;
import java.nio.file.*;
import Common.*;

/** Parses a GeneBank file and converts the DNA sequences contained therein
  * into a packed binary format.                                              */
public class GeneBankFile{



// STATE DATA ==================================================================
private Scanner parser;

private String[] buffer;
private String   mergedBuffer;
private int      mergedBufferIndex;

private int dataWindowSize;

// look up tables
final private boolean[] END_CHARACTER = new boolean[256];
final private int[]     BASE_TO_VAL   = new int    [256];
// STATE DATA ==================================================================


// GeneBankFile() ==============================================================
public GeneBankFile(){
  END_CHARACTER['N'] = END_CHARACTER['n'] = true;

  Arrays.fill(BASE_TO_VAL,4);
  BASE_TO_VAL['A'] = BASE_TO_VAL['a'] = 0;
  BASE_TO_VAL['C'] = BASE_TO_VAL['c'] = 1;
  BASE_TO_VAL['G'] = BASE_TO_VAL['g'] = 2;
  BASE_TO_VAL['T'] = BASE_TO_VAL['t'] = 3;
}
// GeneBankFile() ==============================================================


// loadFromFile() ==============================================================
/** Associates the GeneBankFileInterface object with a file and sets the file
  * parser to the beginning of the DNA sequence.
  * @param file GeneBank file to load.                                        
  * @param windowSize Number of DNA bases to load during readData().
  * @throws Common.OmniException on file access or read error.                */
public void loadFromFile(String file,int windowSize) throws OmniException{
  dataWindowSize = windowSize;

  try{
    parser = new Scanner(createFlatFile(file));
    
    buffer = new String[2];
    buffer[0] = (parser.hasNext() ? parser.next() : "");
    buffer[1] = (parser.hasNext() ? parser.next() : "");
    mergedBuffer = buffer[0] + buffer[1];
  }catch(FileNotFoundException e){
    throw new OmniException(
      OmniException.FILE_ACCESS,
      "Unable to access flattened file -- file permissions issue?"
    );
  }
}
// loadFromFile() ==============================================================


// isSubsequenceDone() =========================================================
/** Indicates whether the file parser is at the end of the current DNA
  * subsequence.
  * @return True if the subsequence is done.                                  */
public boolean isSubsequenceDone(){
  if(mergedBufferIndex+dataWindowSize>mergedBuffer.length())return true;
  for(int i=0;i<dataWindowSize;i++){
    if(END_CHARACTER[mergedBuffer.charAt(mergedBufferIndex+i)])return true;
  }
  return false;
}
// isSubsequenceDone() =========================================================


// readData() ==================================================================
/** Reads the data at the file parser and packs it into a binary
  * representation. Each two bits represents a DNA base (0b00=A, 0b01=C,
  * 0b10=G, 0b11=T). The number of DNA bases read is determined by the
  * windowSize set in loadFromFile(). The bases are packed so that the base
  * that was read last is contained in the two least significant bits; for
  * instance, the sequence ACGT is represented as 0b00011011.
  * @return Packed binary representation of data.
  * @throws Common.OmniException on file access or read error.                */
public long readData() throws OmniException{
  long ret = 0;

  try{
    for(int i=0;i<dataWindowSize;i++){
      char c  = mergedBuffer.charAt(mergedBufferIndex+i);
      int val = BASE_TO_VAL[c];
      if(val==4)throw new OmniException(
        OmniException.FILE_READ_ERROR,"Read malformed data in source file."
      );
      ret = (ret<<2) | val;
    }
    incrementBufferIndex();
  }catch(Exception e){
    throw new OmniException(
      OmniException.FILE_READ_ERROR,"Unexpected end of source file."
    );
  }
  
  return ret;
}
// readData() ==================================================================


// nextSubsequence() ===========================================================
/** Sets the file parser to the beginning of the next DNA subsequence.
  * Returns true if the next subsequence exists, otherwise returns false.
  * @return Whether a new subsequence has been loaded.
  * @throws Common.OmniException if there are no more subsequences.           */
public boolean nextSubsequence() throws OmniException{
  if(!isSubsequenceDone())throw new OmniException();
  try{
    while(!END_CHARACTER[mergedBuffer.charAt(mergedBufferIndex)])incrementBufferIndex();
    while( END_CHARACTER[mergedBuffer.charAt(mergedBufferIndex)])incrementBufferIndex();
    return (mergedBuffer.length()>0);
  }catch(Exception e){return false;}
}
// nextSubsequence() ===========================================================


// createFlatFile() ============================================================
private File createFlatFile(String sourceFile) throws OmniException{
  File flatFile;

  try(Scanner sourceReader = new Scanner(Paths.get(sourceFile))){
    // create temporary flattened file
    flatFile = File.createTempFile("CreateBTree-", ".tmp");
    flatFile.deleteOnExit();

    // parse the source file
    try(BufferedWriter outData = new BufferedWriter(new FileWriter(flatFile))){
      // for each sequence...
      while(sourceReader.hasNext()){
        // parse the LOCUS data for the sequence length
        String locusData = sourceReader.nextLine();
        for(;!locusData.toUpperCase().startsWith("LOCUS")&&sourceReader.hasNext();locusData = sourceReader.nextLine()){}
        if(!sourceReader.hasNext())break;
        int sequenceLength;
        try(Scanner locusReader = new Scanner(locusData)){
          locusReader.next();
          locusReader.next();
          sequenceLength = locusReader.nextInt();
        }
        int nLines = (sequenceLength/60) + (sequenceLength%60>0 ? 1 : 0);

        // search for the beginning of the sequence data
        String originData = sourceReader.nextLine();
        for(;!originData.toUpperCase().startsWith("ORIGIN")&&sourceReader.hasNext();originData = sourceReader.nextLine()){}
        if(!sourceReader.hasNext())throw new OmniException(OmniException.FILE_READ_ERROR,"Unexpected end of source file.");

        // parse the sequence data and write it to the flattened file
        originData = sourceReader.nextLine();
        for(int i=0;i<nLines;i++,originData = sourceReader.nextLine()){
          try(Scanner originReader = new Scanner(originData)){
            originReader.nextInt();
            while(originReader.hasNext()){
              outData.write(originReader.next());
            }
            outData.newLine();
          } // Scanner originReader
        } // for(nLines)

        // write a line of subsequence-break characters
        outData.write("NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
        outData.newLine();

      } // sourceReader.hasNext()
    } // BufferedWriter outData

  }catch(OmniException e){
    throw e;
  }catch(Exception e){ // Scanner SourceReader
    throw new OmniException(
      OmniException.FILE_UNSPECIFIED,
      "Issue creating flattened file."
    );
  } // Scanner sourceReader

  return flatFile;
}
// createFlatFile() ============================================================


// incrementBufferIndex() ======================================================
private void incrementBufferIndex(){
  mergedBufferIndex++;

  if(mergedBufferIndex>=buffer[0].length()){
    buffer[0] = buffer[1];
    buffer[1] = (parser.hasNext() ? parser.next() : null);
    mergedBuffer = (
      (buffer[0]!=null ? buffer[0] : "") + (buffer[1]!=null ? buffer[1] : "")
    );
    mergedBufferIndex = 0;
  }
}
// incrementBufferIndex() ======================================================



} // class GeneBankFile
