/**
 *
 */
package uas.facerecognition.lblda.lib.local;

import java.util.ArrayList;
import java.util.List;

/**
 * @author arunv
 *
 */
public class RegionDescription {
	private List<Long> indices;

	public RegionDescription() {
		indices = new ArrayList<>();
	}

	/**
	 * @return the indices
	 */
	public List<Long> getIndices() {
		return indices;
	}

	/**
	 * @param indices
	 *            the indices to set
	 */
	public void setIndices(List<Long> indices) {
		this.indices = indices;
	}

	public void inIt(long originalWidth, long originalHeight, long localWidth, long posX, long posY) {
		long minx, miny, maxx, maxy;
		minx = posX;
		miny = posY;
		maxx = posX + localWidth;
		maxy = posY + localWidth;

		if (maxx > originalWidth)
			maxx = originalWidth;
		if (maxy > originalHeight)
			maxy = originalHeight;

		long x, y;
		for (y = miny; y < maxy; y++) {
			for (x = minx; x < maxx; x++) {
				indices.add(y * originalWidth + x);
			}
		}
	}
}
