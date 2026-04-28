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

    private String nome;
    private int index;
    private boolean[] via;
    private Object lock;

    public Corridore(String nome, int index, boolean[] via, Object lock) {
        this.nome = nome;
        this.index = index;
        this.via = via;
        this.lock = lock;
        this.nome=nome;
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
