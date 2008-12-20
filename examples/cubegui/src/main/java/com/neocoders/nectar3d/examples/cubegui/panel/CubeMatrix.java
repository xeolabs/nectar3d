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
package com.neocoders.nectar3d.examples.cubegui.panel;
import com.neocoders.nectar3d.scene.SceneException;
import com.neocoders.nectar3d.scene.TransformGroup;
import com.neocoders.nectar3d.scene.Environment;
import com.neocoders.nectar3d.scene.LightSource;
import com.neocoders.nectar3d.common.Vector3;
import com.neocoders.nectar3d.scene.Layer;
import com.neocoders.nectar3d.scene.Name;
import com.neocoders.nectar3d.scene.Box;
import com.neocoders.nectar3d.scene.Appearance;
import java.awt.Color;
import com.neocoders.nectar3d.scene.Label;
import com.neocoders.nectar3d.common.Point3;
import java.util.Vector;
import com.neocoders.nectar3d.scene.SceneElement;
import com.neocoders.nectar3d.scene.Interpolator;
import com.neocoders.nectar3d.scene.Observer;
import java.awt.Font;
import com.neocoders.nectar3d.scene.StringSelector;

class CubeMatrix implements SliceExtractionContext {
    public static final double CUBE_SIZE = 10.0;
    public static final double CUBE_SPACING = 4.0;
    public final static int STATE_DEFAULT = 0;
    public final static int STATE_EXTRACTING_SLICE = 1;
    public final static int STATE_SLICE_EXTRACTED = 2;
    public final static int STATE_RESTORING_SLICE = 3;

    public CubeMatrix(CubeData[] [] [] cubeData) throws SceneException {
        int xSize = cubeData.length;
        int ySize = (xSize == 0) ? 0 : cubeData[0].length;
        int zSize = (ySize == 0) ? 0 : cubeData[0] [0].length;
        index = new TransformGroup[xSize] [ySize] [zSize];
        double inc = CUBE_SPACING + (CUBE_SIZE * 2.0);
        double xmin = ((double)xSize * inc) * -0.5;
        double ymin = ((double)ySize * inc) * -0.5;
        double zmin = ((double)zSize * inc) * -0.5;
        double x = xmin;
        sceneRoot = new Environment();
        sceneRoot.addLightSource(new LightSource(new Vector3(1.0, 0.0, 0.0), new Color(60, 60, 0))); // yellow
        sceneRoot.addLightSource(new LightSource(new Vector3(1.0, -1.0, 0.0), new Color(100, 100, 100))); // white
        layer = new Layer(new  StringSelector("matrix"), true, true, true);
        layer.setParent(sceneRoot);
        // Cube transform, available through this API
        matrixTransform = new TransformGroup();
        matrixTransform.addTransform(TransformGroup.ROTY);
        matrixTransform.addTransform(TransformGroup.ROTX);
        matrixTransform.setParent(layer);
        // Matrix cubes
        for (int i = 0; i < xSize; i++) {
            double y = ymin;
            for (int j = 0; j < ySize; j++) {
                double z = zmin;
                for (int k = 0; k < zSize; k++) {
                    TransformGroup transform = new TransformGroup();
                    transform.addTransform(TransformGroup.TRA);
                    transform.setAttribute(TransformGroup.TRAX_VAL, x);
                    transform.setAttribute(TransformGroup.TRAY_VAL, y);
                    transform.setAttribute(TransformGroup.TRAZ_VAL, z);
                    transform.setParent(matrixTransform);
                    index[i] [j] [k] = transform;
                    Name name = new Name(new CubeSelector(i, j, k));
                    name.setParent(transform);
                    Box cube = new Box(CUBE_SIZE, CUBE_SIZE, CUBE_SIZE);
                    cube.setParent(name);
                    Appearance appearance = new Appearance();
                    appearance.setFillColor(new Color(100, 100, 255));
                    appearance.setHighlightFillColor(new Color(255, 255, 0));
                    appearance.setEdgeColor(new Color(0, 0, 255));
                    appearance.setHighlightEdgeColor(new Color(100, 100, 0));
                    appearance.setParent(cube);
                    Label label;
                    if (i == (xSize - 1) && (k == 0)) {
                        label = new Label("Stage " + (j+1), new Point3(25.0, 0.0, 0.0), new Font("helvetica", 1, 16));
                        label.setParent(transform);
                        Appearance labelAppearance = new Appearance();
                        labelAppearance.setFillColor(new Color(100, 100, 100));
                        labelAppearance.setParent(label);
                    }
                    if (j == (ySize - 1) && (i == (0))) {
                        label = new Label("Day " + (j + 1), new Point3(-25.0, 0.0, 0.0), new Font("helvetica", 1, 16));
                        label.setParent(transform);
                        Appearance labelAppearance = new Appearance();
                        labelAppearance.setFillColor(new Color(100, 100, 100));
                        labelAppearance.setParent(label);
                    }
                    z += inc;
                }
                y += inc;
            }
            x += inc;
        }
        observers = new Vector();
        nInterpolators = 0;
        state = STATE_DEFAULT;
    }

    public int getXSize() {
        return index.length;
    }

    public int getYSize() {
        return (index.length == 0) ? 0 : index[0].length;
    }

    public int getZSize() {
        return (index.length == 0) ? 0 : index[0] [0].length;
    }

    public SceneElement getScene() {
        return sceneRoot;
    }

    public Layer getLayer() {
        return layer;
    }

    public double getMatrixYaw() {
        return matrixTransform.getAttribute(TransformGroup.ROTY_VAL);
    }

    public void setMatrixYaw(double angle) {
        matrixTransform.setAttribute(TransformGroup.ROTY_VAL, angle);
    }

    public double getMatrixPitch() {
        return matrixTransform.getAttribute(TransformGroup.ROTX_VAL);
    }

    public void setMatrixPitch(double angle) {
        matrixTransform.setAttribute(TransformGroup.ROTX_VAL, angle);
    }

    public void extractSlice(Slice slice, int primitiveIndex) {
        if (state != CubeMatrix.STATE_DEFAULT) {
            return;
        }
        if (primitiveIndex == Box.XMIN_FACE_INDEX) {
            extractionStrategy = new XMinSliceExtractionStrategy(this, slice);
        }
        else if (primitiveIndex == Box.XMAX_FACE_INDEX) {
            extractionStrategy = new XMaxSliceExtractionStrategy(this, slice);
        }
        else if (primitiveIndex == Box.YMIN_FACE_INDEX) {
            extractionStrategy = new YMinSliceExtractionStrategy(this, slice);
        }
        else if (primitiveIndex == Box.YMAX_FACE_INDEX) {
            return;
        }
        else if (primitiveIndex == Box.ZMIN_FACE_INDEX) {
            extractionStrategy = new ZMinSliceExtractionStrategy(this, slice);
        }
        else if (primitiveIndex == Box.ZMAX_FACE_INDEX) {
            extractionStrategy = new ZMaxSliceExtractionStrategy(this, slice);
        }
        state = STATE_EXTRACTING_SLICE;
        SceneElement.getSceneMutex().lock(); // get mutex on scene graph
        try {
            extractionStrategy.extract();
        } catch (SceneException se) {
            se.printStackTrace();
        }
        SceneElement.getSceneMutex().unlock();
    }

    public void restoreSlice() {
        if (state != CubeMatrix.STATE_SLICE_EXTRACTED) {
            return;
        }
        SceneElement.getSceneMutex().lock(); // get mutex on scene graph
        state = STATE_RESTORING_SLICE;
        try {
            extractionStrategy.restore();
        }
        catch (SceneException se) {
            se.printStackTrace();
        }
        SceneElement.getSceneMutex().unlock();
    }

    public TransformGroup getTransformGroup(CubeSelector cubeSelector) {
        int x = cubeSelector.getXPos();
        int y = cubeSelector.getYPos();
        int z = cubeSelector.getZPos();
        return index[x] [y] [z];
    }

    public void interpolate(TransformGroup tg, int attrID, long i1, long i2, double target) throws SceneException {
        long[] instants = { i1, i2 };
        double[] values = { tg.getAttribute(attrID), target};
        Interpolator interpolator = new Interpolator(attrID, instants, values);
        interpolator.setParent(tg);
        observeInterpolatorDestruction(interpolator);
    }

    public void graftSlice(Slice slice, SceneElement newParent) {
        CubeSelector[] cubes = slice.getCubes();
        for (int i = 0; i < cubes.length; i++) {
            TransformGroup t = getTransformGroup(cubes[i]);
            SceneElement parent = t.getParent();
            //   if (parent != null) {
            parent.removeChild(t);
            // }
            newParent.addChild(t);
        }
    }

    public TransformGroup getMatrixTransform() {
        return matrixTransform;
    }

    public void extractSliceCube(CubeSelector selector) {
        if (state != CubeMatrix.STATE_SLICE_EXTRACTED) {
            return;
        }
    }

    private void observeInterpolatorDestruction(Interpolator interpolator) {
        interpolator.attachObserver(
            new Observer() {
                public void update() {
                    nInterpolators--;
                    if (nInterpolators > 0) {
                        return;
                    }
                    switch (state) {
                        case STATE_EXTRACTING_SLICE:
                            state = STATE_SLICE_EXTRACTED;
                            break;
                        case STATE_RESTORING_SLICE:
                            state = STATE_DEFAULT;
/* Destroy any temporary scene graph elements used
                        * for slice extraction/restoration
                       */
                            SceneElement.getSceneMutex().lock();
                            extractionStrategy.cleanUp();
                            SceneElement.getSceneMutex().unlock();
                            //   extractionStrategy = null;
                        default:
                            break;
                    }
                    notifyObservers();
                }
            });
        nInterpolators++;
    }

    public void attachObserver(Observer observer) {
        observers.addElement(observer);
    }

    public void detachObserver(Observer observer) {
        observers.removeElement(observer);
    }

    protected void notifyObservers() {
        for (int i = 0; i < observers.size(); i++) {
            ((Observer)observers.elementAt(i)).update();
        }
    }

    public int getState() {
        return state;
    }

    private Vector observers;
    private Environment sceneRoot;
    private Layer layer;
    private TransformGroup[] [] [] index;
    private int nInterpolators;
    private int state;
    private TransformGroup matrixTransform;
    private SliceExtractionStrategy extractionStrategy;

    /** @link dependency */

    /*# CubeSelector lnkCubeSelector; */

    /** @link dependency */

    /*# CubeData lnkCubeData; */
}
