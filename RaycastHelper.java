import org.lwjgl.util.vector.Vector3f;

public class RaycastHelper {

    public static Vector3i raycast(Block[][][] blocks, float camX, float camY, float camZ, float rotX, float rotY, float maxDistance) {
        float radRotX = (float) Math.toRadians(rotX);
        float radRotY = (float) Math.toRadians(rotY);

        Vector3f dir = new Vector3f(
                (float) Math.sin(radRotY) * (float) Math.cos(radRotX),
                -(float) Math.sin(radRotX),
                -(float) Math.cos(radRotY) * (float) Math.cos(radRotX)
        );

        float step = 0.1f;
        for (float d = 0; d < maxDistance; d += step) {
            int x = (int) (camX + dir.x * d);
            int y = (int) (camY + dir.y * d);
            int z = (int) (camZ + dir.z * d);

            if (x < 0 || y < 0 || z < 0 || x >= blocks.length || y >= blocks[0].length || z >= blocks[0][0].length)
                continue;

            Block block = blocks[x][y][z];
            if (block != null && block.type != BlockType.AIR) {
                return new Vector3i(x, y, z);
            }
        }

        return null;
    }
}
