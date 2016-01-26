package functionality;

public abstract class ConvolusionFilter {
	
	private int width; 
	private int height;
	private float[][] data;
	
	
	public int getWidth() {
		return width;
	};
	
	public int getHeight(){
		return height;
	}
	
	public float[][] getData(){
		return data;
	}

}
