import org.lwjgl.opengl.GL11;

public class BlockRenderer {

    public static boolean shouldDrawFace(Block[][][] blocks, int x, int y, int z, Face face) {
        int width = blocks.length;
        int height = blocks[0].length;
        int depth = blocks[0][0].length;

        int nx = x, ny = y, nz = z;

        switch (face) {
            case TOP: ny += 1; break;
            case BOTTOM: ny -= 1; break;
            case LEFT: nx -= 1; break;
            case RIGHT: nx += 1; break;
            case FRONT: nz += 1; break;
            case BACK: nz -= 1; break;
        }

        if (nx < 0 || ny < 0 || nz < 0 || nx >= width || ny >= height || nz >= depth) {
            return true;
        }

        Block neighbor = blocks[nx][ny][nz];
        return (neighbor == null || neighbor.type == BlockType.AIR);
    }

    private static void setShadedColor(BlockType type, Face face) {
        float r = 1f, g = 1f, b = 1f;

        switch (type) {
            case GRASS: r = 0.2f; g = 0.8f; b = 0.2f; break;
            case DIRT:  r = 0.5f; g = 0.3f; b = 0.1f; break;
            case STONE: r = 0.6f; g = 0.6f; b = 0.6f; break;
        }

        // Shade depending on face
        float shade = switch (face) {
            case TOP -> 1.0f;
            case FRONT, BACK, LEFT, RIGHT -> 0.8f;
            case BOTTOM -> 0.6f;
        };

        GL11.glColor3f(r * shade, g * shade, b * shade);
    }


    public static void drawFace(int x, int y, int z, Face face, BlockType type) {
        // Set color based on block type
        setShadedColor(type, face);

        GL11.glBegin(GL11.GL_QUADS);

        switch (face) {
            case TOP:
                GL11.glVertex3f(x,     y + 1, z);
                GL11.glVertex3f(x + 1, y + 1, z);
                GL11.glVertex3f(x + 1, y + 1, z + 1);
                GL11.glVertex3f(x,     y + 1, z + 1);
                break;

            case BOTTOM:
                GL11.glVertex3f(x,     y, z);
                GL11.glVertex3f(x + 1, y, z);
                GL11.glVertex3f(x + 1, y, z + 1);
                GL11.glVertex3f(x,     y, z + 1);
                break;

            case LEFT:
                GL11.glVertex3f(x, y, z);
                GL11.glVertex3f(x, y + 1, z);
                GL11.glVertex3f(x, y + 1, z + 1);
                GL11.glVertex3f(x, y, z + 1);
                break;

            case RIGHT:
                GL11.glVertex3f(x + 1, y, z);
                GL11.glVertex3f(x + 1, y + 1, z);
                GL11.glVertex3f(x + 1, y + 1, z + 1);
                GL11.glVertex3f(x + 1, y, z + 1);
                break;

            case FRONT:
                GL11.glVertex3f(x, y, z + 1);
                GL11.glVertex3f(x + 1, y, z + 1);
                GL11.glVertex3f(x + 1, y + 1, z + 1);
                GL11.glVertex3f(x, y + 1, z + 1);
                break;

            case BACK:
                GL11.glVertex3f(x, y, z);
                GL11.glVertex3f(x + 1, y, z);
                GL11.glVertex3f(x + 1, y + 1, z);
                GL11.glVertex3f(x, y + 1, z);
                break;
        }

        GL11.glEnd();
    }

}
