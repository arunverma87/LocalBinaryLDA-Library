/**
 *
 */
package uas.facerecognition.lblda.lib;

import java.util.List;

import org.jblas.ComplexDoubleMatrix;
import org.jblas.DoubleMatrix;

/**
 * @author arunv
 *
 */
public class Subspace {

	private SubspaceType type; // SubSpace Type
	private int subspaceDim; // dimensionality of the subspace
	private int originalDim; // original dimensionality of samples

	private List<Double> centerOffset;
	private List<Double> subspaceAxes;
	private List<Double> axesCriterionFn;

	/**
	 *
	 */
	public Subspace() {
		type = null;
		subspaceDim = 0;
		originalDim = 0;
		centerOffset = null;
		subspaceAxes = null;
		axesCriterionFn = null;
	}

	public SubspaceType getType() {
		return type;
	}

	public int getSubspaceDim() {
		return subspaceDim;
	}

	public int getOriginalDim() {
		return originalDim;
	}

	public List<Double> getCenterOffset() {
		return centerOffset;
	}

	public List<Double> getSubspaceAxes() {
		return subspaceAxes;
	}

	public List<Double> getAxesCriterionFn() {
		return axesCriterionFn;
	}

	public void setSubspaceData(SubspaceType type, int subspaceDim, int originalDim, List<Double> centerOffset,
			List<Double> subspaceAxes, List<Double> axesCriterionFn) {
		this.type = type;
		this.subspaceDim = subspaceDim;
		this.originalDim = originalDim;
		this.centerOffset = centerOffset;
		this.subspaceAxes = subspaceAxes;
		this.axesCriterionFn = axesCriterionFn;
	}

	public void normalize() {

		double norm;
		int i, j;
		for (i = 0; i < subspaceDim; i++) {
			double sum = 0;
			for (j = 0; j < originalDim; j++) {
				sum += subspaceAxes.get(i * originalDim + j) * subspaceAxes.get(i * originalDim + j);
			}
			norm = Math.sqrt(sum);
			if (norm == 0)
				continue;
			for (j = 0; j < originalDim; j++) {
				subspaceAxes.add(i * originalDim + j, subspaceAxes.get(i * originalDim + j) / norm);
			}
		}

	}

	public void reorderDescending(){
		int i,j;
		double max,tmpd;
		int maxi;
		for(i=0;i<subspaceDim;i++) {
			max = axesCriterionFn.get(i);
			maxi = i;
			for(j=i+1;j<subspaceDim;j++) {
				if(axesCriterionFn.get(j)>max) {
					max = axesCriterionFn.get(j);
					maxi = j;
				}
			}
			if(maxi!=i) {
				tmpd = axesCriterionFn.get(i);
				axesCriterionFn.add(i, axesCriterionFn.get(maxi));
				axesCriterionFn.add(maxi,tmpd);
			}
		}
	}

	public void trim(int dim) {

		if(dim > subspaceDim) return;
		subspaceDim = dim;
		subspaceAxes = subspaceAxes.subList(0, dim);
	}
}
