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

import com.neocoders.nectar3d.scene.SceneElement;
import com.neocoders.nectar3d.scene.Selector;

/**
 * Defines the contract for a renderer. A renderer is configured before use with
 * a ViewConfig object, and fires events which may be handled with a
 * SceneViewListener.
 * <p>
 * A SceneView can be in one of three states: stopped, rendering or
 * highlighting. Initially a SceneView is stopped, either awaiting a scene graph
 * to render, or after some error was found in the last scene graph it attempted
 * to render. After being refreshed with a scene graph, the SceneView enters the
 * rendering state. In this state SceneView periodically renders the scene,
 * incrementing the scene time in it's ViewConfig and updating Interpolators
 * accordingly with each render. While rendering, SceneView may find an error in
 * the scene graph and stop, be refreshed with a new scene graph and continue
 * rendering, or be directed to highlight scene elements, in which case it
 * enters the highlighting state. When highlighting, SceneView redraws the last
 * image rendered with the specified elements highlighted and does not increment
 * the scene time. In this state, a new list of elements may be specified to
 * highlight in which case SceneView will continue to redraw the same image but
 * with a different set of elements highlighted. If refreshed with a new scene
 * graph or reconfigured with a new ViewConfig, SceneView moves back to the
 * rendering state.
 */
public interface SceneRenderer {

	/**
	 * Sets a listener to handle SceneViewEvents fired by this renderer
	 * 
	 * @param listener
	 *            the listener
	 */
	public void setListener(SceneRendererListener listener);

	/**
	 * Configures this renderer
	 * 
	 * @param rendererParams
	 *            new configuration
	 */
	public void setParams(SceneRendererParams rendererParams);

	/**
	 * Refreshes this view with a new scene graph to render
	 * 
	 * @param scene
	 *            new scene graph
	 */
	public void setScene(SceneElement scene);

	public void highlight(Selector[] selectors);

	public void unHighlight();

	public int getState();

	public final static int STATE_STOPPED = 0;

	public final static int STATE_RENDERING = 1;

	public final static int STATE_HIGHLIGHTING = 2;
	/* # SceneRendererParams lnkRendererParams; */

	/**
	 * @supplierCardinality 1
	 */
	/* # SceneRendererListener lnkSceneRendererListener; */
}
