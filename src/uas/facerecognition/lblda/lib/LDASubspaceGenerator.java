/**
 *
 */
package uas.facerecognition.lblda.lib;

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

		int N, n, Nc;
		// getting the problem dimensionality
		N = container.getSize(); // number of samples
		n = container.getSample(0).getDataSize(); // sample dimensionality
		Nc = container.getNumberOfClasses(); // number of classes

		Subspace subspace = new Subspace();

		if (pcaDimension <= 0 && (n <= (N - Nc))) {
			// Direct LDA

		} else {
			// PCA has to be perfomed first
			if (pcaDimension == 0)
				pcaDimension = N - Nc;

			logger.debug("Performing PCA..");

			Subspace pcaSubspace;
			Subspace ldaSubspace = new Subspace();
			PCASubspaceGenerator pcaGen = new PCASubspaceGenerator();

			pcaSubspace = pcaGen.generateSubspace(container);



		}

		return null;
	}

}
