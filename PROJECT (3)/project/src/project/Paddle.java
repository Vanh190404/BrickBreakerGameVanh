/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

/**
 *
 * @author Quang viet
 */
import java.awt.Color;
import java.awt.Graphics;

public class Paddle {
    private int posX;
    private int width = 100;
    private final int height = 8;
    private final int yPosition = 550;

    public Paddle(int startX) {
        this.posX = startX;
    }

    public void moveLeft() {
        posX = Math.max(posX - 20, 10);  // giới hạn không cho vượt quá biên trái
    }

    public void moveRight() {
        posX = Math.min(posX + 20, 600); // giới hạn không cho vượt quá biên phải
    }

    public void draw(Graphics g) {
        g.setColor(Color.green);
        g.fillRect(posX, yPosition, width, height);
    }
    public void setWidth(int width) {
        // Giới hạn chiều rộng trong khoảng từ 50 đến 150
        this.width = Math.max(50, Math.min(width, 150));
    }
    // Getters
    public int getX() { return posX; }
    public int getWidth() { return width; }
    public int getYPosition() { return yPosition; }
}

