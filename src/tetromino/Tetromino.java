/**
 * Tetromino.java
 *
 * The author disclaims copyright to this source code.  In place of
 * a legal notice, here is a blessing:
 *
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 *
 */
package tetromino;

import java.awt.Color;
import main.Coordinate;
import main.GameLabel;

/**
 * @author 陈宇非<yufei.chen@pku.edu.cn>
 * @since 2016-3-24
 * 
 * 实现俄罗斯方块中下落的骨牌
 */
public abstract class Tetromino {
	Coordinate position; // 骨牌中心在[背景坐标系]中的位置
	GameLabel g;
	
	/**
	 * 初始化骨牌
	 * @param _g 对应的游戏面板
	 */
	Tetromino(GameLabel _g) {
		g = _g;
		position = new Coordinate(GameLabel.width / 2,
									GameLabel.height);
	}
	
	/**
	 * 获取骨牌颜色
	 * @return 颜色的Color对象
	 */
	abstract public Color getColor();
	
	/**
	 * 获取骨牌各方块在[自身坐标系]中的位置
	 * @return 骨牌各方块在[自身坐标系]中的坐标
	 */
	abstract Coordinate[] getBlock();
	
	/**
	 * 获取骨牌各方块在[背景坐标系]中的位置
	 * @return 骨牌各方块在[背景坐标系]中的坐标
	 */
	public Coordinate[] getAbsoluteBlock() {
		return getAbsoluteBlock(position); 
	}
	
	/**
	 * 尝试将骨牌左移
	 * @return 是否操作成功
	 */
	public boolean moveLeft() {
		 return move(Coordinate.LEFT);
	}
	
	/**
	 * 尝试将骨牌右移
	 * @return 是否操作成功
	 */
	public boolean moveRight() {
		 return move(Coordinate.RIGHT);
	}
	
	/**
	 * 尝试将骨牌下移
	 * @return 是否操作成功
	 */
	public boolean moveDown() {
		 return move(Coordinate.DOWN);
	}
	
	/**
	 * 尝试将骨牌置底
	 * @return 是否操作成功
	 */
	public void moveBottom() {	
		for(int i=GameLabel.height; i>0; i--) {
			Coordinate newPos = Coordinate.add(position,
					new Coordinate(0, -i));
			if(moveValid(newPos)) {
				position = newPos;
				return;
			}
		}
	}
	
	/**
	 * 指定一个新的骨牌中心，获取骨牌各方块在[背景坐标系]中的位置
	 * @param pos 新的骨牌中心坐标
	 * @return 骨牌各方块在[背景坐标系]中的坐标
	 */
	Coordinate[] getAbsoluteBlock(Coordinate pos) {
		Coordinate[] ret = new Coordinate[4];
		for(int i=0; i<4; i++)
			ret[i] = Coordinate.add(getBlock()[i], pos);
		return ret;
	}
	
	/**
	 * 尝试移动骨牌
	 * @param v 运动向量
	 * @return 是否操作成功
	 */
	boolean move(Coordinate v) {
		Coordinate newPos = Coordinate.add(position, v);
		if(!moveValid(newPos))
			return false;
		position = newPos;
		return true;
	}
	
	/**
	 * 骨牌的某种移动是否合法
	 * @param newPos 骨牌中心新坐标
	 * @return 是否合法
	 */
	boolean moveValid(Coordinate newPos) {
		Coordinate[] absolutePos = getAbsoluteBlock(newPos);
		for(int i=0; i<4; i++) {
			if(absolutePos[i].y < 0) // 下落超过边界
				return false;
			if(absolutePos[i].x < 0 || absolutePos[i].x >= GameLabel.width) // 左右移动超过边界
				return false;
			if(absolutePos[i].y < GameLabel.height && 
					g.isBlockOccupied(absolutePos[i].x, absolutePos[i].y))
				return false;
		}
		return true;
	}
}