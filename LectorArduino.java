import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Allan
 */
public class LectorArduino {

    public static void main(String[] args) {
        RXTX rxtx = new RXTX();
        try {
            rxtx.connect("COM3");

            //Define a working thread to read RxTx and display in the console
            Thread readData = new Thread(new Runnable() {
                @Override
                public void run() {
                    String dataIn;
                    while (true) {
                        if (Thread.interrupted()) {
                            break;
                        }
                        dataIn = rxtx.read();
                        if (data != null) {
                            System.out.print(dataIn);
                        } else {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                }
            });
            readData.start();

            //Define a input thread to get input from the user and send to RxTx
            Thread userInput = new Thread(new Runnable() {
                @Override
                public void run() {
                    int c = 0;
                    byte[] buffer = new byte[1204];
                    try {
                        while ((c = System.in.read(buffer)) > -1) {
                            if (Thread.interrupted()) {
                                break;
                            }
                            //"---" is a string to terminate the program
                            if (c > 0) {
                                if (c == 5 && buffer[0] == '-'
                                        && buffer[1] == '-' && buffer[2] == '-') {
                                    break;
                                }
                                //"+++" is a special string sent to RxTx
                                if (c == 5 && buffer[0] == '+'
                                        && buffer[1] == '+' && buffer[2] == '+') {
                                    c = 3;
                                }
                                rxtx.write(Arrays.copyOf(buffer, c));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            userInput.start();
           
            //wait for the input thread to finish
            userInput.join();
            readData.interrupt();
            readData.join();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close RxTx to release all resources
            rxtx.close();
            System.out.println("Closed session!");
        }
    }
}
