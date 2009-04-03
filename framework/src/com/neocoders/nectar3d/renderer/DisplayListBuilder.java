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
package com.neocoders.nectar3d.renderer;

import java.awt.Font;
import java.awt.Color;
import java.util.Iterator;

import com.neocoders.nectar3d.common.Matrix;
import com.neocoders.nectar3d.common.Point2;
import com.neocoders.nectar3d.common.Point3;
import com.neocoders.nectar3d.common.Vector3;
import com.neocoders.nectar3d.common.Volume3;
import com.neocoders.nectar3d.common.Window2;
import com.neocoders.nectar3d.scene.Appearance;
import com.neocoders.nectar3d.scene.Environment;
import com.neocoders.nectar3d.scene.Face;
import com.neocoders.nectar3d.scene.Geometry;
import com.neocoders.nectar3d.scene.Interpolator;
import com.neocoders.nectar3d.scene.Label;
import com.neocoders.nectar3d.scene.Layer;
import com.neocoders.nectar3d.scene.LightSource;
import com.neocoders.nectar3d.scene.Name;
import com.neocoders.nectar3d.scene.SceneVisitor;
import com.neocoders.nectar3d.scene.Selector;
import com.neocoders.nectar3d.scene.TransformGroup;

class DisplayListBuilder implements SceneVisitor {
    public DisplayListBuilder(SceneRendererParams config,
                              DisplayList displayList) {
        viewMatrix = new Matrix();
        initTempCoordinates();
        initMatrixStack();
        setParams(config);
        pickSelector = null;
        fogEnabled = false;
        shadingEnabled = false;
        appearance = DEFAULT_APPEARANCE;
        environment = DEFAULT_ENVIRONMENT;
        this.displayList = displayList;

        /*
           * Default error handler
           */

        errorHandler = new com.neocoders.nectar3d.common.ErrorHandler() {
            public void handleError(String msg) {
                System.out.println("Error: " + msg);
            }
        };
    }

    private void initTempCoordinates() {
        int i;
        for (i = 0; i < tempVC.length; i++) {
            tempVC[i] = new Point3();
        }
        for (i = 0; i < tempSC.length; i++) {
            tempSC[i] = new Point2();
        }
    }

    private void initMatrixStack() {
        matrixStackTop = -1;
        matrixStack[0] = new Matrix();
        for (int i = 1; i < MATRIX_STACK_MAX; i++) {
            matrixStack[i] = null;
        }
    }

    public void setErrorHandler(
            com.neocoders.nectar3d.common.ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void reset() {
        pickSelector = null;
        matrixStackTop = -1;
        appearance = DEFAULT_APPEARANCE;
        environment = DEFAULT_ENVIRONMENT;
    }

    public void setParams(SceneRendererParams params) {
        this.params = params;

        /*
           * Precompute view transform
           */

        viewMatrix.identity();
        viewMatrix.lookat(params.getEye(), params.getLook(), params.getUp());
        viewMatrix.perspective(params.getVPDist());

        /*
           * Precompute volume-to-window ratios
           */

        Window2 window = params.getWindow();
        Volume3 volume = params.getFrustum();
        xVolToWinMap = ((double) window.xmax - (double) window.xmin)
                / (volume.xmax - volume.xmin);
        yVolToWinMap = ((double) window.ymax - (double) window.ymin)
                / (volume.ymax - volume.ymin);
    }

    public void setDisplayList(DisplayList displayList) {
        this.displayList = displayList;
    }

    public void preOrderVisitEnvironment(Environment e) {
        if (environment != DEFAULT_ENVIRONMENT) {
            errorHandler.handleError("nested environments");
            return;
        }
        environment = e;
    }

    public void postOrderVisitEnvironment(Environment e) {
        environment = DEFAULT_ENVIRONMENT;
    }

    public void preOrderVisitTransformGroup(TransformGroup xform) {
        pushMatrix();
        for (int i = 0; i < xform.getNumTransforms(); i++) {
            switch (xform.getTransform(i)) {
                case TransformGroup.ROTX:
                    rotateMatrixX(xform.getAttribute(TransformGroup.ROTX_VAL));
                    break;
                case TransformGroup.ROTY:
                    rotateMatrixY(xform.getAttribute(TransformGroup.ROTY_VAL));
                    break;
                case TransformGroup.ROTZ:
                    rotateMatrixZ(xform.getAttribute(TransformGroup.ROTZ_VAL));
                    break;
                case TransformGroup.TRA:
                    translateMatrix(xform.getAttribute(TransformGroup.TRAX_VAL),
                            xform.getAttribute(TransformGroup.TRAY_VAL), xform
                            .getAttribute(TransformGroup.TRAZ_VAL));
                    break;
                case TransformGroup.SCA:
                    scaleMatrix(xform.getAttribute(TransformGroup.SCAX_VAL), xform
                            .getAttribute(TransformGroup.SCAY_VAL), xform
                            .getAttribute(TransformGroup.SCAZ_VAL));
                    break;
            }
        }
    }

    private void pushMatrix() {
        if (matrixStackTop >= MATRIX_STACK_MAX) {
            return;
        }
        matrixStackTop++;
        if (matrixStack[matrixStackTop] == null) {
            matrixStack[matrixStackTop] = new Matrix();
        } else {
            matrixStack[matrixStackTop].identity();
        }
    }

    private void rotateMatrixX(double angle) {
        matrixStack[matrixStackTop].rotateX(angle);
    }

    private void rotateMatrixY(double angle) {
        matrixStack[matrixStackTop].rotateY(angle);
    }

    private void rotateMatrixZ(double angle) {
        matrixStack[matrixStackTop].rotateZ(angle);
    }

    private void translateMatrix(double x, double y, double z) {
        matrixStack[matrixStackTop].translate(x, y, z);
    }

    private void scaleMatrix(double x, double y, double z) {
        matrixStack[matrixStackTop].scale(x, y, z);
    }

    public void postOrderVisitTransformGroup(TransformGroup tg) {
        popMatrix();
    }

    private void popMatrix() {
        if (matrixStackTop >= 0) {
            matrixStackTop--;
        }
    }

    public void preOrderVisitGeometry(Geometry g) {
    }

    public void postOrderVisitGeometry(Geometry g) {
        Point3[] verts = g.getVertices();
        Face[] faces = g.getFaces();
        if (verts.length > MAX_VERTEX) {
            errorHandler.handleError("too many vertices in geometry element");
            return;
        }
        for (int i = 0; i < verts.length; i++) {
            tempVC[i] = transform(verts[i]);
        }
        mapToDisplay(tempVC, verts.length, tempSC);
        for (int j = 0; j < faces.length; j++) {
            Face face = faces[j];
            Vector3 normal = getNormal(tempVC[face.verts[0]],
                    tempVC[face.verts[1]], tempVC[face.verts[2]]);
            if (isBackFace(normal)) {
                // System.out.println("DisplayListBuilder::BACKFACE");
                continue;
            }
            int[] sx = new int[face.verts.length];
            int[] sy = new int[face.verts.length];

            double depth = Double.MAX_VALUE;
            for (int k = 0; k < face.verts.length; k++) {
                int vert = face.verts[k];
                sx[k] = tempSC[vert].x;
                sy[k] = tempSC[vert].y;
                // System.out.println("sx["+k+"] = " + sx[k] + ", sy["+k+"] = "
                // + sy[k]);
                if (tempVC[vert].z < depth) {
                    depth = tempVC[vert].z;
                }
            }
            Color fillColor = appearance.getFillColor();
            Color edgeColor = appearance.getEdgeColor();
            Color highlightFillColor = appearance.getHighlightFillColor();
            if (shadingEnabled) {
                fillColor = getShadedColor(fillColor, normal);
                highlightFillColor = getShadedColor(highlightFillColor, normal);
            }
            if (fogEnabled) {
                fillColor = foggedColor(fillColor, depth);
                edgeColor = foggedColor(edgeColor, depth);
            }
            PickInfo pickInfo = new PickInfo(pickSelector, j); // record pick
            // name and
            // index of this
            // face
            try {
                // System.out.println("adding face " + j + " to display list");
                displayList.addPolygon(pickInfo, depth, sx, sy, fillColor,
                        highlightFillColor, edgeColor);
            } catch (DisplayException de) {
                errorHandler.handleError("cant render geometry: no layer open");
            }
        }
    }

    private Point3 transform(Point3 wc) {
        Point3 vc = new Point3(wc.x, wc.y, wc.z);
        for (int i = matrixStackTop; i >= 0; i--) {
            vc = matrixStack[i].transform(vc);
        }
        return viewMatrix.transform(vc);
    }

    private Point2 mapToDisplay(Point3 vc) {
        Window2 window = params.getWindow();
        Volume3 volume = params.getFrustum();
        return new Point2(window.xmin
                + (int) (((vc.x / vc.w) - volume.xmin) * xVolToWinMap),
                window.ymin
                        + (int) (((vc.y / vc.w) - volume.ymin) * yVolToWinMap));
    }

    private void mapToDisplay(Point3[] vc, int nVerts, Point2[] sc) {
        Window2 window = params.getWindow();
        Volume3 volume = params.getFrustum();
        for (int i = 0; i < nVerts; i++) {
            sc[i].x = window.xmin
                    + (int) (((vc[i].x / vc[i].w) - volume.xmin) * xVolToWinMap);
            sc[i].y = window.ymin
                    + (int) (((vc[i].y / vc[i].w) - volume.ymin) * yVolToWinMap);
        }
    }

    private Vector3 getNormal(Point3 a, Point3 b, Point3 c) {
        a = new Point3(a.x / a.w, a.y / a.w, a.z);
        b = new Point3(b.x / b.w, b.y / b.w, b.z);
        c = new Point3(c.x / c.w, c.y / c.w, c.z);
        Vector3 p = new Vector3(c.x - b.x, c.y - b.y, c.z - b.z);
        Vector3 q = new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
        return Vector3.cross(p, q);
    }

    private boolean isBackFace(Vector3 normal) {
        return (Vector3.dot(Vector3.normalize(normal), new Vector3(0.0, 0.0,
                1.0)) <= 0.0);
    }

    private Color getShadedColor(Color color, Vector3 normal) {
        Color newColor = new Color(color.getRed(), color.getGreen(), color
                .getBlue());
        LightSource ls;
        Iterator iterator = environment.getLightSources();
        while (iterator.hasNext()) {
            ls = (LightSource) iterator.next();
            newColor = addColors(newColor, ls.getColor(), Vector3.dot(Vector3
                    .normalize(normal), ls.getDir()));
        }
        return newColor;
    }

    private Color addColors(Color a, Color b, double weight) {
        return new Color(clampColorComponent(a.getRed()
                + (int) (b.getRed() * weight)), clampColorComponent(a
                .getGreen()
                + (int) (b.getGreen() * weight)), clampColorComponent(a
                .getBlue()
                + (int) (b.getBlue() * weight)));
    }

    private int clampColorComponent(int val) {
        return val < 0 ? 0 : val > 255 ? 255 : val;
    }

    private Color foggedColor(java.awt.Color color, double depth) {
        Color bgColor = params.getBackgroundColor();
        Volume3 volume = params.getFrustum();
        double factor = (volume.zmax - depth) / (depth - volume.zmin);
        return new java.awt.Color(interpolate(factor, color.getRed(), bgColor
                .getRed()), interpolate(factor, color.getGreen(), bgColor
                .getGreen()), interpolate(factor, color.getBlue(), bgColor
                .getBlue()));
    }

    private int interpolate(double factor, int min, int max) {
        int val = (int) (min + (factor * (double) (max - min)));
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }

    public void preOrderVisitLabel(Label text) {
    }

    private Font perspectiveFont(Font font, Point3 vc) {
        double size = (double) font.getSize();
        return new Font(font.getFamily(), font.getStyle(), (int) (size / vc.w));
    }

    public void postOrderVisitLabel(Label text) {
        Point3 anchor3 = transform(new Point3(0.0, 0.0, 0.0));
        Point3 offset3 = text.getOffset();
        Point3 pos3 = new Point3(anchor3.x + offset3.x, anchor3.y + offset3.y,
                anchor3.z + offset3.z, anchor3.w);
        Point2 anchor2 = mapToDisplay(anchor3);
        Point2 offset2 = mapToDisplay(pos3);
        java.awt.Font font = perspectiveFont(text.getFont(), anchor3);
        java.awt.Color color = appearance.getFillColor();
        double depth = pos3.z;
        if (shadingEnabled) {
        }
        if (fogEnabled) {
            color = foggedColor(color, depth);
        }
        PickInfo id = new PickInfo(pickSelector);
        try {
            displayList.addText(id, depth, anchor2, offset2, text.getText(),
                    font, color);
        } catch (DisplayException de) {
            errorHandler.handleError("cant add text:no layer open");
        }
    }

    public void preOrderVisitName(Name name) {
        if (pickSelector != null) {
            errorHandler.handleError("name sub-element of other name element");
            return;
        }
        pickSelector = name.getStringSelector();
    }

    public void postOrderVisitName(Name name) {
        pickSelector = null;
    }

    public void preOrderVisitInterpolator(Interpolator interpolator) {
        interpolator.update(params.getTimeElapsed());
    }

    public void postOrderVisitInterpolator(Interpolator interpolator) {
    }

    public void preOrderVisitMaterial(Appearance appearance) {
        this.appearance = appearance;
    }

    public void postOrderVisitAppearance(Appearance appearance) {
    }

    public void preOrderVisitLayer(Layer layer) {
        if (displayList == null) {
            errorHandler.handleError("no display list set");
            return;
        }
        try {
            displayList.createLayer(layer.getStringSelector(), layer
                    .getDepthSort());
            displayList.openLayer(layer.getStringSelector());
            fogEnabled = layer.getFog();
            shadingEnabled = layer.getShade();
        } catch (DisplayException de) {
            errorHandler.handleError("nested layers");
        }
    }

    public void postOrderVisitLayer(Layer layer) {
        try {
            displayList.closeLayer();
        } catch (DisplayException de) {
            errorHandler.handleError("no layer open");
        }
    }

    public com.neocoders.nectar3d.common.ErrorHandler errorHandler;

    public final static int MATRIX_STACK_MAX = 100;

    private Matrix[] matrixStack = new Matrix[MATRIX_STACK_MAX];

    private int matrixStackTop;

    private Matrix viewMatrix;

    /**
     * @supplierCardinality 1
     * @clientCardinality 1
     */
    private SceneRendererParams params;

    private double xVolToWinMap;

    private double yVolToWinMap;

    private Selector pickSelector; // elements get current value of this as
    // they are added to a display list

    private boolean fogEnabled;

    private boolean shadingEnabled;

    public final static int MAX_VERTEX = 400;

    private static final Point3[] tempVC = new Point3[MAX_VERTEX]; // temp view
    // coords

    private static final Point2[] tempSC = new Point2[MAX_VERTEX]; // temp
    // world
    // coords

    private Appearance appearance; // current material properties

    private final static Appearance DEFAULT_APPEARANCE = new Appearance();

    private Environment environment; // current environment

    private final static Environment DEFAULT_ENVIRONMENT = new Environment();

    /**
     * @supplierCardinality 1
     * @clientCardinality 1
     */
    private DisplayList displayList;
}
