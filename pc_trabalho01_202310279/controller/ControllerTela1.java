/* ***************************************************************
* Autor............: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio...........: 13/03/2023
* Ultima alteracao.: 13/03/2023
* Nome.............: ControllerTela1.java
* Funcao...........: Controla a primeira tela do simulador de trens, definindo a posicao inicial dos trens e carregando a proxima tela do simulador
*************************************************************** */

// Importacoes necessarias para a execucao do programa
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import util.DataTransfer;
import javafx.scene.Node;

public class ControllerTela1 implements Initializable {

  @FXML
  private ChoiceBox<String> choicebox_tela1;// Injeta o id da choicebox do arquivo tela1.fxml que sera utilizada para escolher a posicao inicial dos trens

  DataTransfer dataTransfer = DataTransfer.getInstance();// Cria uma instancia estilo Singleton de DataTransfer para poder passar a escolha do usuario para a proxima tela

  @FXML
  private ImageView botaoDeFechar;// Injeta o id do botao de fechar do arquivo tela1.fxml que sera utilizado para fechar a aplicacao

  /* ***************************************************************
  * Metodo: fecharPrograma
  * Funcao: Fecha a aplicacao quando o usuario clica no botao de fechar
  * Parametros: um MouseEvent event que e o evento de clicar no botao de fechar
  * Retorno: void
  *************************************************************** */
  @FXML
  public void fecharPrograma(MouseEvent event) {
    System.exit(0);
  }// Fim do metodo fecharPrograma


  /* ***************************************************************
  * Metodo: start
  * Funcao: Inicializa a aplicacao carregando o segundo fxml no nosso Stage passando a escolha do usuario da posicao inicial dos trens
  * Parametros: um ActionEvent event que e o evento de clicar no botao start
  * Retorno: void
  *************************************************************** */
  @FXML
  public void start(ActionEvent event) throws IOException {

    dataTransfer.setEscolha(choicebox_tela1.getSelectionModel().getSelectedIndex());// Define a escolha do usuario para a proxima tela por meio do metodo setChoice do DataTransfer que e um Singleton

    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/tela2.fxml"));// Carrega o fxml da segunda tela
    Parent root = loader.load();// Define o root com o fxml e junto a isso carrega o controller da tela por meio do metodo load
    Scene scene = new Scene(root);// Define a cena com o root

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();// Define o Stage com a cena, obtendo o mesmo stage do botao start da tela 1 e modificando apenas a cena
    stage.setScene(scene);// Define o Stage com a cena

    stage.show();// Mostra o Stage para o usuario contendo agora a tela 2

  }// Fim do metodo start

  String[] opcoesDeInicializacao = { "Superior esquerdo / Superior direito", "Inferior esquerdo / Inferior direito","Superior esquedo / Inferior direito", "Inferior esquerdo / Superior direito" };// Vetor de opcoes de inicializacao dos trens para serem exibidos na choicebox

  /* ***************************************************************
  * Metodo: initialize
  * Funcao: E util para que antes que essa tela seja exebida a aplicacao carregue as opcoes de inicializacao dos trens na choicebox
  * Parametros: um URL location e um ResourceBundle resources, que sao parametros padroes do metodo initialize
  * Retorno: void
  *************************************************************** */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    choicebox_tela1.getItems().addAll(opcoesDeInicializacao);// Adiciona as opcoes de inicializacao dos trens na choicebox assim que a tela e carregada
    choicebox_tela1.setValue(opcoesDeInicializacao[dataTransfer.getEscolha()]);// Define a escolha do usuario na choicebox, para assim ter uma forma de comunicacao da tela 2 para a tela 1 e vice versa
  }// Fim do metodo initialize

}// Fim da classe Controller_tela1
