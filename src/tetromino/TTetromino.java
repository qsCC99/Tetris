/**
 * TTetromino.java
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
 * @since 2016-3-29
 * T形骨牌
 */
public class TTetromino extends Tetromino {
	Color color = Color.CYAN;
	static final Coordinate[][] block =
		{
			{
				new Coordinate(0, 0),
				new Coordinate(1, 0),
				new Coordinate(-1, 0),
				new Coordinate(0, 1),
			},
			{
				new Coordinate(0, 0),
				new Coordinate(1, 0),
				new Coordinate(0, -1),
				new Coordinate(0, 1),
			},
			{
				new Coordinate(0, 0),
				new Coordinate(1, 0),
				new Coordinate(-1, 0),
				new Coordinate(0, -1),
			},
			{
				new Coordinate(0, 0),
				new Coordinate(-1, 0),
				new Coordinate(0, -1),
				new Coordinate(0, 1),
			},
		};
	
	public TTetromino(GameLabel _g) {
		super(_g);
	}

	public Color getColor() {
		return color;
	}
	
	Coordinate[] getBlock() {
		return block[rotationState];
	}
	
	Coordinate[] getRotatedBlock() {
		return block[(rotationState + 1) % 4];
	}

	boolean isRotationForbidden() {
		return false;
	}
}
