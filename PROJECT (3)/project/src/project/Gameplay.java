/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

/**
 *
 * @author Viet Anh

 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JLabel;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks;
    private final Timer timer;
    private int delay = 1;
    private int col;
    private int row;
    private Ball ball;
    private Paddle paddle;
    private MapGenerator map;
    private boolean isTripleBallActive = false;
    private int tripleBallDuration = 2000;
    private List<Ball> balls = new ArrayList<>(); // Danh sách chứa các quả bóng
    private List<PowerUp> powerUps = new ArrayList<>(); // Danh sách chứa các vật phẩm
    private boolean gameWon = false;

    public Gameplay() {
        ball = new Ball(350, 350, -1, -2);
        paddle = new Paddle(310);
        balls.add(ball);
        int[] size = randomRowsAndCols();
        row = size[0];
        col = size[1];

        map = new MapGenerator(row, col);
        for(int i=0;i<map.map.length;i++){
            for(int j=0;j<map.map[0].length;j++){
                if(map.map[i][j]>=1) totalBricks++; 
            }
        }
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }
    public int[] randomRowsAndCols() {
    Random rand = new Random();

    // Random số hàng trong khoảng từ 3 đến 10
    int row = rand.nextInt(8) + 3;

    // Random số cột sao cho row * col >= 20
    int col = rand.nextInt(200 / row) + 1;

    // Kiểm tra để đảm bảo row * col >= 20
    while (row * col < 20 || 540 / col < 150 / row) {
        row = rand.nextInt(8) + 3;  // Random lại số hàng
        col = rand.nextInt(200 / row) + 1;  // Random lại số cột sao cho điều kiện đúng
    }

    
    return new int[]{row, col};  // Trả về số hàng và cột đã random
}

    
    public void paint(Graphics g) {
    	// nền
    	g.setColor(Color.black);
    	g.fillRect(1, 1, 692, 592);

    	// vẽ map
    	map.draw((Graphics2D) g);

    	// điểm số
    	g.setColor(Color.white);
    	g.setFont(new Font("serif", Font.BOLD, 25));
    	g.drawString("" + score, 590, 30);

    	// biên giới
    	g.setColor(Color.yellow);
    	g.fillRect(0, 0, 3, 592);
    	g.fillRect(0, 0, 692, 3);
    	g.fillRect(692, 0, 3, 592);

    	// vẽ paddle và ball
    	paddle.draw(g);
        for (Ball ball : balls) {
            ball.draw(g);
        }
    	for (PowerUp powerUp : powerUps) {
            if (powerUp.isActive()) {
                powerUp.fall(); // Làm vật phẩm rơi xuống
                powerUp.draw(g);
            }
        }
        
        // Kiểm tra va chạm giữa paddle và vật phẩm
        checkPaddlePowerUpCollision();
        // Kiểm tra nếu tất cả bóng đều rơi xuống dưới đáy màn hình
        boolean allBallsFallen = true;
        for (Ball ball : balls) {
            if (ball.getY() <= 570) {
                allBallsFallen = false;
                break;
            }
        }
        if (gameWon) {
            showWin(g);
            return;
        }
        if (allBallsFallen) {
            showGameOver(g);
        } 
        if (totalBricks <= 0 && play) {
            showWin(g);
        }
    }
    private void checkPaddlePowerUpCollision() {
        Rectangle paddleRect = new Rectangle(paddle.getX(), paddle.getYPosition(), paddle.getWidth(), 8);

        for (PowerUp powerUp : powerUps) {
            if (powerUp.isActive()) {
                Rectangle powerUpRect = new Rectangle(powerUp.getX(), powerUp.getY(), PowerUp.WIDTH, PowerUp.HEIGHT);
                if (paddleRect.intersects(powerUpRect)) {
                    applyPowerUp(powerUp); // Kích hoạt hiệu ứng vật phẩm
                    powerUp.deactivate(); // Tắt vật phẩm sau khi ăn
                }
            }
        }
    }
    private void showGameOver(Graphics g) {
        play = false;
        for (Ball ball : balls) {
            ball.setxDir(0);
            ball.setyDir(0);
        }
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 30));
        g.drawString("Game Over, Scores: " + score, 190, 300);

        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Press Enter to Restart", 230, 350);
    }
    
    private void showWin(Graphics g) {
        play = false;
        gameWon = true; // Đặt gameWon là true khi thắng
        for (Ball ball : balls) {
            ball.setxDir(0);
            ball.setyDir(0);
        }
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 30));
        g.drawString("You Won, Scores: " + score, 190, 300);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Press Enter to Restart ", 230, 350);
    }
    private boolean shouldDropPowerUp() {
        Random rand = new Random();

        
        return rand.nextDouble() < 0.99;
    }

    private PowerUp createRandomPowerUp(int x, int y) {
        Random rand = new Random();
       int type = rand.nextInt(4); // 4 loại vật phẩm: 0, 1, 2, 3
        return new PowerUp(x, y, type);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            boolean allBallsFallen = true;
            List<Ball> ballsToRemove = new ArrayList<>(); // Danh sách lưu các bóng cần xóa

            for (Ball ball : balls) {
                ball.move();
                // Kiểm tra nếu bóng rơi xuống dưới đáy màn hình
                if (ball.getY() > 570) {
                    if (balls.size() > 1) {
                        ballsToRemove.add(ball); // Đánh dấu bóng để xóa sau
                    }
                    continue; // Bỏ qua bóng đã rơi xuống
                } else {
                    allBallsFallen = false; // Nếu còn bóng trên màn hình, game vẫn tiếp tục
                }

                // Kiểm tra va chạm với paddle
                if (new Rectangle(ball.getX(), ball.getY(), ball.getSize(), ball.getSize())
                    .intersects(new Rectangle(paddle.getX(), paddle.getYPosition(), paddle.getWidth(), 8))) {
                    ball.reverseYDir();
                }

                // Kiểm tra va chạm với gạch
                for (int i = 0; i < map.map.length; i++) {
                    for (int j = 0; j < map.map[0].length; j++) {
                        if (map.map[i][j] > 0) {
                            int brickX = j * map.brickWidth + 80;
                            int brickY = i * map.brickHeight + 50;
                            int brickWidth = map.brickWidth;
                            int brickHeight = map.brickHeight;

                            Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                            Rectangle ballRect = new Rectangle(ball.getX(), ball.getY(), ball.getSize(), ball.getSize());

                            if (ballRect.intersects(brickRect)) {
                                  
                                if (map.isSpecialBrick(i, j)) {
                                    if (ball.isExplosive()) {
                                            explodeBrick(i, j, ball);  // Gọi phương thức để xử lý bóng nổ
                                        } else{
                                            map.incrementHitCount(i, j);
                                            if (map.getHitCount(i, j) >= 2) {
                                                map.setBrickValue(0, i, j);
                                                totalBricks--;
                                                score += 10;
                                            }
                                            // Tạo vật phẩm cho gạch đặc biệt
                                        if (shouldDropPowerUp()) {
                                             PowerUp powerUp = createRandomPowerUp(brickX + brickWidth / 2, brickY);
                                             powerUps.add(powerUp);
                                            }
                                        }
                                    } else {
                                        map.setBrickValue(0, i, j);
                                        totalBricks--;
                                        score += 10;

                                        if (ball.isExplosive()) {
                                            explodeBrick(i, j, ball);  // Gọi phương thức để xử lý bóng nổ
                                        }

                                        // Tạo vật phẩm sau khi phá gạch
                                        if (shouldDropPowerUp()) {
                                            PowerUp powerUp = createRandomPowerUp(brickX + brickWidth / 2, brickY);
                                            powerUps.add(powerUp);
                                        }
                                    }
                                

                                // Phản xạ bóng sau khi va chạm
                                if (ball.getX() + 19 <= brickRect.x || ball.getX() + 1 >= brickRect.x + brickRect.width) {
                                    ball.reverseXDir();
                                } else {
                                    ball.reverseYDir();
                                }
                                break; // Dừng vòng lặp sau khi va chạm để không xử lý gạch khác
                            }
                        }
                    }
                }

                // Va chạm với tường
                if (ball.getX() < 0 || ball.getX() > 670) ball.reverseXDir();
                if (ball.getY() < 0) ball.reverseYDir();
            }
            // Xóa các bóng đã rơi xuống sau khi hoàn thành vòng lặp
            balls.removeAll(ballsToRemove);

            // Kiểm tra nếu chỉ còn một bóng, khôi phục khả năng xuất hiện vật phẩm x3 bóng
            if (balls.size() == 1) {
                isTripleBallActive = false;
            }

            // Kiểm tra nếu tất cả bóng đều đã rơi xuống
            if (allBallsFallen) {
                play = false;
            }
        }
        repaint();
    }


    // Phương thức phá gạch của bóng nổ
    private void explodeBrick(int row, int col, Ball explosiveBall) {
        if (map.map[row][col] > 0) {  // Chỉ phá nếu gạch tồn tại
            map.setBrickValue(0, row, col);
            totalBricks--;
            score += 10;
        }

        // Phá các gạch xung quanh trong phạm vi 1 ô
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;

                // Kiểm tra vị trí hợp lệ và phá gạch xung quanh
                if (newRow >= 0 && newRow < map.map.length && newCol >= 0 && newCol < map.map[0].length) {
                    if (map.map[newRow][newCol] > 0) {
                            map.setBrickValue(0, newRow, newCol);
                            totalBricks--;
                            score += 10;
                        }
                    }
                }
            }
        // Sau khi bóng nổ, chuyển nó về trạng thái thường
        explosiveBall.setExplosive(false);
        explosiveBall.resetColor();
    }

    private PowerUpManager powerUpManager = new PowerUpManager();

    private void applyPowerUp(PowerUp powerUp) {
        switch (powerUp.getType()) {
            case 0: // Tăng kích thước paddle
                powerUpManager.activatePowerUp(
                    0,
                    () -> paddle.setWidth(150),    // Áp dụng hiệu ứng tăng paddle
                    () -> paddle.setWidth(100)    // Reset về trạng thái ban đầu
                );
                break;

            case 1: // Giảm kích thước paddle
                powerUpManager.activatePowerUp(
                    1,
                    () -> paddle.setWidth(50),    // Áp dụng hiệu ứng giảm paddle
                    () -> paddle.setWidth(100)    // Reset về trạng thái ban đầu
                );
                break;

            case 2: // x3 bóng
                isTripleBallActive = true;
                if (balls.size() == 1) {
                    Ball ball2 = new Ball(ball.getX(), ball.getY(), -ball.getxDir(), ball.getyDir());
                    Ball ball3 = new Ball(ball.getX(), ball.getY(), ball.getxDir(), -ball.getyDir());

                    if (ball.isExplosive()) {
                        ball2.setExplosive(true);
                        ball3.setExplosive(true);
                        ball2.setColor(Color.red);
                        ball3.setColor(Color.red);
                    }

                    balls.add(ball2);
                    balls.add(ball3);
                }
                break;

            case 3: // Bóng nổ
                for (Ball b : balls) {
                    b.setExplosive(true);
                    b.setColor(Color.red);
                }
                break;
        }
    }
    private class PowerUpManager {
    private Timer activeTimer;
    private int activePowerUpType = -1;

    public void activatePowerUp(int type, Runnable applyEffect, Runnable resetEffect) {
        if (activeTimer != null) {
            activeTimer.stop(); // Dừng bộ đếm thời gian hiện tại nếu có
        }

        // Xử lý logic theo trạng thái hiện tại
        if (activePowerUpType == type) {
            // Nếu cùng loại power-up, chỉ cần reset thời gian
            applyEffect.run();
        } else {
            // Nếu khác loại, reset trạng thái ban đầu và áp dụng hiệu ứng mới
            resetEffect.run();
            applyEffect.run();
        }

        activePowerUpType = type;

        // Khởi động lại bộ đếm thời gian 12 giây
        activeTimer = new Timer(12000, e -> {
            resetEffect.run(); // Trả về trạng thái ban đầu
            activePowerUpType = -1; // Xóa trạng thái hiện tại
        });
        activeTimer.setRepeats(false);
        activeTimer.start();
    }
}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddle.moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paddle.moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // Khởi động lại trò chơi khi nhấn Enter
            if (!play || gameWon) {
                play = true;
                gameWon = false; // Đặt lại trạng thái chiến thắng
                score = 0;

                // Tạo lại các đối tượng
                ball = new Ball(350, 350, -1, -2);
                paddle = new Paddle(310);
                balls.clear();
                balls.add(ball);

                int[] size = randomRowsAndCols();
                row = size[0];
                col = size[1];
                map = new MapGenerator(row, col);

                // Đặt lại số lượng gạch
                totalBricks = 0;
                for (int i = 0; i < map.map.length; i++) {
                    for (int j = 0; j < map.map[0].length; j++) {
                        if (map.map[i][j] >= 1) {
                            totalBricks++;
                        }
                    }
                }             
                repaint();
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
