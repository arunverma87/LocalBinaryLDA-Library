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
public class LDASubspaceGenerator implements ISubspaceGenerator {

	private static Logger logger = LoggerFactory.getLogger(LDASubspaceGenerator.class);
	private int pcaDimension;

	// constructor
	public LDASubspaceGenerator() {
		pcaDimension = 0;
	}

	public LDASubspaceGenerator(int pcaDimension) {
		this.pcaDimension = pcaDimension;
	}

	// generates a LDA subspace based on the training data contained in the
	// sampleSet
	@Override
	public Subspace generateSubspace(SampleContainer container) {

		logger.debug("LDASubspace Generator loaded..");

		int N, n, noOfClasses;
		int col,row;
		// getting the problem dimensionality
		N = container.getSize(); // number of samples
		n = container.getSample(0).getDataSize(); // sample dimensionality
		noOfClasses = container.getNumberOfClasses(); // number of classes

		Subspace retSubspace = new Subspace();

		if (pcaDimension <= 0 && (n <= (N - noOfClasses))) {
			// direct LDA. No need to do PCA.

			Sample avg = container.getAvgSample();

			logger.debug("Getting Beetween-class and Within-class variance matrices...");
			DoubleMatrix B = container.getBetweenClassVariance();
			DoubleMatrix W = container.getWithinClassVariance();

			logger.debug("Computing generalized eigenvectors...");

			DoubleMatrix[] genEigen = Eigen.symmetricGeneralizedEigenvectors(B, W);

			if (genEigen == null)
				return null;

			List<Double> eigenVectors = new ArrayList<>(genEigen[0].length);
			for (col = 0; col < genEigen[0].getColumns(); col++) {
				for (double value : genEigen[0].getColumn(col).toArray()) {
					eigenVectors.add(value);
				}
			}

			List<Double> eigenValues = new ArrayList<>(genEigen[1].getRows());
			for (col = 0; col < genEigen[1].getColumns(); col++) {
				eigenValues.add(genEigen[1].get(1, col));
			}

			genEigen = null;

			retSubspace.setSubspaceData(SubspaceType.SUBSPACE_LDA, n, n, avg.getData(), eigenVectors, eigenValues);

		} else {
			// First doing PCA.
			if (pcaDimension == 0)
				pcaDimension = N - noOfClasses;

			logger.debug("Performing PCA first. why?");

			Subspace pcaSubspace;
			Subspace ldaSubspace = new Subspace();
			PCASubspaceGenerator pcaGen = new PCASubspaceGenerator();

			pcaSubspace = pcaGen.generateSubspace(container);

			logger.debug("Projecting samples into low-dimensional subspace...");

			SubspaceProjector localProjector = new SubspaceProjector(pcaSubspace);

			SampleContainer transformedSamples = localProjector.projectSampleContainer(container, pcaDimension);

			logger.debug("Getting Beetween-class and Within-class variance matrices...");
			DoubleMatrix B = transformedSamples.getBetweenClassVariance();
			DoubleMatrix W = transformedSamples.getWithinClassVariance();

			logger.debug("Computing generalized eigenvectors...");

			DoubleMatrix[] genEigen = Eigen.symmetricGeneralizedEigenvectors(B, W);

			if (genEigen == null)
				return null;

			logger.debug("Generalized EigenVector total length: "+ genEigen[0].length + ". Columns: " +  genEigen[0].getColumns());

			List<Double> eigenVectors = new ArrayList<>(genEigen[0].length);
			for (col = 0; col < genEigen[0].getColumns(); col++) {
				for (double value : genEigen[0].getColumn(col).toArray()) {
					eigenVectors.add(value);
				}
			}

			logger.debug("Generalized Eigen Values: " +  genEigen[1].getColumns() + " and  Rows: " + genEigen[1].getRows());

			List<Double> eigenValues = new ArrayList<>(genEigen[1].getRows());
			for (row = 0; row < genEigen[1].getRows(); row++) {
				eigenValues.add(genEigen[1].get(row,0));
			}

			genEigen = null;

			List<Double> avg = new ArrayList<>();

			ldaSubspace.setSubspaceData(SubspaceType.SUBSPACE_LDA, pcaDimension, pcaDimension, avg, eigenVectors,
					eigenValues);
			ldaSubspace.sortInDescending(true);

			logger.debug("Computing final subspace...");

			DoubleMatrix matPCA = pcaSubspace.getSubspaceAxesAsMatrix(pcaDimension, n);
			DoubleMatrix matLDA = ldaSubspace.getSubspaceAxesAsMatrix(pcaDimension, pcaDimension);
			DoubleMatrix matFinal = matLDA.mmul(matPCA);

			List<Double> finalData = new ArrayList<>(matFinal.length);

			for (row = 0; row < matFinal.getRows(); row++) {
				for (col = 0; col < matFinal.getColumns(); col++) {
					finalData.add(matFinal.get(row,col));
				}
			}

			logger.debug("Saving final subspace of " + SubspaceType.SUBSPACE_LDA);
			retSubspace.setSubspaceData(SubspaceType.SUBSPACE_LDA, pcaDimension, n, pcaSubspace.getCenterOffset(),
					finalData, ldaSubspace.getAxesCriterionFn());
		}

		logger.debug("Sorting SubspceAxes and AxesCriteria Function in descending order..");
		retSubspace.sortInDescending(true);
		logger.debug("Normalizing Subspaces..");
		retSubspace.normalize();
		logger.debug("Trimming Subspaces to " + (noOfClasses-1) + " length.");
		retSubspace.retainAll(noOfClasses - 1);

		return retSubspace;
	}

}
