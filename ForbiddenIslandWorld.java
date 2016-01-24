import java.util.ArrayList;
import java.util.Random;

import javalib.colors.Red;
import javalib.colors.White;
import javalib.impworld.World;
import javalib.worldimages.*;

class ForbiddenIslandWorld extends World {

    // define world size constant
    static final int ISLAND_SIZE = 64;

    // pixel size of a single square
    static final int SQUARE_SIZE = 15;

    // nudge factor
    static final double NUDGE = .8;

    // ocean factor
    static final double OCEAN_FACTOR = .5;

    // number of targets
    static final int NUM_TARGETS = 5;

    // total pixel width/height of world
    int pixels = (ForbiddenIslandWorld.ISLAND_SIZE + 1)
            * ForbiddenIslandWorld.SQUARE_SIZE;

    // All the cells of the game, including the ocean
    IList<Cell> board;

    // the current height of the ocean
    int waterHeight;

    // current world tick
    int tick = 0;

    // represents the player in this world
    Player player;

    // list of all targets
    IList<Target> targets;

    // helicopter
    HelicopterTarget heli;

    // constructor for height of 0
    ForbiddenIslandWorld() {
        this(0);
    }

    // constructor for specified waterHeight, used for testing
    ForbiddenIslandWorld(int waterHeight) {
        this.waterHeight = waterHeight;
        this.initMountain();

    }

 // create image of the world by overlaying each cell on the background
    public WorldImage makeImage() {

        // create background
        WorldImage image = new RectangleImage(new Posn(this.pixels / 2,
                this.pixels / 2), this.pixels, this.pixels, new White());

        // iterate through cells, overlaying each
        for (Cell c : this.board) {
            image = new OverlayImages(image, c.render(this.waterHeight));
        }

        // draw heli
        image = new OverlayImages(image, this.heli.render());

        // draw targets
        for (Target t : this.targets) {
            image = new OverlayImages(image, t.render());
        }

        image = new OverlayImages(image, this.player.render());

        // return the result
        return image;
    }


    // initialize a terrain world
    void initTerrain() {

        // calculate maximum height of the world
        int maxHeight = ForbiddenIslandWorld.ISLAND_SIZE / 2;

        // initialize 2d arraylist of heights
        ArrayList<ArrayList<Double>> heights = new ArrayList<ArrayList<Double>>();

        // iterate through, setting all heights to 0
        for (int i = 0; i < ForbiddenIslandWorld.ISLAND_SIZE + 2; i++) {
            heights.add(new ArrayList<Double>());
            for (int j = 0; j < ForbiddenIslandWorld.ISLAND_SIZE + 2; j++) {
                heights.get(i).add(0.0);
            }
        }

        // init the midpoints of the edges to a height of 1
        heights.get(0).set(maxHeight, 1.0);
        heights.get(ForbiddenIslandWorld.ISLAND_SIZE).set(maxHeight, 1.0);
        heights.get(maxHeight).set(0, 1.0);
        heights.get(maxHeight).set(ForbiddenIslandWorld.ISLAND_SIZE, 1.0);

        // set center height to max height
        heights.get(maxHeight).set(maxHeight, new Double(maxHeight));

        for (int i = maxHeight + 1; i > 2; i = (i / 2) + 1) {

            // calculate mids along horizontal
            for (int j = i / 2; j < ForbiddenIslandWorld.ISLAND_SIZE; j += (i - 1)) {
                for (int k = 0; k < ForbiddenIslandWorld.ISLAND_SIZE; k += (i - 1)) {

                    heights.get(j).set(
                            k,
                            this.getHeightLine(heights.get(j - (i / 2)).get(k),
                                    heights.get(j + (i / 2)).get(k), i * i));
                }
            }

            // calculate mids along vertical
            for (int j = 0; j < ForbiddenIslandWorld.ISLAND_SIZE; j += (i - 1)) {
                for (int k = i / 2; k < ForbiddenIslandWorld.ISLAND_SIZE; k += (i - 1)) {
                    heights.get(j).set(
                            k,
                            this.getHeightLine(heights.get(j).get(k - (i / 2)),
                                    heights.get(j).get(k + (i / 2)), i * i));
                }
            }

            // calculate mids of mids
            for (int j = i / 2; j < ForbiddenIslandWorld.ISLAND_SIZE; j += (i - 1)) {
                for (int k = i / 2; k < ForbiddenIslandWorld.ISLAND_SIZE; k += (i - 1)) {
                    heights.get(j).set(k, this.getHeightRect(
                    // top
                            heights.get(j).get(k - (i / 2)),
                            // bot
                            heights.get(j).get(k + (i / 2)),
                            // left
                            heights.get(j - (i / 2)).get(k),
                            // right
                            heights.get(j + (i / 2)).get(k), i * i));
                }
            }
        }

        // create grid from the heights
        ArrayList<ArrayList<Cell>> grid = this.createGrid(heights);

        // initialize new list of cells
        this.board = new Empty<Cell>();

        // move cells from grid into list
        for (ArrayList<Cell> al : grid) {
            for (Cell c : al) {
                this.board = new Cons<Cell>(c, this.board);
            }
        }
    }

    // generate terrain recursively
    void genTerrain(int x, int y, int endx, int endy,
            ArrayList<ArrayList<Double>> heights) {

        int midx = (x + endx) / 2;
        int midy = (y + endy) / 2;
        int area = (endx - x) * (endy - y);

        // calculate 5 values
        heights.get(midx).set(
                y,
                this.getHeightLine(heights.get(x).get(y), heights.get(endx)
                        .get(y), area));
        heights.get(midx).set(
                endy,
                this.getHeightLine(heights.get(x).get(endy), heights.get(endx)
                        .get(endy), area));
        heights.get(x).set(
                midy,
                this.getHeightLine(heights.get(x).get(y),
                        heights.get(x).get(endy), area));
        heights.get(endx).set(
                midy,
                this.getHeightLine(heights.get(endx).get(y), heights.get(endx)
                        .get(endy), area));
        heights.get(midx).set(
                midy,
                this.getHeightRect(heights.get(x).get(y),
                        heights.get(x).get(endy), heights.get(endx).get(y),
                        heights.get(endx).get(endy), area));

        if (endx - x < 3 && endy - y < 3) {
            return;
        }

        // 4 recursive calls
        // upper left
        this.genTerrain(x, y, midx, midy, heights);
        // upper right
        this.genTerrain(midx, y, endx, midy, heights);
        // lower right
        this.genTerrain(midx, midy, endx, endy, heights);
        // lower left
        this.genTerrain(x, midy, midx, endy, heights);

    }

    double getHeightLine(double a, double b, int area) {
        return Math.min(ForbiddenIslandWorld.ISLAND_SIZE / 2,
                (Math.random() - ForbiddenIslandWorld.OCEAN_FACTOR)
                        * ForbiddenIslandWorld.NUDGE * Math.sqrt(area)
                        + ((a + b) / 2.0));
    }

    double getHeightRect(double a, double b, double c, double d, int area) {
        return Math.min(ForbiddenIslandWorld.ISLAND_SIZE / 2,
                (Math.random() - ForbiddenIslandWorld.OCEAN_FACTOR)
                        * ForbiddenIslandWorld.NUDGE * Math.sqrt(area)
                        + ((a + b + c + d) / 4.0));
    }

    // initialize a mountain world
    void initMountain() {

        // calculate maximum height of the world
        int maxHeight = ForbiddenIslandWorld.ISLAND_SIZE / 2;

        // initialize 2d arraylist of heights
        ArrayList<ArrayList<Double>> heights = new ArrayList<ArrayList<Double>>();

        // iterate through, calculating height using manhattan distance
        for (int i = 0; i < ForbiddenIslandWorld.ISLAND_SIZE + 2; i++) {
            heights.add(new ArrayList<Double>());
            for (int j = 0; j < ForbiddenIslandWorld.ISLAND_SIZE + 2; j++) {
                heights.get(i).add(
                        new Double(maxHeight - this.manhattanDist(i, j)));
            }
        }

        // create grid from the heights
        ArrayList<ArrayList<Cell>> grid = this.createGrid(heights);

        // initialize new list of cells
        this.board = new Empty<Cell>();

        // move cells from grid into list
        for (ArrayList<Cell> al : grid) {
            for (Cell c : al) {
                this.board = new Cons<Cell>(c, this.board);
            }
        }
    }

    // initialize diamond world of random heights
    void initRandom() {

        // initialize random number generator
        Random rand = new Random();

        // calculate max height
        int maxHeight = ForbiddenIslandWorld.ISLAND_SIZE / 2;

        // initialize 2d arraylist of random heights
        ArrayList<ArrayList<Double>> heights = new ArrayList<ArrayList<Double>>();
        // initialize heights for the world
        for (int i = 0; i < ForbiddenIslandWorld.ISLAND_SIZE + 2; i++) {
            heights.add(new ArrayList<Double>());
            for (int j = 0; j < ForbiddenIslandWorld.ISLAND_SIZE + 2; j++) {
                // if cell is on the island, give it a random height
                if (this.manhattanDist(j, i) < maxHeight) {
                    heights.get(i).add(new Double(rand.nextInt(maxHeight) + 1));
                }

                // otherwise give it it's calculated height (as ocean)
                else {
                    heights.get(i).add(
                            new Double(maxHeight - this.manhattanDist(i, j)));
                }
            }
        }

        // create grid from list of list of heights
        ArrayList<ArrayList<Cell>> grid = this.createGrid(heights);

        // initialize list of cells
        this.board = new Empty<Cell>();

        // move cells from grid to board
        for (ArrayList<Cell> al : grid) {
            for (Cell c : al) {
                this.board = new Cons<Cell>(c, this.board);
            }
        }

    }

    // calculate manhattan distance of a cell at x, y
    int manhattanDist(int x, int y) {
        int mid = ForbiddenIslandWorld.ISLAND_SIZE / 2;
        return Math.abs(x - mid) + Math.abs(y - mid);
    }

    // create grid of cells from grid of heights
    ArrayList<ArrayList<Cell>> createGrid(ArrayList<ArrayList<Double>> heights) {

        // initialize the grid
        ArrayList<ArrayList<Cell>> grid = new ArrayList<ArrayList<Cell>>();

        // loop through all heights, adding a new cell for each
        for (int i = 0; i < ForbiddenIslandWorld.ISLAND_SIZE + 2; i++) {
            grid.add(new ArrayList<Cell>());
            for (int j = 0; j < ForbiddenIslandWorld.ISLAND_SIZE + 2; j++) {
                double height = heights.get(i).get(j);

                // if the calculated height is less than or equal to 0, make
                // cell an oceancell
                if (height <= 0) {
                    grid.get(i).add(new OceanCell(height, i, j));
                }

                // otherwise make it a cell
                else {
                    grid.get(i).add(new Cell(height, i, j));
                }
            }
        }

        // nested for loops to link cells to their neighbors
        for (int i = 0; i < ForbiddenIslandWorld.ISLAND_SIZE + 2; i++) {
            for (int j = 0; j < ForbiddenIslandWorld.ISLAND_SIZE + 2; j++) {

                // get current cell
                Cell current = grid.get(i).get(j);

                // set top and bottom
                // if cell is on top, set its top to itself, and its bottom to
                // the cell below it
                if (current.getY() <= 0) {
                    current.setTop(current);
                    current.setBottom(grid.get(i).get(j + 1));
                }
                else {

                    // if cell is on bottoms, set its bottom to itself, and its
                    // top to the cell above it
                    if (current.getY() >= ForbiddenIslandWorld.ISLAND_SIZE) {
                        current.setTop(grid.get(i).get(j - 1));
                        current.setBottom(current);
                    }

                    // otherwise set top and bottom normally
                    else {
                        current.setTop(grid.get(i).get(j - 1));
                        current.setBottom(grid.get(i).get(j + 1));
                    }
                }

                // set left right
                // if cell is on left, set its left to itself, and its right to
                // the cell to the right of it
                if (current.getX() <= 0) {
                    current.setLeft(current);
                    current.setRight(grid.get(i + 1).get(j));
                }
                else {

                    // if cell is on right, set its right to itself, and its
                    // left to the cell to the left of it
                    if (current.getX() >= ForbiddenIslandWorld.ISLAND_SIZE) {
                        current.setLeft(grid.get(i - 1).get(j));
                        current.setRight(current);
                    }

                    // otherwise set left and right normally
                    else {
                        current.setLeft(grid.get(i - 1).get(j));
                        current.setRight(grid.get(i + 1).get(j));
                    }
                }
            }
        }

        // initialize player to random location
        this.player = new Player(this.chooseRandomLand(grid));

        // initialize targets to random locations
        this.targets = new Empty<Target>();
        for (int i = 0; i < ForbiddenIslandWorld.NUM_TARGETS; i++) {
            this.targets = new Cons<Target>(new Target(
                    this.chooseRandomLand(grid)), this.targets);
        }

        // initialize helicopter to center
        this.heli = new HelicopterTarget(grid.get(
                ForbiddenIslandWorld.ISLAND_SIZE / 2).get(
                ForbiddenIslandWorld.ISLAND_SIZE / 2));

        // return the grid
        return grid;
    }

    // flood the board 1 step
    void floodBoard() {
        IList<Cell> coast = new Empty<Cell>();
        for (Cell c : this.board) {
            if (c.floodable(this.waterHeight)) {
                coast = new Cons<Cell>(c, coast);
            }
        }

        for (Cell c : coast) {
            c.setFlooded(true);
        }
    }

    // flood every 10 ticks
    public void onTick() {
        if (this.tick == 9) {
            this.waterHeight++;
            this.tick = 0;
            floodBoard();
        }
        else {
            this.tick++;
        }

    }

    // move the player
    public void onKeyEvent(String ke) {

        if (ke.equals("m")) {
            this.waterHeight = 0;
            this.initMountain();
        }
        else if (ke.equals("r")) {
            this.waterHeight = 0;
            this.initRandom();
        }
        else if (ke.equals("t")) {
            this.waterHeight = 0;
            this.initTerrain();
        }
        else if (ke.equals("left")
                && !this.player.getCell().getLeft().isFlooded()) {
            this.player.setCell(this.player.getCell().getLeft());
        }
        else if (ke.equals("right")
                && !this.player.getCell().getRight().isFlooded()) {
            this.player.setCell(this.player.getCell().getRight());
        }
        else if (ke.equals("up") && !this.player.getCell().getTop().isFlooded()) {
            this.player.setCell(this.player.getCell().getTop());
        }
        else if (ke.equals("down")
                && !this.player.getCell().getBottom().isFlooded()) {
            this.player.setCell(this.player.getCell().getBottom());
        }

        this.checkForCollisions();

    }

    public WorldEnd worldEnds() {
        boolean end = false;
        Posn center = new Posn((ForbiddenIslandWorld.ISLAND_SIZE / 2)
                * ForbiddenIslandWorld.SQUARE_SIZE,
                (ForbiddenIslandWorld.ISLAND_SIZE / 2)
                        * ForbiddenIslandWorld.SQUARE_SIZE);
        String endText = "N/A";

        if (this.player.getCell().isFlooded()) {
            end = true;
            endText = "RIP";
        }

        boolean allCollected = true;
        for (Target t : this.targets) {
            allCollected = allCollected && t.isCollected();
        }

        if (allCollected && this.player.getCell().equalCells(this.heli.getCell())) {
            end = true;
            endText = "You Win!";
        }

        WorldImage endImage = new TextImage(center, endText, 200, 1, new Red());

        return new WorldEnd(end, endImage);
    }

    public Cell chooseRandomLand(ArrayList<ArrayList<Cell>> grid) {
        boolean chosenFlooded = true;
        Cell chosenCell = grid.get(0).get(0);
        while (chosenFlooded) {
            Random r = new Random();
            chosenCell = grid.get(
                    r.nextInt(ForbiddenIslandWorld.ISLAND_SIZE + 1)).get(
                    r.nextInt(ForbiddenIslandWorld.ISLAND_SIZE + 1));
            if (!chosenCell.isFlooded()) {
                chosenFlooded = false;
            }
        }
        return chosenCell;
    }

    // check for collisions
    void checkForCollisions() {
        boolean allCollected = true;
        for (Target t : this.targets) {
            if (t.getCell().equalCells(this.player.getCell())) {
                t.collected();
            }
            allCollected = allCollected && t.isCollected();
        }

    }
}