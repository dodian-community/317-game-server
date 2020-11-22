package net.dodian.plugins.impl.skill;

public class AgilityCourse {
    enum Course { GNOME, BARBARIAN, WILDERNESS }
    //Teleport = x, y, z position
    //FORCEFUL_WALK = duration, directionX, directionY
    //FAIL_WALK = duration, directionX, directionY, failChance (x:1000), x * (Current lvl - obstacle lvl) increase success rate
    enum ObstacleType { TELEPORT, FORCEFUL_WALK, FAIL_WALK }

    private enum obstacles {
      LOG1(Course.GNOME, 555, 1, 55, ObstacleType.FORCEFUL_WALK, 3000, -6, 0)
      ;
      Course course;
      int objectId, level, xp;
      ObstacleType obstacleType;
      int[] values;
      obstacles(Course course, int objectId, int level, int xp, ObstacleType obstacleType, int... values) {
          this.course = course;
          this.objectId = objectId;
          this.level = level;
          this.xp = xp;
          this.obstacleType = obstacleType;
          this.values = values;
      }
    }

}
