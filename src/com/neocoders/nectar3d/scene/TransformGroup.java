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
 * An ordered group of transforms. The following individual transformations may be added:<br>
 * <ul> <li>translation on x,y and z axis,</li> <li>rotation about x axis,</li> <li>rotation about y axis,</li> <li>rotation about z axis and</li> <li>scale on x, y and z axis.</li> </ul>
 * <br> Attributes for transformations may be got or set
 * individually by axis. For example, it is possible to get or set the
 * x-axis translation offset, or the y-axis factor for scale, independantly of other attributes.<br> <br>
 * It is possible to set or get attributes regardless of whether
 * corresponding transformations have been added to the TransformGroup or not.<br>
 */
public class TransformGroup extends SceneElement implements AttributeGroup {
    /** Identifies attribute for x-axis rotation */
    public static final int ROTX_VAL = 0;

    /** Identifies attribute for y-axis rotation */
    public static final int ROTY_VAL = 1;

    /** identifies attribute for z-axis rotation */
    public static final int ROTZ_VAL = 2;

    /** Identifies attribute for x-axis scale transformation */
    public static final int SCAX_VAL = 3;

    /** Identifies attribute for y-axis scale transformation */
    public static final int SCAY_VAL = 4;

    /** Identifies attribute for z-axis scale transformation */
    public static final int SCAZ_VAL = 5;

    /** Identifies attribute for x-axis translation */
    public static final int TRAX_VAL = 6;

    /** Identifies attribute for y-axis translation */
    public static final int TRAY_VAL = 7;

    /** Identifies attribute for z-axis translation */
    public static final int TRAZ_VAL = 8;

    /** Identifies x-axis rotation */
    public static final int ROTX = 0;

    /** Identifies y-axis rotation */
    public static final int ROTY = 1;

    /** Identifies z-axis rotation */
    public static final int ROTZ = 2;

    /** Identifies translation */
    public static final int TRA = 3;

    /** Identifies  scale transformation */
    public static final int SCA = 4;

    /** Maximum number of transforms that can be added to a transform group */
    public final static int MAX_TRANSFORMS = 5;

    /** Maximum number of attributes in a transform group */
    public final static int MAX_ATTRIBUTES = 9;

    /**
     * Creates new empty transformation group. Attributes for transforms
     * can be set or got even though no transforms exist in the group yet.
     */
    public TransformGroup() {
        super();
        xforms = new int[MAX_TRANSFORMS];
        attrs = new double[MAX_ATTRIBUTES];
        xformAdded = new boolean[MAX_TRANSFORMS];
        clearTransforms();
    }

    /**
     *Creates a copy of another transformation group
     * @param t transformation group to copy
     */
    public TransformGroup(TransformGroup t) {
        xforms = new int[MAX_TRANSFORMS];
        attrs = new double[MAX_ATTRIBUTES];
        int i;
        for (i = 0; i < t.nxforms; i++) {
            xforms[i] = t.xforms[i];
        }
        for (i = 0; i < MAX_ATTRIBUTES; i++) {
            attrs[i] = t.attrs[i];
        }
        for (i = 0; i < MAX_TRANSFORMS; i++) {
            xformAdded[i] = t.xformAdded[i];
        }
        nxforms = t.nxforms;
    }

    /**
     * Returns the number of transforms in this group
     * @return number of transforms in this group
     */
    public int getNumTransforms() {
        return nxforms;
    }

    /**
     * Returns the given transform in the group, indexed in order of addition
     * @param i index of transform, in order of addition
     */
    public int getTransform(int i) {
        return xforms[i];
    }

    /** Removes all transforms. Does not change values of their attributes. */
    public void clearTransforms() {
        nxforms = 0;
        for (int i = 0; i < MAX_TRANSFORMS; i++) {
            xformAdded[i] = false;
        }
    }

    /**
     * Adds a transform to the group. Any value already set for the transform is not modified.
     * @param id identifies type of transform; ROT_X, ROT_Y, ROT_Z, TRA or SCA
     * @throws SceneException if given type of transform is already added or if the transform group is full to
     * MAX_TRANSFORMS capacity
     */
    public void addTransform(int id) throws SceneException {
        if (xformAdded[id]) {
            throw new SceneException("duplicate transform");
        }
        if (nxforms >= MAX_TRANSFORMS) {
            throw new SceneException("too many transforms");
        }
        xforms[nxforms++] = id;
    }

    /** Determines if the given transform is in the group.
     * @param id identity of transform
     * @return true if transform added else false
     */
    public boolean containsTransform(int id) {
        return xformAdded[id];
    }

    /**
     * Sets the value of a transformation attribute. The transformation is not required to be in the transform
     * group. If the transform is subsequently added or the transform group cleared, the attribute
     * retains this value.
     * @param id identifies transform attribute; ROT_X, ROT_Y, ROT_z, TRA_X, TRA_Y, TRA_Z, SCA_X, SCA_Y or SCA_Z
     * @param value new value for attribute
     */
    public void setAttribute(int id, double val) {
        attrs[id] = val;
    }

    /**
     * Gets the value of a transformation attribute. The transformation does not required to be in the transform
     * group. If the transform is subsequently added or the transform group cleared, the attribute
     * retains the returned value until new vaue is set.
     * @param id identifies transform attribute; ROT_X, ROT_Y, ROT_z, TRA_X, TRA_Y, TRA_Z, SCA_X, SCA_Y or SCA_Z
     */
    public double getAttribute(int id) {
        return attrs[id];
    }

    /**
     * Accept scene graph visitor before it visits sub-elements
     * @param visitor the scene graph visitor
     */
    public void acceptPreOrderVisitor(SceneVisitor visitor) {
        visitor.preOrderVisitTransformGroup(this);
    }

    /**
     * Accept scene graph visitor after it has visited sub-elements
     * @param visitor the scene graph visitor
     */
    public void acceptPostOrderVisitor(SceneVisitor visitor) {
        visitor.postOrderVisitTransformGroup(this);
    }

    private int nxforms; // Count of transforms added with addXForm
    private int[] xforms; // Transforms in order of addition
    private boolean[] xformAdded; // flag specifies if transform added
    private double[] attrs; // Attribute for each transform
};
