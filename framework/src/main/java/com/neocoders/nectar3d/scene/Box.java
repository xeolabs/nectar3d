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
package com.neocoders.nectar3d.scene;

import com.neocoders.nectar3d.common.Point3;
import com.neocoders.nectar3d.common.Vector3;

/////////////////////////////////////////////
// Optimize this to use common static storage
/////////////////////////////////////////////

public class Box extends Geometry {
    /**Create new box of required dimensions
     */
    public Box(double xSize, double ySize, double zSize) {
        super(new Point3[vertexData.length], new Face[6]);
        for (int i = 0; i < verts.length; i++) {
            verts[i] = new Point3(vertexData[i].x * xSize, vertexData[i].y * ySize, vertexData[i].z * zSize);
        }
        for (int i = 0; i < 6; i++) {
            faces[i] = new Face(polyData[i]);
        }
    }

    /** Vertices */
    private static final Point3[] vertexData = {
            new Point3(1.0, 1.0, 1.0),
            new Point3(-1.0, 1.0, 1.0),
            new Point3(-1.0, -1.0, 1.0),
            new Point3(1.0, -1.0, 1.0),
            new Point3(1.0, 1.0, -1.0),
            new Point3(-1.0, 1.0, -1.0),
            new Point3(-1.0, -1.0, -1.0),
            new Point3(1.0, -1.0, -1.0)};

    /** Faces are lists of vertex indices */
    private final static int[] [] polyData =
        { { 0,1,2,3 },  { 0, 3, 7, 4 }, { 0,4,5,1 }, { 1,5,6,2 }, { 2,6,7,3 }, { 5,4,7,6 }};

    /** ID for each face */
    public final static int XMIN_FACE_INDEX = 3;
    public final static int YMIN_FACE_INDEX = 4;
    public final static int ZMIN_FACE_INDEX = 5;
    public final static int XMAX_FACE_INDEX = 1;
    public final static int YMAX_FACE_INDEX = 2;
    public final static int ZMAX_FACE_INDEX = 0;
}
