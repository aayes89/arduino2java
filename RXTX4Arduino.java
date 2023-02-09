import gnu.io.*;
import java.io.IOException;
import java.util.*;

public class RXTX4Arduino{
public static void main(String[] args){
Scanner in;
SerialPort sp;
CommPortIdentifier cpi;
int timeout = 1000;
int bitrate = 115200; // 9600
try{
 Enumeration ePorts = CommPortIdentifier.getPortIdentifiers();
 // Use this line code one time for COM port discover
 // ePorts.asIterator().forEachRemaining(id-> System.out.println((CommPortIdentifier) id).getName()));
 while( ePorts.hasMoreElements() ){
   cpi = (CommPortIdentifier)ePorts.nextElement();
   if( cpi.getName().contains(" name_of_port ") ){
      sp = (SerialPort)cpi.open(cpi.getName(), timeout);
      break; 
   }
 }
 if( sp != null ){
   sp.setSerialPortParams(bitrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
   in = new Scanner( sp.getInputStream() );
   while( in.hasNext() ){
     System.out.println( in.nextLine() );
   }
   sp.close();
 }
} catch (AWTException | PortInUseException | IOException | UnsupportedCommOperationException ex ){
    System.out.println( ex.getMessage() );
  }
}
