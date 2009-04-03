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

import java.awt.Panel;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.LayoutManager;

class DoubleBufferedPanel extends Panel {
	private int bufferWidth;

	private int bufferHeight;

	private Image bufferImage;

	private Graphics bufferGraphics;

	public DoubleBufferedPanel() {
		super();
	}

	public DoubleBufferedPanel(LayoutManager layout) {
		super(layout);
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		if (bufferWidth != this.getWidth() || bufferHeight != this.getHeight()
				|| bufferImage == null || bufferGraphics == null) {
			resetBuffer();
		}
		if (bufferGraphics != null) {
			bufferGraphics.clearRect(0, 0, bufferWidth, bufferHeight);
			paintBuffer(bufferGraphics);
			g.drawImage(bufferImage, 0, 0, this);
		}
	}

	/** Override this to do the actual rendering */
	public void paintBuffer(Graphics g) {
	}

	private void resetBuffer() {
		bufferWidth = this.getWidth();
		bufferHeight = this.getHeight();
		if (bufferGraphics != null) {
			bufferGraphics.dispose();
			bufferGraphics = null;
		}
		if (bufferImage != null) {
			bufferImage.flush();
			bufferImage = null;
		}
		System.gc();
		bufferImage = this.createImage(bufferWidth, bufferHeight);
		bufferGraphics = bufferImage.getGraphics();
	}
}
