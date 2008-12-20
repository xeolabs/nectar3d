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
package com.neocoders.nectar3d.examples.cubegui.panel;
import java.util.Vector;
import com.neocoders.nectar3d.scene.SceneException;

class CubeMatrixBuilder {
    private Vector columns;
    private Vector column;
    private Vector row;
    private int nColumns;
    private int nRows;
    private int rowSize;
    private CubeMatrix cubeMatrix;

    /** @link dependency */

    /*# CubeData lnkCubeData; */

    /** @link dependency */

    /*# CubeSelector lnkCubeSelector; */

    /** @link dependency */

    /*# BuilderException lnkBuilderException; */

    public CubeMatrixBuilder() {
        init();
    }

    private void init() {
        columns = null;
        column = null;
        row = null;
        nColumns = 0;
        nRows = 0;
        rowSize = 0;
        cubeMatrix = null;
    }

    public void build() {
        init();
        columns = new Vector();
    }

    public void openColumn(String id) throws BuilderException {
        if (columns == null) {
            throw new BuilderException("no matrix being built");
        }
        column = new Vector();
        columns.addElement(column);
        row = null;
    }

    public void openRow(String id) throws BuilderException {
        if (column == null) {
            throw new BuilderException("no column being built");
        }
        row = new Vector();
        column.addElement(row);
    }

    public void addEntry(String id) throws BuilderException {
        if (row == null) {
            throw new BuilderException("no row being built");
        }
        row.addElement(id);
    }

    public void close() throws BuilderException {
        if (row != null) {
            if (column.size() == 1) {
                if (row.size() == 0) {
                    throw new BuilderException("row empty");
                }
                rowSize = row.size();
            }
            else if (row.size() < rowSize) {
                throw new BuilderException("row length mismatch");
            }
            row = null;
        }
        else if (column != null) {
            if (columns.size() == 1) {
                if (column.size() == 0) {
                    throw new BuilderException("column empty");
                }
                nRows = column.size();
            }
            else if (column.size() < nRows) {
                throw new BuilderException("column height mismatch");
            }
            nColumns++;
            column = null;
        }
        else {
            throw new BuilderException("no row or column being built");
        }
    }

    public CubeMatrix getBuilt() throws BuilderException {
        try {
            if (cubeMatrix != null) {
                return cubeMatrix;
            }
            if (column != null) {
                throw new BuilderException("matrix not complete");
            }
            if (columns == null) {
                throw new BuilderException("no matrix built");
            }
           CubeData[] [] [] cubeData = new CubeData[nColumns] [nRows] [rowSize];
            for (int i = 0; i < nColumns; i++) {
                column = (Vector)columns.elementAt(i);
                for (int j = 0; j < nRows; j++) {
                    row = (Vector)column.elementAt(j);
                    for (int k = 0; k < rowSize; k++) {
                        cubeData[i] [j] [k] = new CubeData((String)row.elementAt(k));
                    }
                }
            }

            /** I observe the cube matrix so that I know when slice extraction/restoration has finished */
            cubeMatrix = new CubeMatrix(cubeData);
            return cubeMatrix;
        } catch (SceneException e) {
            throw new BuilderException(e.getMessage());
        }
    }
}
