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
package com.neocoders.cubeGui.panel;

import java.awt.BorderLayout;

import com.neocoders.nectar3d.scene.Observer;
import com.neocoders.nectar3d.renderer.AWTSceneRenderer;
import com.neocoders.nectar3d.renderer.SceneRendererListener;
import com.neocoders.nectar3d.renderer.SceneRendererEvent;
import com.neocoders.nectar3d.renderer.SceneRendererParams;
import com.neocoders.nectar3d.common.Volume3;
import com.neocoders.nectar3d.common.Window2;
import com.neocoders.nectar3d.common.Point3;
import com.neocoders.nectar3d.common.Vector3;
import java.awt.Color;
import com.neocoders.nectar3d.common.Point2;
import com.neocoders.nectar3d.renderer.PickInfo;
import com.neocoders.nectar3d.scene.Selector;

public class CubeGUIPanel extends AWTSceneRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6646555695585703184L;

	public CubeGUIPanel() {
		super();
		setLayout(new BorderLayout());
		cubeMatrix = null;
		sliceFactory = null;
		dragging = false;
		highlightedSelector = null;
		init();
	}

	private void init() {
		try {
			/*
			 * Set up renderer parameters
			 * 
			 */
			SceneRendererParams params = new SceneRendererParams();
			params.setFrustum(new Volume3(-130.0, -130.0, -300.0, 130.0, 130.0,
					-100.0));
			params.setWindow(new Window2(0, 0, 400, 400));
			params.setVPDist(-300.0);
			params.setEye(new Point3(0.0, 0.0, 40.0));
			params.setLook(new Point3(0.0, 0.0, 0.0));
			params.setUp(new Vector3(0.0, 1.0, 0.0));
			params.setBackgroundColor(new Color(220, 220, 255));
			/*
			 * Set up renderer listeners
			 * 
			 */
			setListener(new SceneRendererListener() {
				public void handleSceneViewEvent(SceneRendererEvent e) {
					switch (e.getType()) {
					case SceneRendererEvent.ERROR:
						System.out.println((String) e.getData());
						break;
					case SceneRendererEvent.MOUSE_DRAGGED:
						handleMouseDragged((Point2) e.getData());
						break;
					case SceneRendererEvent.MOUSE_MOVED:
						handleMouseMoved((Point2) e.getData());
						break;
					case SceneRendererEvent.MOUSE_OVER:
						handleMouseOver((PickInfo) e.getData());
						break;
					case SceneRendererEvent.MOUSE_PRESSED:
						handleMousePressed((Point2) e.getData());
						break;
					case SceneRendererEvent.MOUSE_RELEASED:
						handleMouseReleased((Point2) e.getData());
						break;
					case SceneRendererEvent.MOUSE_PICKED:
						handleMousePicked((PickInfo) e.getData());
						break;
					default:
					}
				}
			});
			setParams(params);
			guiState = STATE_INITIAL;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			/*
			 * Set up scene
			 * 
			 */
			CubeMatrixBuilder builder = new CubeMatrixBuilder();
			builder.build();
			for (int i = 0; i < 4; i++) {

				builder.openColumn("column " + i);

				for (int j = 0; j < 4; j++) {
					builder.openRow("row " + j);
					for (int k = 0; k < 4; k++) {
						builder.addEntry("entry " + i + ", " + j + ", " + k);
					}
					builder.close();
				}
				builder.close();
			}
			cubeMatrix = builder.getBuilt();
			cubeMatrix.attachObserver( // to keep state consistent with
					// CubeMatrix
					new Observer() {
						public void update() {
							switch (cubeMatrix.getState()) {
							case CubeMatrix.STATE_DEFAULT:
								guiState = STATE_BROWSING_MATRIX;
								break;
							case CubeMatrix.STATE_EXTRACTING_SLICE:
								guiState = STATE_EXTRACTING_SLICE;
								break;
							case CubeMatrix.STATE_RESTORING_SLICE:
								guiState = STATE_RESTORING_SLICE;
								break;
							case CubeMatrix.STATE_SLICE_EXTRACTED:
								guiState = STATE_BROWSING_SLICE;
								break;
							default:
								break;
							}
						}
					});
			sliceFactory = new SliceFactory(cubeMatrix.getXSize(), cubeMatrix
					.getYSize(), cubeMatrix.getZSize());
			guiState = STATE_BROWSING_MATRIX;

			/*
			 * Start rendering
			 * 
			 */
			setScene(cubeMatrix.getScene());
			repaint();
		} catch (BuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleMousePressed(Point2 pos) {
		mousePressedPos = pos;
		mouseReleasedPos = null;
		lastMouseDragPos = null;
	}

	private void handleMouseMoved(Point2 pos) {
	}

	private void handleMouseReleased(Point2 pos) {
		switch (guiState) {
		case STATE_BROWSING_MATRIX:
			break;
		case STATE_BROWSING_SLICE:
			restoreSlice();
			break;
		default:
			break; // satisfy compiler
		}
		mousePressedPos = null;
		mouseReleasedPos = pos;
		lastMouseDragPos = null;
	}

	private void handleMouseDragged(Point2 pos) {
		if (guiState != STATE_BROWSING_MATRIX) {
			return;
		}
		if (lastMouseDragPos != null) {
			rotateCubeMatrix(lastMouseDragPos, pos);
		}
		lastMouseDragPos = pos;
	}

	private void handleMousePicked(PickInfo id) {
		switch (guiState) {
		case STATE_BROWSING_MATRIX:
			extractSlice(id);
			break;
		case STATE_BROWSING_SLICE:
			restoreSlice();
			break;
		default:
		}
	}

	private void extractSlice(PickInfo id) {
		Selector selector = id.getSelector();
		int primitiveIndex = id.getPrimitiveIndex();
		if (selector instanceof CubeSelector) {
			CubeSelector cubeSelector = (CubeSelector) selector;
			Slice s = sliceFactory.getSlice(cubeSelector, primitiveIndex);
			if (s != null) // null if clicked on illegal cube face
			{
				slice = s;
				guiState = STATE_EXTRACTING_SLICE;
				cubeMatrix.extractSlice(slice, primitiveIndex);
				setScene(cubeMatrix.getScene());
			}
		}
	}

	private void restoreSlice() {
		guiState = STATE_RESTORING_SLICE;
		cubeMatrix.restoreSlice();
		setScene(cubeMatrix.getScene());
	}

	private void handleMouseOver(PickInfo id) {
		switch (guiState) {
		case STATE_BROWSING_MATRIX:
			highlightSlice(id);
			break;
		case STATE_BROWSING_SLICE:
			highlightSliceCube(id);
			break;
		default:
		}
	}

	private void highlightSlice(PickInfo id) {
		Selector selector = id.getSelector();
		int primitiveIndex = id.getPrimitiveIndex();
		if (selector instanceof CubeSelector) {
			CubeSelector cubeSelector = (CubeSelector) selector;
			Slice s = sliceFactory.getSlice(cubeSelector, primitiveIndex);
			if (s != null) {
				highlight(s.getCubes());
			}
		}
	}

	private void highlightSliceCube(PickInfo id) {
		Selector selector = id.getSelector();
		if (!(selector instanceof CubeSelector)) {
			return;
		}
		if (slice.containsCube((CubeSelector) selector)) {
			Selector[] selectors = { selector };
			highlight(selectors);
		}
	}

	private void rotateCubeMatrix(Point2 mousePressedPos, Point2 mouseDragPos) {
		int xDif = mouseDragPos.x - mousePressedPos.x;
		int yDif = mouseDragPos.y - mousePressedPos.y;
		double yawInc = (double) xDif;
		double pitchInc = (double) yDif;
		double pitch = cubeMatrix.getMatrixPitch() - pitchInc;
		double yaw = wrapAngle(cubeMatrix.getMatrixYaw() + yawInc);
		pitch = (pitch < -90.0) ? -90.0 : (pitch > 0.0) ? 0.0 : pitch;
		yaw = wrapAngle(yaw);
		cubeMatrix.setMatrixPitch(pitch);
		cubeMatrix.setMatrixYaw(yaw);
		setScene(cubeMatrix.getScene());
	}

	private double wrapAngle(double a) {
		return (a < 0.0) ? (360.0 - (Math.abs(a) % 360.0))
				: (a > 360.0) ? (a % 360) : a;
	}

	private CubeMatrix cubeMatrix;

	private SliceFactory sliceFactory;

	private Slice slice;

	private CubeSelector highlightedSelector;

	private SceneRendererParams rendererParams;

	private boolean dragging;

	private Point2 mousePressedPos;

	private Point2 mouseReleasedPos;

	private Point2 lastMouseDragPos;

	private int guiState;

	private final static int STATE_INITIAL = 0;

	private final static int STATE_BROWSING_MATRIX = 1;

	private final static int STATE_EXTRACTING_SLICE = 2;

	private final static int STATE_RESTORING_SLICE = 3;

	private final static int STATE_BROWSING_SLICE = 4;
}
