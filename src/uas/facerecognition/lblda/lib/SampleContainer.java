/**
 *
 */
package uas.facerecognition.lblda.lib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.jblas.DoubleMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Jama.Matrix;

/**
 * @author arunv
 *
 */
public class SampleContainer {

	static Logger logger = LoggerFactory.getLogger(SampleContainer.class);

	private List<Sample> samples;
	private Map<String, Boolean> diffClasses;
	private Sample avgSample;
	ExecutorService executorService = Executors.newFixedThreadPool(50);

	public SampleContainer() {
		samples = new ArrayList<>();
		diffClasses = new HashMap<String, Boolean>();
		avgSample = new Sample();
	}

	public void clear() {
		samples.clear();
	}

	public int getSize() {
		if (samples != null)
			return samples.size();
		else
			return 0;
	}

	public List<Sample> getAllSamples() {
		return samples;
	}

	public Sample getSample(int index) {
		return samples.get(index);
	}

	public void addSample(Sample sample) {
		this.samples.add(sample);
	}

	public int getNumberOfClasses() {
		return this.diffClasses.size();
	}

	public Sample getAvgSample() {
		return avgSample;
	}

	private void calculateAvgSample() {
		int i, j;

		int dataSize = samples.get(0).getDataSize();
		avgSample.initAndSetDataSize(dataSize);

		double[] tempData = new double[dataSize];

		Arrays.fill(tempData, 0);

		for (i = 0; i < getSize(); i++) {
			for (j = 0; j < dataSize; j++) {
				tempData[j] += samples.get(i).getDataOfIndex(j);
			}
		}
		for (j = 0; j < dataSize; j++) {
			avgSample.addData(tempData[j] / (samples.size()));
		}

		tempData = null;
	}

	public Sample getAvgSampleOfClass(String className) {
		int i, j, k = 0;
		Sample avg = new Sample(className);

		int dataSize = samples.get(0).getDataSize();
		avg.initAndSetDataSize(dataSize);

		double[] tempData = new double[dataSize];

		Arrays.fill(tempData, 0);

		for (i = 0; i < getSize(); i++) {
			if (!samples.get(i).getClassName().equals(className))
				continue;
			for (j = 0; j < dataSize; j++) {
				tempData[j] += samples.get(i).getDataOfIndex(j);
			}
			k++;
		}
		for (j = 0; j < dataSize; j++) {
			avg.addData(tempData[j] / k);
		}
		tempData = null;
		return avg;
	}


	public DoubleMatrix getAsMatrix() {
		int dataSize = samples.get(0).getDataSize();
		int numSamples = samples.size();
		DoubleMatrix mat = new DoubleMatrix(dataSize, numSamples);

		//Column = Samples
		for (int row = 0; row < dataSize; row++) {
			for (int col = 0; row < numSamples; col++) {
				mat.put(row, col, samples.get(col).getDataOfIndex(row));
			}
		}
		return mat;
	}


	public boolean load(String fileName, int width, int height) {
		// Access File which contains Samples Information
		try (Stream<String> fileContentStrem = Files.lines(Paths.get(fileName))) {
			// Iterate each line one by one and load each line with thread and
			// execute by ExecutorService.
			fileContentStrem.forEach(line -> {
				SampleLoader loader = new SampleLoader(line.trim(), width, height);
				executorService.execute(loader);
			});
			executorService.shutdown();
			try {
				executorService.awaitTermination(5, TimeUnit.MINUTES);
			} catch (InterruptedException inex) {
				// ignore
			}

			// Simultaneously calculate Average of all Samples.
			calculateAvgSample();

		} catch (IOException ioex) {
			System.err.println("Error with File Reading. Path: " + fileName);
			return false;
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}

	private class SampleLoader implements Runnable {

		String fileLine;
		int width;
		int height;

		public SampleLoader(String line, int width, int height) {
			this.fileLine = line;
			this.width = width;
			this.height = height;
		}

		@Override
		public void run() {
			String[] splitString = this.fileLine.split("\\s");
			if (splitString.length == 2) {
				Sample sample = new Sample();
				if (sample.load(splitString[0], splitString[1], width, height)) {
					synchronized (SampleLoader.class) {
						samples.add(sample);
						diffClasses.put(splitString[1], true);
					}
				}
			} else {
				logger.debug(this.fileLine + ": Can not split.");
			}
			splitString = null;
		}
	}

}
