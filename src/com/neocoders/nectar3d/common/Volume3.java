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

public class Volume3 {
	public Volume3(double xmin, double ymin, double zmin, double xmax,
			double ymax, double zmax) {
		this.xmin = xmin;
		this.ymin = ymin;
		this.zmin = zmin;
		this.xmax = xmax;
		this.ymax = ymax;
		this.zmax = zmax;
	}

	public Volume3(Volume3 volume) {
		this.xmin = volume.xmin;
		this.ymin = volume.ymin;
		this.zmin = volume.zmin;
		this.xmax = volume.xmax;
		this.ymax = volume.ymax;
		this.zmax = volume.zmax;
	}

	public boolean inside(Point3 p) {
		return ((p.x >= (xmin * p.w)) && (p.x <= (xmax * p.w))
				&& (p.y >= (ymin * p.w)) && (p.y <= (ymax * p.w))
				&& (p.z >= zmin) && (p.z <= zmax));
	}

	public double xmin;

	public double ymin;

	public double zmin;

	public double xmax;

	public double ymax;

	public double zmax;
}
