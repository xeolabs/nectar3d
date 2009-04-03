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
package com.neocoders.nectar3d.renderer;

public class SceneRendererEvent {
	public SceneRendererEvent(int type) {
		this.type = type;
		this.data = null;
	}

	public SceneRendererEvent(int type, Object data) {
		this.type = type;
		this.data = data;
	}

	public int getType() {
		return type;
	}

	public Object getData() {
		return data;
	}

	private int type;

	private Object data;

	public final static int MOUSE_PRESSED = 0;

	public final static int MOUSE_RELEASED = 1;

	public final static int MOUSE_MOVED = 2;

	public final static int MOUSE_PICKED = 3;

	public final static int MOUSE_OVER = 4;

	public final static int MOUSE_DRAGGED = 5;

	public final static int ERROR = 6;

}
