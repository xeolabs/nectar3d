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


/* The root of a subtree within a scene graph for which separate rendering
* parameters may be specified.
 * There must be at least one Layer child of the root element, and all
 * other non-root elements must be descendants of a Layer. For each Layer
 * we can specify if depth-sorting, fog depth-cueing and shading are
 * applied when it's subtree is rendered. Layers are rendered in the
 * order in which they are specified in the scene graph and may not be
 * descendants of one-another.
 */
public class Layer extends SceneElement {
    /** Create new scene graph layer and specify rendering parameters for it.
     * @param selector identifies this layer
     * @param depthSoft specifies if visible layer elements will be depthsorted
     * @param fog specifies if layer is to be rendered with fog cueing
     * @param shade specifies if visible layer elements will be shaded
     */
    public Layer(Selector selector, boolean depthSort, boolean fog, boolean shade) {
        super();
        this.selector = selector;
        this.depthSort = depthSort;
        this.fog = fog;
        this.shade = shade;
    }

    /** Get selector identifying this layer
     * @return the selector
     */
    public Selector getStringSelector() {
        return selector;
    }

    /** Find out if this layer's visible elements are depthsorted when rendered
     * @return true if depthsorted else false
     */
    public boolean getDepthSort() {
        return depthSort;
    }

    /** Find out if this layer's visible elements are fogged proportional to their
     * depth when rendered
     * @return true if fogged else false
     */
    public boolean getFog() {
        return fog;
    }
    /** Find out if this layer's visible elements are shaded when rendered
     * @return true if fogged else false
     */
    public boolean getShade() {
        return shade;
    }
    /**
     * Accept scene graph visitor before it visits sub-elements
     * @param visitor the scene graph visitor
     */
    public void acceptPreOrderVisitor(SceneVisitor visitor) {
        visitor.preOrderVisitLayer(this);
    }
 /**
     * Accept scene graph visitor after it has visited sub-elements
     * @param visitor the scene graph visitor
     */
    public void acceptPostOrderVisitor(SceneVisitor visitor) {
        visitor.postOrderVisitLayer(this);
    }

    /**
     * @supplierCardinality 1
     * @clientCardinality 0..* 
     */
    private Selector selector;
    private boolean depthSort;
    private boolean fog;
    private boolean shade;
}
