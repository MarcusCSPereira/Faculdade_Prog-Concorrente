/* ***************************************************************
* Autor............: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio...........: 13/03/2023
* Ultima alteracao.: 19/03/2023
* Nome.............: ControllerTela2.java
* Funcao...........: Classe que controla a segunda tela da aplicacao, realizando por meio de timers a chamada dos metodos de movimentacao dos trens e chamando os metodos que nao permitem a colisao entre os trens
*************************************************************** */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Train;
import util.DataTransfer;
import util.ControleDeMovimentoDeTrens;

public class ControllerTela2 implements Initializable {

  @FXML
  private Slider slider_trem1, slider_trem2;//Injetando o Id dos sliders que controlam a velocidade dos trens criados no arquivo tela2.fxml

  @FXML
  private ImageView train1_imagem, train2_imagem, reset_imagem, voltarTelaInicial_imagem, fecharProgama_imagem;//Injetando o Id das imagens dos trens e da imagem do botao de reset criados no arquivo tela2.fxml

  @FXML
  private ChoiceBox<String> choicebox_tela2;//Injetando o Id da choicebox que permite a escolha da posicao inicial dos trens criada no arquivo tela2.fxml

  @FXML
  private Circle zonaCritica1, zonaCritica2;//Injetando o Id dos circulos que representam as zonas criticas criados no arquivo tela2.fxml

  private Train trem1, trem2;//Instanciando os trens por meio da classe model Train

  // Variáveis que controlam a logica de colisao
  private boolean trem1EntrouPrimeiroNaZonaCritica1 = false;
  private boolean trem2EntrouPrimeiroNaZonaCritica1 = false;
  private boolean trem1EntrouPrimeiroNaZonaCritica2 = false;
  private boolean trem2EntrouPrimeiroNaZonaCritica2 = false;
  private boolean semColisoes1 = true;
  private boolean semColisoes2 = true;

  private ControleDeMovimentoDeTrens controleDeMovimentoDeTrem;//Classe que controla o movimento dos trens e que checa a colisão com as zonas criticas.

  private double variavelDeControleDeMovimento = 1.8;//Variavel que controla a velocidade dos trens

  String[] opcoesDeInicializacao = { "Superior esquerdo / Superior direito", "Inferior esquerdo / Inferior direito","Superior esquedo / Inferior direito", "Inferior esquerdo / Superior direito" };// Vetor de opcoes de inicializacao dos trens para serem exibidos na choicebox

  DataTransfer dataTransfer = DataTransfer.getInstance();//Instanciando o objeto dataTransfer e chamando o metodo getInstance da classe DataTransfer para permitir a transferencia de dados entre as telas por meio do padrao Singleton

  AnimationTimer timer_train1 = new AnimationTimer() {//Criando um timer para o trem 1 que sera responsavel por controlar o movimento do trem 1

  /* ***************************************************************
  * Metodo: handle
  * Funcao: Metodo padrao do AnimationTimer que e chamado a cada frame da animacao, executando tudo que esta dentro dele permitindo assim o movimento do trem 1
  * Parametros: long now, o parametro now e um valor longo representando o tempo atual do sistema em nanossegundos. Esse valor e passado automaticamente pelo JavaFX quando o metodo handle e invocado.
  * Retorno: void
  *************************************************************** */
    @Override//Sobrescrevendo o metodo handle
    public void handle(long now){
      Platform.runLater(() -> {//Permite que o codigo dentro dessa funcao lambda seja executado na thread principal do JavaFX evitando erros na modificacao da interface grafica
        trem1.setVelocidade(slider_trem1.getValue() * variavelDeControleDeMovimento);//Define a velocidade do trem 1 de acordo com o valor do slider_trem1 no momento
        controleDeMovimentoDeTrem.moverTrem(trem1);//Chama o metodo moverTrem da classe ControleDeMovimentoDeTrens a cada frame do timer para assim mover o trem 1 de acordo com a velocidade definida
      }//Fim da funcao lambda
      );//Fim do metodo runLater
    }//Fim do metodo handle
  };//Fim do timer_train1

  AnimationTimer timer_train2 = new AnimationTimer()  {//Criando um timer para o trem 2 que sera responsavel por controlar o movimento do trem 2

  /* ***************************************************************
  * Metodo: handle
  * Funcao: Metodo padrao do AnimationTimer, assim como no timer_train1, que e chamado a cada frame da animacao, executando tudo que esta dentro dele so que agora permitindo o movimento do trem 2
  * Parametros: long now, o parametro now e um valor longo representando o tempo atual do sistema em nanossegundos. Esse valor e passado automaticamente pelo JavaFX quando o metodo handle e invocado.
  * Retorno: void
  *************************************************************** */
    @Override//Sobrescrevendo o metodo handle
    public void handle(long now) {
      Platform.runLater(() -> {//Utilizando novamente o metodo runLater para evitar a ocorrencia de erros na mudanca da interface grafica
        trem2.setVelocidade(slider_trem2.getValue() * variavelDeControleDeMovimento);//Define a velocidade do trem 2 de acordo com o valor do slider_trem2 no momento
        controleDeMovimentoDeTrem.moverTrem(trem2);//Chama o metodo moverTrem da classe ControleDeMovimentoDeTrens a cada frame do timer para assim mover o trem 2 de acordo com a velocidade definida
      }//Fim da funcao lambda
      );//Fim do metodo runLater
    }//Fim do metodo handle

  };//Fim do timer_train2


  AnimationTimer colisionTimer = new AnimationTimer() {//Criando um timer executar os metodos da classe ControleDeMovimentoDeTrens que nao permitem a colisao entre os trens

  /* ***************************************************************
  * Metodo: handle
  * Funcao: Metodo padrao do AnimationTimer, assim como nos outros timers, que e chamado a cada frame da animacao, executando tudo que esta dentro dele permitindo a chamada dos metodos que nao permitem a colisao entre os trens
  * Parametros: long now, o parametro now e um valor longo representando o tempo atual do sistema em nanossegundos. Esse valor e passado automaticamente pelo JavaFX quando o metodo handle e invocado.
  * Retorno: void
  *************************************************************** */
    @Override//Sobrescrevendo o metodo handle
    public void handle(long now) {
      Platform.runLater(() -> {//Utilizando novamente o metodo runLater para evitar a ocorrencia de erros na mudanca da interface grafica
        controleDeColisao();//Chama o metodo colisionControl que esta na classe ControllerTela2 a cada frame do timer para assim evitar a colisao entre os trens
      }//Fim da funcao lambda
      );//Fim do metodo runLater
    }//Fim do metodo handle
  };//Fim do colisionTimer

  /* ***************************************************************
  * Metodo: initialize
  * Funcao: Metodo padrao do Initializable que e chamado quando a tela e carregada, permitindo a inicializacao de variaveis e a chamada de metodos que devem ser executados antes mesmo que a tela apareca para o usuario
  * Parametros: um URL location e um ResourceBundle resources, que sao parametros padroes do metodo initialize
  * Retorno: void
  *************************************************************** */
  @Override//Sobrescrevendo o metodo initialize
  public void initialize(URL location, ResourceBundle resources) {

    controleDeMovimentoDeTrem = new ControleDeMovimentoDeTrens();//Instanciando um objeto da classe ControleDeMovimentoDeTrens para controlar o movimento dos trens e checar a colisao com as zonas críticas

    posicionarTrens();//Chama o metodo posicionarTrens que define a posicao inicial dos trens de acordo com a escolha do usuario

    choicebox_tela2.getItems().addAll(opcoesDeInicializacao);//Adiciona as opcoes de inicializacao dos trens na choicebox assim que a tela e carregada
    choicebox_tela2.setValue(opcoesDeInicializacao[dataTransfer.getEscolha()]);//Define a escolha do usuario na choicebox feita na tela 1, dessa forma demonstrando a escolha do usuario na tela 2 correspondente a escolha feita anteriormente.
    choicebox_tela2.setOnAction(this::getEscolha);//Declara o metodo chamado quando acionamos a choicebox como o metodo getEscolha que e responsavel por chamar o metodo posicionarTrens e definir a posicao inicial dos trens de acordo com a nova escolha do usuario

    colisionTimer.start();//Inicia o timer que possui os metodos que evitam a colisao entre os trens

  }//Fim do metodo initialize

  /* ***************************************************************
  * Metodo: getEscolha
  * Funcao: Metodo que e chamado quando o usuario escolhe uma opcao na choicebox, permitindo assim que a animacao mude caso o usuario escolha uma nova posicao inicial para os trens
  * Parametros: um ActionEvent event que e o evento de escolher uma opcao na choicebox
  * Retorno: void
  *************************************************************** */
  public void getEscolha(ActionEvent event) {
    if (dataTransfer.getEscolha() != choicebox_tela2.getSelectionModel().getSelectedIndex()){//Verifica se a escolha do usuario eh diferente da escolha que ele fez anteriormente

      dataTransfer.setEscolha(choicebox_tela2.getSelectionModel().getSelectedIndex());//Recebe a escolha do usuario quando ele escolhe uma opcao na choicebox e define a essa escolha no objeto dataTransfer do padrao Singleton, para quando voltarmos a tela 1 a escolha do usuario seja mantida
      posicionarTrens();//Chama o metodo posicionarTrens que reinicia a animacao e define a posicao inicial dos trens de acordo com a nova escolha do usuario
    }//Fim do if
  }//Fim do metodo getEscolha

  @FXML//Anotacao que indica que o metodo voltar eh um metodo de acao que sera chamado quando a imagem voltarTelaInicial_imagem for clicada
  /* ***************************************************************
  * Metodo: voltar
  * Funcao: Metodo que e chamado quando a imagem voltarTelaInicial_imagem eh clicada, permitindo assim que o usuario retorne para a tela 1, e possa escolher novamente a posicao inicial dos trens
  * Parametros: um MouseEvent event que e o evento de clicar na imagem voltarTelaInicial_imagem
  * Retorno: void
  *************************************************************** */
  public void voltar(MouseEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/tela1.fxml"));//Carrega o fxml da primeira tela
    Parent root = loader.load();//Define o root com o fxml e junto a isso carrega o controller da tela por meio do metodo load
    Scene scene = new Scene(root);//Define a cena com o root
    Stage stage = (Stage) voltarTelaInicial_imagem.getScene().getWindow();//Define o Stage com a cena, obtendo o mesmo stage do botao reset da tela 2 e modificando apenas a cena
    stage.setScene(scene);//Define o Stage com a cena
    stage.show();//Mostra o Stage para o usuario contendo agora a tela 1
  }//Fim do metodo voltar

  @FXML//Anotacao que indica que o metodo resetar eh um metodo de acao que sera chamado quando a imagem reset_imagem for clicada
  /* ***************************************************************
  * Metodo: resetar
  * Funcao: Metodo que e chamado quando a imagem reset_imagem eh clicada, permitindo assim que o usuario reinicie a animacao de acordo a posicao dos trens escolhida , e possa escolher novamente a posicao inicial dos trens
  * Parametros: um MouseEvent event que e o evento de clicar na imagem reset_imagem
  * Retorno: void
  *************************************************************** */
  public void resetar(MouseEvent event) throws IOException {
    posicionarTrens();//Chama o metodo posicionarTrens que define a posicao inicial dos trens de acordo com a escolha do usuario
  }//Fim do metodo resetar

  @FXML//Anotacao que indica que o metodo fecharPrograma eh um metodo de acao que sera chamado quando a imagem de id fecharPrograma_imagem for clicada
  /* ***************************************************************
  * Metodo: fecharPrograma
  * Funcao: Fecha a aplicacao quando o usuario clica na imagem fecharPrograma_imagem
  * Parametros: um MouseEvent event que e o evento de clicar na imagem de id fecharPrograma_imagem
  * Retorno: void
  *************************************************************** */
  public void fecharPrograma(MouseEvent event) {
    System.exit(0);
  }// Fim do metodo fecharPrograma

  /* ***************************************************************
  * Metodo: posicionarTrens
  * Funcao: Metodo que define a posicao inicial dos trens de acordo com a escolha do usuario no momento em que ele e chamado e junto a isso reinicia o valor da velocidade dos trens
  * Parametros: nenhum
  * Retorno: void
  * Observacoes: Esse metodo e chamado no metodo initialize, no metodo resetar e quando o usuario faz alguma acao na choicebox logo ele eh responsavel por definir a posicao inicial dos trens de acordo com a escolha do usuario sobre as posicoes dos trens no momento em que ele eh chamado
  *************************************************************** */
  private void posicionarTrens() {
    switch (dataTransfer.getEscolha()) {//Switch que define a posicao inicial dos trens de acordo com a escolha do usuario na tela 1
      case 0://Caso a escolha seja 0 a imagem do trem 1 sera posicionado no canto superior esquerdo e a imagem do trem 2 no canto superior direito
        trem1 = new Train(train1_imagem, 0);
        trem2 = new Train(train2_imagem, 1);
        slider_trem1.setValue(1);//Define o valor inicial do slider_trem1 como 1
        slider_trem2.setValue(1);//Define o valor inicial do slider_trem2 como 1
        break;
      case 1://Caso a escolha seja 1 a imagem do trem 1 sera posicionado no canto inferior esquerdo e a imagem do trem 2 no canto inferior direito
        trem1 = new Train(train1_imagem, 2);
        trem2 = new Train(train2_imagem, 3);
        break;
      case 2://Caso a escolha seja 2 a imagem do trem 1 sera posicionado no canto superior esquerdo e a imagem do trem 2 no canto inferior direito
        trem1 = new Train(train1_imagem, 0);
        trem2 = new Train(train2_imagem, 3);
        break;
      case 3://Caso a escolha seja 3 a imagem do trem 1 sera posicionado no canto inferior esquerdo e a imagem do trem 2 no canto superior direito
        trem1 = new Train(train1_imagem, 2);
        trem2 = new Train(train2_imagem, 1);
        break;
      default:
        // Caso a escolha seja diferente de 0, 1, 2 ou 3, um erro sera exibido e a aplicacao sera encerrada
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Erro");
        alert.setContentText("Um erro inesperado ocorreu. Reinicie a aplicação.");
        alert.showAndWait();
        System.exit(0);
        break;
    }//Fim do switch que define a posicao inicial dos trens
    slider_trem1.setValue(1);//Define o valor inicial do slider_trem1 como 1
    slider_trem2.setValue(1);//Define o valor inicial do slider_trem2 como 1
  }//Fim do metodo posicionarTrens


  /* ***************************************************************
  * Metodo: controleDeColisao
  * Funcao: Metodo que possui a logica para evitar a colisao entre os trens, permitindo que eles parem quando estiverem na zona critica dando prioridade para o trem que entrou primeiro na zona critica e voltem a se mover quando nao estiverem mais na zona critica
  * Parametros: nenhum
  * Retorno: void
  * Observacoes: O codigo abaixo implementa um controle cuidadoso das operacoes que envolvem a deteccao de colisões utilizando a logica de exclusao mutua entre os trens na regiao critica, e a decisao de parar ou permitir que os trens continuem a se mover com base na presenca ou ausencia de colisoes. Isso e feito de maneira a garantir que apenas uma parte do codigo manipule os estados compartilhados (por exemplo, as variaveis booleanas que indicam se os trens estao na zona critica e quem entrou primeiro) em um determinado momento, a utilização de variáveis booleanas como semColisoes1 e semColisoes2, juntamente com a lógica de controle condicional, serve para garantir que os trens so possam continuar se movendo se nao houver risco de colisao em ambas as zonas criticas.
  *************************************************************** */
  public void controleDeColisao() {

    boolean trem1ControleDeZonaCritica1 = controleDeMovimentoDeTrem.checarColisao(trem1, zonaCritica1);//Chama o metodo checarColisao da classe ControleDeMovimentoDeTrens que retorna um booleano para verificar se o trem 1 esta na zona critica 1
    boolean trem2ControleDeZonaCritica1 = controleDeMovimentoDeTrem.checarColisao(trem2, zonaCritica1);//Chama o metodo checarColisao da classe ControleDeMovimentoDeTrens que retorna um booleano para verificar se o trem 2 esta na zona critica 1

    boolean trem1ControleDeZonaCritica2 = controleDeMovimentoDeTrem.checarColisao(trem1, zonaCritica2);//Chama o metodo checarColisao da classe ControleDeMovimentoDeTrens que retorna um booleano para verificar se o trem 1 esta na zona critica 2
    boolean trem2ControleDeZonaCritica2 = controleDeMovimentoDeTrem.checarColisao(trem2, zonaCritica2);//Chama o metodo checarColisao da classe ControleDeMovimentoDeTrens que retorna um booleano para verificar se o trem 2 esta na zona critica 2

    if (trem1ControleDeZonaCritica1 && trem2ControleDeZonaCritica1) {
      // Caso ambos os trens estão na zona critica 1

      if (trem1EntrouPrimeiroNaZonaCritica1 && !trem2EntrouPrimeiroNaZonaCritica1) {//Caso o trem 1 tenha entrado primeiro na zona critica 1 entao ele tem a prioridade e o trem 2 deve parar
        timer_train2.stop(); //Parando o timer do trem 2 que controla o movimento do trem 2
      } else if (!trem1EntrouPrimeiroNaZonaCritica1 && trem2EntrouPrimeiroNaZonaCritica1) {//Caso o trem 2 tenha entrado primeiro na zona critica 1 entao ele tem a prioridade e o trem 1 deve parar
        timer_train1.stop(); //Parando o timer do trem 1 que controla o movimento do trem 1
      }

      semColisoes1 = false; //Como ha risco de colisao pois ambos os trens estao na zona critica 1 a variavel semColisoes1 e definida como false

    } else {//Caso contrario, ou seja, se os trens nao estiverem na zona critica 1

      if (trem1ControleDeZonaCritica1) { // Checa se o Trem 1 esta na zona critica 1
        trem1EntrouPrimeiroNaZonaCritica1 = true; //Caso sim o Trem 1 entrou primeiro na zona critica 1
        trem2ControleDeZonaCritica1 = false; // Trem 2 nao esta na zona critica 1 portanto voltou a false.
      } else {
        trem1EntrouPrimeiroNaZonaCritica1 = false; // Caso o Trem 1 nao esteja na zona critica 1, ele nao entrou primeiro na zona critica 1
      }

      if (trem2ControleDeZonaCritica1) { //Caso o Trem 2 esteja na zona critica 1
        trem2EntrouPrimeiroNaZonaCritica1 = true; // Trem 2 entrou primeiro na zona critica 1 se torna true
        trem1ControleDeZonaCritica1 = false; // Trem 1 nao esta na zona cratica 1 portanto voltou a false.
      } else {
        trem2EntrouPrimeiroNaZonaCritica1 = false; //Caso contratio Trem 2 nao esta na zona critica 1, portanto voltou a false.
      }

      semColisoes1 = true; // Nao ha risco de colisao pois ambos os trens nao estao na zona critica 1

    }//Fim do if else que verifica se os trens estao na zona critica 1

    if (trem1ControleDeZonaCritica2 && trem2ControleDeZonaCritica2) {
      // Seguimos a mesma logica porem agora para a zona critica 2

      if (trem1EntrouPrimeiroNaZonaCritica2 && !trem2EntrouPrimeiroNaZonaCritica2) {
        timer_train2.stop(); // Trem 2 deve parar
      } else if (!trem1EntrouPrimeiroNaZonaCritica2 && trem2EntrouPrimeiroNaZonaCritica2) {
        timer_train1.stop(); // Trem 1 deve parar
      }

      semColisoes2 = false; // Ha risco de colisao pois ambos os trens estao na zona critica 2

    } else {
      if (trem1ControleDeZonaCritica2) {// Trem 1 esta na zona critica 2
        trem1EntrouPrimeiroNaZonaCritica2 = true; // Trem 1 entrou primeiro na zona critica 2
        trem2ControleDeZonaCritica2 = false; // Trem 2 não esta na zona critica 2 portanto voltou a false.
      } else {
        trem1EntrouPrimeiroNaZonaCritica2 = false; // Trem 1 não esta na zona critica 2 mais, portanto voltou a false.
      }
      if (trem2ControleDeZonaCritica2) { // Trem 2 esta na zona critica 2
        trem2EntrouPrimeiroNaZonaCritica2 = true; // Trem 2 entrou primeiro na zona critica 2
        trem1ControleDeZonaCritica2 = false; // Trem 1 nao esta na zona critica 2 portanto voltou a false.
      } else {
        trem2EntrouPrimeiroNaZonaCritica2 = false; // Trem 2 nao esta na zona critica 2, portanto voltou a false.
      }

      semColisoes2 = true; // Nao ha risco de colisao pois ambos os trens nao estao na zona critica 2

    }//Fim do if else que verifica se os trens estao na zona critica 2

    if (semColisoes1 && semColisoes2) {
      // Caso nao haja risco de colisao, os trens devem voltar a se mover.
      timer_train1.start();
      timer_train2.start();
    }//Fim do if que verifica se nao ha risco de colisao
  }//Fim do metodo controleDeColisao


}//Fim da classe ControllerTela2
