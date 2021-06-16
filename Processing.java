import java.util.ArrayList;
import java.util.List;

public class Processing {

    private static List<Message> messages = new ArrayList<>();

    public static void addMessage(String to, String from, String text, ServerHelper serverHelper){
        messages.add(new Message(to, from, text, serverHelper));
    }

    public static List<String> getMessages(String to){
        List<String> returnMessages = new ArrayList<>();
        for(Message message : messages){
            if (message.to.equals(to) && message.send == false){
                returnMessages.add(message.from + ": " + message.text);
                message.send = true;
            }
        }
        return returnMessages;
    }

    static class Message{
        private String to;
        private String from;
        private String text;
        private ServerHelper serverHelper;
        private boolean send;

        public Message(String to, String from, String text, ServerHelper serverHelper) {
            this.to = to;
            this.text = text;
            this.from = from;
            this.serverHelper = serverHelper;
            this.send = false;
        }
    }
}
