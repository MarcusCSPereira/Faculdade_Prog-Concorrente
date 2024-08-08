/* ***************************************************************
* Autor............: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio...........: 20/04/2023
* Ultima alteracao.: 29/04/2023
* Nome.............: Principal.java
* Funcao...........: Realiza as importacoes necessarias para a execucao do javaFX e inicia a aplicacao
inicializa a primeira tela carregando seu fxml e executando a aplicacao
*************************************************************** */

// Importacoes necessarias para a execucao do programa
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.ControllerTela1;// Importa o controller da tela principal para poder compilar o programa
import controller.ControllerTela2;// Importa o controller da tela secundaria para poder compilar o programa

@SuppressWarnings("unused")// Suprime os avisos de variaveis nao utilizadas, nesse caso, os imports dos controllers, mesmo esses imports nao estarem sendo utilizados nessa classe em especifico, eles sao necessarios para a compilacao correta do programa

public class Principal extends Application {

  /*
   * ***************************************************************
   * Metodo: start
   * Funcao: Inicializa a aplicacao carregando o primeiro fxml no nosso Stage
   * Parametros: um Stage primaryStage que e o palco da aplicacao
   * Retorno: void
   */
  @Override
  public void start(Stage primaryStage) throws Exception {

    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/tela1.fxml"));// Carrega o fxml da primeira tela
    Parent root = loader.load();// Define o root com o fxml e junto a isso carrega o controller da tela por meio do metodo load

    Scene scene = new Scene(root);// Define a cena com o root

    primaryStage.setScene(scene);// Define o Stage com a cena

    Image icon = new Image(getClass().getResourceAsStream("./assets/trem_icon.png"));// Define o icone do Stage
    primaryStage.getIcons().add(icon);// Adiciona o icone ao Stage
    primaryStage.resizableProperty().setValue(false);;// Define o Stage como nao redimensionavel, para evitar erros com o layout e as animacoes
    primaryStage.initStyle(StageStyle.UNDECORATED);// Define o estilo do Stage como sem bordas para uma estilização melhor do programa


    primaryStage.setTitle("Train Simulator");// Define o titulo do Stage
    primaryStage.show();// Mostra o Stage para o usuario

  }// Fim do metodo start

  /*
   * ***************************************************************
   * Metodo: main
   * Funcao: Inicializa a aplicacao quando chama o metodo launch
   * Parametros: um array de Strings chamado args
   * Retorno: void
   */
  public static void main(String[] args) {
    launch(args);
  }// Fim do metodo main

}// Fim da classe Principal
