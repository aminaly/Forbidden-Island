import javalib.colors.Red;
import javalib.colors.White;
import javalib.worldimages.DiskImage;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

public class Player {

    // indicates the players position
    int x;
    int y;

    // current cell player is on
    Cell cell;

    Player(Cell cell) {
        this.setCell(cell);
    }

    // Getters and setters for the player
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

    public void setCell(Cell cell) {
        this.cell = cell;
        this.x = cell.getX();
        this.y = cell.getY();
    }

    public WorldImage render() {
        Posn center = new Posn(this.x * ForbiddenIslandWorld.SQUARE_SIZE
                + (ForbiddenIslandWorld.SQUARE_SIZE / 2), this.y
                * ForbiddenIslandWorld.SQUARE_SIZE
                + (ForbiddenIslandWorld.SQUARE_SIZE / 2));
        return new OverlayImages(new DiskImage(center,
                ForbiddenIslandWorld.SQUARE_SIZE / 2, new White()),
                new DiskImage(center, ForbiddenIslandWorld.SQUARE_SIZE / 3,
                        new Red()));
    }

}
