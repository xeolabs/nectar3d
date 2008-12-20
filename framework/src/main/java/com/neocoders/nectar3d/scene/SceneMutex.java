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

/** Mutual exclusion lock, used for avoiding concurrent modification of
* a scene graph.
*/
public class SceneMutex {
    private Thread busyFlag = null;
    private int busyCount = 0;

    /** Attempt to lock the mutex; block if unsuccessful, to
     * be unblocked when this thread's turn comes up
     */
    public synchronized void lock() {
        while (tryLock () == false) {
            try {
                wait();
            }
            catch (Exception e) { }
        }
    }

    /** Try to lock the mutex
     * @return true if successful else false
     */
    public synchronized boolean tryLock() {
        if (busyFlag == null) {
            busyFlag = Thread.currentThread();
            busyCount = 1;
            return true;
        }
        if (busyFlag == Thread.currentThread()) {
            busyCount++;
            return true;
        }
        return false;
    }

    /** Unock the mutex; all threads that are currently blocked
     * trying to lock the mutex are notified; one lucky blocked thread
     * will get to lock and be unblocked while others will remain blocked
     */
    public synchronized void unlock() {
        if (getOwner() == Thread.currentThread()) {
            busyCount--;
            if (busyCount == 0) {
                busyFlag = null;
                notify();
            }
        }
    }

    /**Get the thread that has locked this mutex, if any
     * @return the thread that has locked this mutex
     */
    public synchronized Thread getOwner() {
        return busyFlag;
    }
}
