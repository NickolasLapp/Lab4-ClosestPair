package closestpairs_util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class ClosestPairs {

    public final int points[][];
    public int pointsXSorted[][];
    public int pointsYSorted[][];

    public ClosestPairs(int points[][]) {
        this.points = points;
        preprocess();
    }

    public int bruteForceMinDist() {
        int minDist = Integer.MAX_VALUE;
        // int[] point1 = { 0, 0 };
        // int[] point2 = { 0, 0 };

        for (int i = 0; i < points.length; ++i) {
            for (int j = i + 1; j < points.length; ++j) {
                if (distSQ(points[i], points[j]) < minDist) {
                    minDist = distSQ(points[i], points[j]);
                    // point1 = points[i];
                    // point2 = points[j];
                }
            }
        }
        // System.out.println("Shortest Between:" + point1[0] + ", " + point1[1]
        // + " and " + point2[0] + ", " + point2[1]);
        return minDist;
    }

    public int minDistExtWithPrints() {
        System.out.println("Finding Closest Points in: ");
        for (int i = 0; i < points.length; ++i)
            printPoint(i);
        System.out.print("\r\n-----------------------------------------------------------------------------------\r\n");
        return minDistWithPrints(0, points.length - 1);
    }

    public void printPoint(int index) {
        if (index >= 0 && index <= points.length)
            System.out.print("(" + points[index][0] + ", " + points[index][1] + ")");
    }

    public int minDistWithPrints(int low, int high) {
        System.out.println("Solving: " + low + "----" + high);
        if (high - low == 1) // only two points
        {
            System.out.print("\tFound Result: ");
            printPoint(low);
            printPoint(high);
            System.out.println("\r\nDistSquared= " + distSQ(pointsXSorted[low], pointsXSorted[high]));
            return distSQ(pointsXSorted[low], pointsXSorted[high]);
        }
        if (high == low) // only 1 point
        {
            System.out.println("\tFound Result: INF");
            return Integer.MAX_VALUE;
        }

        int median = (high - low) / 2 + low;
        int medianXVal = pointsXSorted[median][0];
        System.out.println("    Divided @ " + median + " (x-value " + medianXVal + ")");

        int leftShortest = minDistWithPrints(low, median);
        int rightShortest = minDistWithPrints(median + 1, high);
        int shortest = Math.min(leftShortest, rightShortest);
        System.out.println("Combining Problems: " + low + "----" + median + ", " + median + 1 + "----" + high);
        int crossCut = closestAcross(medianXVal, shortest);
        System.out.println("Found Result: " + Math.min(shortest, crossCut));
        return Math.min(shortest, crossCut);
    }

    public int minDistExt() {
        return minDist(0, points.length - 1);
    }

    public int minDist(int low, int high) {
        if (high - low == 1) // only two points
            return distSQ(pointsXSorted[low], pointsXSorted[high]);
        if (high == low) // only 1 point
            return Integer.MAX_VALUE;

        int median = (high - low) / 2 + low;

        int medianXVal = pointsXSorted[median][0];

        int leftShortest = minDist(low, median);
        int rightShortest = minDist(median + 1, high);
        int shortest = Math.min(leftShortest, rightShortest);
        int crossCut = closestAcross(medianXVal, shortest);

        return Math.min(shortest, crossCut);
    }

    private int closestAcross(int medianXValue, int shortest) {
        ArrayList<int[]> insideCrossCut = new ArrayList<int[]>();

        int toReturn = Integer.MAX_VALUE;

        for (int i = 0; i < pointsYSorted.length; ++i)
            if ((pointsYSorted[i][0] - medianXValue) * (pointsYSorted[i][0] - medianXValue) <= shortest)
                insideCrossCut.add(pointsYSorted[i]);

        for (int i = 0; i < insideCrossCut.size(); ++i)
            toReturn = Math.min(toReturn, minOfNeighbors(insideCrossCut, i));

        return toReturn;
    }

    private int minOfNeighbors(ArrayList<int[]> insideCrossCut, int index) {
        int shortest = Integer.MAX_VALUE;

        for (int i = index - 3; i <= index + 3; ++i)
            if (i != index && i >= 0 && i < insideCrossCut.size())
                if (distSQ(insideCrossCut.get(index), insideCrossCut.get(i)) < shortest)
                    shortest = distSQ(insideCrossCut.get(index), insideCrossCut.get(i));

        return shortest;
    }

    public int distSQ(int[] first, int[] second) {
        int xDist = first[0] - second[0];
        int yDist = first[1] - second[1];
        return xDist * xDist + yDist * yDist;
    }

    private void preprocess() {

        Comparator<int[]> compY = new Comparator<int[]>() {

            @Override
            public int compare(int[] l, int[] r) {
                return Integer.compare(l[1], r[1]);
            }
        };

        Comparator<int[]> compX = new Comparator<int[]>() {

            @Override
            public int compare(int[] l, int[] r) {
                return Integer.compare(l[0], r[0]);
            }
        };

        pointsXSorted = Arrays.copyOf(points, points.length);
        pointsYSorted = Arrays.copyOf(points, points.length);

        Arrays.sort(pointsXSorted, compX);
        Arrays.sort(pointsYSorted, compY);
    }

    public static void main(String[] args) {

        double points[][] = { { 2.0, 7.0 }, { 4.0, 13.0 }, { 5.0, 8.0 }, { 10.0, 5.0 }, { 14.0, 9.0 }, { 15.0, 5.0 },
                { 17.0, 7.0 }, { 19.0, 10.0 }, { 22.0, 7.0 }, { 25.0, 10.0 }, { 29.0, 14.0 }, { 30.0, 2.0 } };

        int testPoints[][] = new int[points.length][points[0].length];
        for (int i = 0; i < points.length; ++i)
            for (int j = 0; j < points[i].length; ++j)
                testPoints[i][j] = (int) points[i][j];

        ClosestPairs testClosest = new ClosestPairs(testPoints);
        // for (int i = 0; i < testPoints.length; ++i) {
        // System.out.println(testClosest.pointsXSorted[i][0] + ", " +
        // testClosest.pointsXSorted[i][1] + "\t"
        // + testClosest.pointsYSorted[i][0] + "," +
        // testClosest.pointsYSorted[i][1]);
        // }
        System.out.println(testClosest.bruteForceMinDist());
        System.out.println(testClosest.minDistExtWithPrints());

        for (int testNum = 1; testNum < 20000; ++testNum) {
            testPoints = new int[testNum][2];

            Random rand = new Random();
            for (int i = 0; i < testPoints.length; ++i)
                testPoints[i] = new int[] { rand.nextInt(10000), rand.nextInt(10000) };
            testClosest = new ClosestPairs(testPoints);
            // for (int i = 0; i < testPoints.length; ++i) {
            // System.out.println(testClosest.pointsXSorted[i][0] + ", " +
            // testClosest.pointsXSorted[i][1] + "\t"
            // + testClosest.pointsYSorted[i][0] + "," +
            // testClosest.pointsYSorted[i][1]);
            // }

            long startTime = System.nanoTime();
            long elapsed = 0;
            int bruteDist = testClosest.bruteForceMinDist();
            elapsed = System.nanoTime() - startTime;
            System.out.print(elapsed + "\t");

            startTime = System.nanoTime();
            int custDist = testClosest.minDistExt();
            elapsed = System.nanoTime() - startTime;
            System.out.println(elapsed);

            if (bruteDist != custDist) {
                System.out.println("Bad Value");
                for (int i = 0; i < testPoints.length; ++i) {
                    System.out.println(testClosest.pointsXSorted[i][0] + ", " + testClosest.pointsXSorted[i][1] + "\t"
                            + testClosest.pointsYSorted[i][0] + "," + testClosest.pointsYSorted[i][1]);
                }
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
                scanner.close();
            }

        }
    }
}
