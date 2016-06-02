/**
 *
 */
package uas.facerecognition.lblda.lib.local;

import java.util.ArrayList;
import java.util.List;

import uas.facerecognition.lblda.lib.Subspace;

/**
 * @author arunv
 *
 */
public class LocalSubspace {

	private List<RegionDescriptor> localDescriptor;

	private List<Subspace> localSubspaces;

	/**
	 *
	 */
	public LocalSubspace() {
		this.localDescriptor = null;
		this.localSubspaces = null;
	}

	/**
	 * @return the localDescriptor
	 */
	public List<RegionDescriptor> getLocalDescriptorList() {
		return this.localDescriptor;
	}

	public RegionDescriptor getLocalDescriptor(int index) {
		if (this.localDescriptor != null) {
			return this.localDescriptor.get(index);
		}
		return null;
	}

	public int getLocalDescriptorListSize() {
		return this.localDescriptor.size();
	}

	public void setLocalDescriptorList(List<RegionDescriptor> localDescriptor) {
		this.localDescriptor = localDescriptor;
	}

	public void setLocalDescriptorListSize(int size) {
		this.localDescriptor = new ArrayList<>(size);
	}

	public void addDescriptor(RegionDescriptor descriptor) {
		this.localDescriptor.add(descriptor);
	}

	/**
	 * @return the localSubspaces
	 */
	public List<Subspace> getLocalSubspaceList() {
		return this.localSubspaces;
	}

	public int getLocalSubspaceListSize() {
		return this.localSubspaces.size();
	}

	public void setLocalSubspaceList(List<Subspace> localSubspaces) {
		this.localSubspaces = localSubspaces;
	}

	public void setLocalSubspaceListSize(int size) {
		this.localSubspaces = new ArrayList<>(size);
	}

	public void addSubspace(Subspace subspace) {
		this.localSubspaces.add(subspace);
	}

}
