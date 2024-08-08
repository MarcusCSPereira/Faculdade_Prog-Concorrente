//pacote da classe
package model;
//importacoes
import java.util.concurrent.Semaphore;

public class Semaforo {
  //tamanho do buffer, no caso sera 3, producao e insercao de 3 produtos enche o buffer
  private static int buffer = 3;
  //mutex realiza o controle do acesso a regiao critica
  static Semaphore mutex = new Semaphore(1);
  //vazio faz a contagem das regioes vazias do buffer 
  static Semaphore vazio = new Semaphore(buffer);
  //cheio faz a contagem das regioes ocupadas do buffer 
  static Semaphore cheio = new Semaphore(0);

  //metodo para reiniciar os semaforos
  public static void reiniciar(int valor, int i){
    mutex = new Semaphore(valor);
    buffer = 3;
    vazio = new Semaphore(buffer);
    cheio = new Semaphore(i);
  }

  public static void DOWN(String t){
    if(t.equals("produtor")){
      try {
        vazio.acquire();
        mutex.acquire();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }else if(t.equals("consumidor")){
      try {
        cheio.acquire();
        mutex.acquire();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void UP(String t){
    if(t.equals("produtor")){
      mutex.release();
      cheio.release();
    }else if(t.equals("consumidor")){
      mutex.release();
      vazio.release();
    }
  }

}
