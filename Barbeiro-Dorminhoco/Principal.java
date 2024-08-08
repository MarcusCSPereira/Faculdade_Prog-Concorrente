/* ***************************************************************
* Autor............: Marcus César Santos Pereira
* Matricula........: 202310279
* Inicio...........: 07/06/2024
* Ultima alteracao.: 07/06/2024
* Nome.............: Principal do Problema Barbeiro Dorminhoco
* Funcao...........: Realiza as importacoes necessarias carrega o
fxml da primeira tela e inicia o programa
*************************************************************** */
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import control.BackgroundScreenController;//importando o controlador da tela principal para poder compilar

public class Principal extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/view/backgroundScreen.fxml"));//Carrega o fxml da tela primaria salvando na variavel root
    Scene scene = new Scene(root);//Cria uma nova cena definindo nela o fxml carregado

    stage.setTitle("Barbearia do Careca");//Define o titulo da janela da aplicacao
    stage.setScene(scene);//Seta a cena com o fxml carregado no stage
    stage.setResizable(false);//Define a tela como nao redimensionavel
    stage.initStyle(StageStyle.UNDECORATED);//retira a decoracao da pagina para que o usuario nao feche a janela sem finalizar as threads
    stage.show();//Mostra o stage
  }

  public static void main(String[] args) throws Exception {
    launch(args);
  }
}//fim classe Principal