/**
 *
 */
package uas.facerecognition.lblda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uas.facerecognition.lblda.lib.ISubspaceGenerator;
import uas.facerecognition.lblda.lib.LDASubspaceGenerator;
import uas.facerecognition.lblda.lib.SampleContainer;
import uas.facerecognition.lblda.lib.local.LocalSubspace;
import uas.facerecognition.lblda.lib.local.LocalSubspaceGenerator;

/**
 * @author arunv
 *
 */
public class LBLDATrainer {

	static Logger logger = LoggerFactory.getLogger(LBLDATrainer.class);

	private String imageFilePath;
	private int imageWidth;
	private int imageHeight;
	private int stepSize;
	private int windowSize;
	private int pcaDimension;
	private int outputDimension;

	private SampleContainer trainSamples;

	private LocalSubspace localSubspace;

	public LBLDATrainer(String imageFilePath, int imageWidth, int imageHeight, int stepSize, int windowSize,
			int pcaDimension, int outputDimension) {
		this.imageFilePath = imageFilePath;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.stepSize = stepSize;
		this.windowSize = windowSize;
		this.pcaDimension = pcaDimension;
		this.outputDimension = outputDimension;

		trainSamples = new SampleContainer();
	}

	public boolean loadSamples() {

		if (!trainSamples.load(imageFilePath, imageWidth, imageHeight)) {
			return false;
		}
		logger.debug("Total samples loaded in to system: " + String.valueOf(trainSamples.getSize()));
		return true;
	}

	public void createSubSpace() {

		logger.info("Creating Subspace");

		ISubspaceGenerator subGenerator = new LDASubspaceGenerator(this.pcaDimension);

		LocalSubspaceGenerator localSubpaceGenerator = new LocalSubspaceGenerator(subGenerator, imageWidth, imageHeight,
				windowSize, stepSize, outputDimension);

		// generate subspace
		localSubspace = localSubpaceGenerator.generateSubspace(trainSamples);

		subGenerator = null;

	}

	public void saveSubSpace() {

	}

}
