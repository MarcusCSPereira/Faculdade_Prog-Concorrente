/* ***************************************************************
* Autor............: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio...........: 20/04/2023
* Ultima alteracao.: 29/04/2023
* Nome.............: Train2Thread
* Funcao...........: Classe que controla todo o movimento do trem 2 por meio de uma thread, definindo a posicao inicial do trem, a velocidade do trem e a solucao do problema de sincronizacao que sera utilizada, alem da manipulacao da entrada e saida do trem na zona critica
*************************************************************** */

package model;

import javafx.scene.image.ImageView;
import controller.ControllerTela2;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Slider;
import util.DataTransfer;

@SuppressWarnings("unused")

public class Train2Thread extends Thread {

    private ImageView imageView;//imagem do trem
    private final Slider speedSlider;//slider de velocidade
    private int posicaoInicial;//posicao inicial do trem
    private double speed;//velocidade do trem
    private int solucao;//solucao escolhida pelo usuario para o controle de concorrencia
    private int processo = 1;//processo do trem utilizado na solucao de peterson para o controle de interesse na regiao critica

    private boolean zonaCritica1 = false;//variavel que controla a entrada e saida do trem na zona critica 1
    private boolean zonaCritica2 = false;//variavel que controla a entrada e saida do trem na zona critica 2

    //Construtor da classe Train2Thread que define a posicao inicial do trem, a imagem do trem, o slider de velocidade e a solucao do problema de sincronizacao por valores passados como parametro
    public Train2Thread(int posicaoInicial, ImageView imageView, Slider speedSlider, int solucao) {
        
        this.imageView = imageView;
        this.speedSlider = speedSlider;
        this.posicaoInicial = posicaoInicial;
        this.speed = speedSlider.getValue();
        this.solucao = solucao;//define a solucao do problema de sincronizacao

        switch (posicaoInicial) {
          case 1://caso a posicao inicial seja inferior esquerda e inferior direita
            this.imageView.setRotate(270);
            this.imageView.setLayoutY(-26);
            this.imageView.setLayoutX(375);
          break;
          case 3://caso a posicao inicial seja superior direita e inferior esquerda
            this.imageView.setRotate(90);
            this.imageView.setLayoutY(801);
            this.imageView.setLayoutX(374);
          default:
          break;
        }//fim do switch


    }

    // Classe interna para criacao de um timer proprio que extende a classe AnimationTimer para controlar o movimento do trem 2
    private class MyTimer2 extends AnimationTimer{
      private Train2Thread train;// Objeto do tipo Train1Thread que sera utilizado para controlar o movimento do trem 2
      

      //Construtor da classe MyTimer2 que define o trem que sera movido por meio de um objeto do tipo Train2Thread
      public MyTimer2(Train2Thread train){
        this.train = train;
      }

      @Override//Soobrescrita do metodo handle da classe AnimationTimer para controlar o movimento do trem 2
      /*
      * ***************************************************************
      * Metodo: handle
      * Funcao: Metodo que eh invocado a cada frame da animacao chamando o metodo moveTrain para controlar o movimento do trem 1
      * Parametros: long now, o tempo atual do frame
      * Retorno: DataTransfer instance, a instancia da classe para permitir a
      * transferencia de dados
      */
      public void handle(long now) {
        //Utilizando o Platform.runLater para atualizar a interface grafica dentro da mesma thread do JavaFX evitando erros de animacao da tela
        Platform.runLater(() -> {
          train.moveTrain();
        });// Fim do Platform.runLater
      }// Fim do metodo handle
    }// Fim da classe MyTimer2

    // Cria o timer de movimento do trem 2 instanciando a classe MyTimer criada acima
    AnimationTimer timerDeMovimento = new MyTimer2(this);

    /*
    * ***************************************************************
    * Metodo: getTimerDeMovimento
    * Funcao: Metodo que retorna o timer de movimento do trem 2 para ser controlado na tela 2
    * Parametros: nenhum
    * Retorno: AnimationTimer, o timer de movimento do trem 2
    */
    public AnimationTimer getTimerDeMovimento(){
      return timerDeMovimento;
    }

    @Override//Soobrescrita do metodo run da classe Thread para iniciar o timer de movimento do trem 2
    /*
    * ***************************************************************
    * Metodo: run
    * Funcao: Metodo que é chamado quando a thread é iniciada, iniciando o timer de movimento do trem 1
    * Parametros: nenhum
    * Retorno: nenhum
    */
    public void run() {
      timerDeMovimento.start();
    }

    /* ***************************************************************
    * Metodo: moveTrain
    * Funcao: Metodo que controla o movimento do trem 2 de acordo com a posicao inicial do trem, a velocidade do trem e a solucao do problema de sincronizacao escolhida pelo usuario
    * Parametros: nenhum
    * Retorno: void
    *************************************************************** */
    public void moveTrain() {

    this.setVelocidade(speedSlider.getValue());//define a velocidade do trem de acordo com o valor do slider de velocidade

    //Switch que controla o movimento do trem de acordo com a posicao inicial do trem
    switch (this.posicaoInicial) {
      case 1:{
        if (this.getTremImagem().getLayoutY() < 97) {
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());

        }
        //Inicio da Zona critica 1
        if (this.getTremImagem().getLayoutY() >= 97 && this.getTremImagem().getLayoutY() < 155) {

          if(!entrouNaZonaCritica1(solucao) && !zonaCritica1){
            // if que verifica a entrada na zona critica 1 de acordo com a solucao escolhida pelo usuario e a variavel de controle da zona critica 1 caso o retorno de ambas seja false o trem para de se mover
            break;  
          }

          this.getTremImagem().setRotate(310);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() - this.getVelocidade());

        }
        if (this.getTremImagem().getLayoutY() >= 155 && this.getTremImagem().getLayoutY() < 260) {
          this.getTremImagem().setRotate(270);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());

        }
        if (this.getTremImagem().getLayoutY() >= 260 && this.getTremImagem().getLayoutY() < 320) {
          this.getTremImagem().setRotate(230);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() + this.getVelocidade());

        }

        //Fim da zona critica 1
        if (this.getTremImagem().getLayoutY() >= 320 && this.getTremImagem().getLayoutY() < 435) {

          saiuDaZonaCritica1(solucao);//metodo que controla a saida da zona critica 1 de acordo com a solucao escolhida pelo usuario

          this.getTremImagem().setRotate(270);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());

        }

        //Inicio da Zona critica 2
        if (this.getTremImagem().getLayoutY() >= 435 && this.getTremImagem().getLayoutY() < 496) {

          if(!entrouNaZonaCritica2(solucao) && !zonaCritica2){
            // if que verifica a entrada na zona critica 2 de acordo com a solucao escolhida pelo usuario e a variavel de controle da zona critica 2 caso o retorno de ambas seja false o trem para de se mover
            break;
          }

          this.getTremImagem().setRotate(310);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() - this.getVelocidade());

        }

        if (this.getTremImagem().getLayoutY() >= 496 && this.getTremImagem().getLayoutY() < 598) {
          this.getTremImagem().setRotate(270);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());

        }
        if (this.getTremImagem().getLayoutY() >= 598 && this.getTremImagem().getLayoutY() < 660) {
          this.getTremImagem().setRotate(230);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() + this.getVelocidade());

        }

        //Fim da zona critica 2
        if (this.getTremImagem().getLayoutY() >= 660 && this.getTremImagem().getLayoutY() < 780) {

          saiuDaZonaCritica2(solucao);//metodo que controla a saida da zona critica 2 de acordo com a solucao escolhida pelo usuario

          this.getTremImagem().setRotate(270);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());

        }
        if (this.getTremImagem().getLayoutY() >= 780) {
          this.getTremImagem().setRotate(270);
          this.getTremImagem().setLayoutY(-26);
          this.getTremImagem().setLayoutX(375);
        }

        break;
      }//Fim do case 1

      case 3:{
        if (this.getTremImagem().getLayoutY() > 660) {
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());

        }

        //Inicio da zona critica 2
        if (this.getTremImagem().getLayoutY() <= 660 && this.getTremImagem().getLayoutY() > 600) {

          if(!entrouNaZonaCritica2(solucao) && !zonaCritica2){
            // if que verifica a entrada na zona critica 2 de acordo com a solucao escolhida pelo usuario e a variavel de controle da zona critica 2 caso o retorno de ambas seja false o trem para de se mover
            break;
          }

          this.getTremImagem().setRotate(50);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() - this.getVelocidade());

        }
        if (this.getTremImagem().getLayoutY() <= 600 && this.getTremImagem().getLayoutY() > 500) {
          this.getTremImagem().setRotate(90);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());

        }
        if (this.getTremImagem().getLayoutY() <= 500 && this.getTremImagem().getLayoutY() > 437) {
          this.getTremImagem().setRotate(130);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() + this.getVelocidade());

        }

        //Saiu da zona critica 2
        if (this.getTremImagem().getLayoutY() <= 437 && this.getTremImagem().getLayoutY() > 325) {

          saiuDaZonaCritica2(solucao);//metodo que controla a saida da zona critica 2 de acordo com a solucao escolhida pelo usuario

          this.getTremImagem().setRotate(90);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());

        }

        //Inicio da Zona critica 1
        if (this.getTremImagem().getLayoutY() <= 325 && this.getTremImagem().getLayoutY() > 260) {

          if(!entrouNaZonaCritica1(solucao) && !zonaCritica1){
            // if que verifica a entrada na zona critica 1 de acordo com a solucao escolhida pelo usuario e a variavel de controle da zona critica 1 caso o retorno de ambas seja false o trem para de se mover
            break;
          }

          this.getTremImagem().setRotate(50);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() - this.getVelocidade());

        }
        if (this.getTremImagem().getLayoutY() <= 260 && this.getTremImagem().getLayoutY() > 165) {
          this.getTremImagem().setRotate(90);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());

        }
        if (this.getTremImagem().getLayoutY() <= 165 && this.getTremImagem().getLayoutY() > 103) {
          this.getTremImagem().setRotate(130);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() + this.getVelocidade());

        }

        //Saiu da zona critica 1
        if (this.getTremImagem().getLayoutY() <= 103 && this.getTremImagem().getLayoutY() > -50) {

          saiuDaZonaCritica1(solucao);//metodo que controla a saida da zona critica 1 de acordo com a solucao escolhida pelo usuario

          this.getTremImagem().setRotate(90);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());

        }
        if (this.getTremImagem().getLayoutY() <= -50) {
          this.getTremImagem().setRotate(90);
          this.getTremImagem().setLayoutY(801);
          this.getTremImagem().setLayoutX(374);
        }
        break;
      }//Fim do case 3
    }// fim do switch
  }// fim do metodo moveTrain


  /* ***************************************************************
  * Metodo: entrouNaZonaCritica1
  * Funcao: Metodo que controla a entrada do trem 2 na zona critica 1 de acordo com a solucao escolhida pelo usuario
  * Parametros: int solucao, a solucao escolhida pelo usuario para o controle de concorrencia entre os trens
  * Retorno: boolean, true se o trem entrou na zona critica 1 e false caso contrario
  *************************************************************** */  
  public boolean entrouNaZonaCritica1(int solucao) {
    switch (solucao) {
      case 0:{//Variavel de travamento da zona critica 1

        if(ControllerTela2.variavelDeTravamento1==1){
          // if que verifica se a variavel de travamento da zona critica 1 esta ocupada, caso esteja o trem nao entra na zona critica 1 ate ela ser desocupada, essa variavel eh criada como um recurso compartilhado para controlar a entrada na zona critica 1
          return false;// retorna false caso a variavel de travamento da zona critica 1 esteja ocupada
        }else{
          ControllerTela2.variavelDeTravamento1 = 1;
          // caso a variavel de travamento da zona critica 1 nao esteja ocupada o trem entra na zona critica 1 e a variavel de travamento eh ocupada
          zonaCritica1 = true;// variavel de controle da zona critica 1
          return true;
        }

      }//Fim do case 0

      case 1:{//Estrita alternancia da zona critica 1

        if(ControllerTela2.turno1EA == 0){
          // if que verifica se o turno da zona critica 1 esta ocupado, caso esteja o trem nao entra na zona critica 1 ate ele ser desocupado, essa variavel eh criada como um recurso compartilhado para controlar a entrada na zona critica 1
          return false;
        }else{
          zonaCritica1 = true;
          // caso o turno da zona critica 1 nao esteja ocupado o trem entra na zona critica 1 e a variavel de controle da zona critica 1 eh ocupada
          return true;
        }

      }//Fim do case 1
      
      case 2:{//Solucao de Peterson para a zona critica 1

        int outroProcesso;//Variavel que armazena o valor do processo do outro trem
        outroProcesso = 1 - processo;// Como utilizamos 2 processos um com valor 0 e outro com valor 1, essa variavel eh utilizada para receber e controlar algumas condicoes do outro processo
        ControllerTela2.interesseNaRegiao1[processo]= true;// Define o interesse do processo do trem 2 na regiao critica 1, ou seja o interesse do trem 2 em entrar na zona critica 1
        ControllerTela2.turno1SP = processo;// Define o turno do processo do trem 2 na regiao critica 1, ou seja diz que o turno atual da zona critica 1 eh do trem 2
        if(ControllerTela2.turno1SP == processo && ControllerTela2.interesseNaRegiao1[outroProcesso] == true){
          // if que verifica se o turno da zona critica 1 eh do trem 1 e se o outro processo tem interesse na regiao critica 1, caso ambas as condicoes sejam verdadeiras o trem nao entra na zona critica 1 ate o turno ser desocupado
          return false;
        }else{
          zonaCritica1 = true;
          // caso o turno da zona critica 1 seja do trem 1 e o outro processo nao tenha interesse na regiao critica 1, o trem entra na zona critica 1
          return true;
        }

      }//Fim do case 2

      default:
        return true;
    }//Fim do switch
  }//Fim do metodo entrouNaZonaCritica1


  /* ***************************************************************
  * Metodo: saiuDaZonaCritica1
  * Funcao: Metodo que controla a saida do trem 2 da zona critica 1 de acordo com a solucao escolhida pelo usuario
  * Parametros: int solucao, a solucao escolhida pelo usuario para o controle de concorrencia entre os trens
  * Retorno: void
  *************************************************************** */
  public void saiuDaZonaCritica1(int solucao) {
    switch (solucao) {
      case 0:{//Variavel de travamento da zona critica 1

        ControllerTela2.variavelDeTravamento1 = 0;
        // caso o trem saia da zona critica 1 a variavel de travamento da zona critica 1 eh desocupada
        zonaCritica1 = false;
        break;

      }//Fim do case 0

      case 1:{//Estrita alternancia da zona critica 1

        ControllerTela2.turno1EA = 0;
        // caso o trem saia da zona critica 1 o turno da zona critica 1 se torna do outro trem
        zonaCritica1 = false;
        break;

      }//Fim do case 1

      case 2:{//Solucao de Peterson para a zona critica 1

        ControllerTela2.interesseNaRegiao1[processo] = false;
        // caso o trem saia da zona critica 1 o interesse do processo do trem 1 na regiao critica 1 eh desativado afinal ele nao esta mais na zona critica 1
        zonaCritica1 = false;
        break;

      }//Fim do case 2
    }//Fim do switch
  }//Fim do metodo saiuDaZonaCritica1

  /* ***************************************************************
  * Metodo: entrouNaZonaCritica2
  * Funcao: Metodo que controla a entrada do trem 2 na zona critica 2 de acordo com a solucao escolhida pelo usuario
  * Parametros: int solucao, a solucao escolhida pelo usuario para o controle de concorrencia entre os trens
  * Retorno: boolean, true se o trem entrou na zona critica 2 e false caso contrario
  *************************************************************** */
  public boolean entrouNaZonaCritica2(int solucao) {
    switch (solucao) {
      case 0:{//Variavel de travamento da zona critica 2

        if(ControllerTela2.variavelDeTravamento2 ==1){
          // if que verifica se a variavel de travamento da zona critica 2 esta ocupada, caso esteja o trem nao entra na zona critica 2 ate ela ser desocupada, essa variavel eh criada como um recurso compartilhado para controlar a entrada na zona critica 2
          return false;
        }else{
          ControllerTela2.variavelDeTravamento2 =1;
          // caso a variavel de travamento da zona critica 2 nao esteja ocupada o trem entra na zona critica 2 e a variavel de travamento eh ocupada
          zonaCritica2 = true;
          return true;
        }

      }//Fim do case 0

      case 1:{//Estrita alternancia da zona critica 2

        if(ControllerTela2.turno2EA == 0){
          // if que verifica se o turno da zona critica 2 esta ocupado, caso esteja o trem nao entra na zona critica 2 ate ele ser desocupado, essa variavel eh criada como um recurso compartilhado para controlar a entrada na zona critica 2
          return false;
        }else{
          // caso o turno da zona critica 2 nao esteja ocupado o trem entra na zona critica 2 e a variavel de controle da zona critica 2 eh ocupada
          zonaCritica2 = true;
          return true;
        }

      }//Fim do case 1

      case 2:{//Solucao de Peterson para a zona critica 2

        int outroProcesso;//Variavel que armazena o valor do processo do outro trem
        outroProcesso = 1 - processo;// Como utilizamos 2 processos um com valor 0 e outro com valor 1, essa variavel eh utilizada para receber e controlar algumas condicoes do outro processo
        ControllerTela2.interesseNaRegiao2[processo] = true;// Define o interesse do processo do trem 2 na regiao critica 2, ou seja o interesse do trem 2 em entrar na zona critica 2
        ControllerTela2.turno2SP = processo;// Define o turno do processo do trem 2 na regiao critica 2, ou seja diz que o turno atual da zona critica 2 eh do trem 2
        if(ControllerTela2.turno2SP == processo && ControllerTela2.interesseNaRegiao2[outroProcesso] == true){
          // if que verifica se o turno da zona critica 2 eh do trem 2 e se o outro processo tem interesse na regiao critica 2, caso ambas as condicoes sejam verdadeiras o trem nao entra na zona critica 2 ate o turno ser desocupado
          return false;
        }else{
          // caso o turno da zona critica 2 seja do trem 2 e o outro processo nao tenha interesse na regiao critica 2, o trem entra na zona critica 2
          zonaCritica2 = true;
          return true;
        }

      }//Fim do case 2
      default:
        return true;
    }//Fim do switch
  }//Fim do metodo entrouNaZonaCritica2

  /* ***************************************************************
  * Metodo: saiuDaZonaCritica2
  * Funcao: Metodo que controla a saida do trem 2 da zona critica 2 de acordo com a solucao escolhida pelo usuario
  * Parametros: int solucao, a solucao escolhida pelo usuario para o controle de concorrencia entre os trens
  * Retorno: void
  *************************************************************** */
  public void saiuDaZonaCritica2(int solucao) {
    switch (solucao) {
      case 0:{//Variavel de travamento da zona critica 2

        ControllerTela2.variavelDeTravamento2 = 0;
        // caso o trem saia da zona critica 2 a variavel de travamento da zona critica 2 eh desocupada
        zonaCritica2 = false;
        break;

      }//Fim do case 0

      case 1:{//Estrita alternancia da zona critica 2

        ControllerTela2.turno2EA = 0;
        // caso o trem saia da zona critica 2 o turno da zona critica 2 se torna do outro trem
        zonaCritica2 = false;
        break;

      }//Fim do case 1

      case 2:{//Solucao de Peterson para a zona critica 2

        ControllerTela2.interesseNaRegiao2[processo] = false;
        zonaCritica2 = false;
        break;

      }//Fim do case 2
    
      default:
        break;
    }//Fim do switch
  }//Fim do metodo saiuDaZonaCritica2

  /* ***************************************************************
  * Metodo: resetPosicao
  * Funcao: Metodo que reseta a posicao do trem 2 para a posicao inicial de acordo com a posicao inicial do trem 2
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void resetPosicao() {
    Platform.runLater(() -> {
    switch (this.posicaoInicial) {
      case 1:
        this.getTremImagem().setRotate(270);
        this.getTremImagem().setLayoutY(-26);
        this.getTremImagem().setLayoutX(375);
      break;
      case 3:
        this.getTremImagem().setRotate(90);
        this.getTremImagem().setLayoutY(801);
        this.getTremImagem().setLayoutX(374);
      break;
      default:
        break;
    }//Fim do switch
    });//Fim do Platform.runLater
  }//Fim do metodo resetPosicao

  // Getters e Setters dos atributos

  /* ***************************************************************
  * Metodo: getposicaoInicial
  * Funcao: Metodo que retorna a posicao inicial do trem 1
  * Parametros: nenhum
  * Retorno: int, a posicao inicial do trem 1
  */
  public int getposicaoInicial() {
    return this.posicaoInicial;
  }

  /* ***************************************************************
  * Metodo: setSolucao
  * Funcao: Metodo que define a solucao do trem 1
  * Parametros: int solucao, a solucao escolhida pelo usuario para o controle de concorrencia entre os trens
  * Retorno: void
  */
  public void setSolucao(int solucao) {
    this.solucao = solucao;
  }

  /* ***************************************************************
  * Metodo: getSolucao
  * Funcao: Metodo que retorna a solucao do trem 1
  * Parametros: nenhum
  * Retorno: int, a solucao do trem 1
  */
  public int getSolucao() {
    return this.solucao;
  }

  /* ***************************************************************
  * Metodo: getVelocidade
  * Funcao: Metodo que retorna a velocidade do trem 1
  * Parametros: nenhum
  * Retorno: double, a velocidade do trem 1
  */
  public double getVelocidade() {
    return this.speed;
  }

  /* ***************************************************************
  * Metodo: setVelocidade
  * Funcao: Metodo que define a velocidade do trem 1
  * Parametros: double speed, a velocidade do trem 1
  * Retorno: void
  */
  public void setVelocidade(double speed) {
    this.speed = speed;
  }

  /* ***************************************************************
  * Metodo: getTremImagem
  * Funcao: Metodo que retorna a imagem do trem 1
  * Parametros: nenhum
  * Retorno: ImageView, a imagem do trem 1
  */
  public ImageView getTremImagem() {
    return this.imageView;
  }

  /* ***************************************************************
  * Metodo: setTremImagem
  * Funcao: Metodo que define a imagem do trem 1
  * Parametros: ImageView imageView, a imagem do trem 1
  * Retorno: void
  */
  public void setTremImagem(ImageView imageView) {
    this.imageView = imageView;
  }

  /* ***************************************************************
  * Metodo: setPosicaoInicial
  * Funcao: Metodo que define a posicao inicial do trem 1
  * Parametros: int posicaoInicial, a posicao inicial do trem 1
  * Retorno: void
  */
  public void setPosicaoInicial(int posicaoInicial) {
    this.posicaoInicial = posicaoInicial;
  }

  /* ***************************************************************
  * Metodo: getPosicaoInicial
  * Funcao: Metodo que retorna a posicao inicial do trem 1
  * Parametros: nenhum
  * Retorno: int, a posicao inicial do trem 1
  */
  public int getPosicaoInicial() {
    return this.posicaoInicial;
  }


}
