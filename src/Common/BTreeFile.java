package Common;

import java.io.*;

/** BTree file I/O. Intended for internal use by BTree. No other code should
  * touch it.
  * @author Christopher M. Larsen (chrislarsen630@u.boisestate.edu)
  * @version 20181202                                                         */
public class BTreeFile{



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
/** Creates a new BTree file. Called by BTreeInterface.createNewFile().
  * @param targetFile File path for the created file. If the file exists,
  *                   it will be overwritten.
  * @param degree         Degree of the BTree.
  * @param sequenceLength Length of DNA sequences, in base pairs.
  * @return Root node of the BTree.
  * @throws OmniException on file creation error.                             */
public BTreeNode createNewFile(String targetFile,int degree,int sequenceLength)
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
  BTreeNode dummyNode = new BTreeNode(degree);
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
  BTreeNode ret = readNode(rootID);
  ret.setLeaf(true);
  writeNode(ret);
  return ret;
}
// createNewFile() =============================================================


// loadFromFile() ==============================================================
/** Loads an existing BTree file from disk. Called by
  * BTreeInterface.loadFromFile().
  * @param sourceFile File path to load the BTree from.
  * @return Root node of the BTree.
  * @throws OmniException on file access or read error.                       */
public BTreeNode loadFromFile(String sourceFile) throws OmniException{
  try{
    filePtr = new RandomAccessFile(sourceFile,"rw");
    readHeader();
    return readNode(rootID);
  }catch(IOException e){
    throw new OmniException(OmniException.FILE_ACCESS,"Issue creating BTree file.");
  }
}
// loadFromFile() ==============================================================


// getSequenceLength() =========================================================
/** Returns the length of the DNA sequences.
  * @return DNA sequence length.                                              */
public int getSequenceLength(){return dnaLength;}
// getSequenceLength() =========================================================


// readNode() ==================================================================
/** Loads the node with the specified ID from disk.
  * @param nodeID ID of the node to read.
  * @return BTree node.
  * @throws OmniException if node does not exist or on read error.            */
public BTreeNode readNode(int nodeID) throws OmniException{
  if( (nodeID<1) || (nodeID>nodeCount) ){
    throw new RuntimeException("Invalid node ID in BTree read.");
  }

  try{
    byte[] blob = new byte[nodeSize];
    filePtr.seek(headerSize + ((nodeID-1) * nodeTotal));
    filePtr.read(blob);
    BTreeNode node = new BTreeNode(btreeDegree);
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
/** Allocates space for a new BTree node and returns the newly created node.
  * @return A newly allocated node.
  * @throws OmniException if a new node cannot be allocated.                  */
public BTreeNode allocateNode() throws OmniException{
  try{
    // calculate file position
    int pos = headerSize + (nodeCount * nodeTotal);

    // write node
    filePtr.seek(pos);
    BTreeNode node = new BTreeNode(btreeDegree);
    node.setID    (nodeCount+1);
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
/** Saves a node to disk.
 * @param node Node to write.
 * @throws OmniException on file write error.                                 */
public void writeNode(BTreeNode node) throws OmniException{
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


// setRootNode() ===============================================================
/** Sets the ID of the root node.
  * @param nodeID ID of the root node.                                        */
public void setRootNode(int nodeID){
  if( (nodeID<1) || (nodeID>nodeCount) ){
    throw new RuntimeException("Invalid node ID set for BTree root node.");
  }
  rootID = nodeID;
  try{writeHeader();}catch(OmniException e){}
}
// setRootNode() ===============================================================


// close() =====================================================================
/** Closes the file stream and deallocates resources.                         */
public void close(){try{filePtr.close();}catch(IOException e){}}
// close() =====================================================================


// writeHeader() ===============================================================
private void writeHeader() throws OmniException{
  try{
    filePtr.seek(0);

    filePtr.writeInt(MAGIC_NUMBER  );
    filePtr.writeInt(FORMAT_VERSION);
    filePtr.writeInt(headerSize    );
    filePtr.writeInt(btreeDegree   );
    filePtr.writeInt(dnaLength     );
    filePtr.writeInt(nodeSize      );
    filePtr.writeInt(nodePad       );
    filePtr.writeInt(nodeCount     );
    filePtr.writeInt(rootID        );

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



} // class BTreeFile
