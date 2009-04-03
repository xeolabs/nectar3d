/*
 * Copyright (c) 2007 Lindsay S. Kay, All Rights Reserved
 *
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 * claim that you wrote the original software. If you use this software
 * in a product, an acknowledgment in the product documentation would be
 * appreciated but is not required.
 *
 * 2. Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software.
 *
 * 3. This notice may not be removed or altered from any source
 * distribution.
 */
package com.neocoders.nectar3d.renderer;

import java.awt.Graphics;
import java.util.Iterator;

import com.neocoders.nectar3d.common.Point2;
import com.neocoders.nectar3d.scene.Selector;

class DisplayListLayer {
	public DisplayListLayer(boolean toDepthSort) {
		this.toDepthSort = toDepthSort;
		list = new SortableVector(1000);
		// list.removeAllElements();
		highlightStringSelectors = null;
		depthSortPending = false;
	}

	public void setDepthSort(boolean toDepthSort) {
		this.toDepthSort = toDepthSort;
	}

	public void clear() {
		list.removeAllElements();
		highlightStringSelectors = null;
	}

	public void add(AbstractDisplayElement element) {
		list.addElement(element);
		if (toDepthSort) {
			depthSortPending = true;
		}
	}

	public void setHighlighted(Selector[] selectors) {
		highlightStringSelectors = selectors;
	}

	public void render(Graphics g) {
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			render(g, (AbstractDisplayElement) iterator.next());
		}
	}

	private void render(Graphics g, AbstractDisplayElement element) {
		if (toDepthSort && depthSortPending) {
			list.sort(new DepthComparator(), SortableVector.SORT_ASCENDING);
			depthSortPending = false;
		}
		Selector selector = element.getPickInfo().getSelector();
		boolean highlight = (selector != null && toHighlight(selector));
		element.render(g, highlight);
	}

	private boolean toHighlight(Selector selector) {
		if (highlightStringSelectors == null) {
			return false;
		}
		for (int i = 0; i < highlightStringSelectors.length; i++) {
			if (selector.compare(highlightStringSelectors[i]) == 0) {
				return true;
			}
		}
		return false;
	}

	public PickInfo pick(Point2 pos) {
		PickInfo id = null;
		for (int i = list.size() - 1; i >= 0; i--) { // find closest pick
			// first
			if (((AbstractDisplayElement) list.elementAt(i)).tryPick(pos)) {
				id = ((AbstractDisplayElement) list.elementAt(i)).getPickInfo();
				if (id != null) {
					return id;
				}
			}
		}
		return null;
	}

	private boolean toDepthSort;

	private SortableVector list;

	private Selector[] highlightStringSelectors; // elements with these

	// selectors get highlighted
	// when rendered

	private boolean depthSortPending;
}
