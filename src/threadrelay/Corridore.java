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
    private Staffetta staffetta = new Staffetta();
    private int distanza = 0;
    
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
