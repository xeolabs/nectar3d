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

import java.awt.Graphics;

import com.neocoders.nectar3d.common.ErrorHandler;
import com.neocoders.nectar3d.common.Point2;

import com.neocoders.nectar3d.scene.SceneElement;
import com.neocoders.nectar3d.scene.SceneIterator;

import com.neocoders.nectar3d.scene.Selector;

/**
 * Renders a scene graph. The rendered scene will be viewed along the decreasing
 * Z-axis.
 * <p>
 * <b>Configuring a SceneRenderer</b>
 * <p>
 * A SceneRenderer is configured with
 * <ol>
 * <li>view volume extents,</li>
 * <li>physical device window extents,</li>
 * <li>distance to the perspective projection plane on the decreasing Z-axis</li>
 * <li>an optional error handler (see below),</li>
 * <li>the camera position, up vector and "lookat" point,</li>
 * <li>background color,</li>
 * <li>one or more light sources and</li>
 * <li>the current scene time in milliseconds.</li>
 * </ol>
 * The view volume and window are given to the constructor since these are not
 * expected to change. The current scene time is used for updating key frame
 * interpolators. Scene time should always advance forwards; note that results
 * are undetermined if scene time goes backwards.
 * <p>
 * <b>Error Handling</b>
 * <p>
 * Errors may occur while rendering a scene graph, either because the scene
 * graph is not structured correctly or because the renderer ran out of some
 * neccessary resource while rendering, eg. memory. When an error occurs a
 * descriptive error message is logged and SceneRenderer recovers as best as it
 * can. Errors are logged with an error handler, a custom instance of which may
 * be supplied to the SceneBuilder. If an error handler is not supplied,
 * SceneRenderer uses it's own default internal handler, which prints messages
 * to standard output.
 * <p>
 * <b>Selection</b>
 * <p>
 * Once a scene is rendered, visible elements may be selected. Selection is made
 * by specifying a physical window coordinate; SceneRenderer selects the nearest
 * named element (on the decreasing Z axis) that intersects the coordinate and
 * returns it's name.
 * <p>
 * <b>Fast Re-rendering</b>
 * <p>
 * A SceneRenderer "remembers" the last scene it renderered and can re-render it
 * quickly via a given java.awt.Graphics object, without referring to the scene
 * graph.
 * <p>
 * <b>Highlighting</b>
 * </p>
 * A list of scene element names can be given to a SceneRenderer so that next
 * time a scene graph is rendered, or redrawn using the fast redraw feature
 * mentioned above, visible elements with names matching those in the list are
 * highlighted. When used with the fast redraw feature, highlighting is handy
 * for mouse-over effects. .
 */
class Renderer {
	/**
	 * Create new scene renderer
	 * 
	 * @param volume
	 *            the view frustum
	 * @param window
	 *            physical device window extents
	 * @param distance
	 *            to projection plane on negative Z-axis
	 */
	public Renderer(SceneRendererParams config) {
		displayList = new DisplayList();
		displayListBuilder = new DisplayListBuilder(config, displayList);
	}

	/**
	 * Set an error handler to log error messages when errors occur while
	 * rendering a scene graph
	 * 
	 * @param errorHandler
	 *            a user-defined error handler
	 */
	public void setErrorHandler(ErrorHandler errorHandler) {
		displayListBuilder.setErrorHandler(errorHandler);
	}

	public void setParams(SceneRendererParams config) {
		displayListBuilder.setParams(config);
	}

	public void highlight(Selector[] selectors) {
		displayList.setHighlighted(selectors);
	}

	public void unHighlight() {
		displayList.setHighlighted(null);
	}

	/**
	 * Render a scene graph.
	 * 
	 * @param root
	 *            root element of the scene graph
	 * @param g
	 *            a graphics context with which to render scene elements
	 */
	public void render(SceneElement root, Graphics g) {

		displayList.clear(); // layers are created by Layer elements
		SceneIterator iterator = new SceneIterator();
		iterator.iterate(root, displayListBuilder);
		displayList.render(g);
	}

	/**
	 * Get the name of the nearest named element (on the decreasing Z axis) that
	 * intersects the given window coordinate
	 * 
	 * @param sc
	 *            window coordinate
	 */
	public PickInfo pick(Point2 sc) {
		return displayList.pick(sc);
	}

	/**
	 * Re-render image previously rendered. Any elements appearing in the image
	 * that have names matching those specified for highlighting will be
	 * highlighted. Any changes to the scene graph or new renderer
	 * configurations (eg. view parameters, background color etc.) since the
	 * last time a scene was rendered will not affect the re-rendered image. A
	 * scene may be re-rendered as many times as required. Nothing will be
	 * rendered if the renderer has been reset since the last render or
	 * re-render.
	 * 
	 * @param g
	 *            a graphics context
	 */
	public void reRender(Graphics g) {
		displayList.render(g);
	}

	/** Clear the renderer. A subsequent call to reRender will render nothing. */
	public void clear() {
		displayListBuilder.reset();
		displayList.clear(); // layers are created by Layer elements
	}

	/**
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 */
	private DisplayList displayList;

	/**
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 */
	private DisplayListBuilder displayListBuilder;
}
