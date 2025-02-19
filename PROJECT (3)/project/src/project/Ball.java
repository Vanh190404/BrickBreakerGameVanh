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

public class Ball {
    private int posX, posY;
    private int xDir, yDir;
    private final int size = 20;
    private boolean isExplosive = false; // Trạng thái bóng nổ
    private Color color = Color.white; // Màu mặc định là màu trắng

    public Ball(int startX, int startY, int startXDir, int startYDir) {
        this.posX = startX;
        this.posY = startY;
        this.xDir = startXDir;
        this.yDir = startYDir;
    }

    public void setExplosive(boolean explosive) {
        this.isExplosive = explosive;
    }

    public boolean isExplosive() {
        return isExplosive;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void resetColor() {
        this.color = Color.white; // Đặt lại màu trắng
    }

    public void move() {
        posX += xDir;
        posY += yDir;
    }

    public void draw(Graphics g) {
        g.setColor(color); // Vẽ bóng với màu hiện tại
        g.fillOval(posX, posY, size, size);
    }

    public void reverseXDir() {
        xDir = -xDir;
    }

    public void reverseYDir() {
        yDir = -yDir;
    }

	// Phương thức getter cho xDir và yDir
    public int getxDir() { return xDir; }
    public int getyDir() { return yDir; }

    public int getX() { return posX; }
    public int getY() { return posY; }
    public void setxDir(int x) { this.xDir = x; }
    public void setyDir(int y) { this.yDir = y; }
    public int getSize() { return size; }
}
