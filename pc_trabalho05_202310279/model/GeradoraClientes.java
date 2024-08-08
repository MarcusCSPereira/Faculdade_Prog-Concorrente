/********************************************************************
* Autor: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio: 20/06/2024
* Ultima alteracao: 22/06/2024
* Nome: GeradoraClientes.java
* Funcao: Thread para Gerar clientes aleatoriamente na tela para serem atendidos pelos carros
********************************************************************/
package model; // Pacote ao qual a classe pertence

import java.util.Random;

import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

// Classe GeradoraClientes
public class GeradoraClientes extends Thread {
  private ImageView[] imagensClientes;
  private Slider velocidade;
  private boolean reset;
  private int index;

  // Construtor da classe
  public GeradoraClientes(ImageView[] imagensClientes, Slider velocidade, boolean reset) {
    this.velocidade = velocidade;
    this.imagensClientes = imagensClientes;
    this.reset = reset;
  }

  /* ***************************************************************
  * Metodo: setReset
  * Funcao: Metodo que define se o computador deve ser resetado
  * Parametros: boolean reset, que define se o computador deve ser resetado
  * Retorno: void
  ********************************************************************/
  public void setReset(boolean reset) {
    this.reset = reset;
  }

  /* ***************************************************************
  * Metodo: run
  * Funcao: Metodo que incia e possui toda a logica da thread de geracao de clientes
  * Parametros: void
  * Retorno: void
  ********************************************************************/
  @Override
  public void run() {
    try {
      while (true) {
        if (reset) {
          break;
        }
        pause();
        // Cria um cliente baseado em um tempo em segundos calculado pela velocidade do slider
        Thread.sleep((int) (12000 - velocidade.getValue() * 1000));

        if (reset) {
          break;
        }
        pause();
        // Escolhe um index aleatório para um cliente
        do {
          if (isAllVisible()) {
            break;
          }
          index = new Random().nextInt(15);
        } while (imagensClientes[index].isVisible());
        // Mostra o cliente na tela de acordo com o index escolhido
        Platform.runLater(() -> imagensClientes[index].setVisible(true));
        if (reset) {
          break;
        }
        pause();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /* ***************************************************************
  * Metodo: pause
  * Funcao: Metodo que pausa a thread de geracao de clientes caso a velocidade do slider seja 0
  * Parametros: void
  * Retorno: void
  ********************************************************************/
  private void pause() {
    while (velocidade.getValue() == 0) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /* ***************************************************************
  * Metodo: isAllVisible
  * Funcao: Metodo que verifica se todos os clientes ja foram gerados, para evitar que a thread continue rodando sem necessidade
  * Parametros: void
  * Retorno: boolean, que define se todos os clientes ja foram gerados
  ********************************************************************/
  private boolean isAllVisible() {
    if (imagensClientes[0].isVisible() && imagensClientes[1].isVisible() && imagensClientes[2].isVisible()
        && imagensClientes[3].isVisible() && imagensClientes[4].isVisible() && imagensClientes[5].isVisible()
        && imagensClientes[6].isVisible() && imagensClientes[7].isVisible() && imagensClientes[8].isVisible()
        && imagensClientes[9].isVisible() && imagensClientes[10].isVisible() && imagensClientes[11].isVisible()
        && imagensClientes[12].isVisible() && imagensClientes[13].isVisible()
        && imagensClientes[14].isVisible()) {
      return true;
    } else {
      return false;
    }
  }
}
