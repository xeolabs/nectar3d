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


import java.util.Iterator;
import java.util.Vector;

public class Switch extends SceneElement {
    public Switch() {
        super();
        index = UNDEFINED_SWITCH_INDEX;
        switchedChild = new Vector();
    }

    /**
     * Removes a child from this element Nothing happens if element is not child of this.
     * @param child child to remove
     */
    public void removeChild(SceneElement child) {
        if (switchedChild.contains(child)) {
            switchedChild.clear();
        }
        super.removeChild(child);
    }

    /**
     *Gets the currently switched child element - returns empty iterator if no child is switched
     * @return child elements
     */
    public Iterator getChildren() {
        return switchedChild.iterator();
    }

    /**
     *Specify which child element is currently switched
     * @param i index of current switched child element
     */
    public void setSwitch(int i) throws SceneException {
        if ((i < 0) || (i >= children.size())) {
            throw new SceneException("child index (" + i + ") out of range (0.." + (children.size() - 1) + ")");
        }
        switchedChild.clear();
        switchedChild.addElement(children.elementAt(i));
        this.index = i;
    }

    /**
     *Gets index of currently switched child element
     * @return index of currently switched child element
     */
    public int getSwitch() {
        return index;
    }

    public final int UNDEFINED_SWITCH_INDEX = -1;
    private int index;
    private Vector switchedChild;
}
