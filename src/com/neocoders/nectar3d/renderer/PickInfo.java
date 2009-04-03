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

import com.neocoders.nectar3d.scene.Selector;

/**
 * Specifies a pick selection that has been made. The selection specifies the
 * identity of a picked scene element, and optionally the index of the primitive
 * within the element that was actually selected. The primitive index is useful
 * for specifying things like which face of a polyhedron was selected when a
 * polyhedron was picked, for example.
 */
public class PickInfo {
	/**
	 * Create pick information specifying the identity picked scene element and
	 * the index of the primitive that was actually picked within the element
	 * 
	 * @param identifier
	 *            for picked scene element
	 * @param index
	 *            of selected primitive in scene element
	 */
	public PickInfo(Selector selector, int primitiveIndex) {
		this.selector = selector;
		this.primitiveIndex = primitiveIndex;
	}

	/**
	 * Create pick info specifying the only the identity of the picked scene
	 * element. The picked primitive index will be set to UNDEFINED_INDEX.
	 * 
	 * @param identity
	 *            of picked scene element
	 */
	public PickInfo(Selector selector) {
		this.selector = selector;
		this.primitiveIndex = UNDEFINED_INDEX;
	}

	/**
	 * Get the identity of the picked element
	 * 
	 * @return identity of element
	 */
	public Selector getSelector() {
		return selector;
	}

	/**
	 * Get index of the picked primitive within the picked element, if defined.
	 * The index will be UNDEFINED_INDEX if no index is defined.
	 * 
	 * @return index of element's primitive
	 */
	public int getPrimitiveIndex() {
		return primitiveIndex;
	}

	private Selector selector;

	private int primitiveIndex;

	private static final int UNDEFINED_INDEX = -1;
}
