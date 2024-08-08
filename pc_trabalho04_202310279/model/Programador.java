/********************************************************************
* Autor: Marcus César Santos Pereira
* Matricula........: 202310279
* Inicio: 03/06/2024
* Ultima alteracao: 05/06/2024
* Nome: Programador.java
* Funcao: Raciocina o codigo (obtem dado) e envia o codigo para compilacao (escreve na base de dados)
********************************************************************/

package model;

import java.lang.Thread;
import java.util.concurrent.Semaphore;

import control.PrincipalController;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;

@SuppressWarnings("unused")
public class Programador extends Thread {

  //Atributos da classe, contendo o controller, o id do programador, os sliders de velocidade de raciocinio e escrita e a barra de progresso
  private PrincipalController controller;
  private int id;
  public boolean waiting = false;
  private Slider raciocinandoSlider;
  private Slider escrevendoSlider;
  private ProgressBar progressBar;
  private boolean isPaused = false;
  private boolean reset = false;

  //Construtor da classe, que recebe o controller, o id, os sliders de velocidade de raciocinio e escrita e a barra de progresso
  public Programador(PrincipalController controller, int id, Slider raciocinandoSlider, Slider escrevendoSlider, ProgressBar progressBar) {
    this.controller = controller;
    this.id = id;
    this.raciocinandoSlider = raciocinandoSlider;
    this.escrevendoSlider = escrevendoSlider;
    this.progressBar = progressBar;
  }

  /* ***************************************************************
  * Metodo: setIsPaused
  * Funcao: Metodo que define se o programador esta pausado ou nao
  * Parametros: boolean isPaused, que define se o programador esta pausado ou nao
  * Retorno: void
  */
  public  void setIsPaused(boolean isPaused) {
    this.isPaused = isPaused;
  }

  /* ***************************************************************
  * Metodo: getIsPaused
  * Funcao: Metodo que retorna se o programador esta pausado ou nao
  * Parametros: nenhum
  * Retorno: boolean, que define se o programador esta pausado ou nao
  */
  public boolean getIsPaused() {
    return isPaused;
  }

  /* ***************************************************************
  * Metodo: setReset
  * Funcao: Metodo que define se o programador deve ser resetado
  * Parametros:  boolean reset, que define se o programador deve ser resetado
  * Retorno: void
  */
  public void setReset(boolean reset) {
    this.reset = reset;
  }

  /* ***************************************************************
  * Metodo: getSpeed
  * Funcao: Metodo que retorna a velocidade do programador
  * Parametros: nenhum
  * Retorno: double, a vwelocidade do programador
  */
  public int getSpeed(Slider slider) {
    double speedAux = slider.getValue();
    int speedProgramador = (int) speedAux;
    return speedProgramador * 50;
  }

  //Metodo run da thread sendo sobreescrito
  @Override
   /*
   * ***************************************************************
   * Metodo: run
   * Funcao: Metodo que e chamado quando a thread e iniciada, realiza todo o processo de raciocinio e escrita do programador
   * Parametros: nenhum
   * Retorno: nenhum
   */
  public void run() {
    while (true) {
      try {
        //Tempo de raciocinio determinado pelo slider
        Thread.sleep(getSpeed(raciocinandoSlider));

        //Varios desses apareceram durante o codigo, eles simbolizam que enquanto o programador estiver pausado, ele fica esperando sem poder fazer nada
        while (isPaused && !reset) {
          Thread.sleep(1);
        }
        //Varios desses apareceram durante o codigo, eles simbolizam que se o programador for resetado, ele para a execucao ate ser reiniciado novamente
        if (reset) {
          break;
        }
        
        if(!waiting){

          while (isPaused && !reset) {
            Thread.sleep(1);
          }
          if (reset) {
            break;
          }
          
          //Muda o estado do programador para raciocinando
          Platform.runLater(() -> controller.mudaEstadoProgramador(id, true, true));

          while (isPaused && !reset) {
            Thread.sleep(1);
          }
          if (reset) {
            break;
          }
          
          //Realiza o processo de raciocinio por meio de uma barra de progresso
          double value = 1.0 / getSpeed(raciocinandoSlider);
          double scale = Math.pow(10, 4);
          value = Math.round(value * scale) / scale;
          double progress = 0;

          Platform.runLater(()->progressBar.setVisible(true));
          progressBar.setProgress(0.0);

          //Enquanto a barra de progresso nao chegar a 100%, o programador continua raciocinando
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

          //Muda o estado do programador para nao raciocinando
          Platform.runLater(() -> controller.mudaEstadoProgramador(id, true, false));
          Platform.runLater(() -> progressBar.setVisible(false));
          
          while (isPaused && !reset) {
            Thread.sleep(1);
          }
          if (reset) {
            break;
          }

          //Muda o estado do programador para escrevendo modificando a label do programador para demonstrar que ele esta escrevendo ou aguardando para escrever!
          Platform.runLater(() -> controller.mudarLabelStatusProgramador(id,0));
          //Espera a liberacao do banco de dados por meio de um semaforo compartilhado entre os programadores e compiladores
          PrincipalController.db.acquire();
          Platform.runLater(() -> controller.mudarLabelStatusProgramador(id, 1));

          while (isPaused && !reset) {
            Thread.sleep(1);
          }
          if (reset) {
            break;
          }

          //Muda o estado do programador para escrevendo
          Platform.runLater(() -> controller.mudaEstadoProgramador(id, false, true));
          value = 1.0 / getSpeed(escrevendoSlider);
          scale = Math.pow(10, 4);
          value = Math.round(value * scale) / scale;
          progress = 0;

          //Realiza a demosntracao de escrita por meio de uma barra de progresso
          Platform.runLater(() -> progressBar.setVisible(true));
          progressBar.setProgress(0.0);

          //Enquanto a barra de progresso nao chegar a 100%, o programador continua escrevendo
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

          //Muda o estado do programador para nao escrevendo, quando ele termina de escrever
          Platform.runLater(() -> controller.mudaEstadoProgramador(id, false, false));
          Platform.runLater(()-> progressBar.setVisible(false));
          //Escreve na base de dados o codigo gerado pelo programador
          Platform.runLater(() -> controller.esceverNoDB(id));
          
          //Libera o banco de dados(semaforo) para que os compiladores possam compilar o codigo, ou outro programador possa escrever
          PrincipalController.db.release();

        }
        
        while (isPaused && !reset) {
          Thread.sleep(1);
        }
        if (reset) {
          break;
        }
        
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
