/********************************************************************
* Autor: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio: 20/06/2024
* Ultima alteracao: 22/06/2024
* Nome: Carro.java
* Funcao: Classe que representa um carro e realiza os metodos de movimentacao do carro, buscar cliente e controle de acesso a regioes criticas por meio de semaforos
********************************************************************/
package model;// Pacote ao qual a classe pertence

import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

// Classe Carro
public class Carro extends Thread {

  private int id;// Identificador do carro
  private boolean pausado;// Variavel para controlar a pausa do carro
  private boolean reset;// Variavel para controlar o reset da aplicacao
  private Slider slider;// Slider para controlar a velocidade do carro
  private ImageView imageCarro;// Imagem do carro
  private ImageView imagem_caminho;// Imagem do caminho que o carro percorre
  private String[] comandos;// Comandos que o carro deve executar, possuindo sua movimentacao, clientes que pode buscar e as operaçoes de semaforo
  private ImageView[] clientes;// Imagens dos clientes que o carro pode buscar
  private Semaphore[][] semaforos;// Semaforos para controlar o acesso a regioes criticas
  public static boolean sound = true;// Variavel para controlar o som da buzina

  // Construtor da classe Carro que recebe os parametros necessarios para a criacao do carro
  public Carro(int id, boolean reset, boolean pausado, Slider slider, ImageView imageCarro,ImageView imagem_caminho, ImageView[] clientes, String[] comandos, Semaphore[][] semaforos) {
    this.id = id;
    this.reset = reset;
    this.pausado = pausado;
    this.slider = slider;
    this.imageCarro = imageCarro;
    this.imagem_caminho = imagem_caminho;
    this.clientes = clientes;
    this.comandos = comandos;
    this.semaforos = semaforos;
  }

  /* ***************************************************************
  * Metodo: setPausado
  * Funcao: Metodo set para definir o valor da variavel pausado que define se o carro esta ou nao pausado
  * Parametros: boolean pausado, que eh o valor que vai ser atribuido a variavel pausado
  * Retorno: void
  ********************************************************************/
  public void setPausado(boolean pausado) {
    this.pausado = pausado;
  }

  /* ***************************************************************
  * Metodo: getPausado
  * Funcao: Metodo get para retornar o valor da variavel pausado que define se o carro esta ou nao pausado
  * Parametros: void
  * Retorno: boolean, que eh o valor da variavel pausado
  ********************************************************************/
  public boolean getPausado() {
    return this.pausado;
  }

  /* ***************************************************************
  * Metodo: setReset
  * Funcao: Metodo set para definir o valor da variavel reset que define se a thread carro deve ser resetada ou nao
  * Parametros: boolean reset, que eh o valor que vai ser atribuido a variavel reset
  * Retorno: void
  ********************************************************************/
  public void setReset(boolean reset) {
    this.reset = reset;
  }

  /* ***************************************************************
  * Metodo: getImagemCarro
  * Funcao: Metodo get para retornar a imagem do carro
  * Parametros: void
  * Retorno: ImageView, que eh a imagem do carro
  ********************************************************************/
  public ImageView getImageCarro() {
    return this.imageCarro;
  }

  /* ***************************************************************
  * Metodo: getImagemCaminho
  * Funcao: Metodo get para retornar a imagem do caminho que o carro percorre
  * Parametros: void
  * Retorno: ImageView, que eh a imagem do caminho que o carro percorre
  ********************************************************************/
  public ImageView getImagemCaminho() {
    return this.imagem_caminho;
  }

  /* ***************************************************************
  * Metodo: run
  * Funcao: Metodo que inicia e possui toda a logica da thread do carro, recebendo comandos de movimentacao, busca de clientes e controle de acesso a regioes criticas e agindo de acordo com esses comandos passados
  * Parametros: void
  * Retorno: void
  ********************************************************************/
  @Override
  public void run() {
    while (true) {
      // Se o carro deve ser resetado, a thread eh encerrada, e chama o metodo pause, que caso o carro esteja pausado, pausa a thread, eh importante citar que esses codigos serao frequentemente utilizados ao longo do metodo, portanto nao serao comentados novamente
      if (reset) {
        break;
      }
      pause();

      // Para cada comando passado ao carro, ele executa a acao correspondente, separando os comandos e realizando diferentes acoeos de acordo a caracteristica do comando
      for (String comando : comandos) {
        // Se o comando tiver apenas um caracter, ele realiza a acao correspondente de movimento do carro em uma rua
        if (comando.length() == 1) {
          switch (comando) {
            case "D":
              moverCarroDireita(51);
              break;
            case "E":
              moverCarroEsquerda(51);
              break;
            case "C":
              moverCarroCima(44);
              break;
            case "B":
              moverCarroBaixo(44);
              break;
            default:
              System.out.println("Erro no comando");
              break;
          }
        //Se o comando tinha o caracter U=(UBER), ele realiza a acao correspondente de movimento do carro em uma rua e busca um cliente nessa rua
        } else if (comando.contains("U")) {
          String[] comandosCliente = comando.split("/");// Separa os comandos do cliente
          int idCliente = Integer.parseInt(comandosCliente[2]);// Pega o id do cliente
          // Para cada comando do cliente, ele executa a acao correspondente, realizando o movimento e a busca do cliente
          switch (comandosCliente[0]) {
            case "D":
              moverCarroDireitaBuscandoCliente(51, idCliente);
              break;
            case "E":
              moverCarroEsquerdaBuscandoCliente(51, idCliente);
              break;
            case "C":
              moverCarroCimaBuscandoCliente(44, idCliente);
              break;
            case "B":
              moverCarroBaixoBuscandoCliente(44, idCliente);
              break;
            default:
              System.out.println("Erro no comando com Uber do carro:" + id);
              break;
          }
        // Se o comando tinha o caracter X=(Cruzamento), ele realiza a acao correspondente de movimento do carro para percorrer um cruzamento
        } else if (comando.charAt(0) == 'X') {
          String[] comandosCruzamento = comando.substring(2).split("/");
          for (String comandoCruzamento : comandosCruzamento) {
            switch (comandoCruzamento) {
              case "D":
                moverCarroDireita(45);
                break;
              case "E":
                moverCarroEsquerda(45);
                break;
              case "C":
                moverCarroCima(44);
                break;
              case "B":
                moverCarroBaixo(44);
                break;
              default:
                System.out.println("Erro no cruzamento do carro:" + id);
                break;
            }
          }
        // Se o comando tinha o caracter A=(Acquire) ou R=(Release), ele realiza a acao correspondente de controle de acesso a regioes criticas por meio de semaforos, chamando os metodos acquire e release para adquirir e liberar o semaforo de acordo os valores passados como parametro, que serao o id do semaforo e a zona critica, que vao ser os indices da matriz de semaforos
        } else if (comando.charAt(0) == 'A' || comando.charAt(0) == 'R') {
          String[] comandosParaSemaforo = comando.split("/");
          int idSemaforo = Integer.parseInt(comandosParaSemaforo[1]);
          int zona_critica = Integer.parseInt(comandosParaSemaforo[2]);
          switch (comandosParaSemaforo[0]) {
            case "A":
              if (reset) {
                break;
              }
              // Chama o metodo acquire para adquirir o semaforo caso o caracter seja A
              acquire(idSemaforo, zona_critica);
              if (reset) {
                break;
              }
              break;
            case "R":
              if (reset) {
                break;
              }
              // Chama o metodo release para liberar o semaforo caso o caracter seja R
              release(idSemaforo, zona_critica);
              if (reset) {
                break;
              }
              break;
            default:
              System.out.println("Erro no semaforo do carro:" + id);
              break;
          }
        }
      }
      if (reset) {
        break;
      }
      pause();
    }
  }

  /* ***************************************************************
  * Metodo: acquire
  * Funcao: Metodo que da DOWN um semaforo de acordo com o id do semaforo e a zona critica passados como parametro
  * Parametros: int idSemaforo, que eh o id do semaforo, int zona_critica, que eh o numero que indica qual zona critica necessita desse DOWN
  * Retorno: void
  ********************************************************************/
  private void acquire(int idSemaforo, int zona_critica) {
    pause();
    try {
      semaforos[idSemaforo][zona_critica].acquire();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    pause();
  }

  /* ***************************************************************
  * Metodo: release
  * Funcao: Metodo que da UP um semaforo de acordo com o id do semaforo e a zona critica passados como parametro
  * Parametros: int idSemaforo, que eh o id do semaforo, int zona_critica, que eh o numero que indica qual zona critica necessita desse UP
  * Retorno: void
  ********************************************************************/
  private void release(int idSemaforo, int zona_critica) {
    pause();
    semaforos[idSemaforo][zona_critica].release();
    pause();
  }

  /* ***************************************************************
  * Metodo: moverCarroDireita
  * Funcao: Metodo que move o carro para a direita de acordo com a quantidade de pixels passados como parametro
  * Parametros: int pixels, que eh a quantidade de pixels que o carro vai se mover
  * Retorno: void
  ********************************************************************/
  private void moverCarroDireita(int pixels) {
    for (int i = 0; i < pixels; i++) {

      if (reset) {
        break;
      }
      pause();

      Platform.runLater(() -> this.getImageCarro().setRotate(0));// Rotaciona a imagem do carro para a direita
      Platform.runLater(() -> this.getImageCarro().setLayoutX(this.getImageCarro().getLayoutX() + 1));// Move o carro para a direita

      try {
        // Pega a velocidade do slider e calcula o tempo de sleep de acordo com a velocidade, para assim simular a movimentacao do carro de acordo a velocidade escolhida pelo usuario
        int velocidade = (int) slider.getValue();
        sleep(33 - (velocidade * 5));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /* ***************************************************************
  * Metodo: moverCarroEsquerda
  * Funcao: Metodo que move o carro para a esquerda de acordo com a quantidade de pixels passados como parametro
  * Parametros: int pixels, que eh a quantidade de pixels que o carro vai se mover
  * Retorno: void
  ********************************************************************/
  private void moverCarroEsquerda(int pixels) {
    for (int i = 0; i < pixels; i++) {

      if (reset) {
        break;
      }
      pause();

      Platform.runLater(() -> this.getImageCarro().setRotate(180));// Rotaciona a imagem do carro para a esquerda
      Platform.runLater(() -> this.getImageCarro().setLayoutX(this.getImageCarro().getLayoutX() - 1));// Move o carro para a esquerda

      try {
        int velocidade = (int) slider.getValue();
        sleep(33 - (velocidade * 5));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /* ***************************************************************
  * Metodo: moverCarroCima
  * Funcao: Metodo que move o carro para cima de acordo com a quantidade de pixels passados como parametro
  * Parametros: int pixels, que eh a quantidade de pixels que o carro vai se mover
  * Retorno: void
  ********************************************************************/
  private void moverCarroCima(int pixels) {
    for (int i = 0; i < pixels; i++) {

      if (reset) {
        break;
      }
      pause();

      Platform.runLater(() -> this.getImageCarro().setRotate(270));// Rotaciona a imagem do carro para cima
      Platform.runLater(() -> this.getImageCarro().setLayoutY(this.getImageCarro().getLayoutY() - 1));// Move o carro para cima

      try {
        int velocidade = (int) slider.getValue();
        sleep(33 - (velocidade * 5));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /* ***************************************************************
  * Metodo: moverCarroBaixo
  * Funcao: Metodo que move o carro para baixo de acordo com a quantidade de pixels passados como parametro
  * Parametros: int pixels, que eh a quantidade de pixels que o carro vai se mover
  * Retorno: void
  ********************************************************************/
  private void moverCarroBaixo(int pixels) {
    for (int i = 0; i < pixels; i++) {

      if (reset) {
        break;
      }
      pause();

      Platform.runLater(() -> this.getImageCarro().setRotate(90));// Rotaciona a imagem do carro para baixo
      Platform.runLater(() -> this.getImageCarro().setLayoutY(this.getImageCarro().getLayoutY() + 1));// Move o carro para baixo

      try {
        int velocidade = (int) slider.getValue();
        sleep(33 - (velocidade * 5));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /* ***************************************************************
  * Metodo: moverCarroDireitaBuscandoCliente
  * Funcao: Metodo que move o carro para a direita de acordo com a quantidade de pixels passados como parametro e busca um cliente de acordo com o id do cliente passado como parametro
  * Parametros: int pixels, que eh a quantidade de pixels que o carro vai se mover, int idCliente, que eh o id do cliente que o carro vai buscar
  * Retorno: void
  ********************************************************************/
  private void moverCarroDireitaBuscandoCliente(int pixels, int idCliente) {
    for (int i = 0; i < pixels; i++) {

      if (reset) {
        break;
      }
      pause();

      Platform.runLater(() -> this.getImageCarro().setRotate(0));// Rotaciona a imagem do carro para a direita
      Platform.runLater(() -> this.getImageCarro().setLayoutX(this.getImageCarro().getLayoutX() + 1));// Move o carro para a direita

      //Quando o for estiver na metade do caminho, ele toca o som de buzina e esconde a imagem do cliente sinalizando que o cliente foi pego
      if (i == 25 && clientes[idCliente].isVisible()) {
        tocarSomBuzina();
        Platform.runLater(() -> clientes[idCliente].setVisible(false));
      }

      try {
        int velocidade = (int) slider.getValue();
        sleep(33 - (velocidade * 5));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /* ***************************************************************
  * Metodo: moverCarroEsquerdaBuscandoCliente
  * Funcao: Metodo que move o carro para a esquerda de acordo com a quantidade de pixels passados como parametro e busca um cliente de acordo com o id do cliente passado como parametro
  * Parametros: int pixels, que eh a quantidade de pixels que o carro vai se mover, int idCliente, que eh o id do cliente que o carro vai buscar
  * Retorno: void
  ********************************************************************/
  private void moverCarroEsquerdaBuscandoCliente(int pixels, int idCliente) {
    for (int i = 0; i < pixels; i++) {
      if (reset) {
        break;
      }
      pause();
      Platform.runLater(() -> this.getImageCarro().setRotate(180));// Rotaciona a imagem do carro para a esquerda
      Platform.runLater(() -> this.getImageCarro().setLayoutX(this.getImageCarro().getLayoutX() - 1));// Move o carro para a esquerda
      //Quando o for estiver na metade do caminho, ele toca o som de buzina e esconde a imagem do cliente sinalizando que o cliente foi pego
      if (i == 25 && clientes[idCliente].isVisible()) {
        tocarSomBuzina();
        Platform.runLater(() -> clientes[idCliente].setVisible(false));
      }
      try {
        int velocidade = (int) slider.getValue();
        sleep(33 - (velocidade * 5));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /* ***************************************************************
  * Metodo: moverCarroCimaBuscandoCliente
  * Funcao: Metodo que move o carro para cima de acordo com a quantidade de pixels passados como parametro e busca um cliente de acordo com o id do cliente passado como parametro
  * Parametros: int pixels, que eh a quantidade de pixels que o carro vai se mover, int idCliente, que eh o id do cliente que o carro vai buscar
  * Retorno: void
  ********************************************************************/
  private void moverCarroCimaBuscandoCliente(int pixels, int idCliente) {
    for (int i = 0; i < pixels; i++) {
      if (reset) {
        break;
      }
      pause();
      Platform.runLater(() -> this.getImageCarro().setRotate(270));// Rotaciona a imagem do carro para cima
      Platform.runLater(() -> this.getImageCarro().setLayoutY(this.getImageCarro().getLayoutY() - 1));// Move o carro para cima
      //Quando o for estiver na metade do caminho, ele toca o som de buzina e esconde a imagem do cliente sinalizando que o cliente foi pego
      if (i == 25 && clientes[idCliente].isVisible()) {
        tocarSomBuzina();
        Platform.runLater(() -> clientes[idCliente].setVisible(false));
      }
      try {
        int velocidade = (int) slider.getValue();
        sleep(33 - (velocidade * 5));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /* ***************************************************************
  * Metodo: moverCarroBaixoBuscandoCliente
  * Funcao: Metodo que move o carro para baixo de acordo com a quantidade de pixels passados como parametro e busca um cliente de acordo com o id do cliente passado como parametro
  * Parametros: int pixels, que eh a quantidade de pixels que o carro vai se mover, int idCliente, que eh o id do cliente que o carro vai buscar
  * Retorno: void
  ********************************************************************/
  private void moverCarroBaixoBuscandoCliente(int pixels, int idCliente) {
    for (int i = 0; i < pixels; i++) {
      if (reset) {
        break;
      }
      pause();
      Platform.runLater(() -> this.getImageCarro().setRotate(90));// Rotaciona a imagem do carro para baixo
      Platform.runLater(() -> this.getImageCarro().setLayoutY(this.getImageCarro().getLayoutY() + 1));// Move o carro para baixo
      //Quando o for estiver na metade do caminho, ele toca o som de buzina e esconde a imagem do cliente sinalizando que o cliente foi pego
      if (i == 25 && clientes[idCliente].isVisible()) {
        tocarSomBuzina();
        Platform.runLater(() -> clientes[idCliente].setVisible(false));
      }
      try {
        int velocidade = (int) slider.getValue();
        sleep(33 - (velocidade * 5));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /* ***************************************************************
  * Metodo: tocarSomBuzina
  * Funcao: Metodo que toca o som da buzina do carro
  * Parametros: void
  * Retorno: void
  ********************************************************************/
  private void tocarSomBuzina() {
    String path = getClass().getResource("../assets/buzina.mp3").toString();// Caminho do arquivo de som da buzina
    AudioClip buzina = new AudioClip(path);// Cria um novo AudioClip com o caminho do arquivo de som da buzina
    // Se a variavel sound for verdadeira, ele toca o som da buzina
    if (sound) {
      buzina.setVolume(0.3);// Define o volume do som da buzina como mais baixo para nao ser muito alto
      buzina.play();// Toca o som da buzina
    }
  }

  /* ***************************************************************
  * Metodo: pause
  * Funcao: Metodo que pausa a thread do carro caso a variavel pausado seja verdadeira
  * Parametros: void
  * Retorno: void
  ********************************************************************/
  private void pause() {
    // Enquanto a variavel pausado for verdadeira, a thread do carro eh pausada executando um loop ocioso
    while (pausado) {
      try {
        sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
