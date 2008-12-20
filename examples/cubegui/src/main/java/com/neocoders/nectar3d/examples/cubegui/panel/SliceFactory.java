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
import com.neocoders.nectar3d.scene.Box;

class SliceFactory {
    public SliceFactory(int xSize, int ySize, int zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public Slice getSlice(CubeSelector cubeSelector, int primitiveIndex) {
        int xPos = cubeSelector.getXPos();
        int yPos = cubeSelector.getYPos();
        int zPos = cubeSelector.getZPos();
        switch (primitiveIndex) // which cube face?
        {
            /* We only allow cube at the extent of the matrix to be selected
            */

            case Box.ZMAX_FACE_INDEX:
                if (zPos == (zSize - 1)) {
                    return getSliceXZ(yPos);
                }
                break;
            case Box.ZMIN_FACE_INDEX:
                if (zPos == 0) {
                    return getSliceXZ(yPos);
                }
                break;
            case Box.XMAX_FACE_INDEX:
                if (xPos == (xSize - 1)) {
                    return getSliceXY(zPos);
                }
                break;
            case Box.XMIN_FACE_INDEX:
                if (xPos == 0) {
                    return getSliceXY(zPos);
                }
                break;
            case Box.YMAX_FACE_INDEX:
                if (yPos == (ySize - 1)) {
                    return getSliceYZ(xPos);
                }
                break;
            case Box.YMIN_FACE_INDEX:
                if (yPos == 0) {
                    return getSliceYZ(xPos);
                }
                break;
        }
        return null;
    }

    private Slice getSliceXZ(int yPos) {
        CubeSelector[] cubes = new CubeSelector[xSize * zSize];
        int i = 0;
        for (int x = 0; x < xSize; x++) {
            for (int z = 0; z < zSize; z++) {
                cubes[i++] = new CubeSelector(x, yPos, z);
            }
        }
        return new Slice(cubes);
    }

    private Slice getSliceXY(int zPos) {
        CubeSelector[] cubes = new CubeSelector[xSize * zSize];
        int i = 0;
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                cubes[i++] = new CubeSelector(x, y, zPos);
            }
        }
        return new Slice(cubes);
    }

    private Slice getSliceYZ(int xPos) {
        CubeSelector[] cubes = new CubeSelector[ySize * zSize];
        int i = 0;
        for (int y = 0; y < ySize; y++) {
            for (int z = 0; z < zSize; z++) {
                cubes[i++] = new CubeSelector(xPos, y, z);
            }
        }
        return new Slice(cubes);
    }

    private int xSize;
    private int ySize;
    private int zSize;

    /**
     * @link dependency
     * @stereotype instantiate
     */

    /*# Slice lnkSlice; */
}
