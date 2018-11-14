package CreateBTree;

import Common.*;

/** Debug file dumper interface for dumping the contents of a BTree to file.
  * Used when GeneBankCreateBTree has a debug level of 1.                     */
public interface DebugFileDumpInterface{



/** Dumps the BTree to file.
 * @param tree BTree to dump.
 * @param file File path of the target file.
 * @throws Common.OmniException on file access or read error. */
public void dumpBTreeToFile(BTreeInterface tree,String file) throws OmniException;



}
