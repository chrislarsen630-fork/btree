package Common.Impl;

import java.io.*;
import Common.*;

/** Chris's implementation of BTreeFile. */
public class BTreeFile_CML implements BTreeFileInterface{



// CONSTANTS ===================================================================
private static final int MAGIC_NUMBER   = 0x3BADC0DE; // Java is big-endian
private static final int FORMAT_VERSION = 0x20181125;
// CONSTANTS ===================================================================


// STATE DATA ==================================================================
private RandomAccessFile filePtr;

private int headerSize  = 4096;
private int btreeDegree = 0;
private int dnaLength   = 0;
private int nodeSize    = 0;
private int nodePad     = 0;
private int nodeTotal   = 0;
private int nodeCount   = 0;
private int rootID      = 0;
// STATE DATA ==================================================================


// createNewFile() =============================================================
@Override public BTreeNodeInterface createNewFile(
  String targetFile,int degree,int sequenceLength)
throws OmniException{
  btreeDegree = degree;
  dnaLength   = sequenceLength;

  // create file  
  try{
    filePtr = new RandomAccessFile(targetFile,"rw");
  }catch(IOException e){
    throw new OmniException(OmniException.FILE_ACCESS,"Issue creating BTree file.");
  }

  // determine maximum node size
  BTreeNodeInterface dummyNode = AllocateC.new_BTreeNode();
  dummyNode.setDegree(degree);
  dummyNode.inflateToMaximumSize();
  nodeSize = dummyNode.convertToBinaryBlob().length;
  if( (nodeSize<4096) && (4096-nodeSize<64)){
    nodePad = 4096 - nodeSize;
  }
  nodeTotal = nodeSize + nodePad;

  // write header padding and data
  try{
    byte[] headerPadding = new byte[headerSize];
    filePtr.write(headerPadding);
    writeHeader();
  }catch(IOException e){
    throw new OmniException(OmniException.FILE_WRITE_ERROR,"Unable to write to BTree file.");
  }

  // create root node
  rootID = allocateNode().getID();
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


// getSequenceLength() =========================================================
@Override public int getSequenceLength(){return dnaLength;}
// getSequenceLength() =========================================================


// readNode() ==================================================================
@Override public BTreeNodeInterface readNode(int nodeID) throws OmniException{
  if( (nodeID<1) || (nodeID>nodeCount) ){
    throw new RuntimeException("Invalid node ID in BTree read.");
  }

  try{
    byte[] blob = new byte[nodeSize];
    filePtr.seek(headerSize + ((nodeID-1) * nodeTotal));
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
@Override public BTreeNodeInterface allocateNode() throws OmniException{
  try{
    // calculate file position
    int pos = headerSize + (nodeCount * nodeTotal);

    // write node
    filePtr.seek(pos);
    BTreeNodeInterface node = AllocateC.new_BTreeNode();
    node.setID    (nodeCount+1);
    node.setDegree(btreeDegree);
    filePtr.seek(pos);
    filePtr.write(node.convertToBinaryBlob());
    if(nodePad>0)filePtr.write(new byte[nodePad]);

    nodeCount++;
    writeHeader();
    return node;
  }catch(IOException e){
    throw new OmniException(
      OmniException.FILE_WRITE_ERROR,"Unable to create node in BTree file."
    );
  }
}
// allocateNode() ==============================================================


// writeNode() =================================================================
@Override public void writeNode(BTreeNodeInterface node) throws OmniException{
  long id = node.getID();
  if( (id<1) || (id>nodeCount) ){
    throw new RuntimeException("Invalid node ID in BTree write.");
  }
  
  try{
    filePtr.seek(headerSize + ((node.getID()-1) * nodeTotal));
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
    filePtr.writeInt (btreeDegree   );
    filePtr.writeInt (dnaLength     );
    filePtr.writeInt (nodeSize      );
    filePtr.writeInt (nodePad       );
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
    
    if(filePtr.readInt()!=FORMAT_VERSION){
      throw new OmniException(OmniException.FILE_READ_ERROR,"Incompatible BTree version.");  
    }
    
    headerSize  = filePtr.readInt();
    btreeDegree = filePtr.readInt();
    dnaLength   = filePtr.readInt();
    nodeSize    = filePtr.readInt();
    nodePad     = filePtr.readInt();
    nodeTotal   = nodeSize + nodePad;
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


// close() =====================================================================
@Override public void close(){try{filePtr.close();}catch(IOException e){}}
// close() =====================================================================



} // class BTreeFile_CML
