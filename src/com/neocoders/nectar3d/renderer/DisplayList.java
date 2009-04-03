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
import java.util.Hashtable;
import java.util.Vector;
import java.awt.Color;
import java.awt.Font;

import com.neocoders.nectar3d.common.Point2;
import com.neocoders.nectar3d.scene.Selector;

class DisplayList {
	public DisplayList() {
		layers = new Vector();
		layerMap = new Hashtable();
		currentLayer = null;
	}

	public void createLayer(Selector selector, boolean depthSort)
			throws DisplayException {
		if (layerMap.get(selector) != null) {
			throw new DisplayException("layer already exists");
		}
		DisplayListLayer layer = getLayer(depthSort);
		layers.addElement(layer);
		layerMap.put(selector, layer);

	}

	/**
	 * Get new layer or old one from re-use pool
	 */
	private DisplayListLayer getLayer(boolean depthSort) {
		DisplayListLayer layer;
		if (pool.size() > 0) {
			layer = (DisplayListLayer) pool.lastElement();
			pool.removeElementAt(pool.size() - 1);
			layer.setDepthSort(depthSort);
		} else {
			layer = new DisplayListLayer(depthSort);
		}
		return layer;
	}

	public boolean layerExists(Selector selector) {
		return (layerMap.get(selector) != null);
	}

	public void openLayer(Selector selector) throws DisplayException {
		if (currentLayer != null) {
			throw new DisplayException("layer already open");
		}
		DisplayListLayer layer = (DisplayListLayer) layerMap.get(selector);
		if (layer == null) {
			throw new DisplayException("layer does not exist");
		}
		currentLayer = layer;
	}

	public void closeLayer() throws DisplayException {
		if (currentLayer == null) {
			throw new DisplayException("cant close layer: no layer open");
		}
		currentLayer = null;
	}

	public void destroyLayer(Selector selector) throws DisplayException {
		DisplayListLayer layer = (DisplayListLayer) layerMap.remove(selector);
		if (layer == null) {
			throw new DisplayException("layer does not exist");
		}
		layers.remove(layer);
		putLayer(layer);
		if (currentLayer == layer) {
			currentLayer = null;
		}
	}

	/**
	 * Release layer to re-use pool
	 */
	private void putLayer(DisplayListLayer layer) {
		pool.addElement(layer);
	}

	public void clear() {
		Iterator i = layers.iterator();
		while (i.hasNext()) {
			DisplayListLayer layer = (DisplayListLayer) i.next();
			layer.clear();
			putLayer(layer);
		}
		layers.clear();
		layerMap.clear();
		currentLayer = null;
	}

	public void clearLayer() throws DisplayException {
		if (currentLayer == null) {
			throw new DisplayException("canr clear: no layer open");
		}
		currentLayer.clear();
	}

	public void addPolygon(PickInfo pickInfo, double depth, int[] sx, int[] sy,
			Color fillColor, Color highlightFillColor, Color edgeColor)
			throws DisplayException {
		if (currentLayer == null) {
			throw new DisplayException("cant add polygon: no layer open");
		}
		currentLayer.add(new PolygonDisplayElement(pickInfo, depth, sx, sy,
				fillColor, highlightFillColor, edgeColor));
	}

	public void addText(PickInfo id, double depth, Point2 anchor,
			Point2 offset, String text, Font font, Color color)
			throws DisplayException {
		if (currentLayer == null) {
			throw new DisplayException("cant add text: no layer open");
		}
		currentLayer.add(new LabelDisplayElement(id, depth, anchor, offset,
				text, font, color));
	}

	public void render(Graphics g) {
		DisplayList displayList;
		Iterator i = layers.iterator();
		while (i.hasNext()) {
			((DisplayListLayer) i.next()).render(g);
		}
	}

	public PickInfo pick(Point2 sc) {
		PickInfo pickInfo;
		Iterator i = layers.iterator();
		while (i.hasNext()) {
			pickInfo = ((DisplayListLayer) i.next()).pick(sc);
			if (pickInfo != null) {
				return pickInfo;
			}
		}
		return null;
	}

	public void setHighlighted(Selector[] selectors) {
		Iterator i = layers.iterator();
		while (i.hasNext()) {
			((DisplayListLayer) i.next()).setHighlighted(selectors);
		}
	}

	private Vector layers;

	private Hashtable layerMap;

	private static Vector pool = new Vector();

	/**
	 * @supplierCardinality 0..*
	 * @clientCardinality 1
	 */
	private DisplayListLayer currentLayer;

	/** @link dependency */
	/* # DepthComparator lnkDepthComparator; */

	/** @link dependency */
	/* # DisplayException lnkDisplayException; */
}
