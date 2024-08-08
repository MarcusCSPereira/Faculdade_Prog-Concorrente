/* ***************************************************************
* Autor............: Marcus Cesar Santos Pereira
* Matricula........: 202310279
* Inicio...........: 13/03/2023
* Ultima alteracao.: 13/03/2023
* Nome.............: DataTransfer.java 
* Funcao...........: Classe que realiza a transferencia de dados entre as telas,
utilizando o padrao Singleton, permitindo assim que os dados sejam acessados e passados
entre as telas.
*************************************************************** */


package util;

public class DataTransfer {

  private static final DataTransfer instance = new DataTransfer();//criando instancia da classe

  private int escolha = 0;//variavel que armazena a escolha do usuario da posição dos trens

  private DataTransfer() {//construtor da classe
  }//fim do construtor

/* ***************************************************************
* Metodo: getInstance
* Funcao: Retorna a instancia da classe do padrao Singleton,
permitindo que os dados sejam acessados e passados entre as telas
* Parametros: nenhum
* Retorno: DataTransfer instance, a instancia da classe para permitir a transferencia de dados
*************************************************************** */
  public static DataTransfer getInstance() {
    return instance;
  }//fim do metodo getInstance

/* ***************************************************************
* Metodo: getEscolha
* Funcao: Retorna a escolha do usuario da posição dos trens
* Parametros: nenhum
* Retorno: int, a escolha do usuario da posição dos trens
*************************************************************** */
  public int getEscolha() {
    return escolha;
  }//fim do metodo getEscolha

/* ***************************************************************
* Metodo: setEscolha
* Funcao: Define a escolha do usuario da posição dos trens
* Parametros: int escolha, a escolha do usuario da posição dos trens
* Retorno: void
*************************************************************** */
  public void setEscolha(int escolha) {
    this.escolha = escolha;
  }//fim do metodo setEscolha
}//fim da classe DataTransfer
