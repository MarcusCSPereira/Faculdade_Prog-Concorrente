/* ***************************************************************
* Autor............: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio...........: 20/04/2023
* Ultima alteracao.: 29/04/2023
* Nome.............: Train1Thread
* Funcao...........: Classe que controla todo o movimento do trem 1 por meio de uma thread, definindo a posicao inicial do trem, a velocidade do trem e a solucao do problema de sincronizacao que sera utilizada, alem da manipulacao da entrada e saida do trem na zona critica
*************************************************************** */

package model;

import javafx.scene.image.ImageView;
import controller.ControllerTela2;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Slider;
import util.DataTransfer;

@SuppressWarnings("unused")

public class Train1Thread extends Thread {
  private ImageView imageView;// Imagem do trem 1
  private final Slider speedSlider;// Slider de velocidade do trem 1
  private int posicaoInicial;// Posicao inicial do trem 1
  private double speed;// Velocidade do trem 1
  private int solucao;// Solucao escolhida pelo usuario para o controle de concorrencia entre os trens
  private int processo = 0;// Processo do trem 1 utilizado no algoritmo de Peterson para definir a thread que ira entrar na zona critica

  private boolean zonaCritica1 = false;// Variavel de controle para a zona critica 1
  private boolean zonaCritica2 = false;// Variavel de controle para a zona critica 2

  // Construtor da classe Train1Thread que inicializa os atributos da classe com os valores passados por parametro
  public Train1Thread(int posicaoInicial, ImageView imageView, Slider speedSlider, int solucao) {
    this.imageView = imageView;
    this.speedSlider = speedSlider;
    this.posicaoInicial = posicaoInicial;
    this.speed = speedSlider.getValue();// Define a velocidade do trem 1 de acordo com o valor do slider de velocidade da tela 2 passado por parametro
    this.solucao = solucao;

    // Define a posicao inicial do trem 1 na tela de acordo com a posicao inicial passada por parametro
    switch (posicaoInicial) {
      case 0:// caso a posicao inicial seja superior esquerda e superior direita
        this.imageView.setLayoutY(-25);
        this.imageView.setLayoutX(253);
        this.imageView.setRotate(270);
        break;
      case 2:// caso a posicao inicial seja superior esquerda e inferior direira
        this.imageView.setLayoutY(800);
        this.imageView.setLayoutX(246);
        this.imageView.setRotate(90);
        break;

      default:
        break;
    }
  }

  // Classe interna para criacao de um timer proprio que extende a classe AnimationTimer para controlar o movimento do trem 1 
  private class MyTimer extends AnimationTimer {

    private Train1Thread train;// Objeto do tipo Train1Thread que sera utilizado para controlar o movimento do trem 1
    
    // Construtor da classe MyTimer que inicializa o objeto train com o trem 1 passado por parametro
    public MyTimer(Train1Thread train) {
      this.train = train;
    }

    @Override// Sobrescrita do metodo handle da classe AnimationTimer
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

  }// Fim da classe MyTimer

  AnimationTimer timerDeMovimento = new MyTimer(this);// Cria o timer de movimento do trem 1 instanciando a classe MyTimer criada acima

  /*
   * ***************************************************************
   * Metodo: getTimerDeMovimento
   * Funcao: Metodo que retorna o timer de movimento do trem 1 para ser controlado na tela 2
   * Parametros: nenhum
   * Retorno: AnimationTimer, o timer de movimento do trem 1
   */
  public AnimationTimer getTimerDeMovimento() {
    return timerDeMovimento;
  }

  @Override// Sobrescrita do metodo run da classe Thread
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

  /*
  * ***************************************************************
  * Metodo: moveTrain
  * Funcao: Metodo que controla o movimento do trem 1 na tela de acordo com a posicao inicial do trem 1, a velocidade do trem 1 e a solucao escolhida pelo usuario para o controle de concorrencia entre os trens
  * Parametros: nenhum
  * Retorno: nenhum
  */
  public void moveTrain() {
    this.setVelocidade(speedSlider.getValue());//Atualiza a velocidade do trem 1 de acordo com o valor do slider de velocidade da tela 2

    //Define o movimento do trem 1 de acordo com a posicao inicial do trem 1
    switch (this.getPosicaoInicial()) {
      case 0:{
        if (this.getTremImagem().getLayoutY() < 97) {// checa a posicao da imagem do trem relativo ao eixo Y
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());// incrementa a posicao da imagem do trem no eixo Y de acordo com a velocidade do trem
        }
        //Inicio da zona critica 1
        if (this.getTremImagem().getLayoutY() >= 97 && this.getTremImagem().getLayoutY() < 160) {
          if (!entrouNaZonaCritica1(solucao) && !zonaCritica1) {
            // if que verifica a entrada na zona critica 1 de acordo com a solucao escolhida pelo usuario e a variavel de controle da zona critica 1 caso o retorno de ambas seja false o trem para de se mover
            break;
          }
          this.getTremImagem().setRotate(230);// define a rotação da imagem do trem para animar a curva que o trem faz nos trilhos
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() + this.getVelocidade());
          // incrementa a posicao da imagem do trem no eixo Y e X de acordo com a velocidade do trem
        }

         if (this.getTremImagem().getLayoutY() >= 160 && this.getTremImagem().getLayoutY() < 255) {
          this.getTremImagem().setRotate(270);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());
         }

         if (this.getTremImagem().getLayoutY() >= 255 && this.getTremImagem().getLayoutY() < 320) {
          this.getTremImagem().setRotate(310);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() - this.getVelocidade());
         }
         //Fim da zona critica 1
         if (this.getTremImagem().getLayoutY() >= 320 && this.getTremImagem().getLayoutY() < 435) {

          saiuDaZonaCritica1(solucao);// Metodo que controla a saida da zona critica 1 de acordo com a solucao escolhida pelo usuario

          this.getTremImagem().setRotate(270);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());
         }

         //Inicio da zona critica 2
         if (this.getTremImagem().getLayoutY() >= 435 && this.getTremImagem().getLayoutY() < 500) {

          if (!entrouNaZonaCritica2(solucao) && !zonaCritica2) {
            // if que verifica a entrada na zona critica 2 de acordo com a solucao escolhida pelo usuario e a variavel de controle da zona critica 2 caso o retorno de ambas seja false o trem para de se mover
            break;
          }

          this.getTremImagem().setRotate(230);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() + this.getVelocidade());
         }

         if (this.getTremImagem().getLayoutY() >= 500 && this.getTremImagem().getLayoutY() < 595) {
          this.getTremImagem().setRotate(270);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());
         }

         if (this.getTremImagem().getLayoutY() >= 595 && this.getTremImagem().getLayoutY() < 660) {
          this.getTremImagem().setRotate(310);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() - this.getVelocidade());
         }

         //Fim da zona Critca 2
         if (this.getTremImagem().getLayoutY() >= 660 && this.getTremImagem().getLayoutY() < 780) {

          saiuDaZonaCritica2(solucao);// Metodo que controla a saida da zona critica 2 de acordo com a solucao escolhida pelo usuario

          this.getTremImagem().setRotate(270);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() + this.getVelocidade());
         }

         if (this.getTremImagem().getLayoutY() >= 780) {// Volta o trem para a posição inicial repetindo o processo de movimento do trem na tela
          this.getTremImagem().setRotate(270);
          this.getTremImagem().setLayoutY(-25);
          this.getTremImagem().setLayoutX(253);
        } // fim dos if

          break;
      }// fim do case 0

      case 2:{

        if (this.getTremImagem().getLayoutY() > 665) {
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());

        }
        
        //Inicio da zona critica 2
        if (this.getTremImagem().getLayoutY() <= 665 && this.getTremImagem().getLayoutY() > 595) {
          if (!entrouNaZonaCritica2(solucao) && !zonaCritica2) {
            // if que verifica a entrada na zona critica 2 de acordo com a solucao escolhida pelo usuario e a variavel de controle da zona critica 2 caso o retorno de ambas seja false o trem para de se mover
            break;
          }
          this.getTremImagem().setRotate(130);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() + this.getVelocidade());

        }

        if (this.getTremImagem().getLayoutY() <= 595 && this.getTremImagem().getLayoutY() > 500) {
          this.getTremImagem().setRotate(90);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());

        }

        if (this.getTremImagem().getLayoutY() <= 500 && this.getTremImagem().getLayoutY() > 430) {
          this.getTremImagem().setRotate(50);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() - this.getVelocidade());

        }

        //Fim da zona critica 2
        if (this.getTremImagem().getLayoutY() <= 430 && this.getTremImagem().getLayoutY() > 325) {

          saiuDaZonaCritica2(solucao);// Metodo que controla a saida da zona critica 2 de acordo com a solucao escolhida pelo usuario

          this.getTremImagem().setRotate(90);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());

        }

        //Inicio da zona critica 1
        if (this.getTremImagem().getLayoutY() <= 325 && this.getTremImagem().getLayoutY() > 255) {

          if (!entrouNaZonaCritica1(solucao) && !zonaCritica1) {
            // if que verifica a entrada na zona critica 1 de acordo com a solucao escolhida pelo usuario e a variavel de controle da zona critica 1 caso o retorno de ambas seja false o trem para de se mover
            break;
          }

          this.getTremImagem().setRotate(130);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() + this.getVelocidade());

        }

        if (this.getTremImagem().getLayoutY() <= 255 && this.getTremImagem().getLayoutY() > 165) {
          this.getTremImagem().setRotate(90);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());

        }

        if (this.getTremImagem().getLayoutY() <= 165 && this.getTremImagem().getLayoutY() > 100) {
          this.getTremImagem().setRotate(50);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());
          this.getTremImagem().setLayoutX(this.getTremImagem().getLayoutX() - this.getVelocidade());

        }

        //Fim da zona critica 1
        if (this.getTremImagem().getLayoutY() <= 100 && this.getTremImagem().getLayoutY() > -50) {

          saiuDaZonaCritica1(solucao);// Metodo que controla a saida da zona critica 1 de acordo com a solucao escolhida pelo usuario

          this.getTremImagem().setRotate(90);
          this.getTremImagem().setLayoutY(this.getTremImagem().getLayoutY() - this.getVelocidade());

        }

        if (this.getTremImagem().getLayoutY() <= -50) {
          this.getTremImagem().setRotate(90);
          this.getTremImagem().setLayoutY(800);
          this.getTremImagem().setLayoutX(246);
        }

        break;
      }// fim do case 2
    }// fim do switch
  }// fim do metodo moverTrem

  /*
   * ***************************************************************
   * Metodo: entrouNaZonaCritica1
   * Funcao: Metodo que controla a entrada na zona critica 1 de acordo com a solucao escolhida pelo usuario, incializando os metodos de controle de entrada na zona critica 1 de acordo com a solucao escolhida podendo ser Variavel de travamento, Estrita alternancia ou Peterson
   * Parametros: int solucao, a solucao escolhida pelo usuario para o controle de concorrencia entre os trens
   * Retorno: boolean, true se o trem entrou na zona critica 1 e false se o trem nao entrou na zona critica 1
   */
  public boolean entrouNaZonaCritica1(int solucao) {
    switch (solucao) {
      case 0:{// Variavel de travamento para a zona critica 1

        if (ControllerTela2.variavelDeTravamento1==1) {
          // if que verifica se a variavel de travamento da zona critica 1 esta ocupada, caso esteja o trem nao entra na zona critica 1 ate ela ser desocupada, essa variavel eh criada como um recurso compartilhado para controlar a entrada na zona critica 1
          return false;// retorna false caso a variavel de travamento da zona critica 1 esteja ocupada
        }else{
          ControllerTela2.variavelDeTravamento1 = 1;
          // caso a variavel de travamento da zona critica 1 nao esteja ocupada, o trem entra na zona critica 1 e a variavel de travamento eh ocupada
          zonaCritica1=true;// variavel de controle para a zona critica 1, que indica que o trem entrou na zona critica 1
          return true;// retorna true caso a variavel de travamento da zona critica 1 nao esteja ocupada
        }

      }// fim do case 0

      case 1:{//Estrita alternancia para a zona critica 1

        if (ControllerTela2.turno1EA == 1) {
          // if que verifica se o turno da zona critica 1 esta ocupado, caso esteja o trem nao entra na zona critica 1 ate ele ser desocupado, essa variavel eh criada como um recurso compartilhado para controlar a entrada na zona critica 1
          return false;
        }else{
          // caso o turno da zona critica 1 seja do trem 1, o trem entra na zona critica 1 e o turno eh ocupado por ele
          zonaCritica1 = true;
          return true;
        }
      }// fim do case 1

      case 2:{//Peterson para a zona critica 1

        int outroProcesso;// variavel que armazena o processo do trem 2
        outroProcesso = 1 - processo;// Como utilizamos 2 processos um com valor 0 e outro com valor 1, essa variavel eh utilizada para receber e controlar algumas condicoes do outro processo
        ControllerTela2.interesseNaRegiao1[processo] = true;// Define o interesse do processo do trem 1 na regiao critica 1, ou seja o interesse do trem 1 em entrar na zona critica 1
        ControllerTela2.turno1SP = processo;// Define o turno do processo do trem 1 na regiao critica 1, ou seja diz que o turno atual da zona critica 1 eh do trem 1
        if(ControllerTela2.turno1SP == processo && ControllerTela2.interesseNaRegiao1[outroProcesso] == true){
          // if que verifica se o turno da zona critica 1 eh do trem 1 e se o outro processo tem interesse na regiao critica 1, caso ambas as condicoes sejam verdadeiras o trem nao entra na zona critica 1 ate o turno ser desocupado
          return false;
        }else{
          // caso o turno da zona critica 1 seja do trem 1 e o outro processo nao tenha interesse na regiao critica 1, o trem entra na zona critica 1
          zonaCritica1 = true;
          return true;
        }

      }// fim do case 2
      default:
        return true;
    }// fim do switch
  }// fim do metodo entrouNaZonaCritica1

  /*
  * ***************************************************************
  * Metodo: saiuDaZonaCritica1
  * Funcao: Metodo que controla a saida da zona critica 1 de acordo com a solucao 
  * escolhida pelo usuario, incializando os metodos de controle de saida da zona critica 1
  * de acordo com a solucao escolhida podendo ser Variavel de travamento,
  * Estrita alternancia ou Peterson
  * Parametros: int solucao, a solucao escolhida pelo usuario para o controle de concorrencia entre os trens
  * Retorno: nenhum
  */
  public void saiuDaZonaCritica1(int solucao) {
    switch (solucao) {
      case 0:{// Variavel de travamento para a zona critica 1

        ControllerTela2.variavelDeTravamento1 = 0;
        // caso o trem saia da zona critica 1 a variavel de travamento da zona critica 1 eh desocupada
        zonaCritica1 = false;
        break;

      }// fim do case 0

      case 1:{//Estrita alternancia para a zona critica 1

        ControllerTela2.turno1EA = 1;
        // caso o trem saia da zona critica 1 o turno da zona critica 1 se torna do outro trem
        zonaCritica1 = false;
        break;
        
      }// fim do case 1

      case 2:{//Peterson para a zona critica 1

        ControllerTela2.interesseNaRegiao1[processo] = false;
        // caso o trem saia da zona critica 1 o interesse do processo do trem 1 na regiao critica 1 eh desativado afinal ele nao esta mais na zona critica 1
        zonaCritica1 = false;
        break;

      }// fim do case 2

      default:
        break;
    }// fim do switch
  }// fim do metodo saiuDaZonaCritica1

  /*
  * ***************************************************************
  * Metodo: entrouNaZonaCritica2
  * Funcao: Metodo que controla a entrada na zona critica 2 de acordo com a solucao escolhida pelo usuario, incializando os metodos de controle de entrada na zona critica 2 de acordo com a solucao escolhida podendo ser Variavel de travamento, Estrita alternancia ou Peterson
  * Parametros: int solucao, a solucao escolhida pelo usuario para o controle de concorrencia entre os trens
  * Retorno: boolean, true se o trem entrou na zona critica 2 e false se o trem nao entrou na zona critica 2
  */
  public boolean entrouNaZonaCritica2(int solucao) {
    switch (solucao) {
      case 0:{// Variavel de travamento para a zona critica 2

        if (ControllerTela2.variavelDeTravamento2 == 1) {
          // if que verifica se a variavel de travamento da zona critica 2 esta ocupada, caso esteja o trem nao entra na zona critica 2 ate ela ser desocupada, essa variavel eh criada como um recurso compartilhado para controlar a entrada na zona critica 2
          return false;
          
        }else{
          ControllerTela2.variavelDeTravamento2 = 1;
          // caso a variavel de travamento da zona critica 2 nao esteja ocupada, o trem entra na zona critica 2 e a variavel de travamento eh ocupada
          zonaCritica2 = true;
          return true;
        }

      }// fim do case 0

      case 1:{//Estrita alternancia para a zona critica 2

        if (ControllerTela2.turno2EA == 1) {
          // if que verifica se o turno da zona critica 2 esta ocupado, caso esteja o trem nao entra na zona critica 2 ate ele ser desocupado, essa variavel eh criada como um recurso compartilhado para controlar a entrada na zona critica 2
          return false;
        }else{
          zonaCritica2 = true;
          // caso o turno da zona critica 2 seja do trem 2, o trem entra na zona critica 2
          return true;
        }

      }// fim do case 1

      case 2:{//Peterson para a zona critica 2

        int outroProcesso;// variavel que armazena o processo do trem 1
        outroProcesso = 1 - processo;// Como utilizamos 2 processos um com valor 0 e outro com valor 1, essa variavel eh utilizada para receber e controlar algumas condicoes do outro processo
        ControllerTela2.interesseNaRegiao2[processo] = true;// Define o interesse do processo do trem 1 na regiao critica 2, ou seja o interesse do trem 1 em entrar na zona critica 2
        ControllerTela2.turno2SP = processo;// Define o turno do processo do trem 1 na regiao critica 2, ou seja diz que o turno atual da zona critica 2 eh do trem 1
        if(ControllerTela2.turno2SP == processo && ControllerTela2.interesseNaRegiao2[outroProcesso] == true){
          // if que verifica se o turno da zona critica 2 eh do trem 1 e se o outro processo tem interesse na regiao critica 2, caso ambas as condicoes sejam verdadeiras o trem nao entra na zona critica 2 ate o turno ser desocupado
          return false;
        }else{
          // caso o turno da zona critica 2 seja do trem 1 e o outro processo nao tenha interesse na regiao critica 2, o trem entra na zona critica 2
          zonaCritica2 = true;
          return true;
        }

      }// fim do case 2
    
      default:
        return true;
    }// fim do switch
  }// fim do metodo entrouNaZonaCritica2


  /* ***************************************************************
  * Metodo: saiuDaZonaCritica2
  * Funcao: Metodo que controla a saida da zona critica 2 de acordo com a solucao
  * escolhida pelo usuario, incializando os metodos de controle de saida da zona critica 2
  * de acordo com a solucao escolhida podendo ser 
  * Variavel de travamento , Estrita alternancia ou Peterson
  * Parametros: int solucao, a solucao escolhida pelo usuario para o controle de concorrencia entre os trens
  * Retorno: nenhum
  */
  public void saiuDaZonaCritica2(int solucao) {
    switch (solucao) {
      case 0:{// Variavel de travamento para a zona critica 2

        ControllerTela2.variavelDeTravamento2 = 0;
        // caso o trem saia da zona critica 2 a variavel de travamento da zona critica 2 eh desocupada
        zonaCritica2 = false;
        break;

      }// fim do case 0

      case 1:{//Estrita alternancia para a zona critica 2

        ControllerTela2.turno2EA = 1;
        // caso o trem saia da zona critica 2 o turno da zona critica 2 se torna do outro trem
        zonaCritica2 = false;
        break;

      }// fim do case 1

      case 2:{//Peterson para a zona critica 2

        ControllerTela2.interesseNaRegiao2[processo] = false;
        // caso o trem saia da zona critica 2 o interesse do processo do trem 1 na regiao critica 2 eh desativado afinal ele nao esta mais na zona critica 2
        zonaCritica2 = false;
        break;

      }// fim do case 2
      default:
        break;
    }// fim do switch
  }// fim do metodo saiuDaZonaCritica2

  /* ***************************************************************
  * Metodo: resetPosicao
  * Funcao: Metodo que reseta a posicao do trem 1 na tela de acordo com a posicao inicial do trem 1
  * Parametros: nenhum
  * Retorno: nenhum
  */
  public void resetPosicao() {
    Platform.runLater(() -> {
      switch (this.posicaoInicial) {
        case 0:
          this.imageView.setLayoutY(-25);
          this.imageView.setLayoutX(253);
          this.imageView.setRotate(270);
          break;
        case 2:
          this.imageView.setLayoutY(800);
          this.imageView.setLayoutX(246);
          this.imageView.setRotate(90);
          break;
        default:
        break;
    }// fim do switch
    });// fim do Platform.runLater
  }// fim do metodo resetPosicao
  
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
