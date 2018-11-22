package Common.Impl;

import java.io.*;
import Common.*;

/** Chris's implementation of BTreeFile. */
public class BTreeFile_CML implements BTreeFileInterface{



// CONSTANTS ===================================================================
private static final int MAGIC_NUMBER   = 0xDEC0AD3B;
private static final int FORMAT_VERSION = 0x20181121;
// CONSTANTS ===================================================================


// STATE DATA ==================================================================
private RandomAccessFile filePtr;

private int  headerSize    = 4096;
private int  nodeSize      = 0;
private int  btreeDegree   = 0;
private int  nodeCount     = 0;
private long rootID        = 0;
// STATE DATA ==================================================================


// createNewFile() =============================================================
@Override public BTreeNodeInterface createNewFile(
  String targetFile,int degree
)throws OmniException{
  btreeDegree = degree;

  // create file  
  try{
    filePtr = new RandomAccessFile(targetFile,"rwd");
  }catch(IOException e){
    throw new OmniException(OmniException.FILE_ACCESS,"Issue creating BTree file.");
  }

  // determine maximum node size
  BTreeNodeInterface dummyNode = AllocateC.new_BTreeNode();
  dummyNode.setDegree(degree);
  dummyNode.inflateToMaximumSize();
  nodeSize = dummyNode.convertToBinaryBlob().length;
  
  // write header padding and data
  try{
    byte[] headerPadding = new byte[headerSize];
    filePtr.write(headerPadding);
    writeHeader();
  }catch(IOException e){
    throw new OmniException(OmniException.FILE_WRITE_ERROR,"Unable to write to BTree file.");
  }

  // create root node
  rootID = allocateNode();
  return readNode(rootID);
}
// createNewFile() =============================================================


// loadFromFile() ==============================================================
@Override public BTreeNodeInterface loadFromFile(String sourceFile) throws OmniException{
  // read file  
  try{
    filePtr = new RandomAccessFile(sourceFile,"rwd");
  }catch(IOException e){
    throw new OmniException(OmniException.FILE_ACCESS,"Issue creating BTree file.");
  }
  
  readHeader();
  return readNode(rootID);
}
// loadFromFile() ==============================================================


// readNode() ==================================================================
@Override public BTreeNodeInterface readNode(long nodeID) throws OmniException{
  try{
    byte[] blob = new byte[nodeSize];
    filePtr.seek(headerSize + (nodeID * nodeSize));
    filePtr.read(blob);
    BTreeNodeInterface node = AllocateC.new_BTreeNode();
    node.convertFromBinaryBlob(blob);
    return node;
  }catch(IOException e){
    throw new OmniException(
      OmniException.FILE_READ_ERROR,"Unable to read node from BTree file."
    );
  }
}
// readNode() ==================================================================


// allocateNode() ==============================================================
@Override public long allocateNode() throws OmniException{
  try{
    byte[] nodePadding = new byte[nodeSize];
    BTreeNodeInterface node = AllocateC.new_BTreeNode();
    node.setID    (nodeCount  );
    node.setDegree(btreeDegree);
    filePtr.seek(headerSize + (nodeCount * nodeSize));
    filePtr.write(nodePadding);
    filePtr.seek(headerSize + (nodeCount * nodeSize));
    filePtr.write(node.convertToBinaryBlob());
  }catch(IOException e){
    throw new OmniException(
      OmniException.FILE_WRITE_ERROR,"Unable to create node in BTree file."
    );
  }

  long ret = nodeCount;
  nodeCount++;
  writeHeader();
  return ret;
}
// allocateNode() ==============================================================


// writeNode() =================================================================
@Override public void writeNode(BTreeNodeInterface node) throws OmniException{
  try{
    filePtr.seek(headerSize + (node.getID() * nodeSize));
    filePtr.write(node.convertToBinaryBlob());
  }catch(IOException e){
    throw new OmniException(
      OmniException.FILE_WRITE_ERROR,"Unable to create node in BTree file."
    );
  }
}
// writeNode() =================================================================


// writeHeader() ===============================================================
private void writeHeader() throws OmniException{
  try{
    filePtr.seek(0);

    filePtr.writeInt (MAGIC_NUMBER  );
    filePtr.writeInt (FORMAT_VERSION);
    filePtr.writeInt (headerSize    );
    filePtr.writeInt (nodeSize      );
    filePtr.writeInt (btreeDegree   );
    filePtr.writeInt (nodeCount     );
    filePtr.writeLong(rootID        );

  }catch(IOException e){
    throw new OmniException(
      OmniException.FILE_WRITE_ERROR,"Unable to write to BTree file."
    );
  }
}
// writeHeader() ===============================================================


// readHeader() ================================================================
private void readHeader() throws OmniException{
  try{
    filePtr.seek(0);

    if(filePtr.readInt()!=MAGIC_NUMBER){
      throw new OmniException(OmniException.FILE_READ_ERROR,"Malformed BTree file.");
    }
    
    if(filePtr.readInt()>FORMAT_VERSION){
      throw new OmniException(OmniException.FILE_READ_ERROR,"Incompatible BTree version.");  
    }
    
    headerSize  = filePtr.readInt();
    nodeSize    = filePtr.readInt();
    btreeDegree = filePtr.readInt();
    nodeCount   = filePtr.readInt();
    rootID      = filePtr.readInt();

  }catch(IOException e){
    throw new OmniException(
      OmniException.FILE_WRITE_ERROR,"Unable to write to BTree file."
    );
  }
}
// readHeader() ================================================================



} // class BTreeFile_Stub
