/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

/**
 *
 * @author Quang viet
 */
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.BasicStroke;
import java.util.Random;


public class MapGenerator {
	public int map[][];
	public int brickWidth;
	public int brickHeight;
	private Color[][] brickColors;
	private Random rand;
	private boolean[][] specialBricks; // Xác định gạch đặc biệt
	private int[][] hitCount; // Lưu trữ số lần va chạm với gạch đặc biệt
	private Image brickImage; // lưu hình ảnh viên gạch
	public MapGenerator(int row, int col) {
		map = new int[row][col];
		specialBricks = new boolean[row][col]; // Khởi tạo mảng đánh dấu gạch đặc biệt
		rand = new Random();
		brickColors = new Color[row][col];
		hitCount = new int[row][col]; // Khởi tạo mảng đếm va chạm
                try {
            brickImage = ImageIO.read(new File("gachdacbiet.jpeg")); // Đảm bảo bạn có file "brick.png" trong thư mục dự án
        } catch (IOException e) {
            System.err.println("Không thể tải ảnh viên gạch.");
            e.printStackTrace();
        }
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				map[i][j] = rand.nextInt(5);// random số để có nhiều gạch;
				if (map[i][j] >= 1) brickColors[i][j] = getRandomColor();
            
				// Xác suất 20% là gạch đặc biệt
				if (rand.nextDouble() < 0.2) {
					specialBricks[i][j] = true;
					hitCount[i][j] = 0; // Đặt số lần va chạm ban đầu là 0
				}
			}
		}
			brickWidth = 540 / col; //540
			brickHeight = 150 / row; //150
	}

	public boolean isSpecialBrick(int row, int col) {
		return specialBricks[row][col];
	}
	
	private Color getRandomColor() {
        Random rand = new Random();
        Color randomColor;
        do {
            randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        } while (randomColor.equals(Color.WHITE)); // Lặp lại nếu màu là trắng
        return randomColor;
    }
	
	public void incrementHitCount(int row, int col) {
        if (specialBricks[row][col]) {
            hitCount[row][col]++;
        }
    }

    public int getHitCount(int row, int col) {
        return hitCount[row][col];
    }
    
	public void draw(Graphics2D g) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] > 0) {
					g.setColor(brickColors[i][j]);
					g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
					g.setColor(Color.white);
					g.setStroke(new BasicStroke(1));
					g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                                        // Nếu gạch là đặc biệt, vẽ hình ảnh gạch phủ lên
                                        if (specialBricks[i][j]) {
                                            // Tính toán tọa độ để căn giữa ảnh gạch trên viên gạch
                                            int x = j * brickWidth + 80; 
                                            int y = i * brickHeight + 50;
                                            g.drawImage(brickImage, x, y, brickWidth, brickHeight, null); // Vẽ hình ảnh viên gạch lên gạch đặc biệt
                                        }
				}
			}
		}
	}
        
	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value;
	}
}
