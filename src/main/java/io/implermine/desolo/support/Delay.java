package io.implermine.desolo.support;

public class Delay {
    private Delay() {}

    public static void delay(double seconds){
        try {
            Thread.sleep((long) (seconds* 1000L));
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void delay(){
        //0~0.5
        final long delay = (long) (Math.random() * 500);
        try {
            Thread.sleep(delay);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
