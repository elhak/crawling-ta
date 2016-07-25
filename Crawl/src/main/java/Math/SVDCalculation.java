package Math;

import Connect.DBConnect;
import Domain.Doc;
import Domain.Token;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakim on 22-Jul-16.
 *
 * @author by Hakim
 *         <p/>
 *         Please update the author field if you are editing
 *         this file and your name is not written.
 */
public class SVDCalculation {

    private MatrixReduce matrixReduce = new MatrixReduce();
    private Vectors vector = new Vectors();
    private DBConnect dbConnect;
    private List<Token> tokenList = new ArrayList<Token>();
    private List<Doc> docList = new ArrayList<Doc>();

    public SVDCalculation(DBConnect dbConnect) {
        this.dbConnect = dbConnect;
        this.tokenList = dbConnect.getTokenList();
        this.docList = dbConnect.getDocList();
    }

    public void calculate(){
        Matrix matrixtfidf = createTfIdfMatrix();

        SingularValueDecomposition matrixSVD = matrixtfidf.svd();

        Matrix reducedV = matrixReduce.reduceRow(matrixSVD.getV(), 300).transpose();
        Matrix reducedS = matrixReduce.reduceRowAndColumn(matrixSVD.getS(), 300, 300);

        Matrix matrixVS = reducedV.times(reducedS);
        int nRowCol = matrixtfidf.getColumnDimension();
        int i = 0;
        int j = i + 1;
        while (i < nRowCol) {
            float[] vectorA = vector.getRowVector(matrixVS, i);
            Doc doc1 = docList.get(i);
            while (j < nRowCol) {
                Doc doc2 = docList.get(j);
                float[] vectorB = vector.getRowVector(matrixVS, j);
                float weightCosine = Math.abs(calculateCosine(vectorA, vectorB));
                dbConnect.insertSimilarity(doc1.getId(), doc2.getId(), weightCosine);
                j++;
            }
            i++;
            j = i + 1;
        }
    }

    private Matrix createTfIdfMatrix(){

        Matrix matrixTfIdf = new Matrix(tokenList.size(), docList.size());

        for (int i = 0; i < tokenList.size(); i++) {
            for (int j = 0; j < docList.size(); j++) {
                int urlId = docList.get(j).getId();
                int termId = tokenList.get(i).getId();
                float weight = dbConnect.getWeight(urlId, termId);
                matrixTfIdf.set(i, j, weight);
            }
            System.out.print(i);
        }

        return matrixTfIdf;
    }

    private float calculateCosine(float[] vectorA, float[] vectorB) {
        float weight = (vector.innerProduct(vectorA, vectorB))/(vector.lenghtVector(vectorA)*vector.lenghtVector(vectorB));
        return weight;
    }
}
