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


    private final boolean[] via = new boolean[4];
    private final boolean[] pausa = new boolean[1];
    private final Corridore[] corridori = new Corridore[4];
    private final Object lock = new Object();

    public void pauseRace() {
        synchronized (lock) {
            pausa[0] = true;
        }
    }

    public void resumeRace() {
        synchronized (lock) {
            pausa[0] = false;
            lock.notifyAll();
        }
    }

    public void startRace() {
        System.out.println("Partenza staffetta");
        pausa[0] = false;
        for (int i = 0; i < 4; i++) {
            via[i] = false;
            corridori[i] = new Corridore("Runner " + (i + 1), i, via, pausa, lock);
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
