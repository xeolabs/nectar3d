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

import java.awt.event.MouseAdapter;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Graphics;

import com.neocoders.nectar3d.common.ErrorHandler;
import com.neocoders.nectar3d.common.Point2;
import com.neocoders.nectar3d.scene.SceneElement;
import com.neocoders.nectar3d.scene.Selector;

public class AWTSceneRenderer extends DoubleBufferedPanel implements
		SceneRenderer {
	public AWTSceneRenderer() {
		super();
		dragging = false;
		bindMouseEvents();
		rendererParams = new SceneRendererParams();
		this.setBackground(rendererParams.getBackgroundColor());
		renderer = new Renderer(rendererParams);
		renderer.setErrorHandler(new ErrorHandler() {
			public void handleError(String message) {
				fire(new SceneRendererEvent(SceneRendererEvent.ERROR, message));
			}
		});
		scene = null;
		listener = null;
		state = STATE_STOPPED;
	}

	public void setScene(SceneElement scene) {

		timeLastRefreshed = System.currentTimeMillis();
		this.scene = scene;
		state = STATE_RENDERING;
		repaint();
	}

	public void setParams(SceneRendererParams params) {
		// this.rendererParams = new SceneRendererParams(params);
		this.rendererParams = params;
		this.setBackground(rendererParams.getBackgroundColor());
		renderer.setParams(params);
		switch (state) {
		case STATE_STOPPED:
			break;
		case STATE_RENDERING:
			repaint();
			break;
		case STATE_HIGHLIGHTING:
			state = STATE_RENDERING;
			repaint();
			break;
		}
	}

	public void highlight(Selector[] selectors) {
		if (state != STATE_RENDERING && state != STATE_HIGHLIGHTING) {
			return;
		}
		renderer.highlight(selectors);
		state = STATE_HIGHLIGHTING;
		repaint();
	}

	public void unHighlight() {
		if (state != STATE_HIGHLIGHTING) {
			return;
		}
		renderer.unHighlight();
		state = STATE_RENDERING;
		repaint();
	}

	public void paintBuffer(Graphics g) {
		if (state == STATE_HIGHLIGHTING) {
			renderer.reRender(g);
		} else if (state == STATE_RENDERING) {
			long timeNow = System.currentTimeMillis() - timeLastRefreshed;
			rendererParams.setTimeElapsed(timeNow);
			renderer.render(scene, g);
			this.repaint();
		}
	}

	private void bindMouseEvents() {
		this.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				handleMouseReleased(e.getPoint());
			}

			public void mousePressed(MouseEvent e) {
				handleMousePressed(e.getPoint());
			}

			public void mouseReleased(MouseEvent e) {
				handleMouseReleased(e.getPoint());
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				handleMouseDragged(e.getPoint());
			}

			public void mouseMoved(MouseEvent e) {
				handleMouseMoved(e.getPoint());
			}
		});
	}

	private void handleMousePressed(Point pos) {
		Point2 sc = new Point2(pos.x, pos.y);
		PickInfo id = renderer.pick(sc);
		if (id != null) {
			mousePressedSelector = id.getSelector();
		}
		fire(new SceneRendererEvent(SceneRendererEvent.MOUSE_PRESSED, sc));
	}

	private void fire(SceneRendererEvent event) {
		if (listener != null) {
			listener.handleSceneViewEvent(event);
		}
	}

	private void handleMouseReleased(Point pos) {
		Point2 sc = new Point2(pos.x, pos.y);
		PickInfo pickInfo = renderer.pick(sc);
		if (pickInfo != null) {
			Selector mouseReleasedStringSelector = pickInfo.getSelector();
			if ((mousePressedSelector != null)
					&& (mouseReleasedStringSelector != null)) {
				if (mouseReleasedStringSelector.compare(mousePressedSelector) == 0) {
					fire(new SceneRendererEvent(
							SceneRendererEvent.MOUSE_PICKED, pickInfo));
				}
			}
		}
		fire(new SceneRendererEvent(SceneRendererEvent.MOUSE_RELEASED, sc));
	}

	private void handleMouseMoved(Point pos) {
		Point2 sc = new Point2(pos.x, pos.y);
		PickInfo id = renderer.pick(sc);
		if (id != null) {
			fire(new SceneRendererEvent(SceneRendererEvent.MOUSE_OVER, id));
		}
		fire(new SceneRendererEvent(SceneRendererEvent.MOUSE_MOVED, sc));
	}

	private void handleMouseDragged(Point pos) {
		Point2 sc = new Point2(pos.x, pos.y);
		fire(new SceneRendererEvent(SceneRendererEvent.MOUSE_DRAGGED, sc));
	}

	public void setListener(SceneRendererListener listener) {
		this.listener = listener;
	}

	public int getState() {
		return state;
	}

	private SceneElement scene;

	private Renderer renderer;

	private boolean dragging;

	private Selector mousePressedSelector;

	private int state;

	private long timeLastRefreshed;

	/** @supplierCardinality 1 */
	private SceneRendererParams rendererParams;

	/**
	 * @clientCardinality 0..*
	 * @supplierCardinality 1
	 */
	private SceneRendererListener listener;
}
