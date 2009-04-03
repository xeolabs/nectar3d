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
package com.neocoders.nectar3d.scene.util.builder;

import java.util.Vector;

import com.neocoders.nectar3d.common.Point3;
import com.neocoders.nectar3d.scene.Face;
import com.neocoders.nectar3d.scene.Geometry;
import com.neocoders.nectar3d.scene.SceneElement;
import com.neocoders.nectar3d.scene.SceneException;

import java.util.Iterator;

class GeometryBuilder extends AbstractBuilder {
    public GeometryBuilder(AbstractContext context, AbstractBuilder parent) {
        super(context, parent);
        vertsBuf = new Vector();
        faceVertsBuf = new Vector();
        facesBuf = new Vector();
        faceBuf = null;
        faceAdded = false;
        faceToCreate = false;
    }

    public void addVertex(double x, double y, double z) {
        vertsBuf.addElement(new Point3(x, y, z));
    }

    public void addFace() {
        if (faceToCreate) {
            createFace();
        }
        faceAdded = true;
    }

    private void createFace() {
        int nVerts = faceVertsBuf.size();
        if (nVerts < 3) {
            getContext().handleError("less than three vertices in face");
        }
        faceBuf = new Face();
        faceBuf.verts = new int[nVerts];
        Iterator i = faceVertsBuf.iterator();
        int j = 0;
        while (i.hasNext()) {
            faceBuf.verts[j++] = ((Integer)i.next()).intValue();
        }
        facesBuf.addElement(faceBuf);
        faceToCreate = false;
    }

    public void addVertexIndex(int index) {
        if (!faceAdded) {
            getContext().handleError("cant add vertex index: no face defined");
            return;
        }
        if (index < 0 || index >= vertsBuf.size()) {
            getContext().handleError("face vertex index out of range");
            return;
        }
        faceVertsBuf.addElement(new Integer(index));
        faceToCreate = true;
    }

    public SceneElement buildElement() {
        int i;
        Point3[] verts = new Point3[vertsBuf.size()];
        Face[] faces = new Face[facesBuf.size()];
        System.out.println("GeometryBuilder::buildElement():verts.length = " + verts.length);
        System.out.println("GeometryBuilder::buildElement():facesBuf.size() = " + facesBuf.size());
        for (i = 0; i < verts.length; i++) {
            verts[i] = (Point3)vertsBuf.elementAt(i);
        }
        System.out.println("GeometryBuilder::buildElement():faces.length = " + faces.length);
        for (i = 0; i < faces.length; i++) {
            faces[i] = (Face)facesBuf.elementAt(i);
        }
        element = new Geometry(verts, faces);
        return element;
    }

    public AbstractBuilder openAppearance() {
        return new AppearanceBuilder(getContext(), this);
    }

    protected String getDescription() {
        return "an element of geometry";
    }

    private Vector vertsBuf;
    private Vector faceVertsBuf;
    private Vector facesBuf;
    private Face faceBuf;
    private boolean faceAdded;
    private boolean faceToCreate;
}

