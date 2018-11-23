package Common.Impl;

import java.io.*;
import Common.*;

/** Chris's implementation of BTreeFile. */
public class BTreeFile_CML implements BTreeFileInterface{



// CONSTANTS ===================================================================
private static final int MAGIC_NUMBER   = 0x3BADC0DE; // Java is big-endian
private static final int FORMAT_VERSION = 0x20181121;
// CONSTANTS ===================================================================


// STATE DATA ==================================================================
private RandomAccessFile filePtr;

private int headerSize  = 4096;
private int nodeSize    = 0;
private int btreeDegree = 0;
private int nodeCount   = 0;
private int rootID      = 0;
// STATE DATA ==================================================================


// createNewFile() =============================================================
@Override public BTreeNodeInterface createNewFile(String targetFile,int degree)
throws OmniException{
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
  BTreeNodeInterface ret = readNode(rootID);
  ret.setLeaf(true);
  writeNode(ret);
  return ret;
}
// createNewFile() =============================================================


// loadFromFile() ==============================================================
@Override public BTreeNodeInterface loadFromFile(String sourceFile) throws OmniException{
  try{
    filePtr = new RandomAccessFile(sourceFile,"rwd");
    readHeader();
    return readNode(rootID);
  }catch(IOException e){
    throw new OmniException(OmniException.FILE_ACCESS,"Issue creating BTree file.");
  }
}
// loadFromFile() ==============================================================


// readNode() ==================================================================
@Override public BTreeNodeInterface readNode(int nodeID) throws OmniException{
  if( (nodeID<1) || (nodeID>nodeCount) ){
    throw new RuntimeException("Invalid node ID in BTree read.");
  }

  try{
    byte[] blob = new byte[nodeSize];
    filePtr.seek(headerSize + ((nodeID-1) * nodeSize));
    filePtr.read(blob);
    BTreeNodeInterface node = AllocateC.new_BTreeNode();
    node.setDegree(btreeDegree);
    node.setID(nodeID);
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
@Override public int allocateNode() throws OmniException{
  try{
    // calculate file position
    int pos = headerSize + (nodeCount * nodeSize);

    // write padding
    filePtr.seek(pos);
    filePtr.write(new byte[nodeSize]);

    // write node
    BTreeNodeInterface node = AllocateC.new_BTreeNode();
    node.setID    (nodeCount+1);
    node.setDegree(btreeDegree);
    filePtr.seek(pos);
    filePtr.write(node.convertToBinaryBlob());

  }catch(IOException e){
    throw new OmniException(
      OmniException.FILE_WRITE_ERROR,"Unable to create node in BTree file."
    );
  }

  nodeCount++;
  writeHeader();
  return nodeCount;
}
// allocateNode() ==============================================================


// writeNode() =================================================================
@Override public void writeNode(BTreeNodeInterface node) throws OmniException{
  long id = node.getID();
  if( (id<1) || (id>nodeCount) ){
    throw new RuntimeException("Invalid node ID in BTree write.");
  }
  
  try{
    filePtr.seek(headerSize + ((node.getID()-1) * nodeSize));
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


// setRootNode() ===============================================================
@Override public void setRootNode(int nodeID){
  if( (nodeID<1) || (nodeID>nodeCount) ){
    throw new RuntimeException("Invalid node ID set for BTree root node.");
  }
  rootID = nodeID;
  try{writeHeader();}catch(OmniException e){}
}
// setRootNode() ===============================================================



} // class BTreeFile_CML
