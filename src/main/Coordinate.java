/**
 * Coordinate.java
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

/**
 * @author 陈宇非<yufei.chen@pku.edu.cn>
 * @since 2016-3-28
 *
 * 二维坐标
 */
public class Coordinate {
	public int x;
	public int y;
	
	public final static Coordinate LEFT = 
			new Coordinate(-1, 0);
	public final static Coordinate RIGHT = 
			new Coordinate(1, 0);
	public final static Coordinate UP = 
			new Coordinate(0, 1);
	public final static Coordinate DOWN = 
			new Coordinate(0, -1);
	
	public Coordinate(int _x, int _y) {
		x = _x;
		y = _y;
	}
	
	public Coordinate(Coordinate c) {
		x = c.x;
		y = c.y;
	}
	
	public static Coordinate add(Coordinate c1, Coordinate c2) {
		Coordinate ret = new Coordinate(c1.x, c1.y);
		ret.x += c2.x;
		ret.y += c2.y;
		return ret;
	}
}
