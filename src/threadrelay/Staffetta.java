/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threadrelay;

/**
 *
 * @author polenzani.pietro
 */
public class Staffetta {

    private static final int RUNNERS = 4;

    private final boolean[] via = new boolean[RUNNERS];
    private final Corridore[] corridori = new Corridore[RUNNERS];
    private final Object lock = new Object();

    public void startRace() {
        System.out.println("Partenza staffetta");
        for (int i = 0; i < RUNNERS; i++) {
            via[i] = false;
            corridori[i] = new Corridore("Runner " + (i + 1), i, via, lock);
        }

        synchronized (lock) {
            via[0] = true;
            lock.notifyAll();
        }
        
        for (Corridore c : corridori) {
            c.start();
        }
        
        for (Corridore c : corridori) {
            try {
                c.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("Staffetta completata");
    }
}
