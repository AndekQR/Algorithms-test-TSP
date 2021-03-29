package app.controller.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Helpers {


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int getRandomNumber(int from, int to) {
        Random random = new Random();
        return random.nextInt(to - from) + from;
    }

    public static double getRandomNumber(double from, double to) {
        Random r = new Random();
        return from + (to - from) * r.nextDouble();
    }

    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
            System.out.println(ex.getLocalizedMessage());
        }
    }

}
