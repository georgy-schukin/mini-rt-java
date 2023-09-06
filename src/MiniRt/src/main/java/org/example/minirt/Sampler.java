package org.example.minirt;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class Sampler {
    public static class Sample2D {
        public double x = 0;
        public double y = 0;

        public Sample2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    // Generate random samples in [0, 1] square.
    public List<Sample2D> sampleRandom(int numOfSamples) {
        SplittableRandom random = new SplittableRandom();
        List<Sample2D> samples = new ArrayList<>();
        for (int i = 0; i < numOfSamples; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            samples.add(new Sample2D(x, y));
        }
        return samples;
    }
}
