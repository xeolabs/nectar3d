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

import com.neocoders.nectar3d.common.Point3;
import com.neocoders.nectar3d.common.Vector3;
import com.neocoders.nectar3d.common.Volume3;
import com.neocoders.nectar3d.common.Window2;

import java.awt.Color;

public class SceneRendererParams {
	public SceneRendererParams() {
		frustum = new Volume3(-200.0, -200.0, -300.0, 200.0, 200.0, -100.0);
		window = new Window2(0, 0, 800, 800);
		vpDist = -500.0;
		eye = new Point3(0.0, 0.0, 0.0);
		look = new Point3(0.0, 0.0, 0.0);
		up = new Vector3(0.0, 1.0, 0.0);
		timeElapsed = 0L;
		backgroundColor = new Color(255, 255, 255);
	}

	public SceneRendererParams(SceneRendererParams params) {
		this.frustum = new Volume3(params.frustum);
		this.window = new Window2(params.window);
		this.vpDist = params.vpDist;
		this.eye = new Point3(params.eye);
		this.look = new Point3(params.look);
		this.up = new Vector3(params.up);
		this.timeElapsed = params.timeElapsed;
		this.backgroundColor = params.backgroundColor;
	}

	public void setFrustum(Volume3 frustum) {
		this.frustum = frustum;
	}

	public Volume3 getFrustum() {
		return frustum;
	}

	public void setWindow(Window2 window) {
		this.window = window;
	}

	public Point3 getEye() {
		return eye;
	}

	public void setEye(Point3 eye) {
		this.eye = eye;
	}

	public Window2 getWindow() {
		return window;
	}

	public void setVPDist(double vpDist) {
		this.vpDist = vpDist;
	}

	public long getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeElapsed(long t) {
		this.timeElapsed = t;
	}

	public double getVPDist() {
		return vpDist;
	}

	public Point3 getLook() {
		return look;
	}

	public void setLook(Point3 look) {
		this.look = look;
	}

	public Vector3 getUp() {
		return up;
	}

	public void setUp(Vector3 up) {
		this.up = up;
	}

	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	private Volume3 frustum;

	private Point3 eye;

	private Window2 window;

	private double vpDist;

	private Point3 look;

	private Vector3 up;

	private long timeElapsed;

	private Color backgroundColor;
}
