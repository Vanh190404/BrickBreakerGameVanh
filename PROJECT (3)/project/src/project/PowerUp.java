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
import java.awt.Graphics;

public class PowerUp {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;

    private int x, y, width, height;
    private int speed = 3;
    private int type; 
    private boolean isActive;

    public PowerUp(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.width = WIDTH;
        this.height = HEIGHT;
        this.type = type;
        this.isActive = true;
    }

    public void fall() {
        y += speed;
        if (y > 600) {
            isActive = false;
        }
    }

    public void draw(Graphics g) {
        if (!isActive) return;
        switch (type) {
            case 0 -> g.setColor(Color.green);//0: tăng paddle
            case 1 -> g.setColor(Color.orange);//1: giảm paddle
            case 2 -> g.setColor(Color.blue);//2: x3 bóng
            case 3 -> g.setColor(Color.red);//3: bóng nổ
            default -> g.setColor(Color.white);
        }
        g.fillRect(x, y, width, height);
    }

    public int getType() {
        return type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
