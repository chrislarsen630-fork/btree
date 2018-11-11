/** GeneBank file interface. Parses a GeneBank file and converts the DNA
  * sequences contained therein into a packed binary format.                  */
public interface GeneBankFileInterface{



/** Associates the GeneBankFileInterface object with a file and sets the file
  * parser to the beginning of the DNA sequence.
  * @param file GeneBank file to load.                                        
  * @param windowSize Number of DNA bases to load during readData().          */
public void loadFromFile(String file,int windowSize);


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
  * 0b10=T, 0b11=G). The number of DNA bases read is determined by the
  * windowSize set in loadFromFile(). The bases are packed so that the base
  * that was read last is contained in the two least significant bits; for
  * instance, the sequence ACTG is represented as 0b00011011.
  * @return Packed binary representation of data.                             */
public long readData();


/** Sets the file parser to the beginning of the next DNA subsequence.        */
public void nextSubsequence();



} // interface GeneBankFileInterface
