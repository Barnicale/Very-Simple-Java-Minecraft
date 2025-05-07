public class TerrainGenerator {
    private int width, height, depth;

    public TerrainGenerator(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public Block[][][] generate() {
        Block[][][] blocks = new Block[width][height][depth];
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                float noise = SimplexNoise.noise(x * 0.1f, z * 0.1f);
                int heightHere = (int) ((noise + 1) / 2 * 10);

                for (int y = 0; y < height; y++) {
                    if (y < heightHere - 1) {
                        blocks[x][y][z] = new Block(x, y, z, BlockType.DIRT);
                    } else if (y == heightHere - 1) {
                        blocks[x][y][z] = new Block(x, y, z, BlockType.GRASS);
                    }
                }
            }
        }
        return blocks;
    }
}
