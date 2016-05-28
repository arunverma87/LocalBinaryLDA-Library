/**
 *
 */
package uas.facerecognition.lblda;

import uas.facerecognition.lblda.lib.SampleContainer;

/**
 * @author arunv
 *
 */
public class LBLDATrainer {

	private String imageFilePath;
	private int imageWidth;
	private int imageHeight;
	private int stepSize;
	private int windowSize;
	private int pcaDimension;
	private int outputDimension;

	public LBLDATrainer(String imageFilePath, int imageWidth, int imageHeight, int stepSize, int windowSize,
			int pcaDimension, int outputDimension) {
		this.imageFilePath = imageFilePath;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.stepSize = stepSize;
		this.windowSize = windowSize;
		this.pcaDimension = pcaDimension;
		this.outputDimension = outputDimension;
	}

	public boolean loadSamples() {

		SampleContainer trainSamples = new SampleContainer();
		if (!trainSamples.load(imageFilePath, imageWidth, imageHeight)) {
			return false;
		}
		return true;
	}
}
