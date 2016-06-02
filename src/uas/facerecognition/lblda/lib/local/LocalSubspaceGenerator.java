/**
 *
 */
package uas.facerecognition.lblda.lib.local;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uas.facerecognition.lblda.exception.CustomException;
import uas.facerecognition.lblda.lib.ISubspaceGenerator;
import uas.facerecognition.lblda.lib.Sample;
import uas.facerecognition.lblda.lib.SampleContainer;
import uas.facerecognition.lblda.lib.Subspace;

/**
 * @author arunv
 *
 */
public class LocalSubspaceGenerator {

	static Logger logger = LoggerFactory.getLogger(LocalSubspaceGenerator.class);

	private ISubspaceGenerator subspaceGenerator; // subspace generator to be
													// used to
	// obtain local features
	private int originalWidth; // width of the images in the sample set
	private int originalHeight; // height of the images in the sample set
	private int windowWidth; // width of the sliding window used to create
								// image
	// patches
	private int windowStep; // translation step of the sliding window
	private int numFeatures; // maximum number of local features, if larger
								// than the
	// maximum possible number of features, features will be
	// trimmed according to corresponding criterion function

	// constructor
	// parameters:
	// subspaceGenerator : subspace generator to be used to obtain local
	// features
	// originalWidth : width of the images in the sample set
	// originalHeight : height of the images in the sample set
	// windowWidth : width of the sliding window used to create image patches
	// windowStep : translation step of the sliding window
	// numFeatures : maximum number of local features, if larger than the
	// maximum possible number of feaures, features will be trimmed according to
	// corresponding criterion function
	public LocalSubspaceGenerator(ISubspaceGenerator subspaceGenerator, int imageWidth, int imageHeight,
			int windowWidth, int windowStep, int numFeatures) {
		this.subspaceGenerator = subspaceGenerator;
		this.originalWidth = imageWidth;
		this.originalHeight = imageHeight;
		this.numFeatures = numFeatures;
		this.windowStep = windowStep;
		this.windowWidth = windowWidth;
	}

	private Sample createLocalSample(Sample originalSample, RegionDescriptor regionDescriptor) {

		Sample localSample = new Sample();
		localSample.initAndSetDataSize(regionDescriptor.getsize());
		localSample.setFileName(originalSample.getFileName());
		localSample.setClassName(originalSample.getClassName());

		for (int i = 0; i < regionDescriptor.getsize(); i++) {
			localSample.addData(originalSample.getDataOfIndex(regionDescriptor.getIndexFromList(i)));
		}

		return localSample;
	}

	private void initLocalRegionDescriptor(LocalSubspace localSubspace) throws CustomException {

		try {
			int nstepsx = (originalWidth - windowWidth) / windowStep + 1;
			int nstepsy = (originalHeight - windowWidth) / windowStep + 1;

			localSubspace.setLocalDescriptorListSize(nstepsx * nstepsy);

			int x, y;
			y = 0;

			for (int i = 0; i < nstepsy; i++) {
				x = 0;
				for (int j = 0; j < nstepsx; j++) {
					RegionDescriptor descriptor = new RegionDescriptor();
					descriptor.initDescriptor(originalWidth, originalHeight, windowWidth, x, y);
					localSubspace.addDescriptor(descriptor);
					x += windowStep;
				}
				y += windowStep;
			}

			logger.debug("LocalDescriptor size :" + localSubspace.getLocalDescriptorListSize());

		} catch (Exception ex) {
			throw new CustomException("Exception while initialize LocalRegionDescriptor", ex);
		}
	}

	private void createLocalSubspaces(SampleContainer originalSampleSet, LocalSubspace localSubspaces)
			throws CustomException {
		try {
			if (localSubspaces != null) {

				localSubspaces.setLocalSubspaceListSize(localSubspaces.getLocalDescriptorListSize());
				SampleContainer localContainer = null;
				Subspace subspace = null;

				for (int i = 0; i < localSubspaces.getLocalDescriptorListSize(); i++) {
					localContainer = new SampleContainer();
					for (int j = 0; j < originalSampleSet.getSize(); j++) {
						Sample sample = createLocalSample(originalSampleSet.getSample(j),
								localSubspaces.getLocalDescriptor(i));
						localContainer.addSample(sample);
					}

					logger.debug("Generating Subspace for DescriptorIndex:" + i + ". Total sample size: " + localContainer.getSize());

					subspace = this.subspaceGenerator.generateSubspace(localContainer);
					localSubspaces.addSubspace(subspace);
				}
			}

		} catch (Exception ex) {
			throw new CustomException("Excpetion while creating localsubspace for regions", ex);
		}
	}

	private void mergeSubspaces(LocalSubspace localSubspaces) throws CustomException {

		try {

		} catch (Exception ex) {
			throw new CustomException("Excpetion while mearging localsubspace of regions", ex);
		}
	}

	// creates local subspace (subspace) from the sample set (samples)
	public LocalSubspace generateSubspace(SampleContainer samples) {

		logger.info("Generating Subspace");

		LocalSubspace localSubspace = new LocalSubspace();

		try {
			logger.info("init Local Descriptor");
			initLocalRegionDescriptor(localSubspace);

			logger.info("Creating Local subspaces for regions");
			createLocalSubspaces(samples, localSubspace);

			logger.info("mearging Local subspaces of regions");
			mergeSubspaces(localSubspace);

		} catch (Exception ex) {
			if (ex instanceof CustomException) {
				logger.error(((CustomException) ex).getMessage() + " - "
						+ ((CustomException) ex).getCapturedException().getMessage());
			}
		}
		return localSubspace;
	}

}
