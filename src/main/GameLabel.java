/**
 * GameLabel.java
 *
 * The author disclaims copyright to this source code.  In place of
 * a legal notice, here is a blessing:
 *
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 *
 */
package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.*;

import tetromino.OTetromino;
import tetromino.Tetromino;
/**
 * @author 陈宇非<yufei.chen@pku.edu.cn>
 * @since 2016-3-24
 *
 */
public class GameLabel extends JLabel implements ActionListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * grid[0][0]在左下角，Y轴正方向向上
	 */
	Color[][] grid = null;
	Color[] colorList = {Color.RED, Color.BLUE, Color.GREEN, Color.orange};
	Tetromino currentTetromino = null;
	Timer animationTimer;
	Timer fallingTimer;
	public static final int width = 20; // X方向方格数
	public static final int height = 25; // Y方向方格数
	static final int blockSize = 25; //每个正方形方格的大小
	static final int downInterval = 300; // 方块下落间隔
	GameLabel() {
		grid = new Color[height][width]; // 以grid[y][x]的方式调用。y-竖直方向-高度，x-水平方向-宽度
		
		// 测试用
		for(int i=2; i<width; i++) {
			grid[0][i] = Color.BLUE;
			grid[1][i] = Color.BLUE;
		}
		
		// 设置大小
		setPreferredSize(new Dimension(width * blockSize, height * blockSize));
		setFocusable(true);
		requestFocus();
		currentTetromino = new OTetromino(this); // 初始化方块
		
		// 动画计时
		animationTimer = new Timer(10, this); // 约100 fps
		animationTimer.setRepeats(true);
		animationTimer.start();
		
		// 下落计时
		fallingTimer = new Timer(downInterval, this);
		fallingTimer.setRepeats(true);
		fallingTimer.start();
		
		addKeyListener(this);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		// 画下落的方块
		Coordinate[] b = currentTetromino.getAbsoluteBlock(); // 获取下落方块各块坐标
		g.setColor(currentTetromino.getColor()); // 获取下落方块颜色
		for(int i=0; i<4; i++)				
				g.fillRect(b[i].x * blockSize,
						// 背景坐标系Y轴方向和Java默认坐标系相反
						(height - b[i].y - 1) * blockSize,
						blockSize, blockSize);
		
		for(int y=0; y<height; y++)
			for(int x=0; x<width; x++) {
				if(grid[y][x] != null) {
					g.setColor(grid[y][x]);
					g.fillRect(x * blockSize,
							 // 背景坐标系Y轴方向和Java默认坐标系相反
							(height - y - 1) * blockSize, 
							blockSize, blockSize);
				}
			}
				
	}

	public void actionPerformed(ActionEvent e) {
		// 动画计时
		if(e.getSource() == animationTimer)
			repaint();
		// 下落计时
		else if (e.getSource() == fallingTimer) {
			if(!currentTetromino.moveDown()) {
				// 不能再下落，将方块固定到背景中
				Coordinate[] b = currentTetromino.getAbsoluteBlock();
				try {
					for(int i=0; i<4; i++)
						grid[b[i].y][b[i].x] = currentTetromino.getColor();
				} catch(ArrayIndexOutOfBoundsException ex) {
					System.out.println("你输了");
					fallingTimer.stop();
					animationTimer.stop();
					return;
				}
				scoring(); // 尝试消行
				currentTetromino = new OTetromino(this);
				fallingTimer.restart(); 
				// TODO：查阅相关标准，查看新方块的下落间隔时间和起始位置
			}
		}
	}

	/** 
	 * 按下按键的操作：
	 * 左右移动方块
	 * 加速方块下落
	 * 方块直接下落到底端
	 */
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT: // 方块向左移动
			currentTetromino.moveLeft();
			break;
		case KeyEvent.VK_RIGHT: // 方块向右移动
			currentTetromino.moveRight();
			break;
		case KeyEvent.VK_DOWN: // 加速方块下落
			currentTetromino.moveDown();
			break;
		case KeyEvent.VK_SPACE: // 方块直接下落到底端
			currentTetromino.moveBottom();
			break;
		}
	}
	
	/**
	 * 查询背景格子是否被占用
	 * @param x 格子x坐标
	 * @param y 格子y坐标
	 * @return 是否被占用
	 */
	public boolean isBlockOccupied(int x, int y) {
		if(grid[y][x] == null)
			return false;
		return true;
	}
	
	/** 
	 * 尝试进行消行
	 */
	void scoring() {
		// 找出下落方块各方块的y坐标，放入fullLine中
		HashMap<Integer, Boolean> fullLine = new HashMap<Integer, Boolean>();
		Coordinate[] b = currentTetromino.getAbsoluteBlock();
		for(int i=0; i<4; i++)
			if(!fullLine.containsKey(b[i].y))
				fullLine.put(b[i].y, true);
		
		// 检查各个y坐标，查看是否为满行。否则置false
		Set<Entry<Integer,Boolean>> s = fullLine.entrySet();
		for(Map.Entry<Integer, Boolean> i : s)
			for(int j=0; j<width; j++)
				if(grid[i.getKey()][j] == null) {
					i.setValue(false); // TODO: 看看能不能直接从map中删掉
					break;
				}
		
		// 将满行消掉得分
		// 先计算第n行方块将下落的高度shift[n]，再将该行向下移动
		int[] shift = new int[height]; // 背景中第y行下落行数
		for(Map.Entry<Integer, Boolean> i : fullLine.entrySet()) {
			if(i.getValue() == false) // 不是满行，跳过
				continue;
			System.out.println("得分");
			for(int j=i.getKey() + 1; j < height; shift[j]++, j++); // 满行上方对应的位移++
		}
		// 将每行向下移动
		for(int i=1; i < height; i++)
			for(int k=0; k<width; k++)
				grid[i-shift[i]][k] = grid[i][k];
	}
	
	/**
	 * 此函数不使用
	 */
	public void keyReleased(KeyEvent e) {}

	/**
	 * 此函数不使用
	 */
	public void keyTyped(KeyEvent e) {}
}
