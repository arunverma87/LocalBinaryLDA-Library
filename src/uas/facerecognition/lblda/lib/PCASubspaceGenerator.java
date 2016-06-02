/**
 *
 */
package uas.facerecognition.lblda.lib;

import java.util.ArrayList;
import java.util.List;

import org.jblas.ComplexDouble;
import org.jblas.ComplexDoubleMatrix;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

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

		DoubleMatrix STS = S.mmul(ST);
		STS = STS.mul(1.00 / container.getSize());

		logger.debug("Computing eigenvectors...");
		// TODO :: Do we need to calculate actual eigenvectors?
		ComplexDoubleMatrix calculatedEigenValues = Eigen.eigenvalues(STS);
		ComplexDoubleMatrix calculatedEigenVectors = Eigen.eigenvectors(STS)[0];

		logger.debug("Saving subspace data...");

		List<Double> eigenValues = new ArrayList<>(calculatedEigenValues.getLength());
		for (ComplexDouble eigenvalue : calculatedEigenValues.toArray()) {
			eigenValues.add(eigenvalue.abs());
		}

		List<Double> eigenVectors = new ArrayList<>(calculatedEigenVectors.getLength());
		for (row = 0; row < calculatedEigenVectors.getRows(); row++) {
			for (ComplexDouble value : calculatedEigenVectors.getRow(row).toArray()) {
				eigenVectors.add(value.abs());
			}
		}

		pca.setSubspaceData(SubspaceType.SUBSPACE_PCA, n, n, avg.getData(), eigenVectors, eigenValues);
		// }

		pca.reorderDescending();
		pca.normalize();
		pca.trim(N-1);

		return pca;
	}

}
