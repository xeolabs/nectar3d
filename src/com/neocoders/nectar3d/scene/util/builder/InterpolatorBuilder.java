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

import com.neocoders.nectar3d.scene.Interpolator;
import com.neocoders.nectar3d.scene.SceneElement;
import com.neocoders.nectar3d.scene.SceneException;

class InterpolatorBuilder extends AbstractBuilder {
    public InterpolatorBuilder(AbstractContext context, AbstractBuilder parent, int attrStringSelector) {
        super(context, parent);
        this.attrStringSelector = attrStringSelector;
        instants = new Vector();
        values = new Vector();
    }

    public void addKeyFrame(long instant, double value) {
        instants.addElement(new Long(instant));
        values.addElement(new Double(value));
    }

    /** Implements template method to get description of element made by this builder */
    protected String getDescription() {
        return "a transform group interpolator";
    }

    /** Implements template method to get scene element created by this builder */
    public SceneElement buildElement() {
        long[] instantArray = new long[instants.size()];
        double[] valueArray = new double[values.size()];
        for (int i = 0; i < instantArray.length; i++) {
            instantArray[i] = ((Long)instants.elementAt(i)).longValue();
            valueArray[i] = ((Double)values.elementAt(i)).doubleValue();
        }
        try {
            element = new Interpolator (attrStringSelector, instantArray, valueArray);
        } catch (SceneException se) {
            getContext().handleError(se.getMessage());
            element = new SceneElement(); // recover
        }
        return element;
    }

    private int attrStringSelector;
    private Vector instants;
    private Vector values;
}
