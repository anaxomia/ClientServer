import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerBase {
    String path;
    ServerBase(String path){
        this.path = path;
    }

    public boolean registerClient(String client, String password){
        try(FileWriter writer = new FileWriter(this.path, true))
        {
            writer.write(client + " " + password + "\n");
            writer.flush();
            return true;
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public boolean checkClient(String client){
        List<String> listClients = new ArrayList<>();

        try(FileReader readerFile = new FileReader(this.path);
            BufferedReader reader = new BufferedReader(readerFile))
        {
            String line = reader.readLine();
            while (line != null){
                String[] lineArr = line.split(" ");
                listClients.add(lineArr[0]);
                line = reader.readLine();
            }

            for (String clientFromFile : listClients){
                if(clientFromFile.equals(client))
                    return true;
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public boolean checkClient(String client, String password){
        Map<String, String> listClients = new HashMap<>();

        try(FileReader readerFile = new FileReader(this.path);
            BufferedReader reader = new BufferedReader(readerFile))
        {
            String line = reader.readLine();
            while (line != null){
                String[] lineArr = line.split(" ");
                listClients.put(lineArr[0], lineArr[1]);
                line = reader.readLine();
            }

            for (Map.Entry<String, String> clientFromFile : listClients.entrySet()){
                if(clientFromFile.getKey().equals(client) && clientFromFile.getValue().equals(password))
                    return true;
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public boolean allUnregistered(){
        try(FileWriter writer = new FileWriter(this.path, false))
        {
            writer.flush();
            return true;
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }
}
