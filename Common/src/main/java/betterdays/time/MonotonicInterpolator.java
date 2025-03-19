package betterdays.time;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * Bezier spline interpolation from a set of points, with a tension parameter.
 */
public class MonotonicInterpolator {
    private final double[] x;
    private final double[] y;
    private final double[] m;
    private final double smoothFactor;

    public MonotonicInterpolator(List<Pair<Integer,Double>> points, double smoothFactor) {
        this.smoothFactor = smoothFactor;
        int n = points.size();
        x = new double[n];
        y = new double[n];
        m = new double[n];

        for (int i = 0; i < n; i++) {
            x[i] = points.get(i).getLeft();;
            y[i] = points.get(i).getRight();
        }
        computeMonotonicDerivatives();
    }

    private void computeMonotonicDerivatives() {
        int n = x.length;
        double[] d = new double[n - 1];

        for (int i = 0; i < n - 1; i++) {
            d[i] = (y[i + 1] - y[i]) / (x[i + 1] - x[i]);
        }

        m[0] = d[0];
        m[n - 1] = d[n - 2];
        for (int i = 1; i < n - 1; i++) {
            if (d[i - 1] * d[i] > 0) {
                double dx = x[i+1] - x[i];  // Interval spacing
                double scaledSmoothFactor = smoothFactor / dx;
                double weightedMean = (scaledSmoothFactor * d[i - 1] + d[i]) / (scaledSmoothFactor + 1);
                m[i] = weightedMean;
            } else {
                m[i] = 0;
            }
        }
    }

    public double evaluate(double xValue) {
        if (xValue <= x[0]) return y[0];
        if (xValue >= x[x.length - 1]) return y[y.length - 1];

        int i = Arrays.binarySearch(x, xValue);
        if (i < 0)
            i = -i - 2;

        double h = x[i + 1] - x[i];
        double t = (xValue - x[i]) / h;

        double h00 = (1 + 2 * t) * (1 - t) * (1 - t);
        double h10 = t * (1 - t) * (1 - t);
        double h01 = t * t * (3 - 2 * t);
        double h11 = t * t * (t - 1);

        return h00 * y[i] + h10 * h * m[i] + h01 * y[i + 1] + h11 * h * m[i + 1];
    }

}
