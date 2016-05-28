/**
 *
 */
package uas.facerecognition.lblda.lib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * @author arunv
 *
 */
public class SampleContainer {

	List<Sample> samples;

	ExecutorService executorService = Executors.newFixedThreadPool(10);

	public SampleContainer() {
		samples = new ArrayList<>();
	}

	public void clear() {
		samples.clear();
	}

	public int size() {
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

	public boolean load(String fileName, int width, int height) {
		try (Stream<String> fileContentStrem = Files.lines(Paths.get(fileName))) {

			fileContentStrem.forEach(line -> {

				executorService.execute(new SampleLoader(line, width, height));

			});

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
				if (sample.load(splitString[0], splitString[1], width, height))
					samples.add(sample);
			}
			splitString = null;
		}
	}

}
