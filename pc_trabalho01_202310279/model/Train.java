/* ***************************************************************
* Autor............: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio...........: 13/03/2023
* Ultima alteracao.: 13/03/2023
* Nome.............: Train.java
* Funcao...........: Define a classe Train que e um modelo de um trem, contendo sua velocidade, sua imagem e sua posicao inicial
*************************************************************** */
package model;

import javafx.scene.image.ImageView;

public class Train {

  private double velocidade;
  private ImageView tremImagem;
  private int posicaoInicial;

  public Train(ImageView tremImagem, int posicaoInicial) {//construtor da classe
    this.tremImagem = tremImagem;//recebe a imagem do trem
    this.posicaoInicial = posicaoInicial;//recebe a posicao inicial do trem

    switch (this.posicaoInicial) {//posiciona a imagem do trem na tela de acordo com a posicao inicial escolhida pelo usuario
      case 0://caso a posicao inicial seja superior esquerda e superior direita
        tremImagem.setLayoutX(253);
        tremImagem.setLayoutY(-25);
        tremImagem.setRotate(270);
        break;
      case 1://caso a posicao inicial seja inferior esquerda e inferior direita
        tremImagem.setLayoutX(375);
        tremImagem.setLayoutY(-28);
        tremImagem.setRotate(270);
        break;
      case 2://caso a posicao inicial seja superior esquerda e inferior direira
        tremImagem.setLayoutX(246);
        tremImagem.setLayoutY(800);
        tremImagem.setRotate(90);
        break;
      case 3://caso a posicao inicial seja superior direita e inferior esquerda
        tremImagem.setLayoutX(374);
        tremImagem.setLayoutY(804);
        tremImagem.setRotate(90);
        break;
      default:
        System.out.println("Error");
        break;
    }//fim do switch
  }//fim do construtor

/* ***************************************************************
* Metodo: getVelocidade
* Funcao: Retorna a velocidade do trem, um numero double que permite modificar a variavel de movimento que muda a posicao do trem na tela
* Parametros: nenhum
* Retorno: double, a velocidade do trem
*************************************************************** */
  public double getVelocidade() {
    return velocidade;
  }//fim do metodo getVelocidade

/* ***************************************************************
* Metodo: setVelocidade
* Funcao: Define a velocidade do trem, um numero double que permite modificar a variavel de movimento que muda a posicao do trem na tela
* Parametros: double velocidade, a velocidade do trem
* Retorno: DataTransfer instance, a instancia da classe para permitir a transferencia de dados
*************************************************************** */
  public void setVelocidade(double velocidade) {
    this.velocidade = velocidade;
  }//fim do metodo setVelocidade

/* ***************************************************************
* Metodo: getTremImagem
* Funcao: Retorna a imagem do trem, um ImageView permitindo o controle da posicao trem na tela
* Parametros: nenhum
* Retorno: ImageView, a imagem do trem
*************************************************************** */
  public ImageView getTremImagem() {
    return tremImagem;
  }//fim do metodo getTremImagem

/* ***************************************************************
* Metodo: getPosicaoInicial
* Funcao: Retorna a posicao inicial do trem escolhida pelo usuario para controle da ordem de movimento dos trens
* Parametros: nenhum
* Retorno: Retorna um inteiro que representa a posicao inicial do trem
*************************************************************** */
  public int getPosicaoInicial() {
    return posicaoInicial;
  }//fim do metodo getPosicaoInicial

}//fim da classe Train

