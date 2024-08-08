/********************************************************************
* Autor: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio: 20/06/2024
* Ultima alteracao: 22/06/2024
* Nome: Principal.java
* Funcao: Carregar a cena inicial do programa e iniciar a aplicacao
********************************************************************/

// Importacao de bibliotecas necessarias
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.TelaController;

// Classe Principal
@SuppressWarnings("unused")// Suprimir avisos de variaveis nao utilizadas, nesse caso a importacao do controller que apesar de nao ser utilizada eh necessaria para compilacao do programa em si
public class Principal extends Application {

  
  /********************************************************************
   * Metodo: start
   * Funcao: metodo padrao que tem a funcao de definir o container da aplicacao e inciar a aplicacao com condicoes iniciais
   * Parametros: primaryStage = janela principal
   * Retorno: void
   ********************************************************************/
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/tela.fxml"));// Carregar o arquivo fxml
    Parent root = loader.load();// Passar o arquivo fxml para o root e carregar o initializable da tela
    Scene scene = new Scene(root);// Criar uma nova cena com o root
    primaryStage.setScene(scene);// Definir a cena no primaryStage
    primaryStage.setTitle("Transito Automato");// Definir o titulo da janela
    Image icon = new Image("./assets/icon.png");// Recebendo o icone da janela na image icons
    primaryStage.getIcons().add(icon);// Adicionando o icone na janela
    primaryStage.setResizable(false);// Definir que a janela nao pode ser redimensionada
    primaryStage.setFullScreen(false);// Definir que a janela nao pode ser maximizada
    primaryStage.setOnCloseRequest((e)->System.exit(0));// Definir que ao fechar a janela o programa sera encerrado
    primaryStage.initStyle(StageStyle.UTILITY);// Definir o estilo da janela, possuindo apenas o botao de fechar
    primaryStage.show();// Mostrar a janela
  }

  /********************************************************************
   * Metodo: main
   * Funcao: metodo principal que inicia a aplicacao
   * Parametros: args = argumentos passados por linha de comando
   * Retorno: void
   ********************************************************************/
  public static void main(String[] args) {
    launch(args);
  }

}