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

import java.awt.Color;



/**
 * Models material properties for a visible parent scene element that has areas
 * to fill with color and edges to draw around the filled areas. The properties include <ol>
 * <li>color it is filled with,</li> <li>color of it's edges,</li> <li>color to fill it with when highlighted and</li>
 * <li>color to draw edges in when highlighted.</li> <ol>
 */
public class Appearance extends SceneElement {
    /**
     *Create new material properties element with the following default settings: <ol> <li>white fill color,</li>
     * <li>black edge color,</li> <li>yellow highlighted fill color and</li> <li>black highlighted edge color.</li> </ol>
     * These defaults are set to ensure that the parent element has <b>some</b>
     * material settings and is therefore visible in the event of the user forgetting to specify them.
     */
    public Appearance() {
        super();
        this.fillColor = new Color(255, 255, 255);
        this.edgeColor = new Color(0, 0, 0);
        this.highlightFillColor = new Color(255, 255, 0);
        this.highlightEdgeColor = new Color(0, 0, 0);
    }

    /**
     * Accept scene graph visitor before it visits sub-elements
     * @param visitor the scene graph visitor
     */
    public void acceptPreOrderVisitor(SceneVisitor visitor) {
        visitor.preOrderVisitMaterial(this);
    }

    /**
     * Accept scene graph visitor after it has visited sub-elements
     * @param visitor the scene graph visitor
     */
    public void acceptPostOrderVisitor(SceneVisitor visitor) {
        visitor.postOrderVisitAppearance(this);
    }

    /**
     *Specify color to fill parent element's fillable regions with
     * @param color fill color
     */
    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    /**
     *Gets color specified to fill parent element's fillable regions with
     * @return fill color
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     *Specify color to draw parent element's edges with
     * @param color edge color
     */
    public void setEdgeColor(Color color) {
        this.edgeColor = color;
    }

    /**
     * Gets color to draw parent element's edges with
     * @return edge color
     */
    public Color getEdgeColor() {
        return edgeColor;
    }

    /**
     *Specify color to fill parent element's fillable regions with when it's highlighted
     * @param color highlighted fill color
     */
    public void setHighlightFillColor(Color color) {
        this.highlightFillColor = color;
    }

    /**
     * Gets color to fill parent element's fillable regions with when it's highlighted
     * @return highlighted fill color
     */
    public Color getHighlightFillColor() {
        return highlightFillColor;
    }

    /**
     *Specify color to draw parent element's edges with when it's highlighted
     * @param color highlighted edge color
     */
    public void setHighlightEdgeColor(Color color) {
        this.edgeColor = color;
    }

    /**
     * Gets color to draw parent element's edges with when it's highlighted
     * @return highlighted edge color
     */
    public Color getHighlightEdgeColor() {
        return highlightEdgeColor;
    }

    private Color fillColor;
    private Color edgeColor;
    private Color highlightFillColor;
    private Color highlightEdgeColor;
}
