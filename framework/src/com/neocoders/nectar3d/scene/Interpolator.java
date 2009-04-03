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

/**
 *An Interpolator may be added as a child of an AttributeGroup to interpolate a
 * selected attribute within a set of key-frames as a function of scene time in milliseconds.
 * An interpolator modifies it's attribute each time it is updated with the current scene time.
 * When scene time falls outside of it's last keyframe the interpolator deletes itself from the scene
 * graph and notifies observers. The interpolator is created with the index of the attribute it interpolates in it's parent and a
 * sequence of keyframe instant-value pairs.
 * The first time the interpolator is updated with the current scene time  it records the time
 * as the starting
 * time of interpolation. Interpolation times are then computed as the difference
 * between the given time instant and the starting time; thefore the first computed interpolation time is zero milliseconds.
 * When the computed interpolation time is before the first keyframe, the interpolator does nothing.
 * When the computed interpolation time is within the first and last keyframes the attribute is interpolated.
 * When the computed interpolation time is after the last keyframe, the interpolator
 * destroys itself, notifying observers as it does so.
 */
public class Interpolator extends SceneElement {
    /**
     * Creates new interpolator to interpolate a parent element's attribute within defined keyframe instant-value pairs
     * @param attrID identifies attribute to interpolate
     * @param instants an instant in time in milliseconds for each keyframe
     * @param values a value for each keyframe
     * @throws SceneException mismatch number of keyframe instants not equal to number of keyframe values or less than
     * two keyframes specified
     */
    public Interpolator(int attrID, long[] instants, double[] values) throws SceneException {
        super();
        timeActivated = -1L; // not activated yet - will be when first updated
        if (instants.length != values.length) {
            throw new SceneException("mismatch in number of interpolation instants and values");
        }
        if (instants.length < 2) {
            throw new SceneException("less than two key frames specified - two is minimum");
        }
        for (int i = 1; i < instants.length; i++) // instants must be in chronological order
        {
            if (instants[i - 1] >= instants[i]) {
                throw new SceneException("instant " + i + " does not come before instant " + (i + 1));
            }
        }
        this.attrID = attrID;
        this.instants = instants;
        this.values = values;
        this.key1 = 0;
        this.key2 = 1;
    }

    /**
     * Get the index of the interpolated attribute
     * @return index of interpolated attribute
     */
    public int getAttrID() {
        return attrID;
    }

    /**
     * Get all the defined keyframe instants
     * @return instants the instants, each in milliseconds, one for each keyframe value
     */
    public long[] getInstants() {
        return instants;
    }

    /**
     * Get all defined keyframe attribute values
     * @return the values, one for each keyframe instant
     */
    public double[] getValues() {
        return values;
    }

    /**
     * Accept scene graph visitor before it visits sub-elements
     * @param visitor the scene graph visitor
     */
    public void acceptPreOrderVisitor(SceneVisitor visitor) {
        visitor.preOrderVisitInterpolator(this);
    }

    /**
     * Accept scene graph visitor after it has visited sub-elements
     * @param visitor the scene graph visitor
     */
    public void acceptPostOrderVisitor(SceneVisitor visitor) {
        visitor.postOrderVisitInterpolator(this);
    }

    /**
     * Causes interpolator to interpolate it's attribute according to the given time instant.<p>
     * The first time the interpolator is updated it records the instant as the starting
     * time of interpolation. Interpolation times are then computed as the difference
     * between the given time instant and the starting time; thefore the first computed
     * interpolation time is zero milliseconds. Three cases are then handled for the computed time value:
     * <ol><li>time before the first keyframe - interpolator does nothing</li>
     * <li>time within the first and last keyframes - attribute is interpolated</li>
     * <li>time after the last keyframe -  interpolator destroys itself and notifies observers</li></ol>
     * @param instant the current scene time in milliseconds
     */
    public void update(long instant) {
    
        SceneElement parent = getParent();
        if (!(parent instanceof AttributeGroup)) {
            return; // Cant interpolate parent; wrong type
        }
        if (timeActivated < 0L) {
            timeActivated = instant; // I am now active
        }
        double value;
        switch (findEnclosingFrame(instant)) {
            case EMPTY:
                return;
            case BEFORE_FIRST:
                return; // not interpolating yet; a "time delay" before interpolation begins
            case AFTER_LAST:
                value = values[values.length - 1];
                ((AttributeGroup)parent).setAttribute(attrID, value);
                destroy(); // served my purpose
                return;
            case FOUND:
                value = interpolate(instant - timeActivated);
                ((AttributeGroup)parent).setAttribute(attrID, value);
                break;
            default: // satisfy compiler
                break;
        }
    }

    private double interpolate(double instant) {
        double u = (double)(instants[key2] - instants[key1]);
        double v = (double)(instant - instants[key1]);
        double w = values[key2] - values[key1];
        return values[key1] + ((v / u) * w);
    }

    private int findEnclosingFrame(long instant) {
        if (instants.length == 0) {
            return EMPTY;
        }
        if (instant < instants[0]) {
            return BEFORE_FIRST;
        }
        if (instant > instants[instants.length - 1]) {
            return AFTER_LAST;
        }
        while (instants[key1] > instant) {
            key1--;
            key2--;
        }
        while (instants[key2] < instant) {
            key1++;
            key2++;
        }
        return FOUND;
    }

    private long timeActivated;
    private int attrID;
    private long[] instants;
    private double[] values;
    private int key1;
    private int key2;
    private final static int EMPTY = 0;
    private final static int FOUND = 1;
    private final static int BEFORE_FIRST = 2;
    private final static int AFTER_LAST = 3;

    /**
     * @supplierCardinality 1
     * @clientCardinality 1 
     */
    private AttributeGroup lnkAttributeGroup;
}
