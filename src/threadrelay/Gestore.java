/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threadrelay;

/**
 *
 * @author polenzani.pietro
 */
public class Gestore {
    private Staffetta s = new Staffetta();
    private Corridore c1 = new Corridore(s);
    private Corridore c2 = new Corridore(s);
    private Corridore c3 = new Corridore(s);
    private Corridore c4 = new Corridore(s);
}
