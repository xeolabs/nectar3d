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
package com.neocoders.nectar3d.scene.util.builder;

import java.util.Vector;
import java.util.Iterator;
import java.awt.Color;
import java.awt.Font;

import com.neocoders.nectar3d.common.Point3;
import com.neocoders.nectar3d.common.Vector3;
import com.neocoders.nectar3d.scene.SceneElement;
import com.neocoders.nectar3d.scene.Selector;

abstract class AbstractBuilder {
	public AbstractBuilder(AbstractContext context, AbstractBuilder parent) {
		this.context = context;
		this.parent = parent;
		children = new Vector();
		element = null;
	}

	public AbstractContext getContext() {
		return context;
	}

	public AbstractBuilder openEnvironment() {
		context.handleError("an environment element cannot be a child of "
				+ getDescription());
		return null;
	}

	public AbstractBuilder openSwitch() {
		context.handleError("a switch element cannot be a child of "
				+ getDescription());
		return null;
	}

	public void addLightSource(Vector3 dir, Color color) {
		context.handleError("" + getDescription()
				+ " cannot have a light source");
	}

	public AbstractBuilder openGeometry() {
		context.handleError("an element of geometry cannot be a child of "
				+ getDescription());
		return null;
	}

	public void addFace() {
		context.handleError("" + getDescription() + " cannot have a face");
	}

	public void addVertex(double x, double y, double z) {
		context.handleError("" + getDescription() + " cannot have a vertex");
	}

	public void addVertexIndex(int index) {
		context.handleError("" + getDescription()
				+ " cannot have a vertex index");
	}

	public AbstractBuilder openLabel() {
		context.handleError("a label cannot be a child of " + getDescription());
		return null;
	}

	public void setText(String text) {
		context.handleError("" + getDescription() + " cannot have text");
	}

	public void setFont(Font font) {
		context.handleError("" + getDescription() + " cannot have a font");
	}

	public void setOffset(Point3 pos) {
		context.handleError("" + getDescription() + " cannot have an offset");
	}

	public AbstractBuilder openBox(double xSize, double ySize, double zSize) {
		context.handleError("a box element cannot be a child of a "
				+ getDescription());
		return null;
	}

	public AbstractBuilder openInterpolator(int attrStringSelector) {
		context.handleError("an interpolator cannot be a child of "
				+ getDescription());
		return null;
	}

	public void addKeyFrame(long instant, double value) {
		context.handleError("a key frame cannot be a child of "
				+ getDescription());
	}

	public AbstractBuilder openLayer(String name, boolean depthSort,
			boolean fog, boolean shade) {
		context.handleError("a layer cannot be a child of " + getDescription());
		return null;
	}

	public AbstractBuilder openTransformGroup() {
		context.handleError("a transform group cannot be a child of "
				+ getDescription());
		return null;
	}

	public void rotateX(double degrees) {
		context.handleError("" + getDescription() + " cannot be rotated");
	}

	public void rotateY(double degrees) {
		context.handleError("" + getDescription() + " cannot be rotated");
	}

	public void rotateZ(double degrees) {
		context.handleError("" + getDescription() + " cannot be rotated");
	}

	public void translate(double x, double y, double z) {
		context.handleError("" + getDescription() + " cannot be translated");
	}

	public void scale(double x, double y, double z) {
		context.handleError("" + getDescription() + " cannot be scaled");
	}

	public AbstractBuilder openAppearance() {
		context.handleError("material properties cannot be a child of "
				+ getDescription());
		return null;
	}

	public void setFillColor(Color fillColor) {
		context
				.handleError("" + getDescription()
						+ " cannot have a fill color");
	}

	public void setHighlightEdgeColor(Color edgeColor) {
		context.handleError("" + getDescription()
				+ " cannot have a highlight edge color");
	}

	public void setHighlightFillColor(Color fillColor) {
		context.handleError("" + getDescription()
				+ " cannot have a highlight fill color");
	}

	public void setEdgeColor(Color edgeColor) {
		context.handleError("" + getDescription()
				+ " cannot have an edge color");
	}

	public AbstractBuilder openName(Selector selector) {
		context
				.handleError("a name cannot be a child of a "
						+ getDescription());
		return null;
	}

	public AbstractBuilder close() {
		SceneElement element = buildElement();
		if (element == null) {
			// error ocurred - did not build element
			return parent; // but we can recover
		}
		Iterator i = children.iterator();
		while (i.hasNext()) {
			element.addChild((SceneElement) i.next());
		}
		if (parent != null) {
			parent.children.addElement(element);
		}
		return parent;
	}

	/** Template method to return created scene element */
	public abstract SceneElement buildElement();

	protected abstract String getDescription();

	public SceneElement getElement() {
		return element;
	}

	private AbstractContext context;

	private AbstractBuilder parent;

	private Vector children;

	protected SceneElement element;
}
