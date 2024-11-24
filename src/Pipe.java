import java.awt.Image;

public class Pipe {
  int x = 360;
  int y = 0;
  int height = 512;
  int width = 64;
  Image image;
  boolean passed = false;

  Pipe(Image image) {
    this.image = image;
  }

}
