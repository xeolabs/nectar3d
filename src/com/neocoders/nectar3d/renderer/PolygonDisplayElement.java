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

import java.awt.Color;
import java.awt.Graphics;

import com.neocoders.nectar3d.common.Point2;

class PolygonDisplayElement extends AbstractDisplayElement {
	public PolygonDisplayElement(PickInfo pickInfo, double depth, int[] sx,
			int[] sy, Color fillColor, Color highlightFillColor, Color edgeColor) {
		super(pickInfo, depth);
		this.sx = sx;
		this.sy = sy;
		this.fillColor = fillColor;
		this.highlightFillColor = highlightFillColor;
		this.edgeColor = edgeColor;
	}

	public void render(Graphics g, boolean toHighlight) {
		if (toHighlight) {
			g.setColor(highlightFillColor);
		} else {
			g.setColor(fillColor);
		}
		g.fillPolygon(sx, sy, sx.length);
		g.setColor(edgeColor);
		g.drawPolygon(sx, sy, sx.length);
	}

	public boolean tryPick(Point2 pos) {
		double totalTheta = 0.0;
		double x1;
		double y1;
		double x2;
		double y2;
		double dp;
		double cp;
		for (int i = 0; i < 4; i++) {
			int j = i + 1;
			if (j == sx.length) {
				j = 0;
			}
			x1 = (double) (sx[i] - pos.x);
			y1 = (double) (sy[i] - pos.y);
			x2 = (double) (sx[j] - pos.x);
			y2 = (double) (sy[j] - pos.y);
			dp = x1 * x2 + y1 * y2;
			cp = x1 * y2 - y1 * x2;
			totalTheta += Math.atan2(cp, dp);
		}
		return (Math.abs(totalTheta) >= Math.PI);
	}

	public int[] sx;

	public int[] sy;

	public Color fillColor;

	public Color highlightFillColor;

	public Color edgeColor;
}
