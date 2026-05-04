/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threadrelay;

/**
 *
 * @author polenzani.pietro
 */
public class Corridore extends Thread {

    private final String nome;
    private final int index;
    private final boolean[] via;
    private final boolean[] pausa;
    private final Object lock;

    public Corridore(String nome, int index, boolean[] via, boolean[] pausa, Object lock) {
        this.nome = nome;
        this.index = index;
        this.via = via;
        this.pausa = pausa;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                while (!via[index]) {
                    lock.wait();
                }
            }
            for (int i = 0; i <= 99; i++) {
                synchronized (lock) {
                    while (pausa[0]) {
                        lock.wait();
                    }
                }
                System.out.println(nome + " -> " + i);
                if (i == 90) {
                    synchronized (lock) {
                        if (index + 1 < via.length) {
                            via[index + 1] = true;
                            lock.notifyAll();
                        }
                    }
                }
                Thread.sleep(60);
            }

        } catch (InterruptedException e) {
            interrupt();
        }
    }
}
