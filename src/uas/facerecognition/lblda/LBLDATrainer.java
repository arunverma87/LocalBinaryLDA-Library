/**
 *
 */
package uas.facerecognition.lblda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

	private LocalSubspace localSubspace = null;

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
		//generate subspace
		this.localSubspace = localSubpaceGenerator.generateSubspace(trainSamples);
		subGenerator = null;
	}

	public void saveSubSpace() {
		String savePath = this.imageFilePath.substring(0, this.imageFilePath.lastIndexOf(".")) + ".dat";
		serializeData(savePath);
	}

	public void serializeData(String path) {
		System.out.println("Starting serialization...");
		try {

			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			out.writeObject(this.localSubspace);
			out.close();

			fileOut.close();
			System.out.println("Serialization Successful... Checkout " + path + " output file..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deSerializeData(String path) {
		System.out.println("Starting deSerialization...");
		try {
			FileInputStream fileIn = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			try {
				this.localSubspace = (LocalSubspace) in.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("Deserialized Data from " + path);
			in.close();
			fileIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}