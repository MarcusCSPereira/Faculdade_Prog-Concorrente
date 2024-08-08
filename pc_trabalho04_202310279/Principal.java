/********************************************************************
* Autor: Marcus César Santos Pereira
* Matricula........: 202310279
* Inicio: 03/06/2024
* Ultima alteracao: 05/06/2024
* Nome: Principal.java
* Funcao: Carregar a cena inicial
********************************************************************/

// Importacoes necessarias para a execucao do programa
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.lang.System;
import javafx.stage.Stage;
//Importacoes do controller para garantir a compilacao correta do programa
import control.PrincipalController;
import javafx.stage.StageStyle;

@SuppressWarnings("unused")// Suprime os avisos de variaveis nao utilizadas, nesse caso, os imports dos controllers, mesmo esses imports nao estarem sendo utilizados nessa classe em especifico, eles sao necessarios para a compilacao correta do programa
public class Principal extends Application {

  /********************************************************************
   * Metodo: createContent
   * Funcao: carregar a tela principal da simulacao
   * Parametros: nenhum
   * Retorno: root(tipo Parent) = os elementos da cena para ser carregada no stage
   * principal, apos
   * ser liberado o inicio da simulacao
   ********************************************************************/
  private Parent createContent() throws Exception {
    // Carrega o arquivo fxml da tela principal
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/principal.fxml"));
    //Define o root como o arquivo fxml carregado
    Parent root = loader.load();
    return root;
  }

  /********************************************************************
   * Metodo: start
   * Funcao: metodo padrao que tem a funcao de definir o container da
   * aplicacao
   * Parametros: primaryStage = janela principal
   * Retorno: void
   ********************************************************************/
  @Override
  public void start(Stage primaryStage) throws Exception {

    //Cria a Scene com o conteudo criado
    Scene scene = new Scene(createContent());
    //Define para o Stage a Scene criada
    primaryStage.setScene(scene);
    //Definindo a aplicacao como sendo não redimensionavel e sem bordas para melhorar a experiencia do usuario
    primaryStage.setResizable(false);
    primaryStage.initStyle(StageStyle.UTILITY);
    Image icon = new Image(getClass().getResourceAsStream("./assets/icon.png"));// Define o a imagem que sera usada como icone do Stage
    primaryStage.getIcons().add(icon);// Adiciona o icone ao Stage
    primaryStage.setTitle("Programadores e Compiladores");
    //Faz a aplicacao ser exibida
    primaryStage.show();
  }

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