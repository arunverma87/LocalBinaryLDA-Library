/**
 *
 */
package uas.facerecognition.lblda.lib.local;

import uas.facerecognition.lblda.lib.Sample;
import uas.facerecognition.lblda.lib.SampleContainer;

/**
 * @author arunv
 *
 */
public class LocalSubspaceProjector {

	private LocalSubspace localSubspace;

	/**
	 * @param localSubspace
	 */
	public LocalSubspaceProjector(LocalSubspace localSubspace) {
		this.localSubspace = localSubspace;
	}


	public void projectSample(Sample originalSample, Sample projectedSample, int dim){

	}

	public void projectSampleContainer(SampleContainer originalSampleContainer, SampleContainer projectedSampleContainer, int dim){

	}



}
