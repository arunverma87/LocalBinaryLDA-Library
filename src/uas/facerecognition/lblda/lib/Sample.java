/**
 *
 */
package uas.facerecognition.lblda.lib;

import java.util.List;

/**
 * @author arunv
 *
 */
public class Sample {

	private List<Float> data;

	private String fileName;
	private String className;
	private int width;
	private int height;

	public Sample() {

	}

	/**
	 * @return the data
	 */
	public List<Float> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Float> data) {
		this.data = data;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	public boolean load(String fileName, String className, int width, int height) {

		return true;
	}
}
