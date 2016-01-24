import java.awt.Color;

import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

// Represents a single square of the game area
class Cell {
    // represents absolute height of this cell, in feet
    double height;
    // In logical coordinates, with the origin at the top-left corner of the
    // screen
    int x;
    int y;
    // the four adjacent cells to this one
    Cell left;
    Cell top;
    Cell right;
    Cell bottom;
    // reports whether this cell is flooded or not
    boolean isFlooded;

    // create cell with specified height, x, and y
    Cell(double height, int x, int y) {
        this.height = height;
        this.x = x;
        this.y = y;
    }

    // is this cell equal to the given cell
    boolean equalCells(Cell that) {
        return this.x == that.getX() && this.y == that.getY();
    }

    // render image of this cell
    WorldImage render(int waterLevel) {
        return new RectangleImage(this.getPosn(),
                ForbiddenIslandWorld.SQUARE_SIZE,
                ForbiddenIslandWorld.SQUARE_SIZE, this.getColor(waterLevel));
    }

    // get the posn for this cell
    Posn getPosn() {
        return new Posn(this.x * ForbiddenIslandWorld.SQUARE_SIZE
                + (ForbiddenIslandWorld.SQUARE_SIZE / 2), this.y
                * ForbiddenIslandWorld.SQUARE_SIZE
                + (ForbiddenIslandWorld.SQUARE_SIZE / 2));
    }

    // get the color of this cell
    Color getColor(int waterLevel) {

        // if this cell is flooded, color is between blue and black
        if (this.isFlooded) {
            return new Color(
                    0f, 0f,
                    Math.min(1f, Math.max(0f, 0.5f - 
                            (float)((waterLevel - this.height) / 
                                    ForbiddenIslandWorld.ISLAND_SIZE))));
        }
        
        // if this cell is below water level, but not flooded, color is
        // between red and green
        else if (this.height - waterLevel < 0) {
            return new Color(
                    Math.min(0.5f + (float)
                            ((waterLevel - this.height) / 
                                    ((ForbiddenIslandWorld.ISLAND_SIZE / 2.0))),
                            1f),
                    Math.max(0.5f - (float) 
                            ((waterLevel - this.height) / 
                                    ((ForbiddenIslandWorld.ISLAND_SIZE / 2.0))), 
                            0), 0f); 
            
        }
        
        // if this cell is above water level, color is between green and
        // white
        else {
            return new Color(Math.min(1f, (float) 
                    (this.height / (ForbiddenIslandWorld.ISLAND_SIZE / 2.0))), 
                    Math.min(1f, 0.5f + (float) 
                            (this.height / ForbiddenIslandWorld.ISLAND_SIZE)), 
                    Math.min(1f, (float) 
                            (this.height / (ForbiddenIslandWorld.ISLAND_SIZE / 2.0))));
        }
    }

    // getters and setters for this cells fields
    public void setLeft(Cell left) {
        this.left = left;
    }

    public void setTop(Cell top) {
        this.top = top;
    }

    public void setRight(Cell right) {
        this.right = right;
    }

    public void setBottom(Cell bottom) {
        this.bottom = bottom;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Cell getLeft() {
        return left;
    }

    public Cell getTop() {
        return top;
    }

    public Cell getRight() {
        return right;
    }

    public Cell getBottom() {
        return bottom;
    }

    // returns the status of flooded boolean
    public boolean isFlooded() {
        return this.isFlooded;
    }

    // set flooded
    public void setFlooded(boolean flooded) {
        this.isFlooded = flooded;
    }

    // determines if a cell is floodable
    // (below water level and adjacent to a flooded cell)
    boolean floodable(int waterHeight) {
        if (!this.isFlooded
                && (this.left.isFlooded() || this.right.isFlooded()
                        || this.top.isFlooded() || this.bottom.isFlooded())
                && (this.height - waterHeight < 1)) {
            return true;
        }
        return false;
    }

}

// class for ocean cells
class OceanCell extends Cell {

    // call super class constructor
    OceanCell(double height, int x, int y) {
        super(height, x, y);

        // all ocean cells are considered "flooded"
        this.isFlooded = true;
    }

}