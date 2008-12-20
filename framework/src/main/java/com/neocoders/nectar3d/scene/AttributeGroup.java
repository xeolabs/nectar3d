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

/** Contract for scene elements that have indexable attributes
*/
public interface AttributeGroup {
    /**Sets the value of an attribute
     * @param attrID index of attribute
     * @param value new value for attribute
     */
    public void setAttribute(int attrID, double value);

    /** Gets the current value of an attribute
     * @param attrID index of attribute
     * @return current value of an attribute
     */
    public double getAttribute(int attrID);
}
