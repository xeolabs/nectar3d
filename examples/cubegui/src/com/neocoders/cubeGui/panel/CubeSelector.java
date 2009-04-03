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
package com.neocoders.cubeGui.panel;
import com.neocoders.nectar3d.scene.Selector;

class CubeSelector implements Selector {
    public CubeSelector(int xPos, int yPos, int zPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }

    public int compare(Selector selector) {
        CubeSelector cubeSelector = (CubeSelector)selector;
        if (cubeSelector.xPos < xPos) {
            return -1;
        }
        else if (cubeSelector.xPos > xPos) {
            return 1;
        }
        if (cubeSelector.yPos < yPos) {
            return -1;
        }
        else if (cubeSelector.yPos > yPos) {
            return 1;
        }
        if (cubeSelector.zPos < zPos) {
            return -1;
        }
        else if (cubeSelector.zPos > zPos) {
            return 1;
        }
        return 0;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public int getZPos() {
        return zPos;
    }

    public String toString() {
        return "xPos = " + xPos + ", yPos = " + yPos + ", zPos = " + zPos;
    }

    private int xPos;
    private int yPos;
    private int zPos;
}
