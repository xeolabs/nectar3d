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

import java.awt.Color;
import java.util.Vector;

import com.neocoders.nectar3d.common.Vector3;
import com.neocoders.nectar3d.scene.Environment;
import com.neocoders.nectar3d.scene.Layer;
import com.neocoders.nectar3d.scene.LightSource;
import com.neocoders.nectar3d.scene.SceneElement;
import com.neocoders.nectar3d.scene.Selector;

import java.util.Iterator;

class EnvironmentBuilder extends AbstractBuilder {
    public EnvironmentBuilder(AbstractContext context, AbstractBuilder parent) {
        super(context, parent);
        this.lightSources = new Vector();
        this.backgroundColor = new Color(255, 255, 255);;
    }

    public AbstractBuilder openLayer(String name, boolean depthSort, boolean fog, boolean shade) {
        return new LayerBuilder(getContext(), this, name, depthSort, fog, shade);
    }

    public void addLightSource(Vector3 dir, Color color) {
        lightSources.addElement(new LightSource(dir, color));
    }



    public SceneElement buildElement() {
        Environment environment = new Environment();
        Iterator iterator = lightSources.iterator();
        while (iterator.hasNext()) {
            environment.addLightSource((LightSource)iterator.next());
        }

        element = environment;
        return element;
    }

    protected String getDescription() {
        return "an environment";
    }

    private Vector lightSources;
    private Color backgroundColor;
}
