/**
 *
 */
package uas.facerecognition.lblda.lib.util;

import java.io.File;
import java.util.List;

/**
 * @author arunv
 *
 */
public class ImageIO {

	private String filePath;

	public ImageIO(String filePath) {
		this.filePath = filePath;
	}

	public List<Float> getGreyImageData() {

		if (new File(this.filePath).exists()) {

		}
		return null;

	}

}