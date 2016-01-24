import javalib.colors.Black;
import javalib.colors.White;
import javalib.worldimages.DiskImage;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

public class Target {

    // indicates the targets position
    int x;
    int y;

    // current cell target is on
    Cell cell;
    boolean collected = false;

    // constructor
    Target(Cell cell) {
        this.setCell(cell);
    }

    // Getters and setters for the target
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Cell getCell() {
        return cell;
    }

    public void collected() {
        this.collected = true;
    }

    public boolean isCollected() {
        return this.collected;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
        this.x = cell.getX();
        this.y = cell.getY();
    }

    public WorldImage render() {
        if (!this.collected) {
            Posn center = new Posn(this.x * ForbiddenIslandWorld.SQUARE_SIZE
                    + (ForbiddenIslandWorld.SQUARE_SIZE / 2), this.y
                    * ForbiddenIslandWorld.SQUARE_SIZE
                    + (ForbiddenIslandWorld.SQUARE_SIZE / 2));
            return new OverlayImages(new DiskImage(center,
                    ForbiddenIslandWorld.SQUARE_SIZE / 2, new White()),
                    new DiskImage(center, ForbiddenIslandWorld.SQUARE_SIZE / 3,
                            new Black()));
        }
        return new RectangleImage(new Posn(0, 0), 0, 0, new White());
    }
}
