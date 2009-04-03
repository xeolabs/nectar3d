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

package com.neocoders.cubeGui.app;

import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import com.neocoders.cubeGui.panel.CubeGUIPanel;

public class CubeGUIApp {
	public CubeGUIApp() {
		try {
			frame = new Frame();
			frame.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.fill = GridBagConstraints.BOTH;
			CubeGUIPanel panel = new CubeGUIPanel();
			frame.add(panel, c);
			frame.setSize(400, 400);
			panel.start();
			frame.validate();
			frame.setResizable(false);
			frame.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		new CubeGUIApp();
	}

	private Frame frame;
}
