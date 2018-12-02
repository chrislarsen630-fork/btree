package Common;

/** Data packet contained within a BTree node.
  * @author Landon Lemieux (landonlemieux@u.boisestate.edu)
  * @author Dylan Leman    (dylanleman@u.boisestate.edu)                      */
public class TreeObject_LLDL{

	int frequency;
	long data;
	
	public TreeObject_LLDL(long data) {
		this.data = data;
		frequency = 0;
	}
	
	public int compareTo(Object o) {
		if(data <  ((TreeObject) o).getData()) {
			return -1;
		}else if(data > ((TreeObject) o).getData()) {
			return 1;
		}
		return 0;
	}

	public long getData() {
		return data;
	}

	public void setData(long value) {
		data = value;
	}

	public long getFrequency() {
		return frequency;
	}

	public void incrementFrequency() {
		frequency++;
	}
}
