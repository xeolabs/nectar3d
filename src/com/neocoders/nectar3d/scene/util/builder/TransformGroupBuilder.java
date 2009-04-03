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

import com.neocoders.nectar3d.scene.Box;
import com.neocoders.nectar3d.scene.SceneElement;
import com.neocoders.nectar3d.scene.SceneException;
import com.neocoders.nectar3d.scene.Selector;
import com.neocoders.nectar3d.scene.TransformGroup;

class TransformGroupBuilder extends AbstractBuilder {
    public TransformGroupBuilder(AbstractContext context, AbstractBuilder parent) {
        super(context, parent);
        xform = new TransformGroup();
    }

    public AbstractBuilder openGeometry() {
        return new GeometryBuilder(getContext(), this);
    }

    public AbstractBuilder openLabel() {
        return new LabelBuilder(getContext(), this);
    }

    public AbstractBuilder openBox(double xSize, double ySize, double zSize) {
        return new BoxBuilder(getContext(), this, xSize, ySize, zSize);
    }

    public AbstractBuilder openInterpolator(int attrStringSelector) {
        return new InterpolatorBuilder(getContext(), this, attrStringSelector);
    }

    public AbstractBuilder openLayer(String name, boolean depthSort, boolean fog, boolean shade) {
        return new LayerBuilder(getContext(), this, name, depthSort, fog, shade);
    }

    public AbstractBuilder openTransformGroup() {
        return new TransformGroupBuilder(getContext(), this);
    }

    public void rotateX(double degrees) {
        try {
            xform.addTransform(TransformGroup.ROTX);
            xform.setAttribute(TransformGroup.ROTX, degrees);
        }
        catch (SceneException e) {
            getContext().handleError(e.getMessage());
        }
    }

    public void rotateY(double degrees) {
        try {
            xform.addTransform(TransformGroup.ROTY);
            xform.setAttribute(TransformGroup.ROTY, degrees);
        }
        catch (SceneException e) {
            getContext().handleError(e.getMessage());
        }
    }

    public void rotateZ(double degrees) {
        try {
            xform.addTransform(TransformGroup.ROTZ);
            xform.setAttribute(TransformGroup.ROTZ, degrees);
        }
        catch (SceneException e) {
            getContext().handleError(e.getMessage());
        }
    }

    public void translate(double x, double y, double z) {
        try {
            xform.addTransform(TransformGroup.TRA);
            xform.setAttribute(TransformGroup.TRAX_VAL, x);
            xform.setAttribute(TransformGroup.TRAY_VAL, y);
            xform.setAttribute(TransformGroup.TRAZ_VAL, z);
        }
        catch (SceneException e) {
            getContext().handleError(e.getMessage());
        }
    }

    public void scale(double x, double y, double z) {
        try {
            xform.addTransform(TransformGroup.SCA);
            xform.setAttribute(TransformGroup.SCAX_VAL, x);
            xform.setAttribute(TransformGroup.SCAY_VAL, y);
            xform.setAttribute(TransformGroup.SCAZ_VAL, z);
        }
        catch (SceneException e) {
            getContext().handleError(e.getMessage());
        }
    }

    public AbstractBuilder openName(Selector selector) {
        return new NameBuilder(getContext(), this, selector);
    }

    protected String getDescription() {
        return "a transform group";
    }

    public SceneElement buildElement() {
        element = xform;
        return element;
    }

    private TransformGroup xform;
}
