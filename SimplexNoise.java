import java.util.Random;

public class SimplexNoise { // technically not perfect perlin but very close
    private static final int[] PERMUTATION = new int[512];
    private static final Random random = new Random();

    static {
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }

        // Shuffle
        for (int i = 0; i < 256; i++) {
            int j = random.nextInt(256);
            int temp = p[i];
            p[i] = p[j];
            p[j] = temp;
        }

        for (int i = 0; i < 512; i++) {
            PERMUTATION[i] = p[i & 255];
        }
    }

    private static float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    private static float grad(int hash, float x, float y) {
        int h = hash & 15;
        float u = h < 8 ? x : y;
        float v = h < 4 ? y : (h == 12 || h == 14 ? x : 0);
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    public static float noise(float x, float y) {
        int X = (int)Math.floor(x) & 255;
        int Y = (int)Math.floor(y) & 255;

        x -= Math.floor(x);
        y -= Math.floor(y);

        float u = fade(x);
        float v = fade(y);

        int A = PERMUTATION[X] + Y;
        int B = PERMUTATION[X + 1] + Y;

        return lerp(
                lerp(grad(PERMUTATION[A], x, y), grad(PERMUTATION[B], x - 1, y), u),
                lerp(grad(PERMUTATION[A + 1], x, y - 1), grad(PERMUTATION[B + 1], x - 1, y - 1), u),
                v
        );
    }
}
