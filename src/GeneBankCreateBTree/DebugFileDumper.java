import java.util.*;
import java.io.*;
import Common.*;

/** Debug file dumper interface for dumping the contents of a BTree to file.
  * Used when GeneBankCreateBTree has a debug level of 1.
  * @author Christopher M. Larsen (chrislarsen630@u.boisestate.edu)
  * @version 20181202                                                         */
public class DebugFileDumper{



// dumpBTreeToFile() ===========================================================
/** Dumps the BTree to file.
 * @param tree BTree to dump.
 * @param file File path of the target file.
 * @param sequenceLength Length of DNA sequences, in base pairs.
 * @throws Common.OmniException on file access or read error.                 */
public void dumpBTreeToFile(BTree tree,String file,int sequenceLength)
throws OmniException{
  final char[] VAL_TO_BASEPAIR = new char[4];
  VAL_TO_BASEPAIR[0] = 'a';
  VAL_TO_BASEPAIR[1] = 'c';
  VAL_TO_BASEPAIR[2] = 'g';
  VAL_TO_BASEPAIR[3] = 't';

  try(BufferedWriter outData = new BufferedWriter(new FileWriter(file))){
    Stack<Integer> parentIDStack    = new java.util.Stack<>();
    Stack<Integer> parentIndexStack = new java.util.Stack<>();
    BTreeNode node = tree.getRootNode();
    while(!node.isLeaf()){
      parentIDStack   .push(node.getID());
      parentIndexStack.push(0);
      node = tree.fetchNode(node.getChildrenIDArray()[0]);
    }

    boolean returnedFromLeaf = false;
    int nodeIndex = 0;
    while(true){
      if(nodeIndex>=node.getNKeys()){
        if(parentIDStack.isEmpty())break;
        node      = tree.fetchNode(parentIDStack.pop());
        nodeIndex = parentIndexStack.pop();
        returnedFromLeaf = true;
        continue;
      }

      if(!node.isLeaf() && (!returnedFromLeaf) ){
        parentIDStack   .push(node.getID());
        parentIndexStack.push(nodeIndex   );
        node      = tree.fetchNode(node.getChildrenIDArray()[nodeIndex]);
        nodeIndex = 0;
        continue;
      }

      TreeObject key = node.getKeyArray()[nodeIndex];
      long keyData = key.getData();
      for(int i=sequenceLength-1;i>-1;i--){
        outData.write(VAL_TO_BASEPAIR[(int)((keyData>>(2*i))&0x3)]);
      }
      outData.write(": "+key.getFrequency());
      outData.newLine();
      nodeIndex++;
      returnedFromLeaf = false;

      if(!node.isLeaf() && (nodeIndex==node.getNKeys())){
        parentIDStack   .push(node.getID());
        parentIndexStack.push(nodeIndex   );
        node      = tree.fetchNode(node.getChildrenIDArray()[nodeIndex]);
        nodeIndex = 0;
      }
    } // while true
  }catch(IOException e){
     throw new OmniException(
       OmniException.FILE_UNSPECIFIED,"Issue creating dump file."
    );
  }
}
// dumpBTreeToFile() ===========================================================



} // class DebugFileDump
