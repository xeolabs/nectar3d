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
import java.util.Iterator;

import com.neocoders.nectar3d.common.Point3;
import com.neocoders.nectar3d.scene.Box;
import com.neocoders.nectar3d.scene.Face;
import com.neocoders.nectar3d.scene.Geometry;
import com.neocoders.nectar3d.scene.SceneElement;
import com.neocoders.nectar3d.scene.SceneException;

class BoxBuilder extends AbstractBuilder {
    public BoxBuilder(AbstractContext context, AbstractBuilder parent, double xSize, double ySize, double zSize) {
        super(context, parent);
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public AbstractBuilder openAppearance() {
        return new AppearanceBuilder (getContext(), this);
    }

    public SceneElement buildElement() {
        element = new Box(xSize, ySize, zSize);
        return element;
    }

    protected String getDescription() {
        return "a box";
    }

    private double xSize;
    private double ySize;
    private double zSize;
}
