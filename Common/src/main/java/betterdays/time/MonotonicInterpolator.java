package betterdays.time;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class MonotonicInterpolator {
    private final double[] x, y, m; // x and y values, and computed slopes
    private final double smoothingFactor;
    public MonotonicInterpolator(List<Pair<Integer, Double>> points, double smoothingFactor) {
        if (points.size() < 2)
            throw new IllegalArgumentException("At least two points are required.");

        this.smoothingFactor = smoothingFactor;
        int n = points.size();
        this.x = new double[n];
        this.y = new double[n];
        this.m = new double[n];

        for (int i = 0; i < n; i++) {
            x[i] = points.get(i).getLeft();
            y[i] = points.get(i).getRight();
        }

        computeMonotonicSlopes();
    }

    private void computeMonotonicSlopes() {
        int n = x.length;
        double[] delta = new double[n - 1];

        // Compute finite differences (secant slopes)
        for (int i = 0; i < n - 1; i++) {
            delta[i] = (y[i + 1] - y[i]) / (x[i + 1] - x[i]);
        }

        // Compute initial slopes (tangents)
        m[0] = delta[0];
        for (int i = 1; i < n - 1; i++) {
            if (delta[i - 1] * delta[i] > 0) { // If same sign
                m[i] = smoothingFactor * (delta[i - 1] + delta[i]) / 2.0;
            } else {
                m[i] = 0.0; // Flat slope to prevent overshoot
            }
        }
        m[n - 1] = delta[n - 2];

        // Adjust to prevent overshoot
        for (int i = 0; i < n - 1; i++) {
            if (delta[i] == 0) { // If flat, force slopes to zero
                m[i] = 0;
                m[i + 1] = 0;
            } else {
                double alpha = m[i] / delta[i];
                double beta = m[i + 1] / delta[i];
                double sum = alpha * alpha + beta * beta;
                if (sum > 9) {
                    double tau = 3.0 / Math.sqrt(sum);
                    m[i] = tau * alpha * delta[i];
                    m[i + 1] = tau * beta * delta[i];
                }
            }
        }
    }

    public double evaluate(double xValue) {
        if (xValue <= x[0]) return y[0];
        if (xValue >= x[x.length - 1]) return y[y.length - 1];

        int i = Arrays.binarySearch(x, xValue);
        if (i < 0) i = -i - 2; // Get interval index

        double h = x[i + 1] - x[i];
        double t = (xValue - x[i]) / h;
        double t2 = t * t, t3 = t2 * t;

        double h00 = (2 * t3 - 3 * t2 + 1);
        double h10 = (t3 - 2 * t2 + t) * h;
        double h01 = (-2 * t3 + 3 * t2);
        double h11 = (t3 - t2) * h;

        return h00 * y[i] + h10 * m[i] + h01 * y[i + 1] + h11 * m[i + 1];
    }
}