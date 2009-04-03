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

import java.util.Vector;
import java.util.Iterator;

/**
 * Basic scene element which can be linked into a directed acyclic graph
 * with other scene elements.
 * <br>
 * <b>Element Destruction</b>
 * <br>
 * An element can be destroyed, and all destroyed elements can be
 * garbage-collected in a batch. When garbage-collected, an element is removed
 * from it's parent and observers of the element are notified. Destruction and
 * garbage-collection are seperate operations in order to avoid a concurrent
 * modification exception being thrown if we are iterating an element's children
 * when we destroy one of them. Garbage collection is therefore to be done after
 * we have iterated a scene graph.
 */
public class SceneElement {
    /** Create new basic scene element with no parent or children */
    public SceneElement() {
        parent = null;
        children = null;
        observers = null;
        state = STATE_DEFAULT;
    }

    /**
     *Get parent of this element
     * @return the parent
     */
    public SceneElement getParent() {
        return parent;
    }

    /**
     *Set new parent of this element. If parent already exists, this element
     * is first removed from the children of the original parent.
     * @param newParent the new parent for this element
     */
    public void setParent(SceneElement newParent) {
        if (parent != null) {
            parent.removeChild(this);
        }
        newParent.addChild(this);
    }

    /**
     *Add a element as last child of this. If the element already has a parent,
     * it is first removed from the children of it's original parent
     * @param child new child for this
     */
    public void addChild(SceneElement child) {
     if (child.parent != null) {
            child.parent.removeChild(child);
        }
        if (children == null) {
            children = new Vector();
        }
        children.addElement(child);
    	child.parent = this;
    }

  //

    /**
     * Removes a child from this element Nothing happens if element is not child of this.
     * @param child child to remove
     */
    public void removeChild(SceneElement child) {
        if (children == null) {
            return;
        }
        if (!children.contains(child)) {
            return;
        }
        children.removeElement(child);
        child.parent = null;
    }

    /**
     *Gets child elements of this
     * @return child elements
     */
    public Iterator getChildren() {
        if (children == null) {
            return null;
        }
        return children.iterator();
    }

    /**
     * Get child element at given index
     * @param i index of child element to get
     */
    public SceneElement getChild(int i) throws SceneException {
        if ((i < 0) || (i >= children.size())) {
            throw new SceneException("child index (" + i + ") out of range (0.." + (children.size() - 1) + ")");
        }
        return (SceneElement)children.elementAt(i);
    }

    /**
     * Accept scene graph visitor before it visits sub-elements
     * @param visitor the scene graph visitor
     */
    public void acceptPreOrderVisitor(SceneVisitor visitor) {
    }

    /**
     * Accept scene graph visitor after it has visited sub-elements
     * @param visitor the scene graph visitor
     */
    public void acceptPostOrderVisitor(SceneVisitor visitor) {
    }

    /** Destroys this element. Observers are notified when garbage collected with subsequent call to garbageCollect. */
    public void destroy() {
        if (state == STATE_DESTROYED) {
            return;
        }
        state = STATE_DESTROYED;
        destroyedElements.addElement(this);
    }

    /** Garbage-collects destroyed elements, removing each element from it's parent and notifying it's observers. */
    public static void garbageCollect() {
        for (int i = 0; i < destroyedElements.size(); i++) {
            SceneElement element = (SceneElement)destroyedElements.elementAt(i);
            element.getParent().removeChild(element);
            element.notifyObservers();
        }
        destroyedElements.clear();
    }

    /**
     *Attach observer to this element to be notified when certain state changes occur on the element, ie. when destroyed
     * @param observer the observer to attach
     */
    public void attachObserver(Observer observer) {
        if (observers == null) {
            observers = new Vector();
        }
        observers.addElement(observer);
    }

    /**
     *Detach observer from this element
     * @param observer to detach
     */
    public void detachObserver(Observer observer) {
        if (observers == null) {
            return;
        }
        observers.removeElement(observer);
    }

    protected void notifyObservers() {
        if (observers != null) {
            for (int i = 0; i < observers.size(); i++) {
                ((Observer) observers.elementAt(i)).update();
            }
        }
    }

    /**
     *Obtain state of this element
     * @return STATE_DEFAULT or STATE_DESTROYED
     */
    public int getState() {
        return state;
    }

    /**
     * Gets scene graph lock, which is used to prevent concurrent modification of a scene graph
     * @return the scene graph lock
     */
    public static SceneMutex getSceneMutex() {
        return mutex;
    }

    public void print()
    {
        System.out.println(this.toString());
       if (children == null)
       {
        return;
       }
        Iterator i = children.iterator();
        while (i.hasNext())
        {
            ((SceneElement)i.next()).print();
        }
    }

    /**
     * @supplierCardinality 0..* 
     */
    protected SceneElement parent;
    protected Vector children;
    private static Vector destroyedElements = new Vector();
    private Vector observers;
    private int state;
    /** Default state of a scene element */
    public final static int STATE_DEFAULT = 0;
    /**State of a scene element that has been destroyed */
    public final static int STATE_DESTROYED = 1;
    private static SceneMutex mutex = new SceneMutex();
}
