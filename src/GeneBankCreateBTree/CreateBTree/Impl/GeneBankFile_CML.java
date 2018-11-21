package CreateBTree.Impl;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import CreateBTree.GeneBankFileInterface;
import Common.*;

/** Chris's implementation of GeneBankFile. */
public class GeneBankFile_CML implements GeneBankFileInterface{



// STATE DATA ==================================================================
private Scanner  parser;

private String[] buffer;
private String   mergedBuffer;
private int      mergedBufferIndex = 0;

private int dataWindowSize = 0;
// STATE DATA ==================================================================


// loadFromFile() ==============================================================
@Override
public void loadFromFile(String file,int windowSize) throws OmniException{
  dataWindowSize = windowSize;

  try{
    parser = new Scanner(createFlatFile(file));
    
    buffer = new String[2];
    buffer[0] = (parser.hasNext() ? parser.next() : null);
    buffer[1] = (parser.hasNext() ? parser.next() : null);
    mergedBuffer = (
      (buffer[0]!=null ? buffer[0] : "") + (buffer[1]!=null ? buffer[1] : "")
    );
  }catch(FileNotFoundException e){
    throw new OmniException(
      OmniException.FILE_ACCESS,
      "Unable to access flattened file -- file permissions issue?"
    );
  }
}
// loadFromFile() ==============================================================


// isSubsequenceDone() =========================================================
@Override public boolean isSubsequenceDone(){
  if(mergedBufferIndex+dataWindowSize>mergedBuffer.length())return true;
  for(int i=0;i<dataWindowSize;i++){
    if(mergedBuffer.charAt(mergedBufferIndex+i)=='N')return true;
  }
  return false;
}
// isSubsequenceDone() =========================================================


// readData() ==================================================================
@Override public long readData() throws OmniException{
  long ret = 0;

  try{
    for(int i=0;i<dataWindowSize;i++){
      char c  = mergedBuffer.charAt(mergedBufferIndex+i);
      int val = 0;
      switch(c){
        case 'A': val = 0; break;
        case 'C': val = 1; break;
        case 'G': val = 2; break;
        case 'T': val = 3; break;
        default: throw new OmniException(
          OmniException.FILE_READ_ERROR,"Read malformed data in source file."
        );
      }
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
@Override public boolean nextSubsequence() throws OmniException{
 if(!isSubsequenceDone())throw new OmniException();
  try{
    while(mergedBuffer.charAt(mergedBufferIndex)!='N')incrementBufferIndex();
    while(mergedBuffer.charAt(mergedBufferIndex)=='N')incrementBufferIndex();
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
              outData.write(originReader.next().toUpperCase());
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



} // class GeneBankFile_CML
