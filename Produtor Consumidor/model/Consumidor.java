//pacote da classe
package model;
//importacoes
import controller.ControllerPC;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Consumidor extends Thread{
  //atributos 
  private String nomeDaThread= "consumidor";
  private int posX;
  private int speed;
  private int pontoParada = 255;
  ControllerPC cT;
  Image entregador_esquerda = new Image("./view/assets/consumidorLEFT_menor.png");
  Image entregador_direita = new Image("./view/assets/consumidorRIGHT_menor.png");
  //construtor do consumidor
  public Consumidor (ControllerPC cT){
    this.cT = cT;
  }

  //Metodo que, baseado no atributo capturado em tempo real do controle, atribui a velocidade ao caminhao do consumidor
  public int velocidadeProd() {
    speed = cT.getVelocidadeSliderConsu();
    switch(speed) {
        case 0: 
            speed = 8;
            break;
        case 1: 
            speed = 5;
            break;
        case 2: 
            speed = 2;
            break;
        default:
            System.out.println("Não foi possivel atribuir");
            break;
    }
    return speed;
  }


  //funcoes que movem o caminhao horizontalmente na tela ate a posicao de parada, no eixo x
  // caminhao indo para esquerda
  public void moveEsq(int stopPoint1, ImageView img){
    while(posX >= stopPoint1){
      if (posX == -200) {
        Platform.runLater(()->cT.getHappy_img().setVisible(false));
      }
      try {
        //tempo de espera varia para cada velocidade setada no slider de cada caminhao
        sleep(velocidadeProd());
        //fazendo movimentacao do caminhao na tela atraves do metodo Platform.runLater
        Platform.runLater(()->img.setX(posX));
        posX--;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  // caminhao indo para direita
  public void moveDir(int stopPoint2,ImageView img){
    while(posX <= stopPoint2){
      try {
        //tempo de espera varia para cada velocidade setada no slider de cada caminhao
        sleep(velocidadeProd());
        //fazendo movimentacao do caminhao na tela atraves do metodo Platform.runLater
        Platform.runLater(()->img.setX(posX));
        posX++;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  //metodo de remocao da regiao critica
  public void removeItem(){//remove do deposito a caixa 3 depois a 2 depois a 1
    try {
      sleep(velocidadeProd());
      if(cT.getImgBox3D().isVisible())
        cT.getImgBox3D().setVisible(false);
      else if(cT.getImgBox2D().isVisible())
        cT.getImgBox2D().setVisible(false);
      else if(cT.getImgBox1D().isVisible())
        cT.getImgBox1D().setVisible(false);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  //metodo de consumo
  public void consome(){
    try {
      //tempo de consumo
      sleep(velocidadeProd());
    }catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


  //funcoes de ida e volta do caminhao
  public void deliveryIda(int stopPoint){
    //muda a imagem do entregador
    Platform.runLater(()->cT.getImgCam2().setImage(entregador_esquerda));
    //deixa imagem visivel
    Platform.runLater(()->cT.getImgCam2().setVisible(true));
    //dirige para esquerda
    moveEsq(stopPoint,cT.getImgCam2());
  }
  public void deliveryVolta(int stopPoint){
    //muda a imagem do entregador
    Platform.runLater(()->cT.getImgCam2().setImage(entregador_direita));
    //deixa a imagem visivel
    Platform.runLater(()->cT.getImgCam2().setVisible(true));
    //animacao smile
    Platform.runLater(()->cT.getHappy_img().setVisible(false));
    //dirige para direita
    moveDir(stopPoint,cT.getImgCam2());
  }

  public void run(){
    //pegando a posicao inicial da imagem
    int auxX = (int)cT.getImgCam2().getX();
    auxX = posX;
    while(true){
        //caminhao vai buscar
        deliveryIda(-pontoParada);
        //////////////////////////////////////
        //REGIAO CRITICA
        Semaforo.DOWN(nomeDaThread);
        removeItem();
        Semaforo.UP(nomeDaThread);
        //////////////////////////////////////
        //caminhao volta para loja
        deliveryVolta(0);
        //consumo
        consome();
        //colocando a posicao como inicial
        posX = auxX;
        //animacao smile
        if (auxX == 0) {
          Platform.runLater(()->cT.getHappy_img().setVisible(true));
        }
    }
  }
}
