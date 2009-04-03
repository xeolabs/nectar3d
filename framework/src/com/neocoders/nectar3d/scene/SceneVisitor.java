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

/** Contract for class that would traverse a scene, ie. renderer, printer etc. */
/** Defines contract for classes that visit scene graph elements. Each
* element is visited twice during scene traversal; once before
* sub-elements are visited, and a second time after sub-elements are visited.
*/
public interface SceneVisitor {
    public void preOrderVisitEnvironment(Environment e);

    public void postOrderVisitEnvironment(Environment e);

    public void preOrderVisitTransformGroup(TransformGroup tg);

    public void postOrderVisitTransformGroup(TransformGroup tg);

    public void preOrderVisitGeometry(Geometry g);

    public void postOrderVisitGeometry(Geometry g);

    public void preOrderVisitLabel(Label t);

    public void postOrderVisitLabel(Label t);

    public void preOrderVisitName(Name n);

    public void postOrderVisitName(Name n);

    public void preOrderVisitInterpolator(Interpolator i);

    public void postOrderVisitInterpolator(Interpolator i);

    public void preOrderVisitMaterial(Appearance m);

    public void postOrderVisitAppearance(Appearance m);

    public void preOrderVisitLayer(Layer m);

    public void postOrderVisitLayer(Layer m);



}
