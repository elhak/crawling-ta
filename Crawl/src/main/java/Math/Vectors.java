package Math;

import Jama.Matrix;

public class Vectors {
	
	public Vectors() {
		// TODO Auto-generated constructor stub
	}
	
	public float[] getRowVector(Matrix matrix, int iRow) {
		float[] rowVector = new float[matrix.getColumnDimension()];
        int nCol = matrix.getColumnDimension();
        
        for (int i = 0; i < nCol; i++) {
        	rowVector[i] = (float) matrix.get(iRow, i);
		}
            
        return rowVector;		
	}
	
	public float[] getColVector(Matrix matrix, int iCol) {
		float[] colVector = new float[matrix.getRowDimension()];
		int nRow = matrix.getRowDimension();
		for (int i = 0; i < nRow; i++) {
			colVector[i] = (float) matrix.get(iCol, i);
		}
		
		return colVector;
	}
	
	public float innerProduct(float[] vectorA, float[] vectorB) {
		float product = 0;
		int nA = vectorA.length;
		int nB = vectorB.length;
		if(nA == nB){
			for (int i = 0; i < nA; i++) {
				product = product + vectorA[i]*vectorB[i];
			}
		}
		
		return product;
	}
	
	public float lenghtVector(float[] vector) {
		float length = 0;
		float sumSquare = 0;
		for (int i = 0; i < vector.length; i++) {
			sumSquare = sumSquare + (vector[i] * vector[i]);
		}
		
//		System.out.println("Sum Square " + sumSquare);
		length = (float) Math.sqrt(sumSquare);
        return length;
	}
}
