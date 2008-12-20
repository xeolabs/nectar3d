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

import com.neocoders.nectar3d.common.Vector3;

import java.awt.Color;
import java.util.Vector;
import java.util.Iterator;

/**  */
public class Environment extends SceneElement {
    /** Make new environment scene element */
    public Environment() {
        super();
        lightSources = new Vector();

    }

    /**
     * Accept scene graph visitor before it visits sub-elements
     * @param visitor the scene graph visitor
     */
    public void acceptPreOrderVisitor(SceneVisitor visitor) {
        visitor.preOrderVisitEnvironment(this);
    }

    /**
     * Accept scene graph visitor after it has visited sub-elements
     * @param v the scene graph visitor
     */
    public void acceptPostOrderVisitor(SceneVisitor visitor) {
        visitor.postOrderVisitEnvironment(this);
    }

    /**
     *Add a light source
     * @param lightSource new light source for scene root
     */
    public void addLightSource(LightSource lightSource) {
        lightSources.addElement(lightSource);
    }


    /**
     *Get light sources
     * @return light sources
     */
    public Iterator getLightSources() {
        return lightSources.iterator();
    }

    private Vector lightSources;

    /**
     * @link aggregationByValue
     * @clientCardinality 1
     * @supplierCardinality 0..* 
     */
    private LightSource lnkLightSource;

}
