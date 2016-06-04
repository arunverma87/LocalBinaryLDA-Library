/**
 *
 */
package uas.facerecognition.lblda.lib;

import java.util.ArrayList;
import java.util.List;

import org.jblas.DoubleMatrix;
import org.jblas.Eigen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author arunv
 *
 */
public class PCASubspaceGenerator implements ISubspaceGenerator {

	private static Logger logger = LoggerFactory.getLogger(PCASubspaceGenerator.class);

	@Override
	public Subspace generateSubspace(SampleContainer container) {

		int row, col;
		int N, n;

		if (container.getSize() == 0)
			return null;

		Subspace pca = new Subspace();

		// getting the problem dimensionality
		N = container.getSize(); // number of samples
		n = container.getSample(0).getDataSize(); // sample dimensionality

		DoubleMatrix S = container.getAsMatrix();
		Sample avg = container.getAvgSample();

		for (row = 0; row < S.getRows(); row++) {
			for (col = 0; col < S.getColumns(); col++) {
				S.put(row, col, S.get(row, col) - avg.getDataOfIndex(row));
			}
		}

		DoubleMatrix ST = S.transpose();

		// if (n > N) {

		logger.debug("Computing Covariance Matrix");

		DoubleMatrix SST = S.mmul(ST);
		SST = SST.mul(1.00 / container.getSize());

		logger.debug("Computing eigenvectors...");

		DoubleMatrix[] calculatedEigenVectors = Eigen.symmetricEigenvectors(SST);

		logger.debug("Saving subspace data...");

		List<Double> eigenVectors = new ArrayList<>();
		for (col = 0; col < calculatedEigenVectors[0].getColumns(); col++) {
			for (double value : calculatedEigenVectors[0].getColumn(col).toArray()) {
				eigenVectors.add(value);
			}
		}

		List<Double> eigenValues = new ArrayList<>(calculatedEigenVectors[1].getRows());
		for (row = 0; row < calculatedEigenVectors[1].getRows(); row++) {
			eigenValues.set(row,calculatedEigenVectors[1].get(row,row));
		}

		calculatedEigenVectors = null;

		//Calling Garbage Collector..
		logger.debug("Calling Garbage Collector...");
		System.gc();

		pca.setSubspaceData(SubspaceType.SUBSPACE_PCA, n, n, avg.getData(), eigenVectors, eigenValues);
		// }

		pca.sortInDescending(false);
		pca.normalize();
		pca.retainAll(N - 1);

		return pca;
	}

}
