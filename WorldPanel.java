// This is the main runner class

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class WorldPanel {
    private static final int worldWidth = 128;
    private static final int worldHeight = 128;
    private static final int worldDepth = 128;

    private Block[][][] blocks;

    public static float camX = 0, camY = 10, camZ = 20;
    public static float rotY = 0, rotX = 20f;
    public static float mouseSensitivity = 0.2f;

    public static BlockType[] hotbar = {
            BlockType.STONE, BlockType.DIRT, BlockType.GRASS
    };

    public static int selectedIndex = 0;

    public void run() throws Exception {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.setResizable(true);
        Display.setFullscreen(true);
        Mouse.setGrabbed(true);
        Display.create();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glClearColor(0.53f, 0.81f, 0.92f, 1.0f); // Sky blue
        GLU.gluPerspective(70, 800f / 600f, 0.1f, 1000);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        TerrainGenerator generator = new TerrainGenerator(worldWidth, worldHeight, worldDepth);
        blocks = generator.generate();

        while (!Display.isCloseRequested()) {
            InputHandler.handleInput(blocks);

            if (Display.wasResized()) {
                int width = Display.getWidth();
                int height = Display.getHeight();
                if (height == 0) height = 1;
                GL11.glViewport(0, 0, width, height);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GLU.gluPerspective(70, (float) width / height, 0.1f, 1000);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
            }

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glLoadIdentity();

            GL11.glRotatef(rotX, 1, 0, 0);
            GL11.glRotatef(rotY, 0, 1, 0);
            GL11.glTranslatef(-camX, -camY, -camZ);

            for (int x = 0; x < worldWidth; x++) {
                for (int y = 0; y < worldHeight; y++) {
                    for (int z = 0; z < worldDepth; z++) {
                        Block block = blocks[x][y][z];
                        if (block == null || block.type == BlockType.AIR) continue;

                        float dx = (x + 0.5f) - camX;
                        float dy = (y + 0.5f) - camY;
                        float dz = (z + 0.5f) - camZ;
                        float distSq = dx * dx + dy * dy + dz * dz;

                        if (distSq > 32 * 32) continue;

                        Block b = blocks[x][y][z];
                        if (BlockRenderer.shouldDrawFace(blocks, x, y, z, Face.TOP)) BlockRenderer.drawFace(x, y, z, Face.TOP, block.type);
                        if (BlockRenderer.shouldDrawFace(blocks, x, y, z, Face.BOTTOM)) BlockRenderer.drawFace(x, y, z, Face.BOTTOM, block.type);
                        if (BlockRenderer.shouldDrawFace(blocks, x, y, z, Face.LEFT)) BlockRenderer.drawFace(x, y, z, Face.LEFT, block.type);
                        if (BlockRenderer.shouldDrawFace(blocks, x, y, z, Face.RIGHT)) BlockRenderer.drawFace(x, y, z, Face.RIGHT, block.type);
                        if (BlockRenderer.shouldDrawFace(blocks, x, y, z, Face.BACK)) BlockRenderer.drawFace(x, y, z, Face.BACK, block.type);
                        if (BlockRenderer.shouldDrawFace(blocks, x, y, z, Face.FRONT)) BlockRenderer.drawFace(x, y, z, Face.FRONT, block.type);
                    }
                }
            }

            Display.setTitle(String.format("Blocky World 3D - X: %.1f Y: %.1f Z: %.1f", camX, camY, camZ));

            Display.update();
            Display.sync(60);
        }

        Display.destroy();
    }
}
