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

		logger.info("OpenCV library loaded successfully");
		String filePath = "TrainAll.txt";
		logger.info("Subspace Generation start..");
		long startTime = System.currentTimeMillis();

		// LBLDA trainer and call load method of it
		LBLDATrainer trainer = new LBLDATrainer(filePath, IMAGE_WIDTH, IMAGE_HEIGHT, STEP_SIZE, WINDOW_SIZE,
				PCA_DIMENSION, OUTPUT_DIMENSION);
		// if (trainer.loadSamples()) {
		// trainer.createSubSpace();
		// trainer.saveSubSpace();
		// }

		String subspacePath = filePath.substring(0, filePath.lastIndexOf(".")) + "1.dat";
		trainer.deSerializeData(subspacePath);

		long endTime = System.currentTimeMillis();
		double timeElapsed = ((endTime - startTime) / 1000.0);
		logger.info("Subspace Generatiion finished in " + timeElapsed + " sec");
	}
}
