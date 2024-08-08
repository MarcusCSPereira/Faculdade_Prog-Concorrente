/* ***************************************************************
* Autor............: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio...........: 20/04/2023
* Ultima alteracao.: 24/05/2023
* Nome.............: ControllerTela2.java
* Funcao...........: Classe que controla a segunda tela da aplicacao, realizando por meio de timers a chamada dos metodos de movimentacao dos trens e chamando os metodos que nao permitem a colisao entre os trens
*************************************************************** */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Train1Thread;
import model.Train2Thread;
import util.DataTransfer;
import javafx.scene.Node;

public class ControllerTela2 implements Initializable {

  @FXML
  private Slider slider_trem1, slider_trem2;//Injetando o Id dos sliders que controlam a velocidade dos trens criados no arquivo tela2.fxml

  @FXML
  private ImageView train1_imagem, train2_imagem, reset_imagem, voltarTelaInicial_imagem, fecharProgama_imagem;//Injetando o Id das imagens dos trens e da imagem do botao de reset criados no arquivo tela2.fxml

  Stage stageAtual, proximoStage;//Instanciando os objetos stageAtual e proximoStage da classe Stage, para controlar os stages da aplicacao no momento da troca de telas

  @FXML
  private ChoiceBox<String> choicebox_tela2_1, choicebox_tela2_2;//Injetando o Id da choicebox que permite a escolha da posicao inicial dos trens criada no arquivo tela2.fxml

  Train1Thread train1Thread;//Instanciando o objeto train1Thread da classe Train1Thread
  Train2Thread train2Thread;//Instanciando o objeto train2Thread da classe Train2Thread

  String[] opcoesDeInicializacao = { "Superior esquerdo / Superior direito", "Inferior esquerdo / Inferior direito","Superior esquedo / Inferior direito", "Inferior esquerdo / Superior direito" };// Vetor de opcoes de inicializacao dos trens para serem exibidos na choicebox

  String[] opcoesDeControleDeColisao = {
    "Vari\u00E1veis de travamento", // Variaveis de travamento
    "Estrita altern\u00E2ncia",      // Estrita alternancia
    "Solu\u00E7\u00E3o de Peterson", // Solucao de Peterson
    "Sem controle de colis\u00E3o"   // Sem controle de colisao
  }; // Vetor de opcoes de controle de colisao dos trens para serem exibidos na choicebox, utilizando unicode para demonstrar a acentuacao correta

  DataTransfer dataTransfer = DataTransfer.getInstance();//Instanciando o objeto dataTransfer e chamando o metodo getInstance da classe DataTransfer para permitir a transferencia de dados entre as telas por meio do padrao Singleton

  //Variaveis de Travamento
  public static volatile int variavelDeTravamento1 = 0;
  public static volatile int variavelDeTravamento2 = 0;
  //Estrita Alternancia
  public static volatile int turno1EA = 0;
  public static volatile int turno2EA = 0;
  //Solucao de Peterson
  public static volatile int turno1SP;
  public static volatile boolean[] interesseNaRegiao1 = {false, false};
  public static volatile int turno2SP;
  public static volatile boolean[] interesseNaRegiao2 = {false, false};

  @Override//Sobrescrevendo o metodo initialize
  public void initialize(URL location, ResourceBundle resources) {

    choicebox_tela2_1.getItems().addAll(opcoesDeInicializacao);//Adiciona as opcoes de inicializacao dos trens na choicebox assim que a tela e carregada
    choicebox_tela2_2.getItems().addAll(opcoesDeControleDeColisao);//Adiciona as opcoes de controle de colisao dos trens na choicebox assim que a tela e carregada
    choicebox_tela2_1.setValue(opcoesDeInicializacao[dataTransfer.getEscolha()]);//Define a escolha do usuario na choicebox feita na tela 1, dessa forma demonstrando a escolha do usuario na tela 2 correspondente a escolha feita anteriormente.
    choicebox_tela2_2.setValue(opcoesDeControleDeColisao[dataTransfer.getControle()]);//Define a escolha do usuario na choicebox feita na tela 1, dessa forma demonstrando a escolha do usuario na tela 2 correspondente a escolha feita anteriormente.
    choicebox_tela2_1.setOnAction(this::getEscolha);//Declara o metodo chamado quando acionamos a choicebox como o metodo getEscolha que e responsavel por chamar o metodo posicionarTrens e definir a posicao inicial dos trens de acordo com a nova escolha do usuario
    choicebox_tela2_2.setOnAction(this::getColisao);//Declara o metodo chamado quando acionamos a choicebox como o metodo getEscolha que e responsavel por chamar o metodo posicionarTrens e definir a posicao inicial dos trens de acordo com a nova escolha do usuario


    if (train1Thread !=null) {
      //Para o timer de movimento do trem 1 caso ele ainda esteja executando ao mudarmos de tela
      train1Thread.getTimerDeMovimento().stop();
    }

    if (train2Thread !=null) {
      //Para o timer de movimento do trem 2 caso ele ainda esteja executando ao mudarmos de tela
      train2Thread.getTimerDeMovimento().stop();
    }

    switch (dataTransfer.getEscolha()) {//Switch que define a posicao inicial dos trens de acordo com a escolha do usuario na tela 1

      case 0://Caso a escolha seja 0 a imagem do trem 1 sera posicionado no canto superior esquerdo e a imagem do trem 2 no canto superior direito
        train1Thread = new Train1Thread(0, train1_imagem, slider_trem1, dataTransfer.getControle());//Instancia o objeto train1Thread da classe Train1Thread na posicao 0, com a imagem do trem 1, o slider do trem 1 e o controle de colisao escolhido pelo usuario
        train2Thread = new Train2Thread(1, train2_imagem, slider_trem2, dataTransfer.getControle());//Instancia o objeto train2Thread da classe Train2Thread na posicao 1, com a imagem do trem 2, o slider do trem 2 e o controle de colisao escolhido pelo usuario
        break;

      case 1://Caso a escolha seja 1 a imagem do trem 1 sera posicionado no canto inferior esquerdo e a imagem do trem 2 no canto inferior direito
        train1Thread = new Train1Thread(2, train1_imagem, slider_trem1, dataTransfer.getControle());//Instancia o objeto train1Thread da classe Train1Thread na posicao 2, com a imagem do trem 1, o slider do trem 1 e o controle de colisao escolhido pelo usuario
        train2Thread = new Train2Thread(3, train2_imagem, slider_trem2, dataTransfer.getControle());//Instancia o objeto train2Thread da classe Train2Thread na posicao 3, com a imagem do trem 2, o slider do trem 2 e o controle de colisao escolhido pelo usuario
        break;

      case 2://Caso a escolha seja 2 a imagem do trem 1 sera posicionado no canto superior esquerdo e a imagem do trem 2 no canto inferior direito
        train1Thread = new Train1Thread(0, train1_imagem, slider_trem1, dataTransfer.getControle());//Instancia o objeto train1Thread da classe Train1Thread na posicao 0, com a imagem do trem 1, o slider do trem 1 e o controle de colisao escolhido pelo usuario
        train2Thread = new Train2Thread(3, train2_imagem, slider_trem2, dataTransfer.getControle());//Instancia o objeto train2Thread da classe Train2Thread na posicao 3, com a imagem do trem 2, o slider do trem 2 e o controle de colisao escolhido pelo usuario
        break;

      case 3://Caso a escolha seja 3 a imagem do trem 1 sera posicionado no canto inferior esquerdo e a imagem do trem 2 no canto superior direito
        train1Thread = new Train1Thread(2, train1_imagem, slider_trem1, dataTransfer.getControle());//Instancia o objeto train1Thread da classe Train1Thread na posicao 2, com a imagem do trem 1, o slider do trem 1 e o controle de colisao escolhido pelo usuario
        train2Thread = new Train2Thread(1, train2_imagem, slider_trem2, dataTransfer.getControle());//Instancia o objeto train2Thread da classe Train2Thread na posicao 1, com a imagem do trem 2, o slider do trem 2 e o controle de colisao escolhido pelo usuario
        break;

      default:
        break;
    }

    train1Thread.start();//Inicia a thread do trem 1
    train2Thread.start();//Inicia o thread do trem 2


  }//Fim do metodo initialize

  /* ***************************************************************
  * Metodo: getEscolha
  * Funcao: Metodo que e chamado quando o usuario escolhe uma opcao na choicebox, permitindo assim que a animacao mude caso o usuario escolha uma nova posicao inicial para os trens
  * Parametros: um ActionEvent event que e o evento de escolher uma opcao na choicebox
  * Retorno: void
  *************************************************************** */
  public void getEscolha(ActionEvent event) {
    if (dataTransfer.getEscolha() != choicebox_tela2_1.getSelectionModel().getSelectedIndex()){//Verifica se a escolha do usuario eh diferente da escolha que ele fez anteriormente

      dataTransfer.setEscolha(choicebox_tela2_1.getSelectionModel().getSelectedIndex());//Recebe a escolha do usuario quando ele escolhe uma opcao na choicebox e define a essa escolha no objeto dataTransfer do padrao Singleton, para quando voltarmos a tela 1 a escolha do usuario seja mantida

      posicionarTrens();//Chama o metodo posicionarTrens que reinicia a animacao e define a posicao inicial dos trens de acordo com a nova escolha do usuario

    }//Fim do if
  }//Fim do metodo getEscolha

  /* ***************************************************************
  * Metodo: getColisao
  * Funcao: Metodo que e chamado quando o usuario escolhe uma opcao na choicebox de controle de colisao, permitindo assim que a animacao mude caso o usuario escolha um novo controle de colisao para os trens
  * Parametros: um ActionEvent event que e o evento de escolher uma opcao na choicebox
  * Retorno: void
  *************************************************************** */
  public void getColisao(ActionEvent event) {
    if (dataTransfer.getControle() != choicebox_tela2_2.getSelectionModel().getSelectedIndex()){//Verifica se a escolha do usuario eh diferente da escolha que ele fez anteriormente

      dataTransfer.setControle(choicebox_tela2_2.getSelectionModel().getSelectedIndex());//Recebe a escolha do usuario quando ele escolhe uma opcao na choicebox e define a essa escolha no objeto dataTransfer do padrao Singleton, para quando voltarmos a tela 1 a escolha do usuario seja mantida

      posicionarTrens();//Chama o metodo posicionarTrens que reinicia a animacao e define a posicao inicial dos trens de acordo com a nova escolha do usuario

    }//Fim do if
  }//Fim do metodo getColisao

  @FXML//Anotacao que indica que o metodo voltar eh um metodo de acao que sera chamado quando a imagem voltarTelaInicial_imagem for clicada
  /* ***************************************************************
  * Metodo: voltar
  * Funcao: Metodo que e chamado quando a imagem voltarTelaInicial_imagem eh clicada, permitindo assim que o usuario retorne para a tela 1, e possa escolher novamente a posicao inicial dos trens
  * Parametros: um MouseEvent event que e o evento de clicar na imagem voltarTelaInicial_imagem
  * Retorno: void
  *************************************************************** */
  public void voltar(MouseEvent event) throws IOException {

    //Necessário chamar esses metodos e modificar o valor das variaveis de travamento para evitar erros na volta do usuario da tela 1 para a tela 2
    train1Thread.getTimerDeMovimento().stop();//Para o timer de movimento do trem 1
    train2Thread.getTimerDeMovimento().stop();//Para o timer de movimento do trem 2
    //Reinicia as variaveis de travamento e interesse nas regioes para que a proxima vez que o usuario escolha uma nova posicao inicial para os trens, os trens possam se movimentar corretamente
    variavelDeTravamento1 = 0;
    variavelDeTravamento2 = 0;
    turno1EA = 0;
    turno2EA = 0;
    interesseNaRegiao1[0] = false;
    interesseNaRegiao1[1] = false;
    interesseNaRegiao2[0] = false;
    interesseNaRegiao2[1] = false;

    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/tela1.fxml"));//Carrega o fxml da primeira tela

    Parent root = loader.load();//Define o root com o fxml e junto a isso carrega o controller da tela por meio do metodo load

    Scene scene = new Scene(root);//Define a cena com o root

    stageAtual = (Stage) ((Node) event.getSource()).getScene().getWindow();//Define o Stage com a cena, obtendo o mesmo stage do botao voltar da tela 2 e modificando apenas a cena
    
    Image icon = new Image(getClass().getResourceAsStream("../assets/trem_icon.png"));// Define o icone do Stage

    proximoStage = new Stage();//Cria um novo Stage
    proximoStage.setScene(scene);//Define a cena no proximoStage
    proximoStage.setTitle("Simulador de Trens");//Define o titulo do proximoStage
    proximoStage.getIcons().add(icon);// Adiciona o icone ao Stage
    proximoStage.resizableProperty().setValue(false);;// Define o Stage como nao redimensionavel, para evitar erros com o layout e as animacoes
    proximoStage.initStyle(StageStyle.UNDECORATED);// Define o estilo do Stage como sem bordas para uma estilização melhor do programa
    proximoStage.show();//Mostra o proximoStage

    stageAtual.close();//Fecha o stageAtual

  }//Fim do metodo voltar

  @FXML//Anotacao que indica que o metodo resetar eh um metodo de acao que sera chamado quando a imagem reset_imagem for clicada
  /* ***************************************************************
  * Metodo: resetar
  * Funcao: Metodo que e chamado quando a imagem reset_imagem eh clicada, permitindo assim que o usuario reinicie a animacao de acordo a posicao dos trens escolhida , e possa escolher novamente a posicao inicial dos trens
  * Parametros: um MouseEvent event que e o evento de clicar na imagem reset_imagem
  * Retorno: void
  *************************************************************** */
  public void resetar(MouseEvent event) throws IOException {
    
    posicionarTrens();//Chama o metodo posicionarTrens que define a posicao inicial dos trens de acordo com a escolha do usuario além de reiniciar a animacao, Threads e velocidade dos trens

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
  * Funcao: Metodo que define a posicao inicial dos trens de acordo com a escolha do usuario no momento em que ele e chamado, junto a isso reinicia o valor da velocidade dos trens, reinicia as threads e os timers de movimento dos trens, garantindo a execucao correta da animacao
  * Parametros: nenhum
  * Retorno: void
  * Observacoes: Esse metodo e chamado no metodo initialize, no metodo resetar e quando o usuario faz alguma acao na choicebox logo ele eh responsavel por definir a posicao inicial dos trens de acordo com a escolha do usuario sobre as posicoes dos trens no momento em que ele eh chamado
  *************************************************************** */
  private void posicionarTrens() {

    train1Thread.getTimerDeMovimento().stop();
    train2Thread.getTimerDeMovimento().stop();

    switch (dataTransfer.getEscolha()) {//Switch que define a posicao inicial dos trens de acordo com a escolha do usuario na tela 1


      case 0://Caso a escolha seja 0 a imagem do trem 1 sera posicionado no canto superior esquerdo e a imagem do trem 2 no canto superior direito
        
        train1Thread = new Train1Thread(0, train1_imagem, slider_trem1, dataTransfer.getControle());//Instancia o objeto train1Thread da classe Train1Thread na posicao 0, com a imagem do trem 1, o slider do trem 1 e o controle de colisao escolhido pelo usuario
        //Define a posicao inicial do trem 1 como 0, a solucao de controle de colisao escolhida pelo usuario e reseta a posicao do trem 1, isso e necessario para que o trem 1 possa se movimentar corretamente apos a mudanca de posicao
        train1Thread.setPosicaoInicial(0);
        train1Thread.setSolucao(dataTransfer.getControle());
        train1Thread.resetPosicao();

        train2Thread = new Train2Thread(1, train2_imagem, slider_trem2, dataTransfer.getControle());//Instancia o objeto train2Thread da classe Train2Thread na posicao 1, com a imagem do trem 2, o slider do trem 2 e o controle de colisao escolhido pelo usuario
        //Define a posicao inicial do trem 2 como 1, a solucao de controle de colisao escolhida pelo usuario e reseta a posicao do trem 2, isso e necessario para que o trem 2 possa se movimentar corretamente apos a mudanca de posicao
        train2Thread.setPosicaoInicial(1);
        train2Thread.setSolucao(dataTransfer.getControle());
        train2Thread.resetPosicao();
      
      break;

      case 1://Caso a escolha seja 1 a imagem do trem 1 sera posicionado no canto inferior esquerdo e a imagem do trem 2 no canto inferior direito

        train1Thread = new Train1Thread(0, train1_imagem, slider_trem1, dataTransfer.getControle());//Instancia o objeto train1Thread da classe Train1Thread na posicao 0, com a imagem do trem 1, o slider do trem 1 e o controle de colisao escolhido pelo usuario
        //Define a posicao inicial do trem 1 como 2, a solucao de controle de colisao escolhida pelo usuario e reseta a posicao do trem 1, isso e necessario para que o trem 1 possa se movimentar corretamente apos a mudanca de posicao
        train1Thread.setPosicaoInicial(2);
        train1Thread.setSolucao(dataTransfer.getControle());
        train1Thread.resetPosicao();

        train2Thread = new Train2Thread(1, train2_imagem, slider_trem2, dataTransfer.getControle());//Instancia o objeto train2Thread da classe Train2Thread na posicao 1, com a imagem do trem 2, o slider do trem 2 e o controle de colisao escolhido pelo usuario
        //Define a posicao inicial do trem 2 como 3, a solucao de controle de colisao escolhida pelo usuario e reseta a posicao do trem 2, isso e necessario para que o trem 2 possa se movimentar corretamente apos a mudanca de posicao
        train2Thread.setPosicaoInicial(3);
        train2Thread.setSolucao(dataTransfer.getControle());
        train2Thread.resetPosicao();
        
      break;

      case 2://Caso a escolha seja 2 a imagem do trem 1 sera posicionado no canto superior esquerdo e a imagem do trem 2 no canto inferior direito

        train1Thread = new Train1Thread(0, train1_imagem, slider_trem1, dataTransfer.getControle());//Instancia o objeto train1Thread da classe Train1Thread na posicao 0, com a imagem do trem 1, o slider do trem 1 e o controle de colisao escolhido pelo usuario
        //Define a posicao inicial do trem 1 como 0, a solucao de controle de colisao escolhida pelo usuario e reseta a posicao do trem 1, isso e necessario para que o trem 1 possa se movimentar corretamente apos a mudanca de posicao
        train1Thread.setPosicaoInicial(0);
        train1Thread.setSolucao(dataTransfer.getControle());
        train1Thread.resetPosicao();

        train2Thread = new Train2Thread(1, train2_imagem, slider_trem2, dataTransfer.getControle());//Instancia o objeto train2Thread da classe Train2Thread na posicao 1, com a imagem do trem 2, o slider do trem 2 e o controle de colisao escolhido pelo usuario
        //Define a posicao inicial do trem 2 como 3, a solucao de controle de colisao escolhida pelo usuario e reseta a posicao do trem 2, isso e necessario para que o trem 2 possa se movimentar corretamente apos a mudanca de posicao
        train2Thread.setPosicaoInicial(3);
        train2Thread.setSolucao(dataTransfer.getControle());
        train2Thread.resetPosicao();

      break;
      
      case 3://Caso a escolha seja 3 a imagem do trem 1 sera posicionado no canto inferior esquerdo e a imagem do trem 2 no canto superior direito

        train1Thread = new Train1Thread(0, train1_imagem, slider_trem1, dataTransfer.getControle());//Instancia o objeto train1Thread da classe Train1Thread na posicao 0, com a imagem do trem 1, o slider do trem 1 e o controle de colisao escolhido pelo usuario
        //Define a posicao inicial do trem 1 como 2, a solucao de controle de colisao escolhida pelo usuario e reseta a posicao do trem 1, isso e necessario para que o trem 1 possa se movimentar corretamente apos a mudanca de posicao
        train1Thread.setPosicaoInicial(2);
        train1Thread.setSolucao(dataTransfer.getControle());
        train1Thread.resetPosicao();

        train2Thread = new Train2Thread(1, train2_imagem, slider_trem2, dataTransfer.getControle());//Instancia o objeto train2Thread da classe Train2Thread na posicao 1, com a imagem do trem 2, o slider do trem 2 e o controle de colisao escolhido pelo usuario
        //Define a posicao inicial do trem 2 como 1, a solucao de controle de colisao escolhida pelo usuario e reseta a posicao do trem 2, isso e necessario para que o trem 2 possa se movimentar corretamente apos a mudanca de posicao
        train2Thread.setPosicaoInicial(1);
        train2Thread.setSolucao(dataTransfer.getControle());
        train2Thread.resetPosicao();
        
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

      train1Thread.start();//Inicia a thread do trem 1
      train2Thread.start();//Inicia a thread do trem 2

      //Reinicia as variaveis de travamento e interesse nas regioes para que a proxima vez que o usuario escolha uma nova posicao inicial para os trens, os trens possam se movimentar corretamente
      variavelDeTravamento1 = 0;
      variavelDeTravamento2 = 0;
      turno1EA = 0;
      turno2EA = 0;
      interesseNaRegiao1[0] = false;
      interesseNaRegiao1[1] = false;
      interesseNaRegiao2[0] = false;
      interesseNaRegiao2[1] = false;

  }//Fim do metodo posicionarTrens

}//Fim da classe ControllerTela2
