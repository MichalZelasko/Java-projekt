package map;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public String toString() {
        switch (this) {
            case NORTH:
                return "N";
            case NORTHEAST:
                return "NE";
            case EAST:
                return "E";
            case SOUTHEAST:
                return "SE";
            case SOUTH:
                return "S";
            case SOUTHWEST:
                return "SW";
            case WEST:
                return "W";
            case NORTHWEST:
                return "NW";

        }
        throw new IllegalArgumentException("Incorrect direction argument");
    }

    public MapDirection next() {
        switch (this) {
            case NORTH:
                return NORTHEAST;
            case NORTHEAST:
                return EAST;
            case EAST:
                return SOUTHEAST;
            case SOUTHEAST:
                return SOUTH;
            case SOUTH:
                return SOUTHWEST;
            case SOUTHWEST:
                return WEST;
            case WEST:
                return NORTHWEST;
            case NORTHWEST:
                return NORTH;
        }
        throw new IllegalArgumentException("Incorrect direction argument");
    }

    public MapDirection previous() {
        switch (this) {
            case NORTH:
                return NORTHWEST;
            case NORTHEAST:
                return NORTH;
            case EAST:
                return NORTHEAST;
            case SOUTHEAST:
                return EAST;
            case SOUTH:
                return SOUTHEAST;
            case SOUTHWEST:
                return SOUTH;
            case WEST:
                return SOUTHWEST;
            case NORTHWEST:
                return WEST;
        }
        throw new IllegalArgumentException("Incorrect direction argument");
    }

    public Vector2d toVector() {
        switch (this) {
            case NORTH:
                return new Vector2d(0, 1);
            case NORTHEAST:
                return new Vector2d(1, 1);
            case EAST:
                return new Vector2d(1, 0);
            case SOUTHEAST:
                return new Vector2d(1, -1);
            case SOUTH:
                return new Vector2d(0, -1);
            case SOUTHWEST:
                return new Vector2d(-1, -1);
            case WEST:
                return new Vector2d(-1, 0);
            case NORTHWEST:
                return new Vector2d(-1, 1);
        }
        throw new IllegalArgumentException("Incorrect direction argument");
    }

    public byte toByte() {
        switch (this) {
            case NORTH:
                return 0;
            case NORTHEAST:
                return 1;
            case EAST:
                return 2;
            case SOUTHEAST:
                return 3;
            case SOUTH:
                return 4;
            case SOUTHWEST:
                return 5;
            case WEST:
                return 6;
            case NORTHWEST:
                return 7;
        }
        throw new IllegalArgumentException("Incorrect direction argument");
    }

    public MapDirection rotation(byte rotationAngle) {
        byte newDirection = (byte) ((this.toByte() + rotationAngle) % (byte) 8);
        switch (newDirection) {
            case 0:
                return NORTH;
            case 1:
                return NORTHEAST;
            case 2:
                return EAST;
            case 3:
                return SOUTHEAST;
            case 4:
                return SOUTH;
            case 5:
                return SOUTHWEST;
            case 6:
                return WEST;
            case 7:
                return NORTHWEST;
        }
        throw new IllegalArgumentException("Incorrect direction argument");
    }
}
