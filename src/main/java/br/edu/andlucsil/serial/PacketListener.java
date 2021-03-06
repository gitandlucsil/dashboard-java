package br.edu.andlucsil.serial;


import br.edu.andlucsil.json.JsonToObj;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PacketListener implements SerialPortPacketListener
{
    /**
     * Consrutor
     */
    public PacketListener() {
    }
    
    /**
     * Metódo para pegar os eventos de recepção de dados da porta serial
     * @return SerialPort.LISTENING_EVENT_DATA_RECEIVED
     */
   @Override
   public int getListeningEvents()
   {
       return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
   }

    /**
     * Metódo para recuperar o tamanho do pacote de dados recebido pela porta serial
     * @return O tamanho do pacote de dados recebido pela porta serial
     */
   @Override
   public int getPacketSize()
   {
       return 109;
   }

   @Override
   public void serialEvent(SerialPortEvent event)
   {
      byte[] newData = event.getReceivedData();
      //System.out.println("Received data of size: " + newData.length);
      String json_from_probes = "";
      for (int i = 0; i < newData.length; ++i){
         //System.out.print((char)newData[i]);
         json_from_probes += (char)newData[i];
      }
      System.out.println(json_from_probes);
      //System.out.println("\n");
      if(!"".equals(json_from_probes)){ //Se a mensagem não estiver vazia
          if(json_from_probes.charAt(0) == '[' && json_from_probes.charAt(getPacketSize()-1) == ']'){//Se começar e terminar com sinalizacao de vetor []
              if(json_from_probes.charAt(1) == '{' && json_from_probes.charAt(getPacketSize()-2) == '}'){
                  try { 
                      new JsonToObj(json_from_probes);
                  } catch (MalformedURLException ex) {
                      Logger.getLogger(PacketListener.class.getName()).log(Level.SEVERE, null, ex);
                  }
              }
          }
      }
   }
}