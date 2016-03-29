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
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

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
	int rotationState;
	
	/**
	 * 初始化骨牌
	 * @param _g 对应的游戏面板
	 */
	Tetromino(GameLabel _g) {
		g = _g;
		rotationState = new Random().nextInt(4);
		position = new Coordinate(GameLabel.width / 2,
									GameLabel.height);
	}
	
	/**
	 * 获取骨牌颜色
	 * @return 颜色的Color对象
	 */
	abstract public Color getColor();
	
	/**
	 * 获取骨牌各方块在[背景坐标系]中的位置
	 * @return 骨牌各方块在[背景坐标系]中的坐标
	 */
	public Coordinate[] getAbsoluteBlock() {
		return getAbsoluteBlock(getBlock(), position); 
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
	
	public boolean rotate() {
		if(!rotateValid())
		   return false;
		rotationState = (rotationState + 1) % 4;
		return true;
	}
	
	/** 
	 * 随机生成一个骨牌
	 * @param _g 游戏面板
	 * @return 返回随机骨牌
	 */
	public static Tetromino randomTetromino(GameLabel _g) {
		Tetromino ret = null;
		ArrayList<Class<?>> tetrominos = getTetrominos();
		Random r = new Random();
		int idx = r.nextInt(tetrominos.size());
	
		// 应该不会产生错误
		try {
			ret = (Tetromino) tetrominos.get(idx)
					.getDeclaredConstructor(
							new Class[] {GameLabel.class})
							.newInstance(_g);
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) { // 传入的参数是正确的
		} catch (InvocationTargetException e) {
	    } catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
		
		return ret;
	}
	
	/** 
	 * 获取骨牌种类列表
	 * @return 骨牌种类的ArrayList
	 */
	public static ArrayList<Class<?>> getTetrominos() {
		ArrayList<Class<?>> ret = new ArrayList<Class<?>>();
	    String pkg = Tetromino.class.getPackage().getName();
	    String relPath = pkg.replace('.', '/');

	    URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
	    if (resource == null) {
	        throw new RuntimeException("Unexpected problem: No resource for "
	                + relPath);
	    }

	    File f = new File(resource.getPath());

	    String[] files = f.list();

	    for (int i = 0; i < files.length; i++) {
	        String fileName = files[i];
	        if (fileName.endsWith(".class")) {
	            String fileNm = fileName.substring(0, fileName.length() - 6);
	            if(fileNm.equals("Tetromino")) // 去掉基类本身
	            		continue;
	            String className = pkg + '.' + fileNm;
	            try {
					ret.add(Class.forName(className));
				} catch (ClassNotFoundException e) {} 
	        }
	    }
	    return ret;
	}
	
	/**
	 * 获取骨牌各方块在[自身坐标系]中的位置
	 * @return 骨牌各方块在[自身坐标系]中的坐标
	 */
	abstract Coordinate[] getBlock();
	
	/**
	 * 获取骨牌旋转后在[自身坐标系]中的位置
	 */
	abstract Coordinate[] getRotatedBlock();
	
	/**
	 * 旋转是否受到周围方格阻碍
	 */
	abstract boolean isRotationForbidden();
	
	/**
	 * 根据各方块的在自身坐标系中的坐标，以及骨牌在坐标系中的位置，
	 * 合成得到各方块在背景坐标系中的位置。
	 * @param pos 新的骨牌中心坐标
	 * @return 骨牌各方块在[背景坐标系]中的坐标
	 */
	static Coordinate[] getAbsoluteBlock(Coordinate[] blocks,
			Coordinate pos) {
		Coordinate[] ret = new Coordinate[4];
		for(int i=0; i<4; i++)
			ret[i] = Coordinate.add(blocks[i], pos);
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
		Coordinate[] absolutePos = getAbsoluteBlock(getBlock(), newPos);
		for(int i=0; i<4; i++)
			if(!posValid(absolutePos[i]))
				return false;
		return true;
	}
	
	/**
	 * 骨牌旋转是否合法
	 */
	boolean rotateValid() {
		Coordinate[] rotatedPos = getAbsoluteBlock(
				getRotatedBlock(), position);
		for(int i=0; i<4; i++)
			if(!posValid(rotatedPos[i]))
				return false;
		
		return !isRotationForbidden();
	}
	
	/**
	 * 方块位置是否合法
	 * @param p 方块在背景坐标系中的位置
	 */
	boolean posValid(Coordinate p) {
		if(p.y < 0) // 下落超过边界
			return false;
		if(p.x < 0 || p.x >= GameLabel.width) // 左右移动超过边界
			return false;
		if(p.y < GameLabel.height && 
				g.isBlockOccupied(p.x, p.y))
			return false;
		return true;
	}
}