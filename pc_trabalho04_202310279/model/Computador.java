/********************************************************************
* Autor: Marcus César Santos Pereira
* Matricula........: 202310279
* Inicio: 03/06/2024
* Ultima alteracao: 05/06/2024
* Nome: Computador.java
* Funcao: Ler os codigos presentes no banco de dados (leitura) e compila os codigod(usa dado lido)
********************************************************************/

package model;

import java.lang.Thread;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import control.PrincipalController;

@SuppressWarnings("unused")
public class Computador extends Thread {

  //Atributos da classe, contendo o controller, o id do computador, os sliders de velocidade de leitura e compilacao e a barra de progresso
  private PrincipalController controller;
  private int id;
  public boolean codigoLido = false;
  public boolean podeLer = true;
  private Slider lendoSlider;
  private Slider compilandoSlider;
  private ProgressBar progressBar;
  private boolean reset = false;
  private boolean isPaused = false;

  //Construtor da classe, que recebe o controller, o id, os sliders de velocidade de leitura e compilacao e a barra de progresso
  public Computador(PrincipalController controller, int id, Slider lendoSlider, Slider compilandoSlider, ProgressBar progressBar) {
    this.controller = controller;
    this.id = id;
    this.lendoSlider = lendoSlider;
    this.compilandoSlider = compilandoSlider;
    this.progressBar = progressBar;
  }

  /* ***************************************************************
  * Metodo: setIsPaused
  * Funcao: Metodo que define se o computador esta pausado ou nao
  * Parametros: boolean isPaused, que define se o computador esta pausado ou nao
  * Retorno: void
  */
  public  void setIsPaused(boolean isPaused) {
    this.isPaused = isPaused;
  }

  /* ***************************************************************
  * Metodo: getIsPaused
  * Funcao: Metodo que retorna se o computador esta pausado ou nao
  * Parametros: nenhum
  * Retorno: boolean, que define se o computador esta pausado ou nao
  */
  public boolean getIsPaused() {
    return isPaused;
  }

  /* ***************************************************************
  * Metodo: setReset
  * Funcao: Metodo que define se o computador deve ser resetado
  * Parametros: boolean reset, que define se o computador deve ser resetado
  * Retorno: void
  */
  public void setReset(boolean reset) {
    this.reset = reset;
  }

  /* ***************************************************************
  * Metodo: getSpeed
  * Funcao: Metodo que retorna a velocidade do computador
  * Parametros: Slider slider, que define o slider de velocidade do computador
  * Retorno: int, a velocidade do computador
  */
  public int getSpeed(Slider slider) {
    double speedAux = slider.getValue();
    int speedComputador = (int) speedAux;
    return speedComputador * 50;
  }

  //Metodo run da thread sendo sobreescrito
  @Override
   /*
   * ***************************************************************
   * Metodo: run
   * Funcao: Metodo que e chamado quando a thread e iniciada, realiza todo o processo de leitura e compilacao do computador
   * Parametros: nenhum
   * Retorno: nenhum
   */
  public void run() {
    while (true) {
      try {
        //tempo de raciocinio determinado pelo slider
        Thread.sleep(getSpeed(lendoSlider));

        //Varios desses apareceram durante o codigo, eles simbolizam que enquanto o computador estiver pausado, ele fica esperando
        while (isPaused && !reset) {
          Thread.sleep(1);
        }
        //Varios desses apareceram durante o codigo, eles simbolizam que se o computador for resetado, ele para a execucao ate ser reiniciado novamente
        if (reset) {
          break;
        }

        //Controla a label de status do leitor, caso ele esteja lendo ou aguardando o semaforo ser liberado para ler
        Platform.runLater(() -> controller.mudarLabelStatusComputador(id,0));
        //Entra no semaforo mutex para realizar uma operacao atomica para controlar o numero de leitores
        PrincipalController.mutex.acquire();
        //Incrementa o numero de leitores lendo
        PrincipalController.leitores++;
        //Se for o primeiro leitor, ele entra no semaforo do banco de dados
        if (PrincipalController.leitores == 1) {
          //Entra no semaforo do banco de dados para ler
          PrincipalController.db.acquire();
        }
        //Sai do semaforo mutex que indica que a operacao atomica foi realizada
        PrincipalController.mutex.release();
        //Agora o leitor esta lendo, entao a label de status do leitor e alterada para lendo
        Platform.runLater(() -> controller.mudarLabelStatusComputador(id,1));

        while (isPaused && !reset) {
          Thread.sleep(1);
        }
        if (reset) {
          break;
        }

        //Se o computador pode ler e ainda nao leu o codigo
        if(podeLer && !codigoLido){

          while (isPaused && !reset) {
            Thread.sleep(1);
          }
          if (reset) {
            break;
          }

          //Muda o estado do computador para lendo
          Platform.runLater(() -> controller.mudaEstadoComputador(id, true, true));

          //Realiza o processo de leitura por meio de uma barra de progresso
          double value = 1.0 / getSpeed(lendoSlider);
          double scale = Math.pow(10, 4);
          value = Math.round(value * scale) / scale;
          double progress = 0;
          Platform.runLater(()-> progressBar.setVisible(true));
          progressBar.setProgress(0.0);

          //Enquanto a barra de progresso nao chegar a 100%, o computador continua lendo
          while (progressBar.getProgress() <= 1.0) {
            progress += value;
            progressBar.setProgress(progress);
            double time = value * (Math.pow(10, 9));

            if (time < 1000000) {
              Thread.sleep(0, (int) time);
            } else {
              int millis = (int) time / 1000000;
              Thread.sleep(millis, (int) (time - (millis * 1000000)));
            }

            while (isPaused && !reset) {
              Thread.sleep(1);
            }
            if (reset) {
              break;
            }
          }

          if (reset) {
            break;
          }

          //Muda o estado do computador para nao lendo quando a barrade progresso chegar a 100%
          Platform.runLater(() -> controller.mudaEstadoComputador(id, true, false));
          Platform.runLater(() ->progressBar.setVisible(false));

          while (isPaused && !reset) {
            Thread.sleep(1);
          }
          if (reset) {
            break;
          }

          //Le o codigo do banco de dados(apenas simboliza que o codigo foi lido, e caso haja um codigo, ele sera compilado)
          controller.lerCodigoDoDB(id);
          
        }
        
        //Entra no semaforo mutex para realizar uma operacao atomica para controlar o numero de leitores
        PrincipalController.mutex.acquire();
        //Decrementa o numero de leitores lendo
        PrincipalController.leitores--;
        //Se nao houver mais leitores, ele libera o semaforo do banco de dados, para que os programadores possam escrever
        if (PrincipalController.leitores == 0) {
          //Libera o semaforo do banco de dados
          PrincipalController.db.release();
        }
        //Sai do semaforo mutex que indica que a operacao atomica foi realizada
        PrincipalController.mutex.release();

        while (isPaused && !reset) {
          Thread.sleep(1);
        }
        if (reset) {
          break;
        }
        //Se o computador leu algum codigo no banco de dados, ele pode compilar
        if (codigoLido) {

          while (isPaused && !reset) {
            Thread.sleep(1);
          }

          //Muda o estado do computador para compilando
          Platform.runLater(() -> controller.mudaEstadoComputador(id, false, true));
          //Realiza o processo de compilacao por meio de uma barra de progresso
          double value = 1.0 / getSpeed(compilandoSlider);
          double scale = Math.pow(10, 4);
          value = Math.round(value * scale) / scale;
          double progress = 0;
          //Mostra a barra de progresso
          Platform.runLater(() -> progressBar.setVisible(true));
          progressBar.setProgress(0.0);

          //Enquanto a barra de progresso nao chegar a 100%, o computador continua compilando
          while (progressBar.getProgress() <= 1.0) {
            progress += value;
            progressBar.setProgress(progress);
            double time = value * (Math.pow(10, 9));

            if (time < 1000000) {
              Thread.sleep(0, (int) time);
            } 
            else {
              int millis = (int) time / 1000000;
              Thread.sleep(millis, (int) (time - (millis * 1000000)));
            }

            while (isPaused && !reset) {
              Thread.sleep(1);
            }
            if (reset) {
              break;
            }
          }

          if (reset) {
            break;
          }

          //Muda o estado do computador para nao compilando quando a barrade progresso chegar a 100%
          Platform.runLater(() ->controller.mudaEstadoComputador(id, false, false));
          Platform.runLater(() ->progressBar.setVisible(false));

          while (isPaused && !reset) {
            Thread.sleep(1);
          }
          if (reset) {
            break;
          }
          //Compila o codigo lido, se trata de chamar o metodo compilarCodigo do controller, que apenas simboliza que o leitor pode voltar a ler, por meio da variavel podeLer
          Platform.runLater(() -> controller.compilarCodigo(id));
          //Apos compilar o codigo, ele muda a variavel codigoLido para false, para simbolizar o computador que nao tem codigo lido e agora ele pode ler outro codigo
          codigoLido = false;
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
}}
