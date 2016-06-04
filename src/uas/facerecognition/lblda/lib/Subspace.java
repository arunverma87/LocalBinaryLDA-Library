/**
 *
 */
package uas.facerecognition.lblda.lib;

import java.io.Serializable;
import java.util.List;

import org.jblas.DoubleMatrix;

/**
 * @author arunv
 *
 */
public class Subspace implements Serializable {

	private static final transient long serialVersionUID = 1L;

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

	public DoubleMatrix getSubspaceAxesAsMatrix(int row, int column) {
		DoubleMatrix mat = DoubleMatrix.zeros(row, column);

		for (int i = 0; i < row; i++)
			for (int j = 0; j < column; j++)
				mat.put(i, j, subspaceAxes.get(i * column + j));

		return mat;
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
				subspaceAxes.set(i * originalDim + j, subspaceAxes.get(i * originalDim + j) / norm);
			}
		}

	}

	public void sortInDescending(boolean usingAbs) {
		int i, j;
		double max, tmpd;
		int maxi;
		for (i = 0; i < subspaceDim; i++) {
			max = axesCriterionFn.get(i);
			maxi = i;
			for (j = i + 1; j < subspaceDim; j++) {
				if (!usingAbs)
					if (axesCriterionFn.get(j) > max) {
						max = axesCriterionFn.get(j);
						maxi = j;
					} else if (Math.abs(axesCriterionFn.get(j)) > Math.abs(max)) {
						max = axesCriterionFn.get(j);
						maxi = j;
					}
			}
			if (maxi != i) {

				List<Double> temp = subspaceAxes.subList((i * originalDim), (i * originalDim) + originalDim);

				for (int setI = 0; setI < originalDim; setI++) {
					subspaceAxes.set(setI + (i * originalDim), subspaceAxes.get(setI + (maxi * originalDim)));
					subspaceAxes.set(setI + (maxi * originalDim), temp.get(setI));
				}
				temp.clear();
				temp = null;

				tmpd = axesCriterionFn.get(i);
				axesCriterionFn.set(i, axesCriterionFn.get(maxi));
				axesCriterionFn.set(maxi, tmpd);
			}
		}
	}

	public void retainAll(int dim) {

		if (dim > subspaceDim)
			return;
		subspaceDim = dim;
		subspaceAxes = subspaceAxes.subList(0, subspaceDim * originalDim);
		axesCriterionFn = axesCriterionFn.subList(0, subspaceDim);
	}
}
