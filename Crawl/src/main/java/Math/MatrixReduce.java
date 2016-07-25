package Math;

import Jama.Matrix;

public class MatrixReduce {
	public Matrix reduceRow(Matrix matrixToReduce, int nRowLast){
		int newNRow = nRowLast;
        int newNCol = matrixToReduce.getColumnDimension();
        return createNewReducedMatrix(matrixToReduce, newNRow, newNCol);
	}
	
	public Matrix reduceCol(Matrix matrixToReduce, int nColumnLast) {
		int newNRow = matrixToReduce.getRowDimension();
		int newNCol = nColumnLast;
		return createNewReducedMatrix(matrixToReduce, newNRow, newNCol);
	}
	
	public Matrix reduceRowAndColumn(Matrix matrixToReduce, int nRowLast, int nColLast) {
		int newNRow = nRowLast;
		int newNCol = nColLast;
		return createNewReducedMatrix(matrixToReduce, newNRow, newNCol);
	}
	
	private Matrix createNewReducedMatrix(Matrix matrixToReduce, int newNRow, int newNCol){
        Matrix newMatrix = new Matrix(newNRow, newNCol);
        int oldNRow = matrixToReduce.getRowDimension();
        int oldNCol = matrixToReduce.getColumnDimension();
        int i=0, j=0;

        while(i < newNRow){
            while( j <newNCol){
                float newValue = 0;
                if(i < oldNRow && j < oldNCol){
                    newValue = (float) matrixToReduce.get(i, j);
                }
                newMatrix.set(i, j, newValue);
                j++;
            }
            j=0;
            i++;
        }
        
        return newMatrix;
    }
}
