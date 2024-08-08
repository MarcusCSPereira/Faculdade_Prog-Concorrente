/* ***************************************************************
* Autor............: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio...........: 13/03/2023
* Ultima alteracao.: 13/03/2023
* Nome.............: ControleDeMovimentoDeTrens.java 
* Funcao...........: Classe que realiza o controle de movimento dos trens na tela, permitindo que os trens se movam de acordo com a posicao inicial escolhida pelo usuario, alem do controle de colisao entre os trens
*************************************************************** */

package util;

import javafx.scene.shape.Circle;
import model.Train;

public class ControleDeMovimentoDeTrens {

  public ControleDeMovimentoDeTrens() {//construtor da classe
  }//fim do construtor

/* ***************************************************************
* Metodo: moverTrem
* Funcao: Chamar o metodo de movimento do trem de acordo com a posicao inicial escolhida pelo usuario
* Parametros: Train trem, o trem que sera movido
* Retorno: void
*************************************************************** */
  public void moverTrem(Train trem) {
    switch (trem.getPosicaoInicial()) {
      case 0://caso a posicao inicial do trem seja superior esquerda
        moverTremNaPosicao0(trem);
        break;
      case 1://caso a posicao inicial do trem seja superior direita
        moverTremNaPosicao1(trem);
        break;
      case 2://caso a posicao inicial do trem seja inferior esquerda
        moverTremNaPosicao2(trem);
        break;
      case 3://caso a posicao inicial do trem seja inferior direita
        moverTremNaPosicao3(trem);
        break;
      default:
        break;
    }//fim do switch
  }

/* ***************************************************************
* Metodo: checarColisao
* Funcao: checar se esta tendo ou nao colisao entre o trem e a zona critica
* Parametros: Train trem, o trem que esta sendo checado para ver se existe colisao, Circle zonaCritica, a zona critica que controla o local onde pode haver colisao
* Retorno: boolean, true se houver colisao, false se nao houver colisao
*************************************************************** */
  public boolean checarColisao(Train trem, Circle zonaCritica) {

    if (trem.getTremImagem().getBoundsInParent().intersects(zonaCritica.getBoundsInParent())) {//checa se a imagem do trem esta colidindo com alguma das partes da zona critica
      return true;//retorna true se houver colisao
    } else {
      return false;//retorna false se nao houver colisao
    }

  }

/* ***************************************************************
* Metodo: moverTremNaPosicao0
* Funcao: Realiza o movimento do trem na posicao inicial 0 que e a superior esquerda, implementando a logica de movimento do trem na tela de acordo sua posicao em relacao ao eixo Y movendo a imagem do trem incrementando ou decrementando a velocidade do trem no eixo Y e eixo X , o que faz com que o trem se mova é o fato desse metodo estar sendo chamado em um AnimationTimer que atualiza a tela a cada frame, fazendo com que o trem se mova a cada frame.
* Parametros: Train trem, o trem que sera movido
* Retorno: void
*************************************************************** */
  private static void moverTremNaPosicao0(Train train) {

    // Aqui sera usado ifs e elses que checam a posição do trem na tela e move ele de acordo com a posição que ele se encontra no momento, sendo modificado a rotação da imagem do trem e determinando como ele movimenta no eixo Y e X

    if (train.getTremImagem().getLayoutY() < 97) {//checa a posicao da imagem do trem relativo ao eixo Y
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());//incrementa a posicao da imagem do trem no eixo Y de acordo com a velocidade do trem

    } else if (train.getTremImagem().getLayoutY() >= 97 && train.getTremImagem().getLayoutY() < 160) {
      train.getTremImagem().setRotate(230);//define a rotação da imagem do trem para animar a curva que o trem faz nos trilhos
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() + train.getVelocidade());//incrementa a posicao da imagem do trem no eixo Y e X de acordo com a velocidade do trem

    } else if (train.getTremImagem().getLayoutY() >= 160 && train.getTremImagem().getLayoutY() < 255) {
      train.getTremImagem().setRotate(270);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 255 && train.getTremImagem().getLayoutY() < 320) {
      train.getTremImagem().setRotate(310);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 320 && train.getTremImagem().getLayoutY() < 435) {
      train.getTremImagem().setRotate(270);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 435 && train.getTremImagem().getLayoutY() < 500) {
      train.getTremImagem().setRotate(230);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 500 && train.getTremImagem().getLayoutY() < 595) {
      train.getTremImagem().setRotate(270);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 595 && train.getTremImagem().getLayoutY() < 660) {
      train.getTremImagem().setRotate(310);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 660 && train.getTremImagem().getLayoutY() < 770) {
      train.getTremImagem().setRotate(270);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 770) {//Volta o trem para a posição inicial repetindo o processo de movimento do trem na tela
      train.getTremImagem().setRotate(270);
      train.getTremImagem().setLayoutY(-25);
      train.getTremImagem().setLayoutX(253);
    }//fim do else if
  }//fim do metodo moverTremNaPosicao0

/* ***************************************************************
* Metodo: moverTremNaPosicao1
* Funcao: Segue o mesmo principio do metodo moverTremNaPosicao0, porem com a logica de movimento do trem na posicao incial 1 que e a superior direita, assim modificando a rotação da imagem do trem e determinando como ele movimenta no eixo Y e X de acordo com a posicao que ele se encontra no momento
* Parametros: Train trem, o trem que sera movido
* Retorno: void
*************************************************************** */
  private static void moverTremNaPosicao1(Train train) {

    if (train.getTremImagem().getLayoutY() < 97) {
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 97 && train.getTremImagem().getLayoutY() < 155) {
      train.getTremImagem().setRotate(310);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 155 && train.getTremImagem().getLayoutY() < 260) {
      train.getTremImagem().setRotate(270);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 260 && train.getTremImagem().getLayoutY() < 320) {
      train.getTremImagem().setRotate(230);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 320 && train.getTremImagem().getLayoutY() < 435) {
      train.getTremImagem().setRotate(270);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 435 && train.getTremImagem().getLayoutY() < 496) {
      train.getTremImagem().setRotate(310);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 496 && train.getTremImagem().getLayoutY() < 598) {
      train.getTremImagem().setRotate(270);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 598 && train.getTremImagem().getLayoutY() < 660) {
      train.getTremImagem().setRotate(230);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 660 && train.getTremImagem().getLayoutY() < 770) {
      train.getTremImagem().setRotate(270);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() >= 770) {
      train.getTremImagem().setRotate(270);
      train.getTremImagem().setLayoutY(-28);
      train.getTremImagem().setLayoutX(375);
    }
  }//fim do metodo moverTremNaPosicao1

/* ***************************************************************
* Metodo: moverTremNaPosicao2
* Funcao: Segue o mesmo principio do metodo moverTremNaPosicao0, porem com a logica de movimento do trem na posicao incial 2 que e a inferior esquerda , assim modificando a rotação da imagem do trem e determinando como ele movimenta no eixo Y e X de acordo com a posicao que ele se encontra no momento, dessa vez o trem vem de baixo para cima na tela, logo o movimento do trem e feito de forma inversa ao movimento do trem nas posicoes 0 e 1
* Parametros: Train trem, o trem que sera movido
* Retorno: void
*************************************************************** */
  private static void moverTremNaPosicao2(Train train) {

    if (train.getTremImagem().getLayoutY() > 665) {
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 665 && train.getTremImagem().getLayoutY() > 595) {
      train.getTremImagem().setRotate(130);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 595 && train.getTremImagem().getLayoutY() > 500) {
      train.getTremImagem().setRotate(90);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 500 && train.getTremImagem().getLayoutY() > 430) {
      train.getTremImagem().setRotate(50);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 430 && train.getTremImagem().getLayoutY() > 325) {
      train.getTremImagem().setRotate(90);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 325 && train.getTremImagem().getLayoutY() > 255) {
      train.getTremImagem().setRotate(130);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 255 && train.getTremImagem().getLayoutY() > 165) {
      train.getTremImagem().setRotate(90);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 165 && train.getTremImagem().getLayoutY() > 100) {
      train.getTremImagem().setRotate(50);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 100 && train.getTremImagem().getLayoutY() > -65) {
      train.getTremImagem().setRotate(90);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= -65) {
      train.getTremImagem().setRotate(90);
      train.getTremImagem().setLayoutY(800);
      train.getTremImagem().setLayoutX(246);
    }
  }//fim do metodo moverTremNaPosicao2

/* ***************************************************************
* Metodo: moverTremNaPosicao3
* Funcao: Segue o mesmo principio do metodo moverTremNaPosicao2, porem com a logica de movimento do trem na posicao incial 3 que e a inferior direita, modificando assim a rotação da imagem do trem e determinando como ele movimenta no eixo Y e X de acordo com a posicao que ele se encontra no momento
* Parametros: Train trem, o trem que sera movido
* Retorno: void
*************************************************************** */
  private static void moverTremNaPosicao3(Train train) {

    if (train.getTremImagem().getLayoutY() > 660) {
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 660 && train.getTremImagem().getLayoutY() > 600) {
      train.getTremImagem().setRotate(50);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 600 && train.getTremImagem().getLayoutY() > 500) {
      train.getTremImagem().setRotate(90);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 500 && train.getTremImagem().getLayoutY() > 437) {
      train.getTremImagem().setRotate(130);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 437 && train.getTremImagem().getLayoutY() > 325) {
      train.getTremImagem().setRotate(90);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 325 && train.getTremImagem().getLayoutY() > 260) {
      train.getTremImagem().setRotate(50);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 260 && train.getTremImagem().getLayoutY() > 165) {
      train.getTremImagem().setRotate(90);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 165 && train.getTremImagem().getLayoutY() > 103) {
      train.getTremImagem().setRotate(130);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());
      train.getTremImagem().setLayoutX(train.getTremImagem().getLayoutX() + train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= 103 && train.getTremImagem().getLayoutY() > -65) {
      train.getTremImagem().setRotate(90);
      train.getTremImagem().setLayoutY(train.getTremImagem().getLayoutY() - train.getVelocidade());

    } else if (train.getTremImagem().getLayoutY() <= -40) {
      train.getTremImagem().setRotate(90);
      train.getTremImagem().setLayoutY(804);
      train.getTremImagem().setLayoutX(374);
    }
  }//fim do metodo moverTremNaPosicao3

}//fim da classe ControleDeMovimentoDeTrens
