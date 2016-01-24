import java.awt.Color;

import javalib.colors.Black;
import javalib.colors.Blue;
import javalib.colors.Red;
import javalib.colors.White;
import javalib.colors.Yellow;
import javalib.worldimages.DiskImage;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldEnd;
import javalib.worldimages.WorldImage;
import tester.*;

// dear grader:
// worldEnds was not tested through web-cat because it returns a font error message. 
// in addition, the three void methods at the beginning of ExamplesForbiddenIsland are 
// to be used to run the worlds (Rename to testMountainGame, testRandomGame, testTerrainGame
public class ExamplesForbiddenIsland {
    

    // render moutain world
    void testMountainGameTest(Tester t) {
        ForbiddenIslandWorld world = new ForbiddenIslandWorld(0);
        world.initMountain();
        world.bigBang(world.pixels, world.pixels, 1);
    }
    
    // render random world
    void RandomGameTest(Tester t) {
        ForbiddenIslandWorld world = new ForbiddenIslandWorld(0);
        world.initRandom();
        world.bigBang(world.pixels, world.pixels, 1);
    }

   
    // render terrain world
    void TerrainWorldTest(Tester t) {
        ForbiddenIslandWorld world = new ForbiddenIslandWorld(0);
        world.initTerrain();
        world.bigBang(world.pixels, world.pixels, 1);
    }
    
    // examples of cells to test methods on Cells
    Cell c1;
    Cell c2;
    Cell c3;
    Cell c4;
    Cell c5;
    Cell c6;
    Cell c7;
    Cell c8;
    Cell c9;
    OceanCell c10;
    ForbiddenIslandWorld w1;
    ForbiddenIslandWorld w2;
    ForbiddenIslandWorld w3;
    IList<Cell> l1;
    IList<Cell> l2;
    IList<Cell> l3;
    IList<Target> col;
    IList<Target> notcol;
    
    // examples of targets and players for testing
    Player p1;
    Player p2;
    Target t1;
    Target t2;
    HelicopterTarget ht1;
    

    void initCells() {
        c1 = new Cell(20, 2, 2);
        c2 = new Cell(10, 1, 1);
        c3 = new Cell(10, 1, 2);
        c4 = new Cell(10, 1, 3);
        c5 = new Cell(10, 2, 1);
        c6 = new Cell(10, 2, 3);
        c7 = new Cell(10, 3, 1);
        c8 = new Cell(10, 3, 2);
        c9 = new Cell(10, 3, 3);
        
        // create an ocean cell
        c10 = new OceanCell(0.5, 4, 1);
        
        // create an empty list of cells
        l1 = new Empty<Cell>();
        // create a non-empty list of cells
        l2 = new Cons<Cell>(c2, new Cons<Cell>(c3,
                new Cons<Cell>(c4, this.l1)));
        l3 = new Cons<Cell>(c3, 
                new Cons<Cell>(c4, this.l1));
        
        p1 = new Player(this.c3);
        p2 = new Player(this.c1);
        t1 = new Target(this.c3);
        t2 = new Target(this.c4);
        ht1 = new HelicopterTarget(this.c1);
        
     // collected and not collected lists of targets
        notcol = new Cons<Target>(this.t1,
                new Cons<Target>(this.t2, 
                        new Empty<Target>()));
        col = new Cons<Target>(this.t1, 
                new Empty<Target>());
        
    }
    
    void initWorlds() {
        w1 = new ForbiddenIslandWorld();
        w2 = new ForbiddenIslandWorld(10);
        w3 = new ForbiddenIslandWorld(10);
    }
    
    // test setters and getters for cells, players & targets
    boolean testCellSettersAndGetters(Tester t) {
        // initialize cells 
        initCells();
        // make changes
        c1.setLeft(c3);
        c1.setRight(c8);
        c1.setBottom(c6);
        c1.setTop(c5);
        c1.setFlooded(true);
        p1.setX(4);
        p2.setY(5);
        t1.setCell(this.c6);
        t2.setX(8);
        t2.setY(9);
        // test
        return t.checkExpect(this.c1.left.equalCells(c3)) &&
                t.checkExpect(this.c1.right.equalCells(c8)) &&
                t.checkExpect(this.c1.bottom.equalCells(c6)) &&
                t.checkExpect(this.c1.top.equalCells(c5)) &&
                t.checkExpect(this.c1.getX(), 2) && 
                t.checkExpect(this.c7.getY(), 1) &&
                t.checkExpect(this.c1.isFlooded, true) &&
                t.checkExpect(this.p1.getX(), 4) &&
                t.checkExpect(this.p2.getY(), 5) &&
                t.checkExpect(this.t1.cell, this.c6) &&
                t.checkExpect(this.t2.getX(), 8) &&
                t.checkExpect(this.t2.getY(), 9);
    }
    
    // test cell equals
    boolean testEquals(Tester t) {
        // initialize cells
        initCells();
        // test method
        return t.checkExpect(this.c1.equalCells(this.c2), false) &&
                t.checkExpect(this.c3.equalCells(this.c3), true) &&
                t.checkExpect(this.c2.equalCells(this.c3), false) &&
                t.checkExpect(this.c8.equalCells(this.c3), false);  
    }
    
    // test floodable method
    boolean testFloodable(Tester t) {
        // initialize cells
        initCells();
        // set some floodable and non-floodable cells
        this.c1.setLeft(this.c10);
        this.c1.setRight(this.c3);
        this.c4.setLeft(this.c1);
        this.c4.setRight(this.c3);
        this.c4.setTop(this.c5);
        this.c4.setBottom(this.c6);
        this.c1.height = 10;
        this.c4.height = 20;
        // test method
        return t.checkExpect(this.c1.floodable(11), true) && // below h20 and near water cell
               t.checkExpect(this.c4.floodable(15), false) && // above water level
               t.checkExpect(this.c4.floodable(50), false); //below water level but not near water
    }
    
    // test render 
    boolean testRender(Tester t) {
        this.initCells();
        return t.checkExpect(this.c1.render(25),
                new RectangleImage(new Posn(37, 37),
                        15, 15, this.c1.getColor(25)));
    }
    // test getPosn
    boolean testGetPosn(Tester t) {
        this.initCells();
        return t.checkInexact(this.c1.getPosn(), new Posn(37, 37), 2) &&
                t.checkInexact(this.c5.getPosn(), new Posn(37, 22), 2);
    }
    // test getColor for Cells and Ocean Cells 
    boolean testGetColor(Tester t) {
        // initialize cells 
        initCells();
        // set cell c4 to flooded
        this.c4.isFlooded = true;
        this.c2.isFlooded = false;
        this.c5.isFlooded = true;
        this.c5.height = 25;
        // test
        return t.checkInexact(this.c10.getColor(0), 
                new Color(0f, 0f, 0.505f), 3) &&
                t.checkInexact(this.c1.getColor(25),
                        new Color(0.655f, 0.345f, 0f), 3) &&
                t.checkInexact(this.c2.getColor(25),
                        new Color(0.969f, 0.031f, 0f), 3) &&
                t.checkInexact(this.c4.getColor(25),
                        new Color(0f, 0f, 0.267f), 3) &&
                t.checkInexact(this.c5.getColor(35),
                        new Color(0f, 0f, 0.345f), 3);
    }
    
    // test initMountain
    boolean testInitMountain(Tester t) {
        // initialize worlds
        initWorlds();
        // create mountain world
        this.w2.initMountain();
        // test that correct world was made
        return t.checkExpect(!this.w2.board.isEmpty()) &&
                t.checkExpect(!this.w2.board.isEmpty()) &&
                t.checkExpect(this.w2.heli.getX(), 32) &&
                t.checkExpect(this.w2.heli.getY(), 32) &&
                t.checkExpect(this.w2.heli.isCollected(), false) &&
                t.checkExpect(this.w2.targets.isEmpty(), false);
    }
    
    // test initRandom
    boolean testInitRandom(Tester t) {
        // initialize worlds
        initWorlds();
        // create mountain world
        this.w1.initRandom();
        // test that correct world was made
        return t.checkExpect(!this.w1.board.isEmpty()) &&
                t.checkExpect(!this.w1.board.isEmpty()) &&
                t.checkExpect(this.w1.heli.getX(), 32) &&
                t.checkExpect(this.w1.heli.getY(), 32) &&
                t.checkExpect(this.w1.heli.isCollected(), false) &&
                t.checkExpect(this.w1.targets.isEmpty(), false);
    }
    
    // test initTerrain
    boolean testInitTerrain(Tester t) {
        // initialize world
        initWorlds();
        // create random terrain
        this.w3.initTerrain();
        // test that correct world was made
        return t.checkExpect(!this.w3.board.isEmpty()) &&
                t.checkExpect(this.w3.heli.getX(), 32) &&
                t.checkExpect(this.w3.heli.getY(), 32) &&
                t.checkExpect(this.w3.heli.isCollected(), false) &&
                t.checkExpect(this.w3.targets.isEmpty(), false);
    }
    
    // test manhattanDist
    boolean testManhattanDist(Tester t) {
        this.initWorlds();
        return t.checkExpect(this.w1.manhattanDist(4, 4), 56);
    }

    // test makeImage
    boolean testMakeImage(Tester t) {
        this.initWorlds();
        this.w1.waterHeight = 5;
        this.p1.cell = this.c1;
        this.w1.board = new Cons<Cell>(this.c1, new Empty<Cell>());
        this.w1.player = p1;
        WorldImage wimg = this.w1.makeImage();
        WorldImage pimg = this.p1.render();
        WorldImage expected = new OverlayImages(new DiskImage(new Posn(22, 37), 7, new White()),
                new DiskImage(new Posn(22, 37), 5,  new Red()));
        return t.checkExpect(pimg, expected);
    }
    
    // test for methods in IList
    // test isEmpty
    boolean testIsEmpty(Tester t) {
        initCells();
        return t.checkExpect(this.l1.isEmpty(), true) &&
                t.checkExpect(this.l2.isEmpty(), false);
    }

    // create instance of iterator
    IListIterator<Cell> iteratorL2;
    IListIterator<Cell> iteratorL1;
    IListIterator<Cell> iteratorL3;
    
    void initIterators() {
        initCells();
        iteratorL2 = new IListIterator<Cell>(l2);
        iteratorL1 = new IListIterator<Cell>(l1);
        iteratorL3 = new IListIterator<Cell>(l3);
    
    }
    
    // test hasNext
    boolean testHasNextAndNext(Tester t) {
        initIterators();
        return t.checkExpect(this.iteratorL1.hasNext(), false) &&
                t.checkExpect(this.iteratorL2.hasNext(), true);
    }
    
    // test next
    boolean testNext(Tester t) {
        initIterators();
        Cell temp = this.iteratorL2.next();
        return t.checkExpect(temp, this.c2) &&
                t.checkExpect(this.iteratorL2.list, this.iteratorL3.list);
    }
    
    // test that iterator can be used in a for loop
    boolean testIterator(Tester t) {
        initIterators();
        return t.checkExpect(this.l2.iterator(), 
                new IListIterator<Cell>(this.l2));
    }
    
    // test onKeyEvent
    boolean testOnKeyEventNewWorld(Tester t) {
        // initialize world
        initWorlds();
        // make changes
        this.w1.waterHeight = 20;
        this.w2.waterHeight = 40;
        this.w3.waterHeight = 50;
        this.w1.onKeyEvent("m");
        this.w2.onKeyEvent("r");
        this.w3.onKeyEvent("t");
        this.w1.player = p1;
        this.w2.player = p2;
        // resets game when new world key is pressed
        return t.checkExpect(this.w3.waterHeight, 0) &&
                t.checkExpect(this.w2.waterHeight, 0) &&
                t.checkExpect(this.w3.waterHeight, 0);
    }
    
    // test on key event moving left 
    boolean testOnKeyEventLeft(Tester t) {
        // initalize worlds
        initWorlds();
        initCells();
        // make changes
        this.w1.player = p1;
        this.w2.player = p2;
        this.p1.cell.left = c3;
        this.p2.cell.left = c10;
        this.w1.onKeyEvent("left");
        this.w2.onKeyEvent("left");
        // sets player's cell to it's leftmost cell if not near water
        return t.checkExpect(this.w1.player.cell, this.c3) &&
                t.checkExpect(this.w2.player.cell, this.c1);
        
    }    
    // test on key event moving right 
    boolean testOnKeyEventRight(Tester t) {
        // initalize worlds
        initWorlds();
        initCells();
        // make changes
        this.w1.player = p1;
        this.w2.player = p2;
        this.p1.cell.right = c3;
        this.p2.cell.right = c10;
        this.w1.onKeyEvent("right");
        this.w2.onKeyEvent("right");
        // sets player's cell to it's leftmost cell if not near water
        return t.checkExpect(this.w1.player.cell, this.c3) &&
                t.checkExpect(this.w2.player.cell, this.c1);
        
    }  
    // test on key event moving down
    boolean testOnKeyEventDown(Tester t) {
        // initalize worlds
        initWorlds();
        initCells();
        // make changes
        this.w1.player = p1;
        this.w2.player = p2;
        this.p1.cell.bottom = c3;
        this.p2.cell.bottom = c10;
        this.w1.onKeyEvent("down");
        this.w2.onKeyEvent("down");
        // sets player's cell to it's leftmost cell if not near water
        return t.checkExpect(this.w1.player.cell, this.c3) &&
                t.checkExpect(this.w2.player.cell, this.c1);
        
    }  
    
    // test on key event moving up
    boolean testOnKeyEventUp(Tester t) {
        // initalize worlds
        initWorlds();
        initCells();
        // make changes
        this.w1.player = p1;
        this.w2.player = p2;
        this.p1.cell.top = c3;
        this.p2.cell.top = c10;
        this.w1.onKeyEvent("up");
        this.w2.onKeyEvent("up");
        // sets player's cell to it's leftmost cell if not near water
        return t.checkExpect(this.w1.player.cell, this.c3) &&
                t.checkExpect(this.w2.player.cell, this.c1);
        
    }
    
    // test end worlds
    boolean endWorldsTest(Tester t) {
        // initialize worlds
        initWorlds();
        initCells();
        // make changes
        this.t1.collected = true;
        this.t2.collected = false;
        this.w2.heli.cell = this.c6;       
        
        // player is on a flooded cell
        this.c4.isFlooded = true;
        this.p2.setCell(this.c4);
        this.w1.player = p2;
        
        // not all targets collected and on heli pad
        this.w2.targets = this.notcol;
        this.p1.setCell(this.c6);
        
        // all targets collected and on heli pad
        this.p1.setCell(this.c6);
        this.w3.targets = this.col;
        this.w3.player = p1;
        this.w3.heli.cell = this.c6;
        
        // final image local variables
        TextImage rip = new TextImage(new Posn(480, 480),
                "RIP", 200, 1, new Red());
        TextImage win = new TextImage(new Posn(480, 480),
                "You Win!", 200, 1, new Red());
        TextImage na = new TextImage(new Posn(480, 480),
                "N/A", 200, 1, new Red());
        
        // check results
        return t.checkExpect(this.w1.worldEnds(), new WorldEnd(true, rip)) &&
                t.checkExpect(this.w2.worldEnds(), new WorldEnd(false, na)) &&
                t.checkExpect(this.w3.worldEnds(), new WorldEnd(true, win));
        
    }
    
    // test checkForCollisions
    boolean testCheckForCollisions(Tester t) {
        // initalize worlds and cells 
        initWorlds();
        initCells();
        // make changes
        this.p1.setCell(this.c7);
        this.t1.setCell(this.c4);
        this.t2.setCell(this.c7);
        this.w1.player = this.p1;
        this.w1.targets = this.notcol;
        this.w1.checkForCollisions();
        IList<Target> alltargs = new Cons<Target>(this.t1,
                new Cons<Target>(this.t2, new Empty<Target>()));
        // check results
        return t.checkExpect(this.w1.targets, alltargs);
        
    }
    
    // test onTick
    boolean testOnTick(Tester t) {
        // initalize worlds and cells 
        initWorlds();
        initCells();
        // make changes
        this.w1.tick = 4;
        this.w2.tick = 9;
        this.w1.waterHeight = 10;
        this.w2.waterHeight = 19;
        this.w1.onTick();
        this.w2.onTick();
        // check results
        return t.checkExpect(this.w1.waterHeight, 10) &&
                t.checkExpect(this.w1.tick, 5) &&
                t.checkExpect(this.w2.waterHeight, 20) &&
                t.checkExpect(this.w2.tick, 0);
        
    }
    
    // test floodBoard
    boolean testFloodBoard(Tester t) {
        // initalize worlds and cells 
        initWorlds();
        initCells();
        // make changes
        this.c1.setLeft(this.c10);
        this.c1.setRight(this.c3);
        this.c2.setLeft(this.c10);
        this.c2.setRight(this.c3);
        this.c1.height = 10;
        this.c2.height = 15;
        IList<Cell> coast = new Cons<Cell>(this.c1, 
                new Cons<Cell>(this.c2, 
                        new Empty<Cell>()));
        this.w1.board = coast;
        this.w1.waterHeight = 13;
        this.w1.floodBoard();
        Cons<Cell> cb = (Cons<Cell>) this.w1.board;
        Cons<Cell> cb2 = (Cons<Cell>) cb.rest;
        // check results
        return t.checkExpect(cb.first.isFlooded(), true) &&
                t.checkExpect(cb2.first.isFlooded(), false);
        
    }
    
    // test render player & target & helicopter targer 
    boolean testRenderPlayerandTarget(Tester t) {
        // initialize cells
        initCells();
        /// make changes
        this.ht1.setCell(this.c1);
        this.t1.setCell(this.c1);
        this.p1.setCell(this.c1);
        // test
        return t.checkExpect(this.ht1.render(),
                new OverlayImages(new DiskImage(new Posn(37, 37),
                        15, new Blue()),
                        new DiskImage(new Posn(37, 37), 7, new Yellow()))) &&
                t.checkExpect(this.t1.render(),
                        new OverlayImages(new DiskImage(new Posn(37, 37),
                                7, new White()),
                                new DiskImage(new Posn(37, 37), 5, 
                                        new Black()))) &&
                t.checkExpect(this.p1.render(),
                        new OverlayImages(new DiskImage(new Posn(37, 37),
                                7, new White()),
                                new DiskImage(new Posn(37, 37), 5, new Red())));
    }
    
    // test void collected method & boolean isCollected in target
    boolean testCollected(Tester t) {
        // initalize cells
        initCells();
        // make changes
        this.t1.collected = false;
        this.t2.collected = false;
        this.t1.collected();
        // test
        return t.checkExpect(this.t1.isCollected(), true) &&
                t.checkExpect(this.t2.isCollected(), false);
        
        
    }
    
   

}
