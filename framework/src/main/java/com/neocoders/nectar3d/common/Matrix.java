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
package com.neocoders.nectar3d.common;

public class Matrix {
	private double[][] e;

	public Matrix() {
		e = new double[4][4];
		identity();
	}

	public void identity() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == j) {
					e[i][j] = 1.0;
				} else {
					e[i][j] = 0.0;
				}
			}
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("\n");
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				sb.append(" " + e[i][j]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public void lookat(Point3 eye, Point3 look, Vector3 up) {
		Vector3 eyeSubLook = new Vector3(eye.x - look.x, eye.y - look.y, eye.z
				- look.z);
		Vector3 n = Vector3.divide(eyeSubLook, Vector3.length(eyeSubLook));
		Vector3 upCrossn = Vector3.cross(up, n);
		Vector3 u = Vector3.divide(upCrossn, Vector3.length(upCrossn));
		Vector3 v = Vector3.cross(n, u);
		e[0][0] = u.x; // u
		e[0][1] = u.y;
		e[0][2] = u.z;
		e[0][3] = 0.0;
		e[1][0] = v.x; // v
		e[1][1] = v.y;
		e[1][2] = v.z;
		e[1][3] = 0.0;
		e[2][0] = n.x; // n
		e[2][1] = n.y;
		e[2][2] = n.z;
		e[2][3] = 0.0;
		e[3][0] = 0.0;
		e[3][1] = 0.0;
		e[3][2] = 0.0;
		e[3][3] = 1.0;
		translate(-eye.x, -eye.y, -eye.z);
	}

	public void perspective(Volume3 v) {
		e[0][0] = (2.0 * v.zmin) / (v.xmax - v.ymin);
		e[0][1] = 0.0;
		e[0][2] = (v.xmax + v.xmin) / (v.xmax - v.xmin);
		e[0][3] = 0.0;
		e[1][0] = 0.0;
		e[1][1] = (2.0 * v.zmin) / (v.ymax - v.ymin);
		e[1][2] = (v.ymax + v.ymin) / (v.ymax - v.ymin);
		e[1][3] = 0.0;
		e[2][0] = 0.0;
		e[2][1] = 0.0;
		e[2][2] = -((v.zmax + v.zmin) / (v.zmax - v.zmin));
		e[2][3] = (-2.0 * v.zmax * v.zmin) / (v.zmax - v.zmin);
		e[3][0] = 0.0;
		e[3][1] = 0.0;
		e[3][2] = -1.0;
		e[3][3] = 0.0;
	}

	public void rotateX(double a) {
		double t;
		a = Math.toRadians(wrapAngle(a));
		double c = Math.cos(a);
		double s = Math.sin(a);
		for (int i = 0; i < 4; i++) {
			t = e[i][1];
			e[i][1] = t * c - e[i][2] * s;
			e[i][2] = t * s + e[i][2] * c;
		}
	}

	public void rotateY(double a) {
		double t;
		a = Math.toRadians(wrapAngle(a));
		double c = Math.cos(a);
		double s = Math.sin(a);
		for (int i = 0; i < 4; i++) {
			t = e[i][0];
			e[i][0] = t * c + e[i][2] * s;
			e[i][2] = e[i][2] * c - t * s;
		}
	}

	public void rotateZ(double a) {
		double t;
		a = Math.toRadians(wrapAngle(a));
		double c = Math.cos(a);
		double s = Math.sin(a);
		for (int i = 0; i < 4; i++) {
			t = e[i][0];
			e[i][0] = t * c - e[i][1] * s;
			e[i][1] = t * s + e[i][1] * c;
		}
	}

	private double wrapAngle(double a) {
		return (a < 0.0) ? (360.0 - (Math.abs(a) % 360.0))
				: (a > 360.0) ? (a % 360) : a;
	}

	public void scale(double sx, double sy, double sz) {
		for (int i = 0; i < 4; i++) {
			e[i][0] *= sx;
			e[i][1] *= sy;
			e[i][2] *= sz;
		}
	}

	public void translate(double tx, double ty, double tz) {
		for (int i = 0; i < 4; i++) {
			e[i][0] += e[i][3] * tx;
			e[i][1] += e[i][3] * ty;
			e[i][2] += e[i][3] * tz;
		}
	}

	public void perspective(double d) {
		double f = 1.0 / d;
		for (int i = 0; i < 4; i++) {
			e[i][3] += e[i][2] * f;
			// e[i] [2] = 0.0;
		}
	}

	public Point3 transform(Point3 p) {
		return new Point3((e[0][0] * p.x) + (e[1][0] * p.y) + (e[2][0] * p.z)
				+ e[3][0], (e[0][1] * p.x) + (e[1][1] * p.y) + (e[2][1] * p.z)
				+ e[3][1], (e[0][2] * p.x) + (e[1][2] * p.y) + (e[2][2] * p.z)
				+ e[3][2], (e[0][3] * p.x) + (e[1][3] * p.y) + (e[2][3] * p.z)
				+ e[3][3]);
	}

	public Vector3 transform(Vector3 v) {
		return new Vector3((e[0][0] * v.x) + (e[1][0] * v.y) + (e[2][0] * v.z),
				(e[0][1] * v.x) + (e[1][1] * v.y) + (e[2][1] * v.z),
				(e[0][2] * v.x) + (e[1][2] * v.y) + (e[2][2] * v.z));
	}
}
