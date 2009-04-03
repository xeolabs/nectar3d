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

import java.util.Vector;

/**
 * A generic sorting vector
 * 
 * @author lindsay
 */
class SortableVector extends Vector {
	/** specifies ascending-order sort */
	public static final int SORT_ASCENDING = 1;

	/** specifies descending-order sort */
	public static final int SORT_DESCENDING = 2;

	public SortableVector() {
		super();
	}

	/** Constructs SortableVector with given initial capacity */
	public SortableVector(int initCapacity) {
		super(initCapacity);
	}

	/**
	 * Sorts the vector
	 * 
	 * @param comp
	 *            a comparison object to compare elements with
	 * @param order
	 *            the order in which to sort - SORT_ASCENDING or SORT_DESCENDING
	 */
	public void sort(Comparator comp, int order) {
		try {
			compare = comp;
			if (order == SORT_ASCENDING) {
				quickSortAscending(0, this.size() - 1);
			} else {
				quickSortDescending(0, this.size() - 1);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("INTERNAL ERROR: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Performs recursive ascending Quicksort on a segment of elements
	 * 
	 * @param left
	 *            the first element
	 * @param right
	 *            the last element
	 */
	private void quickSortAscending(int left, int right) {
		if (right > left) {
			Object o1 = this.elementAt(right);
			int i = left - 1;
			int j = right;
			while (true) {
				while (compare.lessThan(this.elementAt(++i), o1))
					;
				while (j > 0)
					if (compare.lessThanOrEqual(this.elementAt(--j), o1))
						break; // out of while
				if (i >= j)
					break;
				swap(i, j);
			}
			swap(i, right);
			quickSortAscending(left, i - 1);
			quickSortAscending(i + 1, right);
		}
	}

	/**
	 * Performs recursive decsending Quicksort on a segment of elements
	 * 
	 * @param left
	 *            the first element
	 * @param right
	 *            the last element
	 */
	private void quickSortDescending(int left, int right) {
		if (right > left) {
			Object o1 = this.elementAt(right);
			int i = left - 1;
			int j = right;
			while (true) {
				while (compare.lessThan(o1, this.elementAt(++i)))
					;
				while (j > 0)
					if (compare.lessThanOrEqual(o1, this.elementAt(--j)))
						break; // out of while
				if (i >= j)
					break;
				swap(i, j);
			}
			swap(i, right);
			quickSortDescending(left, i - 1);
			quickSortDescending(i + 1, right);
		}
	}

	/**
	 * Swaps two elements
	 * 
	 * @param loc1
	 *            location of first element
	 * @param loc2
	 *            location of second element
	 */
	private void swap(int loc1, int loc2) {
		Object tmp = this.elementAt(loc1);
		this.setElementAt(this.elementAt(loc2), loc1);
		this.setElementAt(tmp, loc2);
	}

	private Comparator compare;
}
