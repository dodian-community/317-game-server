package net.dodian.old.world.model.movement.path;

import net.dodian.old.world.collision.region.RegionClipping;
import net.dodian.old.world.entity.impl.Character;
import net.dodian.old.world.model.Position;

public class PathGenerator {

	public static final int[][] DIR = { { -1, 1 }, { 0, 1 }, { 1, 1 }, { -1, 0 }, { 1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 } };
	public static final int[] NON_DIAGONAL_DIRECTIONS = { 1, 3, 4, 6 };

	public static Position getCombatPath(Character character, Character following) {
		
		if(character == null || following == null) {
			return null;
		}
		
		int characterX = character.getPosition().getX();
		int characterY = character.getPosition().getY();
		int characterZ = character.getPosition().getZ();
		int followX = following.getPosition().getX();
		int followY = following.getPosition().getY();

		double lowDist = 9999.0D;
		int lowX = 0;
		int lowY = 0;

		int x3 = followX;
		int y3 = followY - 1;

		int loop = following.getSize();

		if (RS317PathFinder.accessable(followX, followY, characterZ, x3, y3) && isInteractionPathClear(x3, y3, characterZ, followX, followY)) {
			lowDist = getManhattanDistance(x3, y3, characterX, characterY);
			lowX = x3;
			lowY = y3;
		}
		

		for (int k = 0; k < 4; k++) {
			for (int i = 0; i < loop - (k == 0 ? 1 : 0); i++) {
				if (k == 0) {
					x3++;
				} else if (k == 1) {
					if (i == 0) {
						x3++;
					}
					y3++;
				} else if (k == 2) {
					if (i == 0) {
						y3++;
					}
					x3--;
				} else if (k == 3) {
					if (i == 0) {
						x3--;
					}
					y3--;
				}
				double d;
				if ((d = getManhattanDistance(x3, y3, characterX, characterY)) < lowDist) {
					if (RS317PathFinder.accessable(followX, followY, characterZ, x3, y3) && isInteractionPathClear(x3, y3, characterZ, followX, followY)) {
						lowDist = d;
						lowX = x3;
						lowY = y3;
					}
				}
			}
		}

		return new Position(lowX, lowY, character.getPosition().getZ());
	}

	public static Position getBasicPath(Character character, Character following) {
		if(character == null || following == null) {
			return null;
		}
		int x = following.getPosition().getX();
		int y = following.getPosition().getY();
		int z = following.getPosition().getZ();

		int[][] nodes = { { x + DIR[NON_DIAGONAL_DIRECTIONS[0]][0], y + DIR[NON_DIAGONAL_DIRECTIONS[0]][1] }, { x + DIR[NON_DIAGONAL_DIRECTIONS[1]][0], y + DIR[NON_DIAGONAL_DIRECTIONS[1]][1] }, { x + DIR[NON_DIAGONAL_DIRECTIONS[2]][0], y + DIR[NON_DIAGONAL_DIRECTIONS[2]][1] }, { x + DIR[NON_DIAGONAL_DIRECTIONS[3]][0], y + DIR[NON_DIAGONAL_DIRECTIONS[3]][1] } };

		int bestX = 0;
		int bestY = 0;
		double bestDist = 99999.0D;

		for (int i = 0; i < nodes.length; i++) {
			if (isInteractionPathClear(nodes[i][0], nodes[i][1], z, x, y)) {
				double dist = Math.sqrt(Math.pow(character.getPosition().getX() - nodes[i][0], 2.0D) + Math.pow(character.getPosition().getY() - nodes[i][1], 2.0D));
				RegionClipping reg = RegionClipping.forPosition(x, y);
				if(reg == null) {
					continue;
				}
				if ((dist < bestDist) && (reg.canMove(new Position(character.getPosition().getX(), character.getPosition().getY(), character.getPosition().getZ()), NON_DIAGONAL_DIRECTIONS[i]))) {
					bestDist = dist;
					bestX = nodes[i][0];
					bestY = nodes[i][1];
				}
			}
		}

		if (bestX == 0 && bestY == 0) {
			return null;
		}

		return new Position(bestX, bestY, z);
	}

	public static boolean isInteractionPathClear(final int x0, final int y0, final int characterZ, final int followX, final int followY) {
		int deltaX = followX - x0;
		int deltaY = followY - y0;

		double error = 0;
		final double deltaError = Math.abs((deltaY) / (deltaX == 0 ? ((double) deltaY) : ((double) deltaX)));

		int x = x0;
		int y = y0;

		int pX = x;
		int pY = y;

		boolean incrX = x0 < followX;
		boolean incrY = y0 < followY;

		if (!RS317PathFinder.accessable(x0, y0, characterZ, followX, followY)) {
			return false;
		}

		while (true) {
			if (x != followX) {
				x += (incrX ? 1 : -1);
			}

			if (y != followY) {
				error += deltaError;

				if (error >= 0.5) {
					y += (incrY ? 1 : -1);
					error -= 1;
				}
			}

			if(!RegionClipping.canProjectileMove(pX, pY, followX, followY, characterZ, 1, 1)) {
				return false;
			}

			if (incrX && incrY && x >= followX && y >= followY) {
				break;
			} else if (!incrX && !incrY && x <= followX && y <= followY) {
				break;
			} else if (!incrX && incrY && x <= followX && y >= followY) {
				break;
			} else if (incrX && !incrY && x >= followX && y <= followY) {
				break;
			}

			pX = x;
			pY = y;
		}

		return true;
	}

	/**
	 * Gets Manhattan distance
	 * 
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static int getManhattanDistance(int x, int y, int x2, int y2) {
		return Math.abs(x - x2) + Math.abs(y - y2);
	}
}
