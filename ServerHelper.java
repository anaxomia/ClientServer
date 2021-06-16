import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHelper  implements Closeable {
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public ServerHelper(ServerSocket serverSocket){
        try{
            this.socket = serverSocket.accept();
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