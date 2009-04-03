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

import com.neocoders.nectar3d.scene.Name;
import com.neocoders.nectar3d.scene.SceneElement;
import com.neocoders.nectar3d.scene.Selector;

class NameBuilder extends AbstractBuilder {
    public NameBuilder(AbstractContext context, AbstractBuilder parent, Selector selector) {
        super(context, parent);
        this.selector = selector;
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
    public AbstractBuilder openLayer(String name, boolean depthSort, boolean fog, boolean shade) {
        return new LayerBuilder(getContext(), this, name, depthSort, fog, shade);
    }

    public AbstractBuilder openTransformGroup() {
        return new TransformGroupBuilder(getContext(), this);
    }

    protected String getDescription() {
        return "a named subtree";
    }

    public SceneElement buildElement() {
        element = new Name(selector);
        return element;
    }

   private Selector selector;
}
