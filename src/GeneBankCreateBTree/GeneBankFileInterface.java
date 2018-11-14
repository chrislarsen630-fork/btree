/** GeneBank file interface. Parses a GeneBank file and converts the DNA
  * sequences contained therein into a packed binary format.                  */
public interface GeneBankFileInterface{



/** Associates the GeneBankFileInterface object with a file and sets the file
  * parser to the beginning of the DNA sequence.
  * @param file GeneBank file to load.                                        
  * @param windowSize Number of DNA bases to load during readData().
  * @throws Common.OmniException on file access or read error.                */
public void loadFromFile(String file,int windowSize) throws Common.OmniException;


/** Indicates whether the file parser is at the end of the current DNA
  * subsequence.
  * @return True if the subsequence is done.                                  */
public boolean isSubsequenceDone();


/** Indicates whether there are more DNA subsequences in this file.
  * Generally should be called after isSubsequenceDone() returns true.
  * @return True if there are more DNA subsequences in the file.              */
public boolean hasMoreSubsequences();


/** Reads the data at the file parser and packs it into a binary
  * representation. Each two bits represents a DNA base (0b00=A, 0b01=C,
  * 0b10=G, 0b11=T). The number of DNA bases read is determined by the
  * windowSize set in loadFromFile(). The bases are packed so that the base
  * that was read last is contained in the two least significant bits; for
  * instance, the sequence ACGT is represented as 0b00011011.
  * @return Packed binary representation of data.
  * @throws Common.OmniException on file access or read error.                */
public long readData() throws Common.OmniException;


/** Sets the file parser to the beginning of the next DNA subsequence.
  * @throws Common.OmniException if there are no more subsequences.           */
public void nextSubsequence() throws Common.OmniException;



} // interface GeneBankFileInterface
