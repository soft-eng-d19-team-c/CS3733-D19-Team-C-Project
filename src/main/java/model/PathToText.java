package model;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import static java.lang.Math.sqrt;

public class PathToText{
    // Converts linked list to a detailed text based path

    LinkedList<Node> nodes;
    StringBuilder robotInstructions = new StringBuilder(); //Collects instructions in robot format
    static String postMe;   //The message for POST


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
        String strRobotInstructions;
        StringBuilder textPath = new StringBuilder("Starting at " + listOfNodes.getLast().getLongName() + "\n");
        StringBuilder floor4Text = new StringBuilder();
        StringBuilder floor3Text = new StringBuilder();
        StringBuilder floor2Text = new StringBuilder();
        StringBuilder floor1Text = new StringBuilder();
        StringBuilder groundText = new StringBuilder();
        StringBuilder l1Text = new StringBuilder();
        StringBuilder l2Text = new StringBuilder();


        Node[] nodesArray = listOfNodes.toArray(new Node[listOfNodes.size()]);
        Collections.reverse(Arrays.asList(nodesArray));
        Node prev, curr, next;
        for (int i = 1; i < nodesArray.length - 1; i++) {
            prev = nodesArray[i - 1];
            curr = nodesArray[i];
            next = nodesArray[i + 1];
            String currentFloor = curr.getFloor();
            String nextFloor = next.getFloor();

            if (!currentFloor.equals(nextFloor)) {
                if (curr.getFloorNumber() > next.getFloorNumber()) {
                    textPath.append("Go down a floor at " + curr.getLongName() + "\n");

                    switch (currentFloor){
                        case ("4"): floor4Text.append("Go down a floor at " + curr.getLongName() + "\n");
                            return floor4Text.toString();
                        case ("3"): floor3Text.append("Go down a floor at " + curr.getLongName() + "\n");
                            return floor3Text.toString();
                        case ("2"): floor2Text.append("Go down a floor at " + curr.getLongName() + "\n");
                            return floor2Text.toString();
                        case ("1"): floor1Text.append("Go down a floor at " + curr.getLongName() + "\n");
                            return floor1Text.toString();
                        case ("G"): groundText.append("Go down a floor at " + curr.getLongName() + "\n");
                            return groundText.toString();
                        case ("L1"): l1Text.append("Go down a floor at " + curr.getLongName() + "\n");
                            return l1Text.toString();
                        case ("L2"): l2Text.append("Go down a floor at " + curr.getLongName() + "\n");
                            return l2Text.toString();
                    }
                } else {
                    textPath.append("Go up a floor at " + curr.getLongName() + "\n");
                    switch (currentFloor){
                        case ("4"): floor4Text.append("Go up a floor at " + curr.getLongName() + "\n");
                            return floor4Text.toString();
                        case ("3"): floor3Text.append("Go up a floor at " + curr.getLongName() + "\n");
                            return floor3Text.toString();
                        case ("2"): floor2Text.append("Go up a floor at " + curr.getLongName() + "\n");
                            return floor2Text.toString();
                        case ("1"): floor1Text.append("Go up a floor at " + curr.getLongName() + "\n");
                            return floor1Text.toString();
                        case ("G"): groundText.append("Go up a floor at " + curr.getLongName() + "\n");
                            return groundText.toString();
                        case ("L1"): l1Text.append("Go up a floor at " + curr.getLongName() + "\n");
                            return l1Text.toString();
                        case ("L2"): l2Text.append("Go up a floor at " + curr.getLongName() + "\n");
                            return l2Text.toString();
                    }
                }
            } else {

                Vector2D v_prev = new Vector2D(curr.getX(), curr.getY(), prev.getX(), prev.getY());
                Vector2D v_next = new Vector2D(curr.getX(), curr.getY(), next.getX(), next.getY());

                EnumDirectionType dir = v_prev.getDirection(v_next);

                switch (dir) {
                    case LEFT:
                        switch (currentFloor){
                            case ("4"): floor4Text.append("Take a left at " + curr.getLongName());
                            break;
                            case ("3"):floor3Text.append("Take a left at " + curr.getLongName());
                                break;
                            case ("2"): floor2Text.append("Take a left at " + curr.getLongName());
                                break;
                            case ("1"): floor1Text.append("Take a left at " + curr.getLongName());
                                break;
                            case ("G"): groundText.append("Take a left at " + curr.getLongName());
                                break;
                            case ("L1"): l1Text.append("Take a left at " + curr.getLongName());
                                break;
                            case ("L2"): l2Text.append("Take a left at " + curr.getLongName());
                                break;
                        }
                        textPath.append("Take a left at " + curr.getLongName());
                        robotInstructions.append('L');
                        break;
                    case RIGHT:
                        switch (currentFloor){
                            case ("4"): floor4Text.append("Take a right at " + curr.getLongName());
                                break;
                            case ("3"):floor3Text.append("Take a right at " + curr.getLongName());
                                break;
                            case ("2"): floor2Text.append("Take a right at " + curr.getLongName());
                                break;
                            case ("1"): floor1Text.append("Take a right at " + curr.getLongName());
                                break;
                            case ("G"): groundText.append("Take a right at " + curr.getLongName());
                                break;
                            case ("L1"): l1Text.append("Take a right at " + curr.getLongName());
                                break;
                            case ("L2"): l2Text.append("Take a right at " + curr.getLongName());
                                break;
                        }
                        textPath.append("Take a right at " + curr.getLongName());
                        robotInstructions.append('R');
                        break;
                    case STRAIGHT:
                        switch (currentFloor){
                            case ("4"): floor4Text.append("Continue straight past " + curr.getLongName());
                                break;
                            case ("3"):floor3Text.append("Continue straight past " + curr.getLongName());
                                break;
                            case ("2"): floor2Text.append("Continue straight past " + curr.getLongName());
                                break;
                            case ("1"): floor1Text.append("Continue straight past " + curr.getLongName());
                                break;
                            case ("G"): groundText.append("Continue straight past " + curr.getLongName());
                                break;
                            case ("L1"): l1Text.append("Continue straight past " + curr.getLongName());
                                break;
                            case ("L2"): l2Text.append("Continue straight past " + curr.getLongName());
                                break;
                        }
                        textPath.append("Continue straight past " + curr.getLongName());
                        robotInstructions.append('S');
                        break;
                    default:
                        System.err.println("Default case in direction switch");
                }

                robotInstructions.append(',');

                // 3ft = 8px
                double distance = findEuclideanDistance(curr, next) * 3 / 8;

                textPath.append(String.format(" distance: %.0fft\n", distance));
                switch (currentFloor){
                    case ("4"): floor4Text.append(String.format(" distance: %.0fft\n", distance));
                        break;
                    case ("3"):floor3Text.append(String.format(" distance: %.0fft\n", distance));
                        break;
                    case ("2"): floor2Text.append(String.format(" distance: %.0fft\n", distance));
                        break;
                    case ("1"): floor1Text.append(String.format(" distance: %.0fft\n", distance));
                        break;
                    case ("G"): groundText.append(String.format(" distance: %.0fft\n", distance));
                        break;
                    case ("L1"): l1Text.append(String.format(" distance: %.0fft\n", distance));
                        break;
                    case ("L2"): l2Text.append(String.format(" distance: %.0fft\n", distance));
                        break;
                }


                double inches = distance / 12.0;
                int robotInches = (int) inches;
                robotInstructions.append(robotInches);
                robotInstructions.append(',');


            }
            if(listOfNodes.getFirst().getFloor().equals(currentFloor)){
                switch (currentFloor){
                    case "4": floor4Text.append("Finally, arrive at " + listOfNodes.getFirst().getLongName() + "\n");
                        return floor4Text.toString();
                    case "3": floor3Text.append("Finally, arrive at " + listOfNodes.getFirst().getLongName() + "\n");
                        return floor3Text.toString();
                    case "2": floor2Text.append("Finally, arrive at " + listOfNodes.getFirst().getLongName() + "\n");
                        return floor2Text.toString();
                    case "1": floor1Text.append("Finally, arrive at " + listOfNodes.getFirst().getLongName() + "\n");
                        return floor1Text.toString();
                    case "G": groundText.append("Finally, arrive at " + listOfNodes.getFirst().getLongName() + "\n");
                        return groundText.toString();
                    case "L1": l1Text.append("Finally, arrive at " + listOfNodes.getFirst().getLongName() + "\n");
                        return l1Text.toString();
                    case "L2": l2Text.append("Finally, arrive at " + listOfNodes.getFirst().getLongName() + "\n");
                        return l2Text.toString();
                }
                textPath.append("Finally, arrive at " + listOfNodes.getFirst().getLongName() + "\n");
            }

        }

        strRobotInstructions = robotInstructions.toString();
        postMe = strRobotInstructions.substring(0, strRobotInstructions.length() - 1); //Get rid of the last extra comma
//        System.out.println(textPath.toString());

        //return textPath.toString();


    return null;

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
