public class Vector3i {
    public int x, y, z;

    public Vector3i(int x, int y, int z) {
        this.x = x; this.y = y; this.z = z;
    }

    public Vector3i add(Vector3i other) {
        return new Vector3i(x + other.x, y + other.y, z + other.z);
    }
}
