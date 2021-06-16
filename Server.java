import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    static ServerBase base = new ServerBase("1.base");
    private static Map<String, ServerHelper> clientsLoginedList = new HashMap<>();


    public static void main(String[] args){

        //base.allUnregistered();

        try(ServerSocket serverSocket = new ServerSocket(8000))
        {
            System.out.println("Server started!");
            while (true) {
                try {
                    ServerHelper server = new ServerHelper(serverSocket);
                    new Thread(() -> {
                        try {
                            dialogWithClient(server, Thread.currentThread());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e){

            throw new RuntimeException(e);
        } finally {
            base.allUnregistered();
        }

    }

    private static void dialogWithClient(ServerHelper server, Thread thread) throws IOException {

        boolean registered = false;
        boolean login = false;
        boolean exit = false;
        String message;
        String allMessages = "";
        String toClient;

        server.writeLine("Hello, client");
        while(!exit) {
            String request = server.readLine();

            String[] requestArr = request.split(" ");

            if (requestArr.length < 1) {
                server.writeLine("BAD_REQUEST");
                server.writeLine("Wrong request, request must have min 1 words");
            }
            else
                switch (requestArr[0]) {
                    case "0":
                        if (registered) {
                            server.writeLine("ALREADY_REGISTERED");
                            System.out.println("You are already registered");
                        } else {
                            if (requestArr.length < 3) {
                                server.writeLine("BAD_REQUEST");
                                System.out.println("Wrong request. Register request must have 3 words");

                            } else {
                                String clientName = requestArr[1];
                                String clientPassword = requestArr[2];
                                if (base.checkClient(clientName)) {
                                    server.writeLine("ALREADY_REGISTERED");
                                    System.out.println("You are already registered");
                                } else if (base.registerClient(clientName, clientPassword)) {
                                    server.writeLine("REGISTERED_SUCCESS");
                                    System.out.println("You are registered");
                                    registered = true;
                                } else {
                                    server.writeLine("REGISTERED_ERROR");
                                    System.out.println("Registered error");
                                }
                            }
                        }
                        break;
                    case "1":
                        if (requestArr.length < 3){
                            System.out.println("Wrong request. Register request must have 3 words");
                            server.writeLine("BAD_REQUEST");
                        }
                        else {
                            String clientName = requestArr[1];
                            String clientPassword = requestArr[2];

                            if (base.checkClient(clientName, clientPassword)) {
                                System.out.println(clientName + ", you are login");
                                server.writeLine("LOGIN_SUCCESS");
                                login = true;
                                clientsLoginedList.put(clientName, server);
                                thread.setName(clientName);
                            } else {
                                System.out.println("You're not registered");
                                server.writeLine("LOGIN_ERROR");
                            }
                        }
                        break;

                   case "2":
                       if (!login){
                           server.writeLine("NOT_LOGIN");
                           System.out.println("You aren't login");
                       } else {
                           message = requestArr[1];
                           for (Map.Entry<String, ServerHelper> client : clientsLoginedList.entrySet()) {
                               if (!client.getKey().equals(thread.getName())) {
                                   Processing.addMessage(client.getKey(), thread.getName(), message, client.getValue());
                                   //client.getValue().writeLine(message);
                               }
                           }
                       }
                       break;

                   case "3":
                       if (!login){
                           server.writeLine("NOT_LOGIN");
                           System.out.println("You aren't login");
                       } else if (requestArr.length < 3)
                            server.writeLine("Wrong request. Register request must have 3 words");
                       else {
                            toClient = requestArr[1];
                            message = requestArr[2];
                           for (Map.Entry<String, ServerHelper> client : clientsLoginedList.entrySet()) {
                               if (!client.getKey().equals(thread.getName()) && client.getKey().equals(toClient))
                                    Processing.addMessage(client.getKey(), thread.getName(), message, client.getValue());
                                    //client.getValue().writeLine(message);
                            }
                       }
                       break;

                   case "4":
                       if (!login) {
                           server.writeLine("NOT_LOGIN");
                           System.out.println("You aren't login");
                       } else {
                           List<String> messages = Processing.getMessages(thread.getName());
                           if (messages.size() < 1)
                               server.writeLine("NO_MESSAGES");
                           else{
                               allMessages = "";
                               for (String mess: messages)
                                   allMessages += mess + "\n";
                               server.writeLine(allMessages);
                           }
                       }
                   break;
                   case "9":
                        exit = true;
                        server.writeLine("Bye");
                        server.close();
                        break;
                   default:
                        server.writeLine("Wrong request");
                }
        }
    }

}
