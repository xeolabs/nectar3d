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

public class Vector3 {
	public Vector3() {
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
	}

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3(Vector3 vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}

	public static Vector3 subtract(Vector3 a, Vector3 b) {
		return new Vector3(a.x - b.y, a.y - b.y, a.z - b.z);
	}

	public static Vector3 cross(Vector3 a, Vector3 b) {
		return new Vector3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x
				* b.y - a.y * b.x);
	}

	public static double length(Vector3 a) {
		return Math.sqrt((a.x * a.x) + (a.y * a.y) + (a.z * a.z));
	}

	public static Vector3 divide(Vector3 a, double divisor) {
		return new Vector3(a.x / divisor, a.y / divisor, a.z / divisor);
	}

	public static Vector3 scale(Vector3 a, double scalar) {
		return new Vector3(a.x * scalar, a.y * scalar, a.z * scalar);
	}

	public static Vector3 normalize(Vector3 a) {
		return divide(a, length(a));
	}

	public static double dot(Vector3 a, Vector3 b) {
		return (a.x * b.x) + (a.y * b.y) + (a.z * b.z);
	}

	public double x;

	public double y;

	public double z;
}
