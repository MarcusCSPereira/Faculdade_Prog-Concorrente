package threads;

import control.BackgroundScreenController;
import javafx.scene.control.Slider;

public class GeradoraClientes extends Thread {
  public static boolean pausado = false;
  private Slider sliderVelocidade;
  
  BackgroundScreenController bSC;
  public void setbSC(BackgroundScreenController bSC) {
    this.bSC = bSC;
  }

  public GeradoraClientes(Slider sliderVelocidade){
    this.sliderVelocidade = sliderVelocidade;
  }
  int velocidade;

  @Override
  public void run() {
    while(true){
      if(!pausado){
        Cliente cliente = new Cliente(bSC.getClienteCortando(), bSC.getClienteEspiando());
        cliente.start();
        velocidade = (int)sliderVelocidade.getValue();
        try{
          sleep(6000 - (1000 + (40 * velocidade)));
        } catch(Exception e){
          e.printStackTrace();
        }
      } else {
        try{
          sleep(1);
        } catch(Exception e){
          e.printStackTrace();
        }
      }
    }
  }

  public boolean pausarOuContinuar(){
    if(!pausado){
      pausado = true;
    } else if(pausado){
      pausado = false;
    }
    return pausado;
  }
}

