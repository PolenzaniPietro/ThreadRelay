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
    private boolean occupato;

    public boolean isOccupato() {
        return occupato;
    }
    
    private Corridore[] corridori;

    public Staffetta() {
        corridori = new Corridore[4];
        corridori[0] = new Corridore("Corridore 1", null);
        corridori[1] = new Corridore("Corridore 2", corridori[0]);
        corridori[2] = new Corridore("Corridore 3", corridori[1]);
        corridori[3] = new Corridore("Corridore 4", corridori[2]);
    }

    public void startRace() {
    for (Corridore c : corridori) {
        c.start();
    }
    for (Corridore c : corridori) {
        try {
            c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
}
