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
import java.awt.Color;
import java.awt.Font;

import com.neocoders.nectar3d.common.ErrorHandler;
import com.neocoders.nectar3d.common.Point3;
import com.neocoders.nectar3d.common.Vector3;
import com.neocoders.nectar3d.scene.SceneElement;
import com.neocoders.nectar3d.scene.SceneException;
import com.neocoders.nectar3d.scene.Selector;

/**
 * Builds a scene graph. To build a scene graph element, a SceneBuilder is directed
 * to open the element, add components to it, then close it. Some types of elements
 * may be opened within others to create parent-child relationships, for example the SceneBuilder might be directed to open
 * an environment element, then open a layer element, then open a transform group element, add transforms, open a geometry
 * element, specify components, close geometry element, close transform group element, close layer element, close environment
 * element and then provide the constructed scene graph. SceneBuilder encapsulates the rules for what constitutes a legal
 * scene graph; in the interests of building only legal scene graphs, SceneBuilder will not allow some types of elements to be
 * be opened within others, or certain components to be added to certain elements. When such illegal directions are given to a
 * SceneBuilder, a descriptive error message is logged and the direction is otherwise ignored. Errors are logged with an error
 * handler, a custom instance of which may be supplied to the SceneBuilder. If an error handler is not supplied, SceneBuilder
 * uses it's own default internal handler, which prints messages to standard output.
 */
public class SceneBuilder {
    /** Creates new scene builder, which so far has an empty scene graph. */
    public SceneBuilder() {
        reset();
        errorHandler = new ErrorHandler() { // default error handler
            public void handleError(String message) {
                System.out.println(message);
            }
        };
    }

    /**
     * Specifies if debig mode is on or off
     * @param toDebug true if debug mode on else false
     */
    public void setDebug(boolean toDebug) {
        this.toDebug = toDebug;
    }

    private void printDebugMessage(String message) {
        for (int i = 0; i < debugIndent; i += 4) {
            System.out.print("   ");
        }
        System.out.println(message);
    }

    /**
     * Set an error handler to be notified whenever the builder is directed
     * to do something that would result in an illegal scene.
     * @param errorHandler the error handler
     */
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    /** Reset scene builder to initial state, in which it has an empty scene graph */
    public void reset() {
        builder = new RootBuilder(
            new AbstractContext() {
                public void handleError(String message) {
                    errorHandler.handleError(message);
                }
            });
        toDebug = false;
        debugIndent = 0;
    }

    /** Open an environment element */
    public SceneBuilder openEnvironment() {
        tryOpen(builder.openEnvironment());
        return this;
    }

    public SceneBuilder openSwitch() {
        tryOpen(builder.openSwitch());
        return this;
    }

    /** Convenience method, sets current builder state to given, only if the given state is not null */
    private void tryOpen(AbstractBuilder newBuilder) {
        if (newBuilder != null) {
            builder = newBuilder;
        }
        if (toDebug) {
            printDebugMessage("opened " + builder.getDescription());
            debugIndent++;
        }
    }

    /**
     * Add a light source to the currently open scene element
     * @param dir direction of lightsource from origin
     * @param color color of light source
     */
    public SceneBuilder addLightSource(Vector3 dir, Color color) {
        printDebugMessage("adding a lightsource");
        builder.addLightSource(dir, color);
        return this;
    }

    /** Open a geometry element */
    public SceneBuilder openGeometry() {
        tryOpen(builder.openGeometry());
        return this;
    }

    /**
     * Add a face to the currently open scene element.
     * Note that a face must be closed, ie. the last vertex index
     * must equal the first vertex
     */
    public SceneBuilder addFace() {
        printDebugMessage("adding a face");
        builder.addFace();
        return this;
    }

    /**
     * Add a vertex to the currently open scene element
     * @param x x-component of vertex
     * @param y y-component of vertex
     * @param z z-component of vertex
     */
    public SceneBuilder addVertex(double x, double y, double z) {
        printDebugMessage("adding a vertex");
        builder.addVertex(x, y, z);
        return this;
    }

    /**
     * Add a vertex index to the currently open scene element, to index a vertex
     * previously added to the element. An error is logged if the index is out of range of vertices previously added.
     * @param index the vertex index
     */
    public SceneBuilder addVertexIndex(int index) {
        printDebugMessage("adding vertex index");
        builder.addVertexIndex(index);
        return this;
    }

    /**
     * Open a text element. The text will be 12 "point"
     * courier, filled with black, with no offset from the current
     * coordinate space origin.
     */
    public SceneBuilder openLabel() {
        tryOpen(builder.openLabel());
        return this;
    }

    /**
     * Set text for the currently open element
     * @param text text for currently open element
     */
    public SceneBuilder setText(String text) {
        printDebugMessage("setting text");
        builder.setText(text);
        return this;
    }

    /**
     * Set offset for the currently open element
     * @param offset offset for currently open element
     */
    public SceneBuilder setOffset(Point3 offset) {
        printDebugMessage("setting an offet");
        builder.setOffset(offset);
        return this;
    }

    /**
     * Set font for currently open element
     * @param font font for element
     */
    public SceneBuilder setFont(Font font) {
        printDebugMessage("setting font");
        builder.setFont(font);
        return this;
    }

    /**
     * Add a box to the currently open scene element.
     * @param xSize size of box on x-axis
     * @param ySize size of box on y-axis
     * @param zSize size of box on z-axis
     */
    public SceneBuilder openBox(double xSize, double ySize, double zSize) {
        tryOpen(builder.openBox(xSize, ySize, zSize));
        return this;
    }

    /**
     * Open an interpolator scene element to interpolate a selected attribute of the currently open element
     * @param attrStringSelector selects an attribute of the element to interpolate
     */
    public SceneBuilder openInterpolator(int attrStringSelector) {
        tryOpen(builder.openInterpolator(attrStringSelector));
        return this;
    }

    /**
     * Add a keyframe to the currently open scene element
     * @param instant time stamp for keyframe in milliseconds
     * @param value for keyframe
     */
    public SceneBuilder addKeyFrame(long instant, double value) {
        printDebugMessage("adding key frame");
        builder.addKeyFrame(instant, value);
        return this;
    }

    /**
     * Open a layer scene element
     * @param @name name for layer
     * @param depthSort specifies if visible sub-elements of layer are depthsorted before rendering
     * @param fog specifies if colors of visible sub-elements of layer are fogged to help simulate depth when rendered
     * @param shade specifies if visible sub-elements of layer are shaded (where applicable) when rendered
     */
    public SceneBuilder openLayer(String name, boolean depthSort, boolean fog, boolean shade) {
        tryOpen(builder.openLayer(name, depthSort, fog, shade));
        return this;
    }

    /** Opens a transform group scene element */
    public SceneBuilder openTransformGroup() {
        tryOpen(builder.openTransformGroup());
        return this;
    }

    /**
     * Add x-axis rotation transformation to currently open scene element
     * @param degrees for x-axis rotation attribute, in degrees
     */
    public SceneBuilder rotateX(double degrees) {
        printDebugMessage("rotating about x");
        builder.rotateX(degrees);
        return this;
    }

    /**
     * Add y-axis rotation transformation to currently open scene element
     * @param degrees for y-axis rotation attribute, in degrees
     */
    public SceneBuilder rotateY(double degrees) {
        printDebugMessage("rotating about y");
        builder.rotateY(degrees);
        return this;
    }

    /**
     * Add z-axis rotation transformation to currently open scene element
     * @param degrees for z-axis rotation attribute, in degrees
     */
    public SceneBuilder rotateZ(double degrees) {
        printDebugMessage("rotating about z");
        builder.rotateZ(degrees);
        return this;
    }

    /**
     * Add translation transformation to currently open scene element
     * @param x for x-axis translation attribute
     * @param y for y-axis translation attribute
     * @param z for z-axis translation attribute
     */
    public SceneBuilder translate(double x, double y, double z) {
        printDebugMessage("translating");
        builder.translate(x, y, z);
        return this;
    }

    /**
     * Add scale transformation to currently open scene element
     * @param x for x-axis scale attribute
     * @param y for y-axis scale attribute
     * @param z for z-axis scale attribute
     */
    public SceneBuilder scale(double x, double y, double z) {
        printDebugMessage("scaling");
        builder.scale(x, y, z);
        return this;
    }

    /** Open a material properties scene element */
    public SceneBuilder openAppearance() {
        printDebugMessage("appearance");
        tryOpen(builder.openAppearance());
        return this;
    }

    /**
     * Set fill color for currently open scene element
     * @param fillColor the fill color
     */
    public SceneBuilder setFillColor(Color fillColor) {
        printDebugMessage("setting fill color");
        builder.setFillColor(fillColor);
        return this;
    }

    /**
     * Set edge color for currently open scene element
     * @param edgeColor the edge color
     */
    public SceneBuilder setEdgeColor(Color edgeColor) {
        printDebugMessage("setting edge color");
        builder.setEdgeColor(edgeColor);
        return this;
    }

    /**
     * Set fill color for when currently open scene element is highlighted
     * @param fillColor the fill color
     */
    public SceneBuilder setHighlightFillColor(Color fillColor) {
        printDebugMessage("setting highlighted fill color");
        builder.setHighlightFillColor(fillColor);
        return this;
    }

    /**
     * Set edge color for when currently open scene element is highlighted
     * @param edgeColor the edge color
     */
    public SceneBuilder setHighlightEdgeColor(Color edgeColor) {
        printDebugMessage("setting highlighted edge color");
        builder.setHighlightEdgeColor(edgeColor);
        return this;
    }

    /**
     * Open a name scene element
     * @param selector identifier for the Name element
     */
    public SceneBuilder openName(Selector selector) {
        tryOpen(builder.openName(selector));
        return this;
    }

    /** Close and return the currently open scene element */
    public SceneBuilder close() {
        if (toDebug) {
            debugIndent--;
            printDebugMessage("closed " + builder.getDescription());
        }
        AbstractBuilder closed = builder;
        AbstractBuilder test = builder.close();
        if (test == null) {
            errorHandler.handleError("unexpected close");
            return this; // recover
        }
        builder = test;
        return this;
    }

    /**
     * Get a scene graph that has been built. If no elements have been defined,
     * the scene graph will be a single SceneElement with no children.
     */
    public SceneElement buildScene() {
        builder.close();
        return builder.getElement();
    }

    private AbstractBuilder builder;
    private ErrorHandler errorHandler;
    private boolean toDebug;
    private int debugIndent;
}

