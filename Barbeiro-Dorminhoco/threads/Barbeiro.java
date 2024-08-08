package threads;

import control.BackgroundScreenController;
import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

public class Barbeiro extends Thread {
  private ImageView cortando;
  private ImageView dormindo;
  private ImageView clienteCortando;
  private ImageView clienteCareca;
  private Slider sliderVelocidade;
  private boolean pausado = false;
  private boolean finalizado = false;
  int velocidade;

  public Barbeiro(ImageView cortando, ImageView dormindo, ImageView clienteCortando, ImageView clienteCareca, Slider sliderVelocidade){
    this.cortando = cortando;
    this.dormindo = dormindo;
    this.clienteCortando = clienteCortando;
    this.clienteCareca = clienteCareca;
    this.sliderVelocidade = sliderVelocidade;
  }

  @Override
  public void run() {
    while(true){
      try{
        if(finalizado){
          break;
        }
        checarPausado();
        if(BackgroundScreenController.clientes.availablePermits() == 0){
          Platform.runLater(()->cortando.setVisible(false));
          Platform.runLater(()->dormindo.setVisible(true));
        }
        if(finalizado){
          break;
        }
        checarPausado();
        BackgroundScreenController.clientes.acquire();
        if(finalizado){
          break;
        }
        checarPausado();
        Platform.runLater(()->cortando.setVisible(true));
        Platform.runLater(()->dormindo.setVisible(false));
        if(finalizado){
          break;
        }
        checarPausado();
        BackgroundScreenController.mutex.acquire();
        if(finalizado){
          break;
        }
        checarPausado();
        BackgroundScreenController.esperando--;
        if(finalizado){
          break;
        }
        checarPausado();
        BackgroundScreenController.barbeiros.release();
        if(finalizado){
          break;
        }
        checarPausado();
        BackgroundScreenController.mutex.release();
        if(finalizado){
          break;
        }
        checarPausado();

        cortarCabelo();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void cortarCabelo(){
    velocidade = (int)sliderVelocidade.getValue();
    try{
      checarPausado();
      if(!finalizado){
        sleep(6000 - (1000 + (40 * velocidade)));
      }
      checarPausado();
      if(!finalizado){
        Platform.runLater(()->clienteCortando.setVisible(false));
        Platform.runLater(()->clienteCareca.setVisible(true));
      }
      checarPausado();
      if(!finalizado){
        sleep(800);
      }
      checarPausado();
      if(!finalizado){
        Platform.runLater(()->clienteCareca.setVisible(false));
      }
      checarPausado();
      if(!finalizado){
        sleep(800);
      }
      checarPausado();
    } catch(Exception e){
      e.printStackTrace();
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

  public void checarPausado(){
    while(pausado && !finalizado){//testa se a thread nao esta finalizada para que possa ser caso necessite enquanto esta pausada
      try{
        sleep(1);//thread dorme por 1 ms a cada checagem positiva
      } catch(Exception e){
        e.printStackTrace();
      }//fim try catch
    }//fim while
  }

  public void finalizar(){
    finalizado = true;
  }
}
