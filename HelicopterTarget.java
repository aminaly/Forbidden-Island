import javalib.colors.Blue;
import javalib.colors.Yellow;
import javalib.worldimages.DiskImage;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

public class HelicopterTarget extends Target {

    HelicopterTarget(Cell cell) {
        super(cell);
    }

    public WorldImage render() {
        Posn center = new Posn(this.x * ForbiddenIslandWorld.SQUARE_SIZE
                + (ForbiddenIslandWorld.SQUARE_SIZE / 2), this.y
                * ForbiddenIslandWorld.SQUARE_SIZE
                + (ForbiddenIslandWorld.SQUARE_SIZE / 2));
        return new OverlayImages(new DiskImage(center,
                ForbiddenIslandWorld.SQUARE_SIZE, new Blue()),
                new DiskImage(center, ForbiddenIslandWorld.SQUARE_SIZE / 2,
                        new Yellow()));
    
    }

}
