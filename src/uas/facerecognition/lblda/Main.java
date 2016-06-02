/**
 *
 */
package uas.facerecognition.lblda;

import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author arunv
 *
 */
public class Main implements IConstant {

	static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		logger.debug("OpenCV library loaded successfully");
		String filePath = "TrainAll.txt";

		logger.info("Training started..");

		// LBLDA trainer and call load method of it
		LBLDATrainer trainer = new LBLDATrainer(filePath, IMAGE_WIDTH, IMAGE_HEIGHT, STEP_SIZE, WINDOW_SIZE,
				PCA_DIMENSION, OUTPUT_DIMENSION);
		if (trainer.loadSamples()) {

			trainer.createSubSpace();

			trainer.saveSubSpace();

		}
	}
}
