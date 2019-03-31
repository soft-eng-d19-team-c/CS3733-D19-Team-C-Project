package model;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;



import java.util.LinkedList;

public class PathToText {
    // Converts linked list to a detailed text based path

    LinkedList<Node> nodes;

    public PathToText(LinkedList<Node> nodes) {
        this.nodes = nodes;
    }

    public String getDetailedPath(LinkedList<Node> listOfNodes){
        StringBuilder textPath = new StringBuilder();
        //get start
        System.out.println(listOfNodes);
        textPath.append("Start at " + listOfNodes.getFirst().getShortName() + ", ");
        for (Node n : listOfNodes.subList(1,listOfNodes.size()-1)){ // skips first node
            textPath.append("then go to "+ n.getShortName() + ", ");
        }
        textPath.append("and end at " + listOfNodes.getFirst().getShortName() + ".");

        System.out.println(textPath);

        Twilio test;


        return textPath.toString();
    }

    public void SmsSender (String text) {
        // Find your Account Sid and Auth Token at twilio.com/console
        String ACCOUNT_SID =
                "ACc2d1c6c918a7391c72ce1bb4187e5228";
        String AUTH_TOKEN = // Not posting this on github for now I get charged one cent every time a text is send. 
                "";

            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message message = Message
                    .creator(new PhoneNumber("+17746411640"), // to
                            new PhoneNumber("+17742293016"), // from
                            text)
                    .create();

            System.out.println(message.getSid());
    }

    public String getDetailedPath(){
        return getDetailedPath(nodes);
    }


}
