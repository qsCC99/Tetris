/**
 * OTetromino.java
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
 * O形骨牌
 */
public class OTetromino extends Tetromino {
	
	Color color = Color.GREEN;
	static final Coordinate[] block =
		{
			new Coordinate(0, 1),
			new Coordinate(0, 0),
			new Coordinate(1, 0),
			new Coordinate(1, 1),
		};
	
	public OTetromino(GameLabel _g) {
		super(_g);
	}

	public Color getColor() {
		return color;
	}
	
	Coordinate[] getBlock() {
		return block;
	}

	Coordinate[] getRotatedBlock() {
		return block;
	}

	@Override
	/**
	 * O形骨牌旋转后不变
	 */
	public boolean rotate() {
		return true;
	}

	/**
	 * O形骨牌没有特殊的旋转阻碍规则
	 */
	boolean isRotationForbidden() {
		return false;
	}
}