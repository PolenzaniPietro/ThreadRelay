/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threadrelay;

/**
 *
 * @author polenzani.pietro
 */
public class Corridore extends Thread{
    private int distanza = 0;
    private Staffetta staffetta;
    public Corridore(Staffetta s) {
        this.staffetta=s;
    }
    
    public void corri(){
        if(distanza<99){
            this.distanza++;
        }
        
    }
    public void Run(){
        try{
            if(!this.staffetta.isOccupato()){
            this.corri();
            Thread.sleep(90);
            }
        }
        catch(Exception e){
        }
    }
}
