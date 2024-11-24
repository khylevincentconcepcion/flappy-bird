import java.awt.Image;

public class Bird {
  int x = 360 / 8;
  int y = 640 / 2;
  int width = 24;
  int height = 24;
  Image image;

  Bird(Image image) {
    this.image = image;
  }
}
