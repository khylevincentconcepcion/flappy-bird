import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
  int boardWidth = 360;
  int boardHeight = 640;

  Image backgroundImage;
  Image birdImage;
  Image topPipeImage;
  Image bottomPipeImage;

  // bird
  Bird bird = new Bird(birdImage);

  // pipes
  Pipe topPipe = new Pipe(topPipeImage);

  // game logic
  int velocityY = 0;
  int velocityX = -4; // pipe speed
  int gravity = 1;

  ArrayList<Pipe> pipes;
  Random random = new Random();

  Timer gameTimer;
  Timer placePipesTimer;

  boolean gameOver = false;
  double score = 0;

  FlappyBird() {
    setPreferredSize(new Dimension(boardWidth, boardHeight));
    setFocusable(true);
    addKeyListener(this);

    backgroundImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
    birdImage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
    topPipeImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
    bottomPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

    // bird
    bird = new Bird(birdImage);
    pipes = new ArrayList<Pipe>();

    // pipes timer
    placePipesTimer = new Timer(1500, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        placePipes();
      }
    });
    placePipesTimer.start();

    // timer
    gameTimer = new Timer((1000 / 60), this);
    gameTimer.start();
  }

  public void placePipes() {
    int randomPipeY = (int) (topPipe.y - (topPipe.height / 4) - Math.random() * (topPipe.height / 2));
    int openingSpace = boardHeight / 4;

    Pipe topPipe = new Pipe(topPipeImage);
    topPipe.y = randomPipeY;
    pipes.add(topPipe);

    Pipe bottomPipe = new Pipe(bottomPipeImage);
    bottomPipe.y = topPipe.y + topPipe.height + openingSpace;
    pipes.add(bottomPipe);
  }

  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    draw(graphics);
  }

  public void draw(Graphics graphics) {
    graphics.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);
    graphics.drawImage(bird.image, bird.x, bird.y, bird.width, bird.height, null);

    for (int i = 0; i < pipes.size(); i++) {
      Pipe pipe = pipes.get(i);
      graphics.drawImage(pipe.image, pipe.x, pipe.y, pipe.width, pipe.height, null);
    }

    graphics.setColor(Color.white);
    graphics.setFont(new Font("Arial", Font.PLAIN, 32));

    if (gameOver) {
      graphics.drawString("Game Over : " + String.valueOf((int) score), 10, 35);
    } else {
      graphics.drawString(String.valueOf((int) score), 10, 35);
    }
  }

  public void move() {
    // bird
    velocityY += gravity;
    bird.y += velocityY;
    bird.y = Math.max(bird.y, 0);

    for (int i = 0; i < pipes.size(); i++) {
      Pipe pipe = pipes.get(i);
      pipe.x += velocityX;

      if (!pipe.passed && bird.x > pipe.x + pipe.width) {
        pipe.passed = true;
        score += 0.5;
      }

      if (collision(bird, pipe)) {
        gameOver = true;
      }
    }

    if (bird.y > boardHeight) {
      gameOver = true;
    }
  }

  public boolean collision(Bird bird, Pipe pipe) {
    return bird.x < pipe.x + pipe.width &&
        bird.x + bird.width > pipe.x &&
        bird.y < pipe.y + pipe.height &&
        bird.y + bird.height > pipe.y;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    move();
    repaint();

    if (gameOver) {
      placePipesTimer.stop();
      gameTimer.stop();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      velocityY = -9;
      if (gameOver) {
        bird.y = 640 / 2;
        velocityY = 0;
        pipes.clear();
        score = 0;
        gameOver = false;
        gameTimer.start();
        placePipesTimer.start();
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}
