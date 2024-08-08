/********************************************************************
* Autor: Marcus César Santos Pereira
* Matricula........: 202310279
* Inicio: 03/06/2024
* Ultima alteracao: 05/06/2024
* Nome: PrincipalController.java
* Funcao: Gerenciar os objetos da cena e inicializar as threads, controlando a simulacao do problema dos leitores e escritores
********************************************************************/

package control;

//Importacao necessaria para o funcionamento do programa
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;
import javafx.application.Platform;
import javafx.fxml.FXML;
import java.lang.String;
import java.lang.Math;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.lang.System;
import javafx.scene.input.MouseEvent;
import model.Computador;
import model.Programador;


public class PrincipalController implements Initializable {

  //Atributos da classe, contendo os elementos da cena em FXML
  @FXML private ImageView close_button;
  @FXML private ImageView bg;
  @FXML private ImageView pause_button_leitor0;
  @FXML private ImageView pause_button_leitor1;
  @FXML private ImageView pause_button_leitor2;
  @FXML private ImageView pause_button_leitor3;
  @FXML private ImageView pause_button_leitor4;
  @FXML private ImageView pause_button_escritor0;
  @FXML private ImageView pause_button_escritor1;
  @FXML private ImageView pause_button_escritor2;
  @FXML private ImageView pause_button_escritor3;
  @FXML private ImageView pause_button_escritor4;
  @FXML private Label label0;
  @FXML private Label label1;
  @FXML private Label label2;
  @FXML private Label label3;
  @FXML private Label label4;
  @FXML private Label label5;
  @FXML private Label label6;
  @FXML private Label label7;
  @FXML private Label label_leitor0;
  @FXML private Label label_leitor1;
  @FXML private Label label_leitor2;
  @FXML private Label label_leitor3;
  @FXML private Label label_leitor4;
  @FXML private Label status_leitor0;
  @FXML private Label status_leitor1;
  @FXML private Label status_leitor2;
  @FXML private Label status_leitor3;
  @FXML private Label status_leitor4;
  @FXML private Label label_escritor0;
  @FXML private Label label_escritor1;
  @FXML private Label label_escritor2;
  @FXML private Label label_escritor3;
  @FXML private Label label_escritor4;
  @FXML private Label status_escritor0;
  @FXML private Label status_escritor1;
  @FXML private Label status_escritor2;
  @FXML private Label status_escritor3;
  @FXML private Label status_escritor4;
  @FXML private ProgressBar progress_bar_leitor0;
  @FXML private ProgressBar progress_bar_leitor1;
  @FXML private ProgressBar progress_bar_leitor2;
  @FXML private ProgressBar progress_bar_leitor3;
  @FXML private ProgressBar progress_bar_leitor4;
  @FXML private ProgressBar progress_bar_escritor0;
  @FXML private ProgressBar progress_bar_escritor1;
  @FXML private ProgressBar progress_bar_escritor2;
  @FXML private ProgressBar progress_bar_escritor3;
  @FXML private ProgressBar progress_bar_escritor4;
  @FXML private Slider slider_compilacao0;
  @FXML private Slider slider_compilacao1;
  @FXML private Slider slider_compilacao2;
  @FXML private Slider slider_compilacao3;
  @FXML private Slider slider_compilacao4;
  @FXML private Slider slider_leitura0;
  @FXML private Slider slider_leitura1;
  @FXML private Slider slider_leitura2;
  @FXML private Slider slider_leitura3;
  @FXML private Slider slider_leitura4;
  @FXML private Slider slider_raciocinio0;
  @FXML private Slider slider_raciocinio1;
  @FXML private Slider slider_raciocinio2;
  @FXML private Slider slider_raciocinio3;
  @FXML private Slider slider_raciocinio4;
  @FXML private Slider slider_escrevendo0;
  @FXML private Slider slider_escrevendo1;
  @FXML private Slider slider_escrevendo2;
  @FXML private Slider slider_escrevendo3;
  @FXML private Slider slider_escrevendo4;

  //Atributos da classe, contendo os semaforos necessarios para a execucao do problema, sendo um para o banco de dados(db) e outro para realizar operacoes criticas que controlarao o numero de leitores no banco de dados(mutex)
  public static Semaphore db = new Semaphore(1);
  public static Semaphore mutex = new Semaphore(1); 

  //ArrayList que contem os codigos escritos pelos programadores, para simbolizar aos leitores que eles leram o codigo para compilar
  public ArrayList<String> codigosEscritos = new ArrayList<>();
  //Array de Strings que contem os possiveis codigos que serao escritos pelos programadores, utilizamos aqui unicode para simbolizar a acentuacao e pontuacao das palavras para assim nao haver inconsistencias de acordo a plataforma que o programa for executado
  public String[] codigos = {
    "\u2022 C\u00F3digo em Java (1);",
    "\u2022 C\u00F3digo em JavaScript (1);",
    "\u2022 C\u00F3digo em C++ (1);",
    "\u2022 C\u00F3digo em Flutter (1);",
    "\u2022 C\u00F3digo em Pascal (1);",
    "\u2022 C\u00F3digo em Ruby (1);",
    "\u2022 C\u00F3digo em Brainfuck (1);",
    "\u2022 C\u00F3digo em Swift (1);"
  };

  //ArrayList que sera usado para que a cada execucao a saida dos codigos na base de dados seja aletoria porem sem repetir os codigos
  private List<String> codigosTemp = new ArrayList<>(Arrays.asList(codigos));
  //ArrayList que sera usado para controlar o numero de codigos daquele tipo ja foram escritos na Base de dados
  private int[] codigoAcimaDoLimite={1,1,1,1,1,1,1,1};
  //ArrayList que salva o valor do codigo que foi passado naquela posicao para que assim a operacao de manipulacao de String seja feita de forma correta
  private String[] codigoFixo={"","","","","","","",""};
  //ArrayList que sera responsavel por simbolizar o ultimo valor que sera ao numero de codigos de determinado tipo, nesse caso sera: (...)
  private String[] codigoFinal={"","","","","","","",""};

  //Declarando as Threads leitoras
  Computador leitor0;
  Computador leitor1;
  Computador leitor2;
  Computador leitor3;
  Computador leitor4;

  //Declarando as Threads escritoras
  Programador escritor0;
  Programador escritor1;
  Programador escritor2;
  Programador escritor3;
  Programador escritor4;

  //Variavel que controla o numero de leitores que estao lendo o codigo, para que assim o escritor nao possa escrever enquanto houver leitores lendo
  public static int leitores = 0;

  /********************************************************************
  * Metodo: mudaEstadoComputador
  * Funcao: mudar o estado do computador, se ele esta lendo ou compilando
  * Parametros: int id = id do computador, boolean state = estado do computador, boolean bool = se a label do computador esta visivel ou nao
  * Retorno: void
  ********************************************************************/
  public void mudaEstadoComputador(int id, boolean state, boolean bool){
    switch(id){
      case 0:{
        if(bool){
          if(state){
            label_leitor0.setText("Lendo...");
            label_leitor0.setVisible(true);
          }
          else{
            label_leitor0.setText("Compilando...");
            label_leitor0.setVisible(true);
          }
          
        }
        else{
          label_leitor0.setVisible(false);
        }
        break;   
      }
      case 1:{
        if(bool){
          if(state){
            label_leitor1.setText("Lendo...");
            label_leitor1.setVisible(true);
          }
          else{
            label_leitor1.setText("Compilando...");
            label_leitor1.setVisible(true);
          }
          
        }
        else{
          label_leitor1.setVisible(false);
        }
        break;   
      }
      case 2:{
        if(bool){
          if(state){
            label_leitor2.setText("Lendo...");
            label_leitor2.setVisible(true);
          }
          else{
            label_leitor2.setText("Compilando...");
            label_leitor2.setVisible(true);
          }
          
        }
        else{
          label_leitor2.setVisible(false);
        }
        break;   
      }
      case 3:{
        if(bool){
          if(state){
            label_leitor3.setText("Lendo...");
            label_leitor3.setVisible(true);
          }
          else{
            label_leitor3.setText("Compilando...");
            label_leitor3.setVisible(true);
          }
          
        }
        else{
          label_leitor3.setVisible(false);
        }
        break;   
      }
      case 4:{
        if(bool){
          if(state){
            label_leitor4.setText("Lendo...");
            label_leitor4.setVisible(true);
          }
          else{
            label_leitor4.setText("Compilando...");
            label_leitor4.setVisible(true);
          }
          
        }
        else{
          label_leitor4.setVisible(false);
        }
        break;   
      }
    }
    
  }

  /********************************************************************
  * Metodo: mudaEstadoProgramador
  * Funcao: mudar o estado do programador, se ele esta slider_raciocinio ou slider_escrevendo
  * Parametros: int id = id do programador, boolean state = estado do programador, boolean bool = se a label do programador esta visivel ou nao
  * Retorno: void
  ********************************************************************/
  public void mudaEstadoProgramador(int id, boolean state, boolean bool){
    switch(id){
      case 0:{
        if(bool){
          if(state){
            label_escritor0.setText("Raciocinando...");
            label_escritor0.setVisible(true);
          }
          else{
            label_escritor0.setText("Escrevendo...");
            label_escritor0.setVisible(true);
          }
          
        }
        else{
          label_escritor0.setVisible(false);
        }
        break;   
      }
      case 1:{
        if(bool){
          if(state){
            label_escritor1.setText("Raciocinando...");
            label_escritor1.setVisible(true);
          }
          else{
            label_escritor1.setText("Escrevendo...");
            label_escritor1.setVisible(true);
          }
          
        }
        else{
          label_escritor1.setVisible(false);
        }
        break;   
      }
      case 2:{
        if(bool){
          if(state){
            label_escritor2.setText("Raciocinando...");
            label_escritor2.setVisible(true);
          }
          else{
            label_escritor2.setText("Escrevendo...");
            label_escritor2.setVisible(true);
          }
          
        }
        else{
          label_escritor2.setVisible(false);
        }
        break;   
      }
      case 3:{
        if(bool){
          if(state){
            label_escritor3.setText("Raciocinando...");
            label_escritor3.setVisible(true);
          }
          else{
            label_escritor3.setText("Escrevendo...");
            label_escritor3.setVisible(true);
          }
          
        }
        else{
          label_escritor3.setVisible(false);
        }
        break;   
      }
      case 4:{
        if(bool){
          if(state){
            label_escritor4.setText("Raciocinando...");
            label_escritor4.setVisible(true);
          }
          else{
            label_escritor4.setText("Escrevendo...");
            label_escritor4.setVisible(true);
          }
          
        }
        else{
          label_escritor4.setVisible(false);
        }
        break;   
      }
    }
  }

  /********************************************************************
  * Metodo: mudarLabelStatusProgramador
  * Funcao: mudar a label do programador para demonstrar que se ele esta bloqueado pelo semaforo ou nao
  * Parametros: int id = id do programador, int opc = opcao para mostrar ou nao a label
  * Retorno: void
  ********************************************************************/
  public void mudarLabelStatusProgramador(int id, int opc){
    switch (id) {
      case 0:
      if(opc == 0){
        //Unicode para simbolizar a acentuacao e pontuacao das palavras
        status_escritor0.setText("Bloqueado pelo sem\u00E1foro\u0021");
        status_escritor0.setVisible(true);
      }else{
        status_escritor0.setVisible(false);
      }
        break;
      case 1:
      if(opc == 0){
        status_escritor1.setText("Bloqueado pelo sem\u00E1foro\u0021");
        status_escritor1.setVisible(true);
      }else{
        status_escritor1.setVisible(false);
      }
        break;
      case 2:
      if(opc == 0){
        status_escritor2.setText("Bloqueado pelo sem\u00E1foro\u0021");
        status_escritor2.setVisible(true);
      }else{
        status_escritor2.setVisible(false);
      }
        break;
      case 3:
      if(opc == 0){
        status_escritor3.setText("Bloqueado pelo sem\u00E1foro\u0021");
        status_escritor3.setVisible(true);
      }else{
        status_escritor3.setVisible(false);
      }
        break;
      case 4:
      if(opc == 0){
        status_escritor4.setText("Bloqueado pelo sem\u00E1foro\u0021");
        status_escritor4.setVisible(true);
      }else{
        status_escritor4.setVisible(false);
      }
        break;

      default:
        break;
    }
  }
  
  /********************************************************************
  * Metodo: mudarLabelStatusComputador
  * Funcao: mudar a label do leitor para demonstrar que se ele esta bloqueado pelo semaforo ou nao
  * Parametros: int id = id do leitor, int opc = opcao para mostrar ou nao a label
  * Retorno: void
  ********************************************************************/
  public void mudarLabelStatusComputador(int id, int opc){
    switch (id) {
      case 0:
      if(opc == 0){
        //Unicode para simbolizar a acentuacao e pontuacao das palavras
        status_leitor0.setText("Aguardando para ler do BD\u0021");
        status_leitor0.setVisible(true);
      }else{
        status_leitor0.setVisible(false);
      }
        break;
      case 1:
      if(opc == 0){
        status_leitor1.setText("Aguardando para ler do BD\u0021");
        status_leitor1.setVisible(true);
      }else{
        status_leitor1.setVisible(false);
      }
        break;
      case 2:
      if(opc == 0){
        status_leitor2.setText("Aguardando para ler do BD\u0021");
        status_leitor2.setVisible(true);
      }else{
        status_leitor2.setVisible(false);
      }
        break;
      case 3:
      if(opc == 0){
        status_leitor3.setText("Aguardando para ler do BD\u0021");
        status_leitor3.setVisible(true);
      }else{
        status_leitor3.setVisible(false);
      }
        break;
      case 4:
      if(opc == 0){
        status_leitor4.setText("Aguardando para ler do BD\u0021");
        status_leitor4.setVisible(true);
      }else{
        status_leitor4.setVisible(false);
      }
        break;

      default:
        break;
    }
  }

  /********************************************************************
  * Metodo: escreverNoDB
  * Funcao: escrever um codigo na base de dados, ou incrementar o contador de codigos ja escritos no banco de dados
  * Parametros: int id = id do programador
  * Retorno: void
  ********************************************************************/
  public void esceverNoDB(int id){
    try{
      //verifica se a lista de codigos que podem ser escritos nao esta vazia
      if (codigosTemp.isEmpty()) {
          codigosTemp = new ArrayList<>(Arrays.asList(codigos));
      }
      //sorteia um codigo para ser escrito, e o adiciona na lista de codigos escritos e retira ele da lista de codigos que podem ser escritos, dessa forma evitando repeticoes mas gerando uma ordem aleatoria a cada execucao
      int range = codigosTemp.size();
      int rand = (int)(Math.random() * range);
      String codigo = codigosTemp.get(rand);
      codigosEscritos.add(codigo);
      codigosTemp.remove(rand);
      //verifica a posicao do codigo na lista de codigos escritos por meio de uma operacao %, para que assim possa ser feita a manipulacao de String correta
      int x = (codigosEscritos.size()-1) % 8;
      switch(x){
        case 0:{
          //Isso sera algo que se repete em todos os cases, e o que ele faz é verificar se a label 7 esta visivel, e se o contador de codigos escritos naquela posicao nao ultrapassou o limite de 10 codigos, se a label 7 esta visivel porem o contador ainda nao passou do valor 10 , incrementa o contador de codigos escritos naquela posicao e atualiza a label com o codigo escrito, caso a label 7 esteja visivel e o contador ja tenha passado do valor 10, atualiza a label com o codigo final, que sera: (...)
          if (label7.isVisible() && codigoAcimaDoLimite[0] < 10) {
            codigoAcimaDoLimite[0]++;
            codigoFixo[0] = codigoFixo[0].substring(0, codigoFixo[0].length()-4) + "(" + codigoAcimaDoLimite[0] + ");";
            label0.setText(codigoFixo[0]);
          }else if(codigoAcimaDoLimite[0] >= 10){
            codigoFinal[0] = codigoFixo[0].substring(0, codigoFixo[0].length()-5) + "(...);";
            label0.setText(codigoFinal[0]);
          }else{
            codigoFixo[0] = codigo;
            label0.setText(codigoFixo[0]);
          }
          label0.setVisible(true);
          break;
        }
        case 1:{
          if (label7.isVisible() && codigoAcimaDoLimite[1] < 10) {
            codigoAcimaDoLimite[1]++;
            codigoFixo[1] = codigoFixo[1].substring(0, codigoFixo[1].length()-4) + "(" + codigoAcimaDoLimite[1] + ");";
            label1.setText(codigoFixo[1]);
          }else if(codigoAcimaDoLimite[1] >= 10){
            codigoFinal[1] = codigoFixo[1].substring(0, codigoFixo[1].length()-5) + "(...);";
            label1.setText(codigoFinal[1]);
          }else{
            codigoFixo[1] = codigo;
            label1.setText(codigoFixo[1]);
          }
          label1.setVisible(true);
          break;
        }
        case 2:{
          if (label7.isVisible() && codigoAcimaDoLimite[2] < 10) {
            codigoAcimaDoLimite[2]++;
            codigoFixo[2] = codigoFixo[2].substring(0, codigoFixo[2].length()-4) + "(" + codigoAcimaDoLimite[2] + ");";
            label2.setText(codigoFixo[2]);
          }else if(codigoAcimaDoLimite[2] >= 10){
            codigoFinal[2] = codigoFixo[2].substring(0, codigoFixo[2].length()-5) + "(...);";
            label2.setText(codigoFinal[2]);
          }else{
            codigoFixo[2] = codigo;
            label2.setText(codigoFixo[2]);
          }
          label2.setVisible(true);
          break;
        }
        case 3:{
          if (label7.isVisible() && codigoAcimaDoLimite[3] < 10) {
            codigoAcimaDoLimite[3]++;
            codigoFixo[3] = codigoFixo[3].substring(0, codigoFixo[3].length()-4) + "(" + codigoAcimaDoLimite[3] + ");";
            label3.setText(codigoFixo[3]);
          }else if(codigoAcimaDoLimite[3] >= 10){
            codigoFinal[3] = codigoFixo[3].substring(0, codigoFixo[3].length()-5) + "(...);";
            label3.setText(codigoFinal[3]);
          }else{
            codigoFixo[3] = codigo;
            label3.setText(codigoFixo[3]);
          }
          label3.setVisible(true);
          break;
        }
        case 4:{
          if (label7.isVisible() && codigoAcimaDoLimite[4] < 10) {
            codigoAcimaDoLimite[4]++;
            codigoFixo[4] = codigoFixo[4].substring(0, codigoFixo[4].length()-4) + "(" + codigoAcimaDoLimite[4] + ");";
            label4.setText(codigoFixo[4]);
          }else if(codigoAcimaDoLimite[4] >= 10){
            codigoFinal[4] = codigoFixo[4].substring(0, codigoFixo[4].length()-5) + "(...);";
            label4.setText(codigoFinal[4]);
          }else{
            codigoFixo[4] = codigo;
            label4.setText(codigoFixo[4]);
          }
          label4.setVisible(true);
          break;
        }
        case 5:{
          if (label7.isVisible() && codigoAcimaDoLimite[5] < 10) {
            codigoAcimaDoLimite[5]++;
            codigoFixo[5] = codigoFixo[5].substring(0, codigoFixo[5].length()-4) + "(" + codigoAcimaDoLimite[5] + ");";
            label5.setText(codigoFixo[5]);
          }else if(codigoAcimaDoLimite[5] >= 10){
            codigoFinal[5] = codigoFixo[5].substring(0, codigoFixo[5].length()-5) + "(...);";
            label5.setText(codigoFinal[5]);
          }else{
            codigoFixo[5] = codigo;
            label5.setText(codigoFixo[5]);
          }
          label5.setVisible(true);
          break;
        }
        case 6:{
          if (label7.isVisible() && codigoAcimaDoLimite[6] < 10) {
            codigoAcimaDoLimite[6]++;
            codigoFixo[6] = codigoFixo[6].substring(0, codigoFixo[6].length()-4) + "(" + codigoAcimaDoLimite[6] + ");";
            label6.setText(codigoFixo[6]);
          }else if(codigoAcimaDoLimite[6] >= 10){
            codigoFinal[6] = codigoFixo[6].substring(0, codigoFixo[6].length()-5) + "(...);";
            label6.setText(codigoFinal[6]);
          }else{
            codigoFixo[6] = codigo;
            label6.setText(codigoFixo[6]);
          }
          label6.setVisible(true);
          break;
        }
        case 7:{
          if (label7.isVisible() && codigoAcimaDoLimite[7] < 10) {
            codigoAcimaDoLimite[7]++;
            codigoFixo[7] = codigoFixo[7].substring(0, codigoFixo[7].length()-4) + "(" + codigoAcimaDoLimite[7] + ");";
            label7.setText(codigoFixo[7]);
          }else if(codigoAcimaDoLimite[7] >= 10){
            codigoFinal[7] = codigoFixo[7].substring(0, codigoFixo[7].length()-5) + "(...);";
            label7.setText(codigoFinal[7]);
          }else{
            codigoFixo[7] = codigo;
            label7.setText(codigoFixo[7]);
          }
          label7.setVisible(true);
          break;
        }
        default:{
          break;
        }
      }
    } catch(Exception e){
      System.out.println("Write code");
      System.out.println(e.getMessage());
    }
    
  }

  /********************************************************************
  * Metodo: lerCodigoDoDB
  * Funcao: Apos o computador fazer o processo de leitura em sua Thread ele chama esse metodo que sinaliza que um codigo da base de dados foi lido , e atualiza o valor da variavel booleana da Thread em questao para simbolizar que o leitor leu o codigo e agora pode compilar caso ele tenha lido algum valor na base de dados
  * Parametros: int id = id do leitor
  * Retorno: void
  ********************************************************************/
  public void lerCodigoDoDB(int id){
    try{
      //verifica se a lista de codigos escritos nao esta vazia, logo, verifica se o leitor leu algum codigo, se sim ele atualiza a variavel booleana da Thread para simbolizar que o leitor leu o codigo e agora pode compilar
      if(codigosEscritos.size() > 0){
        switch(id){
          case 0:{
            leitor0.codigoLido = true;
            break;
          }
          case 1:{
            leitor1.codigoLido = true;
            break;
          }
          case 2:{
            leitor2.codigoLido = true;
            break;
          }
          case 3:{
            leitor3.codigoLido = true;
            break;
          }
          case 4:{
            leitor4.codigoLido = true;
            break;
          }
        }
      }
    } catch(Exception e){
      System.out.println("Read code");
      System.out.println(e.getMessage());
    }
    
  }

  /********************************************************************
  * Metodo: compilarCodigo
  * Funcao: Apos o computador fazer o processo de compilacao em sua Thread ele chama esse metodo que sinaliza que um codigo da base de dados foi compilado , e atualiza o valor da variavel booleana da Thread em questao para simbolizar que o leitor compilou o codigo e agora pode ler outro codigo
  * Parametros: int id = id do leitor
  * Retorno: void
  ********************************************************************/
  public void compilarCodigo(int id){
      try{
          switch(id){
            case 0:{
              leitor0.podeLer = true;
              break;
            }
            case 1:{
              leitor1.podeLer = true;
              break;
            }
            case 2:{
              leitor2.podeLer = true;
              break;
            }
            case 3:{
              leitor3.podeLer = true;
              break;
            }
            case 4:{
              leitor4.podeLer = true;
              break;
            }
          }
      } catch (Exception e){
        System.out.println("compilar code");
        System.out.println(e.getMessage());
      } 
  }

  /********************************************************************
  * Metodo: pauseLeitor0
  * Funcao: Pausar ou despausar a Thread do leitor0, quando o usuario clicar no botao de pause dessa Thread
  * Parametros: MouseEvent event
  * Retorno: void
  ********************************************************************/
  @FXML
  void pauseleitor0(MouseEvent event) {
    Platform.runLater(() ->{
      if(!leitor0.getIsPaused()){
        pause_button_leitor0.setImage(new Image("/assets/resume_button.png"));
        leitor0.setIsPaused(true);
      }
      else{
        pause_button_leitor0.setImage(new Image("/assets/pause_button.png"));
        leitor0.setIsPaused(false);
      }
    });
  }

  /********************************************************************
  * Metodo: pauseLeitor1
  * Funcao: Pausar ou despausar a Thread do leitor1, quando o usuario clicar no botao de pause dessa Thread
  * Parametros: MouseEvent event
  * Retorno: void
  ********************************************************************/
  @FXML
  void pauseleitor1(MouseEvent event) {
    Platform.runLater(()->{
      if(!leitor1.getIsPaused()){
        pause_button_leitor1.setImage(new Image("/assets/resume_button.png"));
        leitor1.setIsPaused(true);
      }
      else{
        pause_button_leitor1.setImage(new Image("/assets/pause_button.png"));
        leitor1.setIsPaused(false);
      }
    });
  }

  /********************************************************************
  * Metodo: pauseLeitor2
  * Funcao: Pausar ou despausar a Thread do leitor2, quando o usuario clicar no botao de pause dessa Thread
  * Parametros: MouseEvent event
  * Retorno: void
  ********************************************************************/
  @FXML
  void pauseleitor2(MouseEvent event) {
    Platform.runLater(()->{
      if(!leitor2.getIsPaused()){
        pause_button_leitor2.setImage(new Image("/assets/resume_button.png"));
        leitor2.setIsPaused(true);
      }
      else{
        pause_button_leitor2.setImage(new Image("/assets/pause_button.png"));
        leitor2.setIsPaused(false);
      }
    });
  }

  /********************************************************************
  * Metodo: pauseLeitor3
  * Funcao: Pausar ou despausar a Thread do leitor3, quando o usuario clicar no botao de pause dessa Thread
  * Parametros: MouseEvent event
  * Retorno: void
  ********************************************************************/
  @FXML
  void pauseleitor3(MouseEvent event) {
    Platform.runLater(()->{
      if(!leitor3.getIsPaused()){
        pause_button_leitor3.setImage(new Image("/assets/resume_button.png"));
        leitor3.setIsPaused(true);
      }
      else{
        pause_button_leitor3.setImage(new Image("/assets/pause_button.png"));
        leitor3.setIsPaused(false);
      }
    });
  }

  /********************************************************************
  * Metodo: pauseLeitor4
  * Funcao: Pausar ou despausar a Thread do leitor4, quando o usuario clicar no botao de pause dessa Thread
  * Parametros: MouseEvent event
  * Retorno: void
  ********************************************************************/
  @FXML
  void pauseleitor4(MouseEvent event) {
    Platform.runLater(()->{
      if(!leitor4.getIsPaused()){
        pause_button_leitor4.setImage(new Image("/assets/resume_button.png"));
        leitor4.setIsPaused(true);
      }
      else{
        pause_button_leitor4.setImage(new Image("/assets/pause_button.png"));
        leitor4.setIsPaused(false);
      }
    });
  }

  /********************************************************************
  * Metodo: pauseEscritor0
  * Funcao: Pausar ou despausar a Thread do escritor0, quando o usuario clicar no botao de pause dessa Thread
  * Parametros: MouseEvent event
  * Retorno: void
  ********************************************************************/
  @FXML
  void pauseescritor0(MouseEvent event) {
    Platform.runLater(()->{
      if(!escritor0.getIsPaused()){
        pause_button_escritor0.setImage(new Image("/assets/resume_button.png"));
        escritor0.setIsPaused(true);
      }
      else{
        pause_button_escritor0.setImage(new Image("/assets/pause_button.png"));
        escritor0.setIsPaused(false);
      }
    });
  }

  /********************************************************************
  * Metodo: pauseEscritor1
  * Funcao: Pausar ou despausar a Thread do escritor1, quando o usuario clicar no botao de pause dessa Thread
  * Parametros: MouseEvent event
  * Retorno: void
  ********************************************************************/
  @FXML
  void pauseescritor1(MouseEvent event) {
    Platform.runLater(()->{
      if(!escritor1.getIsPaused()){
        pause_button_escritor1.setImage(new Image("/assets/resume_button.png"));
        escritor1.setIsPaused(true);
      }
      else{
        pause_button_escritor1.setImage(new Image("/assets/pause_button.png"));
        escritor1.setIsPaused(false);
      }
    });
  }

  /********************************************************************
  * Metodo: pauseEscritor2
  * Funcao: Pausar ou despausar a Thread do escritor2, quando o usuario clicar no botao de pause dessa Thread
  * Parametros: MouseEvent event
  * Retorno: void
  ********************************************************************/
  @FXML
  void pauseescritor2(MouseEvent event) {
    Platform.runLater(()->{
      if(!escritor2.getIsPaused()){
        pause_button_escritor2.setImage(new Image("/assets/resume_button.png"));
        escritor2.setIsPaused(true);
      }
      else{
        pause_button_escritor2.setImage(new Image("/assets/pause_button.png"));
        escritor2.setIsPaused(false);
      }
    });
  }

  /********************************************************************
  * Metodo: pauseEscritor3
  * Funcao: Pausar ou despausar a Thread do escritor3, quando o usuario clicar no botao de pause dessa Thread
  * Parametros: MouseEvent event
  * Retorno: void
  ********************************************************************/
  @FXML
  void pauseescritor3(MouseEvent event) {
    Platform.runLater(()->{
      if(!escritor3.getIsPaused()){
        pause_button_escritor3.setImage(new Image("/assets/resume_button.png"));
        escritor3.setIsPaused(true);
      }
      else{
        pause_button_escritor3.setImage(new Image("/assets/pause_button.png"));
        escritor3.setIsPaused(false);
      }
    });
  }

  /********************************************************************
  * Metodo: pauseEscritor4
  * Funcao: Pausar ou despausar a Thread do escritor4, quando o usuario clicar no botao de pause dessa Thread
  * Parametros: MouseEvent event
  * Retorno: void
  ********************************************************************/
  @FXML
  void pauseescritor4(MouseEvent event) {
    Platform.runLater(()->{
      if(!escritor4.getIsPaused()){
        pause_button_escritor4.setImage(new Image("/assets/resume_button.png"));
        escritor4.setIsPaused(true);
      }
      else{
        pause_button_escritor4.setImage(new Image("/assets/pause_button.png"));
        escritor4.setIsPaused(false);
      }
    });
  }

  /********************************************************************
  * Metodo: close
  * Funcao: Fechar a aplicacao quando o usuario clicar no botao de fechar
  * Parametros: MouseEvent event
  * Retorno: void
  ********************************************************************/
  @FXML
  void close(MouseEvent event) {
    System.exit(0);
  }

  /********************************************************************
  * Metodo: reset
  * Funcao: Resetar a aplicacao quando o ususario clicar no botao de resetar, voltando todos os valores para o estado inicial, e reiniciando as Threads para possa reiniciar a aplicacao sem a necessidade de fechar e abrir novamente
  * Parametros: MouseEvent event
  * Retorno: void
  ********************************************************************/
  @FXML
  void reset(MouseEvent event) {
    //Define o reset como true para que as Threads possam ser reiniciadas e pararem a execucao de suas tarefas
    leitor0.setReset(true);
    leitor1.setReset(true);
    leitor2.setReset(true);
    leitor3.setReset(true);
    leitor4.setReset(true);
    escritor0.setReset(true);
    escritor1.setReset(true);
    escritor2.setReset(true);
    escritor3.setReset(true);
    escritor4.setReset(true);
    
    //Reinicia as Threads instanciando novas Threads
    leitor0 = new Computador(this, 0, slider_leitura0, slider_compilacao0, progress_bar_leitor0);
    leitor1 = new Computador(this, 1, slider_leitura1, slider_compilacao1, progress_bar_leitor1);
    leitor2 = new Computador(this, 2, slider_leitura2, slider_compilacao2, progress_bar_leitor2);
    leitor3 = new Computador(this, 3, slider_leitura3, slider_compilacao3, progress_bar_leitor3);
    leitor4 = new Computador(this, 4, slider_leitura4, slider_compilacao4, progress_bar_leitor4);
    escritor0 = new Programador(this, 0, slider_raciocinio0, slider_escrevendo0, progress_bar_escritor0);
    escritor1 = new Programador(this, 1, slider_raciocinio1, slider_escrevendo1, progress_bar_escritor1);
    escritor2 = new Programador(this, 2, slider_raciocinio2, slider_escrevendo2, progress_bar_escritor2);
    escritor3 = new Programador(this, 3, slider_raciocinio3, slider_escrevendo3, progress_bar_escritor3);
    escritor4 = new Programador(this, 4, slider_raciocinio4, slider_escrevendo4, progress_bar_escritor4);
    
    Platform.runLater(()-> {
      //Ocultar todas as progress bars
      progress_bar_leitor0.setVisible(false);
      progress_bar_leitor1.setVisible(false);
      progress_bar_leitor2.setVisible(false);
      progress_bar_leitor3.setVisible(false);
      progress_bar_leitor4.setVisible(false);
      progress_bar_escritor0.setVisible(false);
      progress_bar_escritor1.setVisible(false);
      progress_bar_escritor2.setVisible(false);
      progress_bar_escritor3.setVisible(false);
      progress_bar_escritor4.setVisible(false);

      //Reinicia as imagens dos botoes de pause para a imagem incial
      pause_button_leitor0.setImage(new Image("/assets/pause_button.png"));
      pause_button_leitor1.setImage(new Image("/assets/pause_button.png"));
      pause_button_leitor2.setImage(new Image("/assets/pause_button.png"));
      pause_button_leitor3.setImage(new Image("/assets/pause_button.png"));
      pause_button_leitor4.setImage(new Image("/assets/pause_button.png"));
      pause_button_escritor0.setImage(new Image("/assets/pause_button.png"));
      pause_button_escritor1.setImage(new Image("/assets/pause_button.png"));
      pause_button_escritor2.setImage(new Image("/assets/pause_button.png"));
      pause_button_escritor3.setImage(new Image("/assets/pause_button.png"));
      pause_button_escritor4.setImage(new Image("/assets/pause_button.png"));

      //Reinicia os valores dos sliders
      slider_raciocinio0.setValue(50);
      slider_raciocinio1.setValue(50);
      slider_raciocinio2.setValue(50);
      slider_raciocinio3.setValue(50);
      slider_raciocinio4.setValue(50);
      slider_escrevendo0.setValue(50);
      slider_escrevendo1.setValue(50);
      slider_escrevendo2.setValue(50);
      slider_escrevendo3.setValue(50);
      slider_escrevendo4.setValue(50);
      slider_leitura0.setValue(50);
      slider_leitura1.setValue(50);
      slider_leitura2.setValue(50);
      slider_leitura3.setValue(50);
      slider_leitura4.setValue(50);
      slider_compilacao0.setValue(50);
      slider_compilacao1.setValue(50);
      slider_compilacao2.setValue(50);
      slider_compilacao3.setValue(50);
      slider_compilacao4.setValue(50);
      
      //Ocultar todas as labels da Base de Dados
      label0.setVisible(false);
      label1.setVisible(false);
      label2.setVisible(false);
      label3.setVisible(false);
      label4.setVisible(false);
      label5.setVisible(false);
      label6.setVisible(false);
      label7.setVisible(false);

      //Ocultar todas as labels de status dos programadores e leitores
      status_escritor0.setVisible(false);
      status_escritor1.setVisible(false);
      status_escritor2.setVisible(false);
      status_escritor3.setVisible(false);
      status_escritor4.setVisible(false);
      status_leitor0.setVisible(false);
      status_leitor1.setVisible(false);
      status_leitor2.setVisible(false);
      status_leitor3.setVisible(false);
      status_leitor4.setVisible(false);

      //Ocultar todas as labels de leitores e programadores
      label_leitor0.setVisible(false);
      label_leitor1.setVisible(false);
      label_leitor2.setVisible(false);
      label_leitor3.setVisible(false);
      label_leitor4.setVisible(false);
      label_escritor0.setVisible(false);
      label_escritor1.setVisible(false);
      label_escritor2.setVisible(false);
      label_escritor3.setVisible(false);
      label_escritor4.setVisible(false);
    });

    //Reinicia e limpa o ArrayList de codigos escritos no BD
    codigosEscritos.clear();

    //Reinicia os semaforos instanciando novos semaforos
    mutex = new Semaphore(1);
    db = new Semaphore(1);

    //Reincia a variavel leitores que conta quantos leitores estao lendo no momento
    leitores = 0;
    //Reinicia o ArrayList de codigos temporarios que determina os codigos que podem ser sorteados e escritos no BD
    codigosTemp = new ArrayList<>(Arrays.asList(codigos));
    //Reinicia os contadores de codigos escritos acima do limite e os codigos fixos e finais por meio de um loop for
    for(int i =0; i<8; i++){
      codigoAcimaDoLimite[i]=1;
      codigoFixo[i]="";
      codigoFinal[i]="";
    }

    //Reinicia as Threads executando-as, afinal quando usamos o metodo setReset(true) elas param de executar por um break no metodo run
    escritor0.start();
    escritor1.start();
    escritor2.start();
    escritor3.start();
    escritor4.start();
    leitor0.start();
    leitor1.start();
    leitor2.start();
    leitor3.start();
    leitor4.start();
  }

  /********************************************************************
  * Metodo: initialize
  * Funcao: Metodo que eh executado quando a aplicacao é inicializada, instanciando as Threads e iniciando-as, comecando a execucao do programa
  * Parametros: URL arg0, ResourceBundle rb (padroes)
  * Retorno: void
  ********************************************************************/
  @Override
  public void initialize(URL arg0, ResourceBundle rb) {
    escritor0 = new Programador(this, 0, slider_raciocinio0, slider_escrevendo0, progress_bar_escritor0);
    escritor1 = new Programador(this, 1, slider_raciocinio1, slider_escrevendo1, progress_bar_escritor1);
    escritor2 = new Programador(this, 2, slider_raciocinio2, slider_escrevendo2, progress_bar_escritor2);
    escritor3 = new Programador(this, 3, slider_raciocinio3, slider_escrevendo3, progress_bar_escritor3);
    escritor4 = new Programador(this, 4, slider_raciocinio4, slider_escrevendo4, progress_bar_escritor4);
    leitor0 = new Computador(this, 0, slider_leitura0, slider_compilacao0, progress_bar_leitor0);
    leitor1 = new Computador(this, 1, slider_leitura1, slider_compilacao1, progress_bar_leitor1);
    leitor2 = new Computador(this, 2, slider_leitura2, slider_compilacao2, progress_bar_leitor2);
    leitor3 = new Computador(this, 3, slider_leitura3, slider_compilacao3, progress_bar_leitor3);
    leitor4 = new Computador(this, 4, slider_leitura4, slider_compilacao4, progress_bar_leitor4);
    escritor0.start();
    escritor1.start();
    escritor2.start();
    escritor3.start();
    escritor4.start();
    leitor0.start();
    leitor1.start();
    leitor2.start();
    leitor3.start();
    leitor4.start();
  }
}
