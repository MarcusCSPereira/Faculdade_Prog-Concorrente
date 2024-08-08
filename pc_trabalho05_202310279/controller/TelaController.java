/********************************************************************
* Autor: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio: 20/06/2024
* Ultima alteracao: 22/06/2024
* Nome: TelaController.java
* Funcao: Controlar a tela do programa possuindo todos os metodos de interacao com a interface grafica e a criacao das threads dos carros e a thread dos clientes, alem de possuir os comandos que serao passados para os carros e a logica de pause e reset
********************************************************************/
package controller;// Pacote ao qual a classe pertence

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Carro;
import model.GeradoraClientes;

// Classe TelaController implementa a interface Initializable que eh necessaria para inicializar a interface grafica e seus objetos e threads
public class TelaController implements Initializable {

  // Injecao FXML de dependencias dos elementos da interface grafica que serao utilizados na classe
  //Imagens dos carros
  @FXML
  private ImageView imageCarro0;
  @FXML
  private ImageView imageCarro1;
  @FXML
  private ImageView imageCarro2;
  @FXML
  private ImageView imageCarro3;
  @FXML
  private ImageView imageCarro4;
  @FXML
  private ImageView imageCarro5;
  @FXML
  private ImageView imageCarro6;
  @FXML
  private ImageView imageCarro7;
  //imagens de botoes para fechar e resetar o programa
  @FXML
  private ImageView close_btt;
  @FXML
  private ImageView reset_btt;
  //imagens de botoes para mostrar o caminho dos carros
  @FXML
  private ImageView gps_carro0;
  @FXML
  private ImageView gps_carro1;
  @FXML
  private ImageView gps_carro2;
  @FXML
  private ImageView gps_carro3;
  @FXML
  private ImageView gps_carro4;
  @FXML
  private ImageView gps_carro5;
  @FXML
  private ImageView gps_carro6;
  @FXML
  private ImageView gps_carro7;
  //imagens de caminho dos carros
  @FXML
  private ImageView caminho_carro0;
  @FXML
  private ImageView caminho_carro1;
  @FXML
  private ImageView caminho_carro2;
  @FXML
  private ImageView caminho_carro3;
  @FXML
  private ImageView caminho_carro4;
  @FXML
  private ImageView caminho_carro5;
  @FXML
  private ImageView caminho_carro6;
  @FXML
  private ImageView caminho_carro7;
  //imagens dos botoes de pause dos carros
  @FXML
  private ImageView pause_carro0;
  @FXML
  private ImageView pause_carro1;
  @FXML
  private ImageView pause_carro2;
  @FXML
  private ImageView pause_carro3;
  @FXML
  private ImageView pause_carro4;
  @FXML
  private ImageView pause_carro5;
  @FXML
  private ImageView pause_carro6;
  @FXML
  private ImageView pause_carro7;
  //imagens dos clientes
  @FXML 
  private ImageView cliente0;
  @FXML 
  private ImageView cliente1;
  @FXML 
  private ImageView cliente2;
  @FXML 
  private ImageView cliente3;
  @FXML 
  private ImageView cliente4;
  @FXML 
  private ImageView cliente5;
  @FXML 
  private ImageView cliente6;
  @FXML 
  private ImageView cliente7;
  @FXML 
  private ImageView cliente8;
  @FXML 
  private ImageView cliente9;
  @FXML 
  private ImageView cliente10;
  @FXML 
  private ImageView cliente11;
  @FXML 
  private ImageView cliente12;
  @FXML 
  private ImageView cliente13;
  @FXML 
  private ImageView cliente14;
  //imagem do botao de controle de som da buzina
  @FXML
  private ImageView sound_Image;
  //sliders de controle de velocidade dos carros e da velocidade de geracao de clientes
  @FXML
  private Slider slider_carro0;
  @FXML
  private Slider slider_carro1;
  @FXML
  private Slider slider_carro2;
  @FXML
  private Slider slider_carro3;
  @FXML
  private Slider slider_carro4;
  @FXML
  private Slider slider_carro5;
  @FXML
  private Slider slider_carro6;
  @FXML
  private Slider slider_carro7;
  @FXML
  private Slider slider_geradoraClientes;

  // Declaracao das Threads dos carros
  private Carro carro0;
  private Carro carro1;
  private Carro carro2;
  private Carro carro3;
  private Carro carro4;
  private Carro carro5;
  private Carro carro6;
  private Carro carro7;
  // Array de carros para melhor manipulacao dos mesmos
  Carro carros[];
  
  // Array das imagens dos clientes
  ImageView clientes[];
  // Declaracao da Thread da geradora de clientes
  GeradoraClientes geradoraClientes;

  //O tamanho da matriz de semaforos eh 28(que eh o numero de possibilidades que temos de formar pares de carros sem repetir), por 10(que representa as zonas criticas distintas entre 2 carros, ou seja, cada carro pode ter ate 10 zonas criticas distintas com o outro carro do seu par)
  Semaphore semaforo[][] = new Semaphore[28][10];

  //X significa percorrer o cruzamento: X/direcao que esta vindo/direcao que esta indo
  //D | C | B | E significa percorrer a rua em si:  Direita | Cima | Baixo | Esquerda
  //D | C | B | E seguido de /U significa pegar um cliente, ou seja colocar a imagem de pedido de corrida como false: Direção/U/numero do id da imagem do cliente que foi pego

  //A significa dar Acquire no semaforo: A/id do semaforo/zona critica
  //R significa dar Release no semaforo: R/id do semaforo/zona critica

  //id semaforo 0 = carro 0 com carro 1
  //id semaforo 1 = carro 0 com carro 2
  //id semaforo 2 = carro 0 com carro 3
  //id semaforo 3 = carro 0 com carro 4
  //id semaforo 4 = carro 0 com carro 5
  //id semaforo 5 = carro 0 com carro 6
  //id semaforo 6 = carro 0 com carro 7
  //id semaforo 7 = carro 1 com carro 2
  //id semaforo 8 = carro 1 com carro 3
  //id semaforo 9 = carro 1 com carro 4
  //id semaforo 10 = carro 1 com carro 5
  //id semaforo 11 = carro 1 com carro 6
  //id semaforo 12 = carro 1 com carro 7
  //id semaforo 13 = carro 2 com carro 3
  //id semaforo 14 = carro 2 com carro 4  
  //id semaforo 15 = carro 2 com carro 5
  //id semaforo 16 = carro 2 com carro 6
  //id semaforo 17 = carro 2 com carro 7
  //id semaforo 18 = carro 3 com carro 4
  //id semaforo 19 = carro 3 com carro 5
  //id semaforo 20 = carro 3 com carro 6
  //id semaforo 21 = carro 3 com carro 7
  //id semaforo 22 = carro 4 com carro 5
  //id semaforo 23 = carro 4 com carro 6
  //id semaforo 24 = carro 4 com carro 7
  //id semaforo 25 = carro 5 com carro 6
  //id semaforo 26 = carro 5 com carro 7
  //id semaforo 27 = carro 6 com carro 7

  //Dica: Para entender melhor sobre os semaforos e zonas criticas, voce deve fazer um mapeamento e estudo de caso, analise o codigo e o seu estudo e veja como os carros se comportam, eh importante entender o caminho de cada carro com todos separadamente de 2 em 2 e integrado com mais carros para entender o que causa e como evitar a ocorrencia de deadlocks, dependendo da situacao voce entende que a zona critica deve comecar anteriormente ou terminar posteriormente, ou seja, voce deve analisar o comportamento de cada par de carros e a interacao entre eles como um todo.

  //Comandos dos carros para percorrer o seu caminho
  String[] comandosCarro0={"C","A/5/0","X/C/D","D/U/4","A/6/0","A/0/0","A/2/0","A/4/3","X/D/D","R/4/3","R/2/0","D/U/2","A/3/0","X/D/D","R/3/0","R/0/0","R/6/0", "D", "X/D/D", "D","A/0/1","A/6/1","A/4/1","X/D/C","C","A/4/2","A/6/2", "X/C/E","R/4/1","R/6/1", "E", "X/E/E", "E","A/3/1","X/E/E","R/6/2","R/0/1", "E","A/0/2","A/2/1", "X/E/E","R/5/0","R/4/2", "E/U/1", "X/E/E", "E","A/3/2","A/2/2", "X/E/B","R/2/1","R/3/1", "B","A/5/3","X/B/B", "B","A/1/0","X/B/B", "B","A/3/3","A/6/3","X/B/D","R/1/0","R/5/3","R/0/2","R/2/2","R/3/2", "D/U/6","A/0/3","X/D/D","R/6/3","D","A/4/5","A/2/3","X/D/D","R/2/3","R/0/3", "D","A/0/4","A/6/4","X/D/D","R/3/3", "D", "X/D/B","R/6/4","R/4/5","B","A/1/1","A/5/4","X/B/E","R/0/4","E/U/11","A/6/5","X/E/E","R/5/4","E","A/5/5","A/2/4","X/E/E","R/2/4","R/6/5","E/U/12","A/0/5","X/E/E","R/0/5","E","A/0/6","A/6/6","A/2/5","X/E/B","R/5/5","R/1/1","B","A/6/7","A/2/6","X/B/D","R/2/5","R/6/6","D","X/D/D","R/0/6","D","A/5/6","X/D/D","R/2/6","R/6/7", "D/U/13", "X/D/D","R/5/6", "D","A/0/7", "X/D/D", "D","A/1/2","X/D/C","C","A/5/7", "X/C/C", "C","A/4/0","X/C/C","C","A/6/8","X/C/E","R/4/0","R/5/7","R/0/7","E", "X/E/E","R/6/8","E","A/0/8","A/3/4","X/E/E","R/3/4","E","A/6/9","A/4/4","A/2/7","X/E/E","R/2/7","R/4/4","R/0/8","E","X/E/C","R/6/9","R/1/2"};

  String[] comandosCarro1={"C","A/11/0","A/7/3","A/0/5","X/C/C","R/0/5","R/7/3","R/11/0","C/U/9","A/9/3","A/0/3","A/12/0","X/C/D","R/12/0", "D","A/10/3","A/8/0","X/D/C","R/0/3","R/9/3", "C/U/7","A/7/0","A/0/8","A/12/6","X/C/D","R/12/6","R/8/0","R/10/3", "D","A/9/4","X/D/B","R/0/8","R/7/0", "B","A/0/4","A/12/3","A/10/4","X/B/D","R/9/4", "D", "X/D/B","R/10/4","R/12/3", "B","A/11/5","A/7/2","X/B/B","R/7/2","R/11/5","R/0/4", "B/U/14","A/0/7","X/B/D", "D", "X/D/C", "C","A/11/4","A/7/1", "X/C/C","C","A/10/0", "X/C/C","C","A/12/4", "X/C/C","R/7/1","R/0/7","C/U/5","A/11/3","A/0/1","X/C/C","R/11/4","C","A/12/5","A/10/1", "X/C/E","R/12/4","R/10/0", "E", "X/E/E","A/9/0","E","A/12/7","X/E/B","R/10/1","R/0/1","R/12/5","B/U/3","A/0/0","X/B/E","R/9/0","R/11/3","E/U/2","A/8/1","A/10/2","A/11/2","X/E/C","R/0/0","R/12/7","C","A/0/2","A/9/1","A/8/2", "X/C/E","R/11/2","R/8/1","R/10/2","E/U/1", "X/E/E", "E","A/9/2","A/8/3", "X/E/B","R/8/2","R/9/1", "B","A/11/1", "X/B/B", "B","A/7/4", "X/B/B", "B","A/12/1", "X/B/B","R/0/2","R/9/2","B","A/0/6", "X/B/B","R/7/4","R/11/1", "B","A/12/2","A/8/4","X/B/D","R/12/1","R/8/3", "D", "X/D/C","R/8/4","R/12/2","R/0/6"};

  String[] comandosCarro2={"D","A/1/2","A/17/3","X/D/D","D","A/7/0","A/15/0","A/13/0","X/D/D","R/13/0","R/15/0","R/17/3","D","A/14/0","X/D/D","R/14/0","R/7/0","D","A/17/2","X/D/D","D","A/16/2","A/7/1","A/15/1","X/D/B","R/17/2","B", "X/B/B","R/15/1", "B","A/16/3","X/B/E","R/7/1","R/16/2","R/1/2", "E","A/1/1","A/7/2", "X/E/E","R/7/2", "E/U/11","A/17/1","X/E/E","R/16/3", "E","A/16/0","A/13/1","X/E/E","R/13/1","R/17/1", "E/U/12","A/7/3","X/E/E","R/7/3","A/14/1","E","A/16/1","A/13/2","A/7/4","A/17/0","X/E/C","R/16/0","R/1/1", "C","A/1/0","X/C/C","R/17/0","C", "X/C/D","R/1/0","R/14/1","R/7/4","R/13/2","R/16/1"};

  String[] comandosCarro3={"C/U/10","A/19/0","A/8/0","A/18/0","A/2/3","X/C/C","R/2/3","R/18/0","A/20/2","C/U/7","A/21/0","A/13/0","A/2/7","X/C/C","R/2/7","R/13/0","R/8/0","A/8/1","C","A/2/0","X/C/C","R/2/0","R/21/0","C","A/18/1","A/8/2","A/2/1", "X/C/E","R/8/1","R/20/2","R/19/0","E/U/1","X/E/E","E","A/18/2","A/8/3","A/2/2","X/E/B","R/2/1","R/8/2","R/18/1","B","A/20/1","X/B/B","B","A/13/2","X/B/B","B","A/21/1","X/B/B","R/2/2","R/18/2","B","A/2/5","X/B/B","R/13/2","R/20/1","B","A/21/2","A/2/6","A/8/4","X/B/D","R/2/5","R/21/1","R/8/3","D","X/D/D","R/8/4","D","A/21/3","A/20/0", "X/D/C","R/2/6","R/21/2", "C","A/13/1","A/2/4","X/C/C","R/2/4","R/13/1","R/20/0","R/21/3"};

  String[] comandosCarro4={"A/23/1","C","A/24/2","A/9/0","A/3/0","X/C/C","R/3/0","C/U/3","A/3/1","A/22/0","X/C/E","R/9/0","R/24/2", "E","A/18/1","A/9/1", "X/E/E","R/22/0","R/23/1","E/U/1","X/E/E", "E","A/18/2","A/9/2","A/3/2","X/E/B","R/9/1","R/18/1","R/3/1", "B","A/14/1","A/23/0", "X/B/B","B","A/24/0","X/B/B","B","A/3/3","X/B/D","R/14/1","R/23/0","R/3/2","R/9/2","R/18/2", "D/U/6","A/9/3","X/D/D","R/24/0","D","A/22/1","A/18/0","X/D/D","R/18/0","R/9/3", "D","A/9/4","A/24/1","X/D/C","R/24/1","R/22/1","R/3/3", "C","A/14/0","A/3/4","X/C/C","R/3/4","R/14/0","R/9/4"};

  String[] comandosCarro5={"D/U/8","A/10/0","A/25/2","A/15/1","A/4/0","X/D/C", "C","A/26/1","A/25/0","X/C/C","R/4/0","R/15/1", "C/U/5","A/4/1","X/C/C","R/25/2", "C","A/4/2","A/26/2","A/10/1", "X/C/E","R/4/1","R/26/1","R/10/0","E", "X/E/E", "E","A/22/0","X/E/E","R/10/1","R/26/2", "E","A/19/0","A/10/2","X/E/B","R/22/0","R/4/2","B","A/26/3","A/4/3","X/B/B","R/4/3","R/10/2","R/25/0","B","A/10/3","A/15/0","A/4/4","X/B/B","R/4/4","R/15/0","R/26/3","B/U/7","A/4/5","A/22/1","X/B/D","R/10/3","R/19/0","D","A/26/0","A/10/4","X/D/D","R/22/1","D","X/D/D","R/10/4","R/26/0","R/4/5"};

  String[] comandosCarro6={"B","A/5/6","X/B/E", "E/U/13","A/27/0","A/20/0","X/E/C","R/5/6", "C","A/16/0","A/5/5", "X/C/E","R/20/0","R/27/0", "E/U/12","A/11/0","X/E/E","R/11/0", "E","A/20/1","A/11/1","A/16/1","A/27/1", "X/E/C","A/23/0","R/5/5","R/16/0", "C","A/5/3","X/C/C","R/27/1", "C", "X/C/C","R/16/1", "C", "X/C/D","R/5/3","R/23/0","R/20/1","R/11/1", "D/U/0","A/5/0","X/D/D","A/23/1","D/U/4","A/25/0","A/20/2","A/27/3","A/27/2","A/11/2","X/D/C","R/27/2","C","X/C/D","R/20/2","D","R/11/2","A/11/3","X/D/B","R/25/0","B/U/3","X/B/D","R/11/3","R/27/3","R/23/1", "D", "X/D/D", "D","A/11/4","A/25/2","A/27/4", "X/D/B","R/5/0","B/U/5","A/16/2","A/5/7", "X/B/B","R/27/4", "B", "X/B/B","R/25/2", "B","A/16/3", "X/B/E","R/5/7","R/16/2","R/11/4", "E","A/5/4","A/11/5","X/E/E","R/11/5", "E/U/11","A/27/5","X/E/B","R/27/5","R/5/4","R/16/3"};

  String[] comandosCarro7={"A/27/1","A/24/0","B","A/6/3","A/12/0","X/B/E","R/12/0","A/17/0","E/U/6","A/21/1","A/12/1","X/E/B","R/6/3","R/24/0", "B","A/6/6","X/B/B","R/17/0","R/27/1", "B","A/21/2","A/6/7","A/12/2","X/B/D","R/6/6","R/12/1","R/21/1", "D", "X/D/D","R/12/2", "D","A/27/0","A/21/3","R/6/7","X/D/C","R/21/2", "C","A/17/1","A/6/5","X/C/D","R/21/3","R/27/0", "D","A/27/5","X/D/C","R/27/5","R/6/5","R/17/1", "C","A/26/0","A/12/3","A/6/4","A/24/1","X/C/D","R/24/1", "D","A/6/8","X/D/C","R/6/4","R/12/3","R/26/0","A/27/4","C","A/17/2","X/C/D","D","A/26/1","A/12/4","X/D/C","R/6/8","R/17/2", "C/U/5","A/27/3","A/6/1","X/C/C","R/27/4", "C","A/26/2","A/12/5","A/6/2", "X/C/E","R/6/1","R/12/4","R/26/1","E", "X/E/E", "E","A/12/7","A/24/2","X/E/B","R/6/2","R/12/5","R/26/2","B/U/3","A/6/0","X/B/E","R/24/2","R/27/3","E/U/2","A/26/3","A/21/0","A/27/2","X/E/B","R/27/2","R/6/0","R/12/7","B","A/17/3","A/6/9","A/12/6","X/B/E","R/12/6","R/21/0","R/26/3", "E", "X/E/B","R/6/9","R/17/3"};

  /* ***************************************************************
  * Metodo: gps0
  * Funcao: chama o método de mostrar o caminho do carro 0 quando o botao de gps eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void gps0(MouseEvent event) {
    Platform.runLater(() ->mostrarCaminho(0));
  }

  /* ***************************************************************
  * Metodo: gps1
  * Funcao: chama o método de mostrar o caminho do carro 1 quando o botao de gps eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void gps1(MouseEvent event) {
    Platform.runLater(() ->mostrarCaminho(1));
  }

  /* ***************************************************************
  * Metodo: gps2
  * Funcao: chama o método de mostrar o caminho do carro 2 quando o botao de gps eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void gps2(MouseEvent event) {
    Platform.runLater(() ->mostrarCaminho(2));
  }

  /* ***************************************************************
  * Metodo: gps3
  * Funcao: chama o método de mostrar o caminho do carro 3 quando o botao de gps eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void gps3(MouseEvent event) {
    Platform.runLater(() ->mostrarCaminho(3));
  }

  /* ***************************************************************
  * Metodo: gps4
  * Funcao: chama o método de mostrar o caminho do carro 4 quando o botao de gps eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void gps4(MouseEvent event) {
    Platform.runLater(() ->mostrarCaminho(4));
  }

  /* ***************************************************************
  * Metodo: gps5
  * Funcao: chama o método de mostrar o caminho do carro 5 quando o botao de gps eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void gps5(MouseEvent event) {
    Platform.runLater(() ->mostrarCaminho(5));
  }

  /* ***************************************************************
  * Metodo: gps6
  * Funcao: chama o método de mostrar o caminho do carro 6 quando o botao de gps eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void gps6(MouseEvent event) {
    Platform.runLater(() ->mostrarCaminho(6));
  }

  /* ***************************************************************
  * Metodo: gps7
  * Funcao: chama o método de mostrar o caminho do carro 7 quando o botao de gps eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void gps7(MouseEvent event) {
    Platform.runLater(() ->mostrarCaminho(7));
  }

  /* ***************************************************************
  * Metodo: pause0
  * Funcao: chama o método de pausar o carro passando o id do carro em questao quando o botao de pausar eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void pause0(MouseEvent event) {
    pausarCarro(0);
  }

  /* ***************************************************************
  * Metodo: pause1
  * Funcao: chama o método de pausar o carro passando o id do carro em questao quando o botao de pausar eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void pause1(MouseEvent event) {
    pausarCarro(1);
  }
  
  /* ***************************************************************
  * Metodo: pause2
  * Funcao: chama o método de pausar o carro passando o id do carro em questao quando o botao de pausar eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void pause2(MouseEvent event) {
    pausarCarro(2);
  }
  
  /* ***************************************************************
  * Metodo: pause3
  * Funcao: chama o método de pausar o carro passando o id do carro em questao quando o botao de pausar eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void pause3(MouseEvent event) {
    pausarCarro(3);
  }
  
  /* ***************************************************************
  * Metodo: pause4
  * Funcao: chama o método de pausar o carro passando o id do carro em questao quando o botao de pausar eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void pause4(MouseEvent event) {
    pausarCarro(4);
  }
  
  /* ***************************************************************
  * Metodo: pause5
  * Funcao: chama o método de pausar o carro passando o id do carro em questao quando o botao de pausar eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void pause5(MouseEvent event) {
    pausarCarro(5);
  }
  
  /* ***************************************************************
  * Metodo: pause6
  * Funcao: chama o método de pausar o carro passando o id do carro em questao quando o botao de pausar eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void pause6(MouseEvent event) {
    pausarCarro(6);
  }
  
  /* ***************************************************************
  * Metodo: pause7
  * Funcao: chama o método de pausar o carro passando o id do carro em questao quando o botao de pausar eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void pause7(MouseEvent event) {
    pausarCarro(7);
  }

  /* ***************************************************************
  * Metodo: reset
  * Funcao: chama o método de resetar o sistema quando o botao de resetar eh clicado na interface grafica, resetando toda a interface grafica, as threads e os semaforos
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void reset(MouseEvent event) {

    Platform.runLater(() -> {
      pause_carro0.setImage(new Image("/assets/pause_button.png"));
      pause_carro1.setImage(new Image("/assets/pause_button.png"));
      pause_carro2.setImage(new Image("/assets/pause_button.png"));
      pause_carro3.setImage(new Image("/assets/pause_button.png"));
      pause_carro4.setImage(new Image("/assets/pause_button.png"));
      pause_carro5.setImage(new Image("/assets/pause_button.png"));
      pause_carro6.setImage(new Image("/assets/pause_button.png"));
      pause_carro7.setImage(new Image("/assets/pause_button.png"));
      caminho_carro0.setVisible(false);
      caminho_carro1.setVisible(false);
      caminho_carro2.setVisible(false);
      caminho_carro3.setVisible(false);
      caminho_carro4.setVisible(false);
      caminho_carro5.setVisible(false);
      caminho_carro6.setVisible(false);
      caminho_carro7.setVisible(false);
      cliente0.setVisible(false);
      cliente1.setVisible(false);
      cliente2.setVisible(false);
      cliente3.setVisible(false);
      cliente4.setVisible(false);
      cliente5.setVisible(false);
      cliente6.setVisible(false);
      cliente7.setVisible(false);
      cliente8.setVisible(false);
      cliente9.setVisible(false);
      cliente10.setVisible(false);
      cliente11.setVisible(false);
      cliente12.setVisible(false);
      cliente13.setVisible(false);
      cliente14.setVisible(false);
      slider_carro0.setValue(3);
      slider_carro1.setValue(3);
      slider_carro2.setValue(3);
      slider_carro3.setValue(3);
      slider_carro4.setValue(3);
      slider_carro5.setValue(3);
      slider_carro6.setValue(3);
      slider_carro7.setValue(3);
      slider_geradoraClientes.setValue(5);
      resetarImagemCarros();//reseta a imagem dos carros
    });

    //reseta as threads dos carros
    for (int i = 0; i < carros.length; i++) {
      carros[i].setReset(true);
    }

    //reseta a thread da geradora de clientes
    geradoraClientes.setReset(true);

    //reseta os semaforos
    criarSemaforos();

    //reseta os clientes, preenchendo o array de clientes e instanciando uma nova geradora de clientes
    preencherArrayDeClientes();
    criarGeradoraClientes();

    //reseta os carros, preenchendo o array de carros, instanciando novos carros e iniciando as threads
    criarObjetosCarros();
    preencherArrayDeCarros();
    preencherArrayDeClientes();

    //inicia as threads novas instanciadas
    for (int i = 0; i < carros.length; i++) {
      carros[i].start();
    }

    geradoraClientes.start();

    resetSound();

  }

  /* ***************************************************************
  * Metodo: close
  * Funcao: fecha a aplicacao quando o botao de fechar eh clicado na interface grafica
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML
  void close(MouseEvent event) {
    System.exit(0);
  }

  /* ***************************************************************
  * Metodo: controlSound
  * Funcao: controla o som da aplicacao quando o botao de som eh clicado na interface grafica, mudando a imagem do botao e o valor da variavel de controle de som da buzina, caso o som esteja ligado, ele desliga e vice-versa,
  * Parametros: MouseEvent event, evento de clique do mouse feito pelo usuario
  * Retorno: void
  ********************************************************************/
  @FXML 
  void controlSound(MouseEvent event) {
    Image somON = new Image("/assets/sound.png");
    Image somOFF = new Image("/assets/no_sound.png");
    if(Carro.sound) {
      sound_Image.setImage(somOFF);
      Carro.sound = false;
    } else {
      sound_Image.setImage(somON);
      Carro.sound = true;
    }
  }

  /* ***************************************************************
  * Metodo: resetSound
  * Funcao: reseta o som da aplicacao, mudando a imagem do botao de som e ligando o som da buzina
  * Parametros: void
  * Retorno: void
  ********************************************************************/
  private void resetSound(){
    Image somON = new Image("/assets/sound.png");
    sound_Image.setImage(somON);
    Carro.sound = true;
  }

  /* ***************************************************************
  * Metodo: initialize
  * Funcao: metodo de inicializacao da interface grafica, instanciando os semaforos, os clientes, os carros e iniciando as threads
  * Parametros: URL location, ResourceBundle resources
  * Retorno: void
  ********************************************************************/
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    //inicializa os semaforos
    criarSemaforos();

    //cria e preenche os objetos que gerencia os clientes
    clientes = new ImageView[15];
    preencherArrayDeClientes();
    criarGeradoraClientes();

    //cria e inicia as threads dos carros e inicializa a thread de clientes
    criarObjetosCarros();
    carros = new Carro[8];
    preencherArrayDeCarros();

    for (int i = 0; i < carros.length; i++) {
      carros[i].start();
    }

    geradoraClientes.start();
  }

  /* ***************************************************************
  * Metodo: criarSemaforos
  * Funcao: cria os semaforos que controlam a passagem dos carros em cada cruzamento, informando e preenchendo cada posicao e zona critica com um semaforo e seu numero de sinais, ou nulo caso nao haja aquela zona critica entre os carros
  * Parametros: void
  * Retorno: void
  ********************************************************************/
  private void criarSemaforos() {
    //nulo significa que os carros nao possuem zona critica entre si naquela posicao do array
    //Semaforos do carro 0 com o carro 1 possuem id 0:)
    semaforo[0][0] = new Semaphore(1);
    semaforo[0][1] = new Semaphore(1);
    semaforo[0][2] = new Semaphore(1);
    semaforo[0][3] = new Semaphore(1);
    semaforo[0][4] = new Semaphore(1);
    semaforo[0][5] = new Semaphore(1);
    semaforo[0][6] = new Semaphore(1);
    semaforo[0][7] = new Semaphore(1);
    semaforo[0][8]= new Semaphore(1);
    semaforo[0][9]= null;
    //Semaforos do carro 0 com o carro 2 possuem id 1:
    semaforo[1][0] = new Semaphore(1);
    semaforo[1][1] = new Semaphore(1);
    semaforo[1][2]= new Semaphore(1);
    semaforo[1][3]= null;
    semaforo[1][4]= null;
    semaforo[1][5]= null;
    semaforo[1][6]= null;
    semaforo[1][7]= null;
    semaforo[1][8]= null;
    semaforo[1][9]= null;
    //Semaforos do carro 0 com o carro 3 possuem id 2:
    semaforo[2][0] = new Semaphore(1);
    semaforo[2][1]= new Semaphore(1);
    semaforo[2][2]= new Semaphore(1);
    semaforo[2][3]= new Semaphore(1);
    semaforo[2][4]= new Semaphore(1);
    semaforo[2][5]= new Semaphore(1);
    semaforo[2][6]= new Semaphore(1);
    semaforo[2][7]= new Semaphore(1);
    semaforo[2][8]= new Semaphore(1);
    semaforo[2][9]= null;
    //Semaforos do carro 0 com o carro 4 possuem id 3:
    semaforo[3][0] = new Semaphore(1);
    semaforo[3][1] = new Semaphore(1);
    semaforo[3][2] = new Semaphore(1);
    semaforo[3][3] = new Semaphore(1);
    semaforo[3][4]= new Semaphore(1);
    semaforo[3][5]= null;
    semaforo[3][6]= null;
    semaforo[3][7]= null;
    semaforo[3][8]= null;
    semaforo[3][9]= null;
    //Semaforos do carro 0 com o carro 5 possuem id 4:
    semaforo[4][0] = new Semaphore(1);
    semaforo[4][1]= new Semaphore(1);
    semaforo[4][2]= new Semaphore(1);
    semaforo[4][3] = new Semaphore(1);
    semaforo[4][4]= new Semaphore(1);
    semaforo[4][5]= new Semaphore(1);
    semaforo[4][6]= null;
    semaforo[4][7]= null;
    semaforo[4][8]= null;
    semaforo[4][9]= null;
    //Semaforos do carro 0 com o carro 6 possuem id 5:
    semaforo[5][0] = new Semaphore(1);
    semaforo[5][1]= null;
    semaforo[5][2]= null;
    semaforo[5][3]= new Semaphore(1);
    semaforo[5][4]= new Semaphore(1);
    semaforo[5][5]= new Semaphore(1);
    semaforo[5][6]= new Semaphore(1);
    semaforo[5][7]= new Semaphore(1);
    semaforo[5][8]= null;
    semaforo[5][9]= null;
    //Semaforos do carro 0 com o carro 7 possuem id 6:
    semaforo[6][0]= new Semaphore(1);
    semaforo[6][1]= new Semaphore(1);
    semaforo[6][2]= new Semaphore(1);
    semaforo[6][3]= new Semaphore(1);
    semaforo[6][4]= new Semaphore(1);
    semaforo[6][5]= new Semaphore(1);
    semaforo[6][6]= new Semaphore(1);
    semaforo[6][7]= new Semaphore(1);
    semaforo[6][8]= new Semaphore(1);
    semaforo[6][9]= new Semaphore(1);
    //Semaforos do carro 1 com o carro 2 possuem id 7:
    semaforo[7][0] = new Semaphore(1);
    semaforo[7][1]= new Semaphore(1);
    semaforo[7][2] = new Semaphore(1);
    semaforo[7][3]= new Semaphore(1);
    semaforo[7][4]= new Semaphore(1);
    semaforo[7][5]= null;
    semaforo[7][6]= null;
    semaforo[7][7]= null;
    semaforo[7][8]= null;
    semaforo[7][9]= null;
    //Semaforos do carro 1 com o carro 3 possuem id 8:
    semaforo[8][0] = new Semaphore(1);
    semaforo[8][1]= new Semaphore(1);
    semaforo[8][2]= new Semaphore(1);
    semaforo[8][3]= new Semaphore(1);
    semaforo[8][4]= new Semaphore(1);
    semaforo[8][5]= null;
    semaforo[8][6]= null;
    semaforo[8][7]= null;
    semaforo[8][8]= null;
    semaforo[8][9]= null;
    //Semaforos do carro 1 com o carro 4 possuem id 9:
    semaforo[9][0] = new Semaphore(1);
    semaforo[9][1]= new Semaphore(1);
    semaforo[9][2]= new Semaphore(1);
    semaforo[9][3]= new Semaphore(1);
    semaforo[9][4]= new Semaphore(1);
    semaforo[9][5]= null;
    semaforo[9][6]= null;
    semaforo[9][7]= null;
    semaforo[9][8]= null;
    semaforo[9][9]= null;
    //Semaforos do carro 1 com o carro 5 possuem id 10:
    semaforo[10][0] = new Semaphore(1);
    semaforo[10][1] = new Semaphore(1);
    semaforo[10][2] = new Semaphore(1);
    semaforo[10][3]= new Semaphore(1);
    semaforo[10][4]= new Semaphore(1);
    semaforo[10][5]= null;
    semaforo[10][6]= null;
    semaforo[10][7]= null;
    semaforo[10][8]= null;
    semaforo[10][9]= null;
    //Semaforos do carro 1 com o carro 6 possuem id 11:
    semaforo[11][0] = new Semaphore(1);
    semaforo[11][1] = new Semaphore(1);
    semaforo[11][2]= new Semaphore(1);
    semaforo[11][3]= new Semaphore(1);
    semaforo[11][4]= new Semaphore(1);
    semaforo[11][5]= new Semaphore(1);
    semaforo[11][6]= null;
    semaforo[11][7]= null;
    semaforo[11][8]= null;
    semaforo[11][9]= null;
    //Semaforos do carro 1 com o carro 7 possuem id 12:
    semaforo[12][0] = new Semaphore(1);
    semaforo[12][1]= new Semaphore(1);
    semaforo[12][2]= new Semaphore(1);
    semaforo[12][3]= new Semaphore(1);
    semaforo[12][4]= new Semaphore(1);
    semaforo[12][5]= new Semaphore(1);
    semaforo[12][6]= new Semaphore(1);
    semaforo[12][7]= new Semaphore(1);
    semaforo[12][8]= null;
    semaforo[12][9]= null;
    //Semaforos do carro 2 com o carro 3 possuem id 13:
    semaforo[13][0] = new Semaphore(1);
    semaforo[13][1]= new Semaphore(1);
    semaforo[13][2]= new Semaphore(1);
    semaforo[13][3]= null;
    semaforo[13][4]= null;
    semaforo[13][5]= null;
    semaforo[13][6]= null;
    semaforo[13][7]= null;
    semaforo[13][8]= null;
    semaforo[13][9]= null;
    //Semaforos do carro 2 com o carro 4 possuem id 14:
    semaforo[14][0] = new Semaphore(1);
    semaforo[14][1]= new Semaphore(1);
    semaforo[14][2]= null;
    semaforo[14][3]= null;
    semaforo[14][4]= null;
    semaforo[14][5]= null;
    semaforo[14][6]= null;
    semaforo[14][7]= null;
    semaforo[14][8]= null;
    semaforo[14][9]= null;
    //Semaforos do carro 2 com o carro 5 possuem id 15:
    semaforo[15][0] = new Semaphore(1);
    semaforo[15][1]= new Semaphore(1);
    semaforo[15][2]= null;
    semaforo[15][3]= null;
    semaforo[15][4]= null;
    semaforo[15][5]= null;
    semaforo[15][6]= null;
    semaforo[15][7]= null;
    semaforo[15][8]= null;
    semaforo[15][9]= null;
    //Semaforos do carro 2 com o carro 6 possuem id 16:
    semaforo[16][0] = new Semaphore(1);
    semaforo[16][1]= new Semaphore(1);
    semaforo[16][2]= new Semaphore(1);
    semaforo[16][3]= new Semaphore(1);
    semaforo[16][4]= null;
    semaforo[16][5]= null;
    semaforo[16][6]= null;
    semaforo[16][7]= null;
    semaforo[16][8]= null;
    semaforo[16][9]= null;
    //Semaforos do carro 2 com o carro 7 possuem id 17:
    semaforo[17][0] = new Semaphore(1);
    semaforo[17][1]= new Semaphore(1);
    semaforo[17][2]= new Semaphore(1);
    semaforo[17][3]= new Semaphore(1);
    semaforo[17][4]= null;
    semaforo[17][5]= null;
    semaforo[17][6]= null;
    semaforo[17][7]= null;
    semaforo[17][8]= null;
    semaforo[17][9]= null;
    //Semaforos do carro 3 com o carro 4 possuem id 18:
    semaforo[18][0] = new Semaphore(1);
    semaforo[18][1]= new Semaphore(1);
    semaforo[18][2]= new Semaphore(1);
    semaforo[18][3]= null;
    semaforo[18][4]= null;
    semaforo[18][5]= null;
    semaforo[18][6]= null;
    semaforo[18][7]= null;
    semaforo[18][8]= null;
    semaforo[18][9]= null;
    //Semaforos do carro 3 com o carro 5 possuem id 19:
    semaforo[19][0] = new Semaphore(1);
    semaforo[19][1]= null;
    semaforo[19][2]= null;
    semaforo[19][3]= null;
    semaforo[19][4]= null;
    semaforo[19][5]= null;
    semaforo[19][6]= null;
    semaforo[19][7]= null;
    semaforo[19][8]= null;
    semaforo[19][9]= null;
    //Semaforos do carro 3 com o carro 6 possuem id 20:
    semaforo[20][0] = new Semaphore(1);
    semaforo[20][1] = new Semaphore(1);
    semaforo[20][2]= new Semaphore(1);
    semaforo[20][3]= null;
    semaforo[20][4]= null;
    semaforo[20][5]= null;
    semaforo[20][6]= null;
    semaforo[20][7]= null;
    semaforo[20][8]= null;
    semaforo[20][9]= null;
    //Semaforos do carro 3 com o carro 7 possuem id 21:
    semaforo[21][0] = new Semaphore(1);
    semaforo[21][1]= new Semaphore(1);
    semaforo[21][2]= new Semaphore(1);
    semaforo[21][3]= new Semaphore(1);
    semaforo[21][4]= null;
    semaforo[21][5]= null;
    semaforo[21][6]= null;
    semaforo[21][7]= null;
    semaforo[21][8]= null;
    semaforo[21][9]= null;
    //Semaforos do carro 4 com o carro 5 possuem id 22:
    semaforo[22][0] = new Semaphore(1);
    semaforo[22][1]= new Semaphore(1);
    semaforo[22][2]= null;
    semaforo[22][3]= null;
    semaforo[22][4]= null;
    semaforo[22][5]= null;
    semaforo[22][6]= null;
    semaforo[22][7]= null;
    semaforo[22][8]= null;
    semaforo[22][9]= null;
    //Semaforos do carro 4 com o carro 6 possuem id 23:
    semaforo[23][0] = new Semaphore(1);
    semaforo[23][1] = new Semaphore(1);
    semaforo[23][2]= null;
    semaforo[23][3]= null;
    semaforo[23][4]= null;
    semaforo[23][5]= null;
    semaforo[23][6]= null;
    semaforo[23][7]= null;
    semaforo[23][8]= null;
    semaforo[23][9]= null;
    //Semaforos do carro 4 com o carro 7 possuem id 24:
    semaforo[24][0] = new Semaphore(1);
    semaforo[24][1] = new Semaphore(1);
    semaforo[24][2] = new Semaphore(1);
    semaforo[24][3] = null;
    semaforo[24][4] = null;
    semaforo[24][5] = null;
    semaforo[24][6] = null;
    semaforo[24][7] = null;
    semaforo[24][8] = null;
    semaforo[24][9] = null;
    //Semaforos do carro 5 com o carro 6 possuem id 25:
    semaforo[25][0] = new Semaphore(1);
    semaforo[25][1] = null;
    semaforo[25][2] = new Semaphore(1);
    semaforo[25][3] = null;
    semaforo[25][4] = null;
    semaforo[25][5] = null;
    semaforo[25][6] = null;
    semaforo[25][7] = null;
    semaforo[25][8] = null;
    semaforo[25][9] = null;
    //Semaforos do carro 5 com o carro 7 possuem id 26:
    semaforo[26][0] = new Semaphore(1);
    semaforo[26][1] = new Semaphore(1);
    semaforo[26][2] = new Semaphore(1);
    semaforo[26][3] = new Semaphore(1);
    semaforo[26][4] = null;
    semaforo[26][5] = null;
    semaforo[26][6] = null;
    semaforo[26][7] = null;
    semaforo[26][8] = null;
    semaforo[26][9] = null;
    //Semaforos do carro 6 com o carro 7 possuem id 27:
    semaforo[27][0] = new Semaphore(1);
    semaforo[27][1] = new Semaphore(1);
    semaforo[27][2] = new Semaphore(1);
    semaforo[27][3] = new Semaphore(1);
    semaforo[27][4] = new Semaphore(1);
    semaforo[27][5] = new Semaphore(1);
    semaforo[27][6] = null;
    semaforo[27][7] = null;
    semaforo[27][8] = null;
    semaforo[27][9] = null;
    }

  /* ***************************************************************
  * Metodo: resetarImagemCarros
  * Funcao: reseta a posicao de todos os carros na interface grafica para sua posicao inicial, chamando o metodo de set_XY_Image para cada carro, passando o mesmo como parametro e a sua posicao X e Y
  * Parametros: void
  * Retorno: void
  ********************************************************************/
  private void resetarImagemCarros() {
    //Carro 0
    set_XY_Image(carro0,135,218);
    //Carro 1
    set_XY_Image(carro1, 135, 615);
    //Carro 2
    set_XY_Image(carro2, 41, 265);
    //Carro 3
    set_XY_Image(carro3, 278, 483);
    //Carro 4
    set_XY_Image(carro4, 420 , 218);
    //Carro 5
    set_XY_Image(carro5, 607, 398);
    //Carro 6
    set_XY_Image(carro6, 420, 574);
    //Carro 7
    set_XY_Image(carro7, 135, 312);
  }

  /* ***************************************************************
  * Metodo: set_XY_Image
  * Funcao: seta a posicao X e Y de um carro na interface grafica, recebendo o carro e os valores de X e Y como parametros
  * Parametros: Carro carro que representa qual carro tera sua imagem modificada, int valorDeX que representa a posicao X inicial do carro, int valorDeY que representa a posicao Y inicial do carro
  * Retorno: void
  ********************************************************************/
  private void set_XY_Image(Carro carro, int valorDeX, int valorDeY) {
      carro.getImageCarro().setLayoutX(valorDeX);//seta a posicao X da imagem do carro 
      carro.getImageCarro().setLayoutY(valorDeY);//seta a posicao Y da imagem do carro
  }

  /* ***************************************************************
  * Metodo: preencherArrayDeCarros
  * Funcao: preenche o array de carros com os carros instanciados
  * Parametros: void
  * Retorno: void
  ********************************************************************/
  private void preencherArrayDeCarros() {
    carros[0] = carro0;
    carros[1] = carro1;
    carros[2] = carro2;
    carros[3] = carro3;
    carros[4] = carro4;
    carros[5] = carro5;
    carros[6] = carro6;
    carros[7] = carro7;
  }

  /* ***************************************************************
  * Metodo: preencherArrayDeClientes
  * Funcao: preenche o array de clientes com os clientes instanciados
  * Parametros: void
  * Retorno: void
  ********************************************************************/
  private void preencherArrayDeClientes() {
    clientes[0] = cliente0;
    clientes[1] = cliente1;
    clientes[2] = cliente2;
    clientes[3] = cliente3;
    clientes[4] = cliente4;
    clientes[5] = cliente5;
    clientes[6] = cliente6;
    clientes[7] = cliente7;
    clientes[8] = cliente8;
    clientes[9] = cliente9;
    clientes[10] = cliente10;
    clientes[11] = cliente11;
    clientes[12] = cliente12;
    clientes[13] = cliente13;
    clientes[14] = cliente14;
  }

  /* ***************************************************************
  * Metodo: criarObjetosCarros
  * Funcao: cria os objetos de carros, instanciando cada um deles com seus respectivos valores e atributos
  * Parametros: void
  * Retorno: void
  ********************************************************************/
  private void criarObjetosCarros() {
    carro0 = new Carro(0, false, false, slider_carro0, imageCarro0,caminho_carro0,clientes, comandosCarro0, semaforo);
    carro1 = new Carro(1, false, false, slider_carro1, imageCarro1,caminho_carro1,clientes, comandosCarro1, semaforo);
    carro2 = new Carro(2, false, false, slider_carro2, imageCarro2,caminho_carro2,clientes, comandosCarro2, semaforo);
    carro3 = new Carro(3, false, false, slider_carro3, imageCarro3,caminho_carro3,clientes, comandosCarro3, semaforo);
    carro4 = new Carro(4, false, false, slider_carro4, imageCarro4,caminho_carro4,clientes, comandosCarro4, semaforo);
    carro5 = new Carro(5, false, false, slider_carro5, imageCarro5, caminho_carro5, clientes, comandosCarro5, semaforo);
    carro6 = new Carro(6, false, false, slider_carro6, imageCarro6,caminho_carro6,clientes, comandosCarro6, semaforo);
    carro7 = new Carro(7, false, false, slider_carro7, imageCarro7,caminho_carro7,clientes, comandosCarro7, semaforo);
  }

  /* ***************************************************************
  * Metodo: criarGeradoraClientes
  * Funcao: cria o objeto de geradora de clientes, instanciando ele com seus respectivos valores e atributos
  * Parametros: void
  * Retorno: void
  ********************************************************************/
  private void criarGeradoraClientes() {
    geradoraClientes = new GeradoraClientes(clientes, slider_geradoraClientes,false);
  }

  /* ***************************************************************
  * Metodo: pausarCarro
  * Funcao: pausa ou despausa um carro, alterando a imagem do botao de pausa do carro na interface grafica e a variavel de controle de pausa do carro
  * Parametros: int id que representa o id do carro que sera pausado
  * Retorno: void
  ********************************************************************/
  private void pausarCarro(int id) {
    if(!carros[id].getPausado()) {
      carros[id].setPausado(true);
      Platform.runLater(() -> {
        switch (id) {
          case 0:
            pause_carro0.setImage(new Image("/assets/resume_button.png"));
            break;
          case 1:
            pause_carro1.setImage(new Image("/assets/resume_button.png"));
            break;
          case 2:
            pause_carro2.setImage(new Image("/assets/resume_button.png"));
            break;
          case 3:
            pause_carro3.setImage(new Image("/assets/resume_button.png"));
            break;
          case 4:
            pause_carro4.setImage(new Image("/assets/resume_button.png"));
            break;
          case 5:
            pause_carro5.setImage(new Image("/assets/resume_button.png"));
            break;
          case 6:
            pause_carro6.setImage(new Image("/assets/resume_button.png"));
            break;
          case 7:
            pause_carro7.setImage(new Image("/assets/resume_button.png"));
            break;
          default:
            System.out.println("Carro não encontrado, erro na pausa.");
            break;
        }
      });
    } else {
      carros[id].setPausado(false);
      Platform.runLater(() -> {
        switch (id) {
          case 0:
            pause_carro0.setImage(new Image("/assets/pause_button.png"));
            break;
          case 1:
            pause_carro1.setImage(new Image("/assets/pause_button.png"));
            break;
          case 2:
            pause_carro2.setImage(new Image("/assets/pause_button.png"));
            break;
          case 3:
            pause_carro3.setImage(new Image("/assets/pause_button.png"));
            break;
          case 4:
            pause_carro4.setImage(new Image("/assets/pause_button.png"));
            break;
          case 5:
            pause_carro5.setImage(new Image("/assets/pause_button.png"));
            break;
          case 6:
            pause_carro6.setImage(new Image("/assets/pause_button.png"));
            break;
          case 7:
            pause_carro7.setImage(new Image("/assets/pause_button.png"));
            break;
          default:
            System.out.println("Carro não encontrado, erro na pausa.");
            break;
        }
      });
    }
  }

  /* ***************************************************************
  * Metodo: mostrarCaminho
  * Funcao: mostra ou esconde o caminho de um carro na interface grafica, alterando a visibilidade da imagem do caminho do carro
  * Parametros: int id que representa o id do carro que tera seu caminho mostrado ou escondido
  * Retorno: void
  ********************************************************************/
  private void mostrarCaminho(int id) {
    if(carros[id].getImagemCaminho().isVisible()) {
      carros[id].getImagemCaminho().setVisible(false);
    }else{
      carros[id].getImagemCaminho().setVisible(true);
    }
  }
}
