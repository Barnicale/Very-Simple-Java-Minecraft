import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputHandler {
    public static void handleInput(Block[][][] blocks) {
        float speed = 0.2f;
        float dx = Mouse.getDX(), dy = Mouse.getDY();

        WorldPanel.rotY += dx * WorldPanel.mouseSensitivity;
        WorldPanel.rotX -= dy * WorldPanel.mouseSensitivity;
        WorldPanel.rotX = Math.max(-90, Math.min(90, WorldPanel.rotX));

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            WorldPanel.camX += Math.sin(Math.toRadians(WorldPanel.rotY)) * speed;
            WorldPanel.camZ -= Math.cos(Math.toRadians(WorldPanel.rotY)) * speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            WorldPanel.camX -= Math.sin(Math.toRadians(WorldPanel.rotY)) * speed;
            WorldPanel.camZ += Math.cos(Math.toRadians(WorldPanel.rotY)) * speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            WorldPanel.camX -= Math.cos(Math.toRadians(WorldPanel.rotY)) * speed;
            WorldPanel.camZ -= Math.sin(Math.toRadians(WorldPanel.rotY)) * speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            WorldPanel.camX += Math.cos(Math.toRadians(WorldPanel.rotY)) * speed;
            WorldPanel.camZ += Math.sin(Math.toRadians(WorldPanel.rotY)) * speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) WorldPanel.camY += speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) WorldPanel.camY -= speed;

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) Mouse.setGrabbed(false);
        if (Mouse.isButtonDown(0)) Mouse.setGrabbed(true);

        for (int i = 0; i < WorldPanel.hotbar.length; i++) {
            if (Keyboard.isKeyDown(Keyboard.KEY_1 + i)) {
                WorldPanel.selectedIndex = i;
            }
        }


        if (Mouse.isButtonDown(1)) { // Right click = place block
            Vector3i hit = RaycastHelper.raycast(blocks, WorldPanel.camX, WorldPanel.camY, WorldPanel.camZ, WorldPanel.rotX, WorldPanel.rotY, 5f);
            if (hit != null) {
                // Just place above it for now
                int px = hit.x, py = hit.y + 1, pz = hit.z;

                if (px >= 0 && py >= 0 && pz >= 0 && px < blocks.length && py < blocks[0].length && pz < blocks[0][0].length) {
                    if (blocks[px][py][pz] == null || blocks[px][py][pz].type == BlockType.AIR) {
                        BlockType selected = WorldPanel.hotbar[WorldPanel.selectedIndex];
                        blocks[px][py][pz] = new Block(px, py, pz, selected);
                        System.out.println("Placed block: " + selected + " at " + px + "," + py + "," + pz);
                    }
                }

                for (int i = 0; i < WorldPanel.hotbar.length; i++) {
                    if (Keyboard.isKeyDown(Keyboard.KEY_1 + i)) {
                        WorldPanel.selectedIndex = i;
                        System.out.println("Selected block: " + WorldPanel.hotbar[WorldPanel.selectedIndex]);
                    }
                }

            }
        } else if (Mouse.isButtonDown(0)) { // Left click = destroy block
            Vector3i hit = RaycastHelper.raycast(blocks, WorldPanel.camX, WorldPanel.camY, WorldPanel.camZ, WorldPanel.rotX, WorldPanel.rotY, 5f);

            if (hit != null) {
                int px = hit.x, py = hit.y, pz = hit.z;

                if (px >= 0 && py >= 0 && pz >= 0 && px < blocks.length && py < blocks[0].length && pz < blocks[0][0].length) {
                    if (blocks[px][py][pz] != null && blocks[px][py][pz].type != BlockType.AIR) {
                        blocks[px][py][pz] = new Block(px, py, pz, BlockType.AIR); // break the block
                    }
                }
            }

        }
    }
}
