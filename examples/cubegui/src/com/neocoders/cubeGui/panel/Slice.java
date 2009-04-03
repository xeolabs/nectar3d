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


class Slice {
    /**Create an empty slice with no cubes
     */
    public Slice()
    {
        this.cubes = new CubeSelector[0];
    }
    public Slice(CubeSelector[] cubes) {
        this.cubes = cubes;
    }

    public CubeSelector[] getCubes() {
        return cubes;
    }

    public boolean containsCube(CubeSelector cube) {
        for (int i = 0; i < cubes.length; i++) {
            if (cubes[i].compare(cube) == 0) {
                return true;
            }
        }
        return false;
    }

    private CubeSelector[] cubes;
}
