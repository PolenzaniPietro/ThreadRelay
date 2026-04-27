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

    private int distanza = 0;
    private String nome;
    private Staffetta staffetta;
    private Corridore precedente;

    public Corridore(Staffetta s) {
        this.staffetta = s;
    }

    public Corridore(String nome, Corridore precedente) {
        this.nome = nome;
        this.precedente = precedente;
    }

    private void corri() throws InterruptedException {
        for (int i = 0; i <= 100; i += 1) {
            System.out.println(nome + " -> " + i + " metri");
            Thread.sleep(80);
        }
    }

    @Override
    public void run() {
        try {
            if (precedente != null) {
                precedente.join();
            }

            System.out.println(nome + " pronto"); 
            corri();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
