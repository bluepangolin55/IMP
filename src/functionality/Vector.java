package functionality;

public class Vector {
	
	private double[] values;
	
	//create an empty vector
	public Vector(int size) {
		values=new double[size];
		for(int i=1;i<size()+1;i++){
			set(i,0);
		}
	}
	
	//create a vector with the given values	
	public Vector(double[] values) {
		this.values=values;
	}
	
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++
	//operations on this vector +++++++++++++++++++++++++++++++++++
	
	public void multiply_by_scalar(double scalar){
		for(int i=1;i<size()+1;i++){
			set(i,get(i)*scalar);
		}
	}
	
	public void add(Vector v){
		for(int i=1;i<size()+1;i++){
			set(i, get(i)+v.get(i));
		}
	}
	
	public void subtract(Vector v){
		for(int i=1;i<size()+1;i++){
			set(i, get(i)-v.get(i));
		}
	}
	
	public void set(int index, double new_value) {
		values[index-1]=new_value;
	}
	
	
	//queries+++++++++++++++++++++++++++++++++
	
	public int size(){
		return values.length;
	}
	
	public double get(int index){
		return values[index-1];
	}
	
	public double[] get_values(){
		return values;
	}
	
	public double euclidean_norm(){
		double sum = 0;
		for(int i=1;i<size()+1;i++){
			sum=sum + Math.pow(get(i),2);
		}
		return Math.sqrt(sum);
	}
	
	public double p_norm(int norm){
		double sum = 0;
		for(int i=1;i<size()+1;i++){
			sum=sum + Math.pow(get(i),norm);
		}
		return Math.pow(sum, 1.0/norm);
	}
	
	public Vector unit_vector (){
		return scalar_multiplication(this, 1/euclidean_norm());
	}
	
	public boolean is_unit_vector (){
		if(Math.abs(euclidean_norm()-1)<0.00000000000001)
			return true;
		else
		return false;
	}
	
	public Vector transform(Matrix A){
		Vector transformation=new Vector(A.rows());
		if(A.columns()==this.size()){
			for(int i=1;i<A.rows()+1;i++)
				transformation.set(i, Vector.scalar_product(A.get_row_vector(i), this));
		}
		else{
			transformation=this;
			System.out.println("the operation can not be done");
		}
		return transformation;
	}
	
	//static vector operations +++++++++++++++++++++++++++++++++++
	
	public static double scalar_product(Vector x, Vector y){
		double scalar = 0;
		for(int i=1;i<x.size()+1;i++){
			scalar=scalar + x.get(i)*y.get(i);
		}
		return scalar;
	}
	
	public static Matrix outer_product(Vector x, Vector y){
		Matrix outer_product = new Matrix(x.size(),y.size());
		
		for(int i=1;i<x.size();i++){
			for(int j=1;j<y.size();j++){
				outer_product.set(i, j, x.get(i)*y.get(j));
			}
		}
		return outer_product;
	}
	
	public static Vector sum (Vector x, Vector y){
		Vector sum = new Vector(x.size());
		for(int i=1;i<x.size()+1;i++)
			sum.set(i,x.get(i)+y.get(i));
		return sum;
	}
	
	public static Vector difference (Vector x, Vector y){
		Vector difference = new Vector(x.size());
		for(int i=1;i<x.size()+1;i++)
			difference.set(i,x.get(i)-y.get(i));
		return difference;
	}
	
	public static Vector scalar_multiplication (Vector x, double scalar){
		Vector scalar_multiplication = new Vector(x.size());
		for(int i=1;i<x.size()+1;i++){
			scalar_multiplication.set(i, scalar*x.get(i));
		}
		return scalar_multiplication;
	}
	
	public static boolean orthogonal (Vector x, Vector y){
		if(scalar_product(x, y)==0)
			return true;
		else
		return false;
	}
	
	public static boolean orthonormal (Vector x, Vector y){
		if(orthogonal(x,y) && x.is_unit_vector())
			return true;
		else
		return false;
	}

	@Override
	public String toString() {
		String output="";
		output=output + "\n";
		for(int j=0;j<size()+1;j++){
			output=output + "- - - -\t";
		}
		output=output + "\n";
		for(int i=1;i<size()+1;i++){
			output=output + get(i);
			output=output + "\t";
		}
		output=output + "T\n";
		for(int j=0;j<size()+1;j++){
			output=output + "- - - -\t";
		}
		return output;
	}
}
