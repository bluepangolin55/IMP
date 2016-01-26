package functionality;

public class Matrix {
	
	private double[][] values;
	
	//create an empty matrix
	public Matrix(int rows, int columns) {
		values=new double[rows][columns];
		
		//fill matrix with zeros
		for(int i=0;i<rows;i++){
			for(int j=0;j<columns;j++){
				values[i][j]=0;
			}
		}
	}
	
	//create a matrix with the given values	
	public Matrix(double[][] values) {
		this.values=values;
	}
	
	public void multiply_by_scalar(double scalar){
		for(int i=0;i<rows();i++){
			for(int j=0;j<columns();j++){
				values[i][j]=scalar*values[i][j];
			}
		}
	}
	
	public void create_as_outer_product(Vector u, Vector v){
		values=new double[u.size()][v.size()];
		
		for(int i=0;i<rows();i++){
			for(int j=0;j<columns();j++){
				values[i][j]=u.get(i)*v.get(j);
			}
		}
	}
	
//	public void remove_last_row(){
//		rows=rows-1;
//		double[][] old_values=values;
//		values=new double[rows][columns];
//		
//		for(int i=0;i<rows;i++){
//			for(int j=0;j<columns;j++){
//				values[i][j]=old_values[i][j];
//			}
//		}
//	}
	
	public double set(int i, int j, double new_value){
		return values[i-1][j-1]=new_value;
	}
	
	//queries+++++++++++++++++++++++++++++++++
	public int rows(){
		return values.length;
	}
	
	public int columns(){
		return values[0].length;
	}
	
	public double get(int i, int j){
		return values[i-1][j-1];
	}
	
	public Vector get_column_vector(int i){
		Vector column_vector = new Vector(columns());
		for(int j=1;j<columns()+1;j++){
			column_vector.set(j, get(j,i));
		}
		return column_vector;
	}
	
	public Vector get_row_vector(int index){
		return new Vector(values[index-1]);
	}	
	
	public Matrix transpose (){
		Matrix transpose = new Matrix(columns(), rows());
		for(int i=1;i<rows()+1;i++)
			for(int j=1;j<columns()+1;j++)
				transpose.set(j, i, get(i,j));
		return transpose;
	}
	
	//static matrix operations +++++++++++++++++++++++++++++++++++
	
	public static Matrix sum (Matrix A, Matrix B){
		Matrix sum = new Matrix(A.rows(), A.columns());
		for(int i=1;i<sum.rows()+1;i++)
			for(int j=1;j<sum.columns()+1;j++)
				sum.set(i, j, A.get(i,j)+B.get(i,j));
		return sum;
	}
	
	public static Matrix difference (Matrix A, Matrix B){
		Matrix difference = new Matrix(A.rows(), A.columns());
		for(int i=1;i<difference.rows()+1;i++)
			for(int j=1;j<difference.columns()+1;j++)
				difference.set(i, j, A.get(i,j)-B.get(i,j));
		return difference;
	}
	
	public static Matrix product (Matrix A, Matrix B){
		Matrix product = new Matrix(A.rows(), A.columns());
		for(int i=1;i<A.rows()+1;i++)
			for(int j=1;j<B.columns()+1;j++){
				product.set(i, j, Vector.scalar_product(A.get_row_vector(i), B.get_column_vector(j)));
			}
		return product;
	}
	
	public static Matrix scalar_multiplication (Matrix A, double scalar){
		Matrix scalar_multiplication = new Matrix(A.rows(), A.columns());
		for(int i=1;i<scalar_multiplication.rows()+1;i++)
			for(int j=1;j<scalar_multiplication.columns()+1;j++)
				scalar_multiplication.set(i, j, A.get(i,j)*scalar);
		return scalar_multiplication;
	}
	
	
@Override
	public String toString() {
	String output="";
	output=output + "\n";
	for(int j=0;j<columns();j++){
		output=output + "- - - -\t";
	}
	for(int i=0;i<rows();i++){
		output=output + "\n";
		for(int j=0;j<columns();j++){
			output=output + values[i][j];
			output=output + "\t";
		}
	}
	output=output + "\n";
	for(int j=0;j<columns();j++){
		output=output + "- - - -\t";
	}
		return output;
	}
}
