package threads;

import control.BackgroundScreenController;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Cliente extends Thread {
  private ImageView cortando;
  private ImageView esperando;
  private Image ImageClienteEspiando;

  public Cliente(ImageView cortando, ImageView esperando){
    this.cortando = cortando;
    this.esperando = esperando;
  }

  @Override
  public void run(){
    try{
      clienteEspiando();
      sleep(500);
      BackgroundScreenController.mutex.acquire();
      if(BackgroundScreenController.esperando < BackgroundScreenController.CADEIRAS){
        BackgroundScreenController.esperando++;
        clienteParaDeEspiar();
        if(BackgroundScreenController.esperando > 0){
          Platform.runLater(()->BackgroundScreenController.filaClientes.get(BackgroundScreenController.esperando - 1).setVisible(true));
        }
        sleep(800);
        BackgroundScreenController.clientes.release();
        BackgroundScreenController.mutex.release();
        BackgroundScreenController.barbeiros.acquire();
        sentarNaCadeira();
      } else{
        BackgroundScreenController.mutex.release();
        vaiEmboraBarbearia();
      }
    } catch(Exception e){
      e.printStackTrace();
    }
  }

  private void clienteEspiando(){
    ImageClienteEspiando = new Image(getClass().getResourceAsStream("../assets/clienteEspiando.png"));
    Platform.runLater(()->esperando.setImage(ImageClienteEspiando));
    Platform.runLater(()->esperando.setVisible(true));
  }

  private void clienteParaDeEspiar(){
    Platform.runLater(()->esperando.setVisible(false));
  }

  private void sentarNaCadeira(){
    try{
      Platform.runLater(()->BackgroundScreenController.filaClientes.get(0).setVisible(false));
      Platform.runLater(()->cortando.setVisible(true));
      sleep(300);
      if(BackgroundScreenController.esperando < 5 && BackgroundScreenController.esperando >= 0)
      Platform.runLater(()->BackgroundScreenController.filaClientes.get(BackgroundScreenController.esperando).setVisible(false));
      else
      Platform.runLater(()->BackgroundScreenController.filaClientes.get(4).setVisible(false));
      if(BackgroundScreenController.esperando > 0)      
      Platform.runLater(()->BackgroundScreenController.filaClientes.get(0).setVisible(true));

    } catch(Exception e){
      e.printStackTrace();
    }
  }

  private void vaiEmboraBarbearia(){
    ImageClienteEspiando = new Image(getClass().getResourceAsStream("../assets/clientetriste.png"));
    Platform.runLater(()->esperando.setImage(ImageClienteEspiando));
    Platform.runLater(()->esperando.setVisible(true));
    try{
      sleep(500);
      Platform.runLater(()->esperando.setVisible(false));
      sleep(800);
    } catch(Exception e){
      e.printStackTrace();
    }

  }
}
