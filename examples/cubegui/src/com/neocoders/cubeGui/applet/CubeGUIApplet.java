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
package com.neocoders.cubeGui.applet;

import java.applet.Applet;
import java.awt.BorderLayout;

import com.neocoders.cubeGui.panel.CubeGUIPanel;

public class CubeGUIApplet extends Applet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6646555695585703184L;

	public void init() {
		setLayout(new BorderLayout());
		this.guiPanel = new CubeGUIPanel();
		add(guiPanel);
		setSize(400, 400);
		validate();
	}

	public void start() {
		guiPanel.start();
		validate();
	}

	public void stop() {
	}

	public void destroy() {
	}

	public String getAppletInfo() {
		return "Hello, World";
	}

	private CubeGUIPanel guiPanel;
}
