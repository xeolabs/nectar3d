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
import java.awt.Color;
import java.awt.Font;

import com.neocoders.nectar3d.common.Point2;

class LabelDisplayElement extends AbstractDisplayElement {
	public LabelDisplayElement(PickInfo id, double depth, Point2 anchor,
			Point2 offset, String text, Font font, Color color) {
		super(id, depth);
		this.anchor = anchor;
		this.offset = offset;
		this.text = text;
		this.font = font;
		this.color = color;
	}

	public void render(Graphics g, boolean toHighlight) {
		g.setFont(font);
		if (toHighlight) {
			g.setColor(color.brighter().brighter());
		} else {
			g.setColor(color);
		}
		g.drawString(text, offset.x, offset.y);
		g.drawLine(anchor.x, anchor.y, offset.x, offset.y);
	}

	public Font getFont() {
		return font;
	}

	public double getDepth() {
		return depth;
	}

	public boolean tryPick(Point2 pos) {
		return false; // cant pick text yet
	}

	private String text;

	private Font font;

	private Color color;

	private double depth;

	private Point2 anchor;

	private Point2 offset;
}
