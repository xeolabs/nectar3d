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

import com.neocoders.nectar3d.scene.TransformGroup;

class XMaxSliceExtractionStrategy implements SliceExtractionStrategy {
    public XMaxSliceExtractionStrategy(SliceExtractionContext context, Slice slice) {
        this.slice = slice;
        this.context = context;
    }

    public void extract() {
        try {
  /* Set up temporary slice transform
            */

            extract = new TransformGroup();
            extract.setParent(context.getLayer());

        /* Extract from matrix
        */

            extract.addTransform(TransformGroup.TRA);
            context.interpolate(extract, TransformGroup.TRAX_VAL, 0, 800, EXTRACTION_OFFSET);

        /* Rotate about matrix
        */

            extract.addTransform(TransformGroup.ROTY);
            TransformGroup matrixTransform = context.getMatrixTransform();
            roty = matrixTransform.getAttribute(TransformGroup.ROTY_VAL);
            extract.setAttribute(TransformGroup.ROTY, roty);
            context.interpolate(extract, TransformGroup.ROTY_VAL, 800, 1600, 270.0);
            extract.addTransform(TransformGroup.ROTX);
            rotx = matrixTransform.getAttribute(TransformGroup.ROTX_VAL);
            extract.setAttribute(TransformGroup.ROTX, rotx);
            context.interpolate(extract, TransformGroup.ROTX_VAL, 800, 1600, 0.0);

        /* Orient slice
        */

            orient = new TransformGroup();
            orient.setParent(extract);
            orient.addTransform(TransformGroup.ROTY);
            if (roty > 270) {
                context.interpolate(orient, TransformGroup.ROTY_VAL, 800, 1600, 90.0);
            }
            else {
                context.interpolate(orient, TransformGroup.ROTY_VAL, 800, 1600, -90.0);
            }

        /* Transfer cubes from matrix to slice transform
        */

            context.graftSlice(slice, orient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restore() {
        try {
            context.interpolate(extract, TransformGroup.ROTY_VAL, 0, 800, roty);
            context.interpolate(extract, TransformGroup.ROTX_VAL, 0, 800, rotx);
            context.interpolate(orient, TransformGroup.ROTY_VAL, 0, 800, 0.0);
            context.interpolate(extract, TransformGroup.TRAX_VAL, 800, 1600, 0.0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Remove temporary transforms that were added to scene graph to do extraction and restoration */
    public void cleanUp() {
        extract.destroy();
        orient.destroy();
        context.graftSlice(slice, context.getMatrixTransform());
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

