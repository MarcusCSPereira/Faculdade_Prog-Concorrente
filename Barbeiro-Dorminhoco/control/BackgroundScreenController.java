package control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import threads.Barbeiro;
import threads.GeradoraClientes;

public class BackgroundScreenController implements Initializable {
  @FXML
  private ImageView clienteSentado1;
  @FXML
  private ImageView clienteSentado2;
  @FXML
  private ImageView clienteSentado3;
  @FXML
  private ImageView clienteSentado4;
  @FXML
  private ImageView clienteSentado5;
  @FXML 
  private ImageView clienteEspiando;
  public ImageView getClienteEspiando() {
    return clienteEspiando;
  }
  @FXML
  private ImageView clienteCareca;
  public ImageView getClienteCareca() {
    return clienteCareca;
  }
  @FXML
  private ImageView clienteCortando;
  public ImageView getClienteCortando() {
    return clienteCortando;
  }


  public static ArrayList<ImageView> filaClientes = new ArrayList<ImageView>();

  @FXML
  private ImageView barbeiroCortando;
  @FXML
  private ImageView barbeiroDormindo;

  @FXML
  private ImageView closeButton;
  @FXML
  void finalizarTudo(MouseEvent event) {
    System.exit(0);
  }

  @FXML
  private ImageView clientesPauseButton;
  @FXML
  void pausarClientes(MouseEvent event) {
    if(geradoraClientes.pausarOuContinuar()){
      clientesPauseButton.setImage(new Image("/assets/resumeButton.png"));
    } else {
      clientesPauseButton.setImage(new Image("/assets/pauseButton.png"));
    }
  }
  @FXML
  private Slider clientesSpeedSlider;

  @FXML
  private ImageView barbeiroPauseButton;
  @FXML
  void pausarBarbeiro(MouseEvent event) {
    if(barbeiro.pausarOuContinuar()){
      barbeiroPauseButton.setImage(new Image("/assets/resumeButton.png"));
    } else {
      barbeiroPauseButton.setImage(new Image("/assets/pauseButton.png"));
    }
  }
  @FXML
  private Slider barbeiroSpeedSlider;

  @FXML
  private ImageView resetButton;
  @FXML
  void resetAll(MouseEvent event) {
    if(!GeradoraClientes.pausado){
      geradoraClientes.pausarOuContinuar();
    }
    barbeiro.finalizar();

    clientesPauseButton.setImage(new Image("/assets/pauseButton.png"));
    barbeiroPauseButton.setImage(new Image("/assets/pauseButton.png"));

    barbeiroSpeedSlider.setValue(30);
    clientesSpeedSlider.setValue(30);

    for (ImageView cliente : filaClientes){
      cliente.setVisible(false);
    }
    clienteCareca.setVisible(false);
    clienteCortando.setVisible(false);
    barbeiroCortando.setVisible(true);
    barbeiroDormindo.setVisible(false);

    clientes = new Semaphore(0);
    barbeiros = new Semaphore(0);
    mutex = new Semaphore(1);
    esperando = 0;

    barbeiro = new Barbeiro(barbeiroCortando, barbeiroDormindo, clienteCortando, clienteCareca, barbeiroSpeedSlider);
    barbeiro.start();
    geradoraClientes.pausarOuContinuar();
  }

  public void clienteEspiando(){
    clienteEspiando.setVisible(true);
  }


  public static final int CADEIRAS = 5;
  public static Semaphore clientes;
  public static Semaphore barbeiros;
  public static Semaphore mutex;
  public static int esperando;

  private Barbeiro barbeiro;
  private GeradoraClientes geradoraClientes;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    filaClientes.add(clienteSentado1);
    filaClientes.add(clienteSentado2);
    filaClientes.add(clienteSentado3);
    filaClientes.add(clienteSentado4);
    filaClientes.add(clienteSentado5);
    for (ImageView cliente : filaClientes){
      cliente.setVisible(false);
    }
    clienteCareca.setVisible(false);
    clienteCortando.setVisible(false);
    clienteEspiando.setVisible(false);
    barbeiroDormindo.setVisible(false);

    barbeiroSpeedSlider.setValue(30);
    clientesSpeedSlider.setValue(30);

    clientes = new Semaphore(0);
    barbeiros = new Semaphore(0);
    mutex = new Semaphore(1);
    esperando = 0;

    barbeiro = new Barbeiro(barbeiroCortando, barbeiroDormindo, clienteCortando, clienteCareca, barbeiroSpeedSlider);
    geradoraClientes = new GeradoraClientes(clientesSpeedSlider);
    geradoraClientes.setbSC(this);
    barbeiro.start();
    geradoraClientes.start();
  }
}