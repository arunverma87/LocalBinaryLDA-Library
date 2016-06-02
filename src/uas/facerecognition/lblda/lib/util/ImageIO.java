/**
 *
 */
package uas.facerecognition.lblda.lib.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author arunv
 *
 */
public class ImageIO {

	static Logger logger = LoggerFactory.getLogger(ImageIO.class);

	private String filePath;
	private int width;
	private int height;

	public ImageIO(String filePath, int width, int height) {
		this.filePath = filePath;
		this.width = width;
		this.height = height;
	}

	public List<Double> getGreyImageData() {
		if (new File(this.filePath).exists()) {

			List<Double> data = new ArrayList<>();
			Mat img = Imgcodecs.imread(this.filePath);
			// Mat grayImg = new Mat(height, width, CvType.CV_8U);
			// Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGR2GRAY);

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					// double[] temp = img.get(i, j)[0];
					data.add(img.get(i, j)[0]);
				}
			}
			return data;
		}
		logger.debug(this.filePath + " : is not exist ");
		return null;
	}

}