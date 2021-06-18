import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {


        System.out.println("Client: Connected to server...");

        try (ClientHelper clientHelper = new ClientHelper("127.0.0.1", 8000)) {
             System.out.println("Server:" + clientHelper.readLine());

             Scanner in = new Scanner(System.in);
             String nameClient;
             String passwordClient;
             String message;
             String toClient;
             boolean active = true;
             int userCommand;

             while(active) {

                 System.out.print("What do you want? \n" +
                         "0 - Registering on the server \n" +
                         "1 - Authorization on the server \n" +
                         "2 - Send a message to all clients \n" +
                         "3 - Send a message to the client\n" +
                         "4 - Get all messages\n" +
                         "9 - Exit \n ");

                 userCommand = in.nextInt();
                //s System.out.println(userCommand);

                 switch (userCommand){
                    case 0:
                        System.out.println("Please, input your name");
                        nameClient = in.next();
                        if (nameClient.isEmpty()) {
                            System.out.println("Wrong client name");
                        } else{
                            System.out.println("Please, input your password");
                            passwordClient = in.next();
                            if (passwordClient.isEmpty()) {
                                System.out.println("Wrong password");
                            } else {
                                clientHelper.writeLine("0" + " " + nameClient + " " + passwordClient);
                            }
                        }
                        System.out.println("=== SERVER === :" + clientHelper.readLine());
                    break;
                    case 1:
                         System.out.println("Please, input your name");
                         nameClient = in.next();
                         if (nameClient.isEmpty()) {
                             System.out.println("Wrong client name");
                         } else{
                             System.out.println("Please, input your password");
                             passwordClient = in.next();
                             if (passwordClient.isEmpty()) {
                                 System.out.println("Wrong password");
                             } else {
                                 clientHelper.writeLine("1" + " " + nameClient + " " + passwordClient);
                             }
                         }
                        System.out.println("=== SERVER === :" + clientHelper.readLine());
                    break;
                    case 2:
                        System.out.println("Please, input text message");
                        message = in.next();
                        if(message.isEmpty()) {
                            System.out.println("Wrong message");
                        } else {
                            clientHelper.writeLine("2" + " " + message);
                        }
                    break;
                    case 3:
                         System.out.println("Please, input client name");
                         toClient = in.next();
                         if (toClient.isEmpty()) {
                             System.out.println("Wrong client name");
                         } else{
                             System.out.println("Please, input text messages");
                             message = in.next();
                             if (message.isEmpty()) {
                                 System.out.println("Wrong message");
                             } else {
                                 clientHelper.writeLine("3" + " " + toClient + " " + message);
                             }
                         }
                    break;
                     case 4:
                         System.out.println("Getting all messages");
                         clientHelper.writeLine("4");
                         System.out.println("=== SERVER === :" + clientHelper.readLine());
                         break;
                    case 9:
                         in.close();
                         active = false;
                         break;
                     default:
                        System.out.println("Incorrect operation");
                        break;
                }
             }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    static class ClientHelper implements Closeable {
        private final Socket socket;
        private final BufferedReader reader;
        private final BufferedWriter writer;

        public ClientHelper(String ip, int port){
            try{
                this.socket = new Socket(ip, port);
                this.writer = createWriter();
                this.reader = createReader();
            } catch (IOException q) {
                throw new RuntimeException();
            }
        }

        public void writeLine(String message){
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }

        public String readLine(){
            try {
                return reader.readLine();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }

        private BufferedReader createReader() throws IOException {
            return new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
        }

        private BufferedWriter createWriter() throws IOException {
            return new BufferedWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream()));
        }

        @Override
        public void close() throws IOException {
            reader.close();
            writer.close();
            socket.close();

        }
    }
}
