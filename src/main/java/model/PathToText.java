package model;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import static java.lang.Math.sqrt;

public class PathToText {
    // Converts linked list to a detailed text based path

    LinkedList<Node> nodes;

    public PathToText(LinkedList<Node> nodes) {
        this.nodes = nodes;
    }

    public String getDetailedPath(LinkedList<Node> listOfNodes){
        /*
        StringBuilder textPath = new StringBuilder();
        //get start
        System.out.println(listOfNodes);
        textPath.append("Start at " + listOfNodes.getFirst().getShortName() + ",\n");
        for (Node n : listOfNodes.subList(1,listOfNodes.size()-1)){ // skips first node
            textPath.append("then go to "+ n.getShortName() + ",\n");
        }
        textPath.append("and end at " + listOfNodes.getFirst().getShortName() + ".");

        System.out.println(textPath);

        Twilio test;


        return textPath.toString();
        */

        StringBuilder textPath = new StringBuilder("Starting at " + listOfNodes.getLast().getLongName() + "\n");
        Node[] nodesArray = listOfNodes.toArray(new Node[listOfNodes.size()]);
        Collections.reverse(Arrays.asList(nodesArray));
        Node prev, curr, next;
        for (int i = 1; i < nodesArray.length - 1; i++) {
            prev = nodesArray[i - 1];
            curr = nodesArray[i];
            next = nodesArray[i + 1];

            if (!curr.getFloor().equals(next.getFloor())) {
                if (curr.getFloorNumber() > next.getFloorNumber()) {
                    textPath.append("Go down a floor at " + curr.getLongName() + "\n");
                } else {
                    textPath.append("Go up a floor at " + curr.getLongName() + "\n");
                }
            } else {

                Vector2D v_prev = new Vector2D(curr.getX(), curr.getY(), prev.getX(), prev.getY());
                Vector2D v_next = new Vector2D(curr.getX(), curr.getY(), next.getX(), next.getY());

                EnumDirectionType dir = v_prev.getDirection(v_next);

                switch (dir) {
                    case LEFT:
                        textPath.append("Take a left at " + curr.getLongName());
                        break;
                    case RIGHT:
                        textPath.append("Take a right at " + curr.getLongName());
                        break;
                    case STRAIGHT:
                        textPath.append("Continue straight past " + curr.getLongName());
                        break;
                    default:
                        System.err.println("Default case in direction switch");
                }

                // 3ft = 8px
                double distance = findEuclideanDistance(curr, next) * 3 / 8;

                textPath.append(String.format(" distance: %.0fft\n", distance));
            }
        }

        textPath.append("Finally, arrive at " + listOfNodes.getFirst().getLongName() + "\n");

//        System.out.println(textPath.toString());

        return textPath.toString();



    }

    public void SmsSender (String text, PhoneNumber phoneNumber) {
        // Find your Account Sid and Auth Token at twilio.com/console
        String ACCOUNT_SID =
                "ACc2d1c6c918a7391c72ce1bb4187e5228";
        String AUTH_TOKEN = // Not posting this on github for now I get charged one cent every time a text is send.
                "fc1db4d983cbeeb7137a9f634a155eb8";

            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message message = Message
                    .creator(phoneNumber, // to
                            new PhoneNumber("+17742293016"), // from this is the phone number I bought
                            text)
                    .create();

            System.out.println(message.getSid());
    }

    public String getDetailedPath(){
        return getDetailedPath(nodes);
    }

    @SuppressWarnings("Duplicates")
    //also used to find the predicted cost to the end
    private double findEuclideanDistance(Node a, Node b) {

        int aX = a.getX();
        int aY = a.getY();
        int bX = b.getX();
        int bY = b.getY();

        int xDistance = aX - bX;
        int yDistance = aY - bY;
        int distSquared = (xDistance * xDistance) + (yDistance * yDistance);
        return sqrt(distSquared);
    }


}
