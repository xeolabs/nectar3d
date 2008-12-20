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

class ZMaxSliceExtractionStrategy implements SliceExtractionStrategy {
    public ZMaxSliceExtractionStrategy(SliceExtractionContext context, Slice slice) {
        this.slice = slice;
        this.context = context;
    }

    public void extract() throws SceneException {
        extract = new TransformGroup();
        extract.setParent(context.getLayer());

        /* Extract from matrix
        */

        extract.addTransform(TransformGroup.TRA);
        context.interpolate(extract, TransformGroup.TRAZ_VAL, 0, 800, EXTRACTION_OFFSET);

        /* Rotate about matrix
        */

        extract.addTransform(TransformGroup.ROTY);
        TransformGroup matrixTransform = context.getMatrixTransform();
        roty = matrixTransform.getAttribute(TransformGroup.ROTY_VAL);
        extract.setAttribute(TransformGroup.ROTY, roty);
        if (roty < 180) {
            context.interpolate(extract, TransformGroup.ROTY_VAL, 800, 1600, 0.0);
        }
        else {
            context.interpolate(extract, TransformGroup.ROTY_VAL, 800, 1600, 360.0);
        }
        extract.addTransform(TransformGroup.ROTX);
        rotx = matrixTransform.getAttribute(TransformGroup.ROTX_VAL);
        extract.setAttribute(TransformGroup.ROTX, rotx);
        context.interpolate(extract, TransformGroup.ROTX_VAL, 800, 1600, 0.0);

        /* Orient slice
        */

        orient = new TransformGroup();
        orient.setParent(extract);
        orient.addTransform(TransformGroup.ROTX);
        context.interpolate(orient, TransformGroup.ROTX_VAL, 800, 1600, -90.0);

        /* Transfer cubes from matrix to slice transform
        */

        context.graftSlice(slice, orient);
    }

    public void restore() throws SceneException {
        context.interpolate(extract, TransformGroup.TRAZ_VAL, 800, 1600, 0);
        context.interpolate(extract, TransformGroup.ROTY_VAL, 0, 800, roty);
        context.interpolate(extract, TransformGroup.ROTX_VAL, 0, 800, rotx);
        context.interpolate(orient, TransformGroup.ROTX_VAL, 0, 800, 0.0);
    }

    public void cleanUp() {
        context.graftSlice(slice, context.getMatrixTransform());
        extract.destroy();
        orient.destroy();
    }

    private Slice slice;
    private SliceExtractionContext context;

    /* What we need to remember after extraction so we can restore
    */

    private TransformGroup extract;
    private TransformGroup orient;
    double roty;
    double rotx;
}

