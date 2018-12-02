package Common;

public class TreeObject implements TreeObjectInterface {

	int frequency;
	long data;
	
	public TreeObject(long data) {
		this.data = data;
		frequency = 0;
	}
	
	@Override
	public int compareTo(Object o) {
		if(data <  ((TreeObject) o).getData()) {
			return -1;
		}else if(data > ((TreeObject) o).getData()) {
			return 1;
		}
		return 0;
	}

	@Override
	public long getData() {
		return data;
	}

	@Override
	public void setData(long value) {
		data = value;
	}

	@Override
	public long getFrequency() {
		return frequency;
	}

	@Override
	public void incrementFrequency() {
		frequency++;
	}
}
