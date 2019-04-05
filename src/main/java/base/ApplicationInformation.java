package base;

import model.Node;

/**
 * This is a class that will be used to store volatile information
 * about the currently running application. Think about it like a "temporary" database
 * where we can change configuration for the app while it's running.
 * @author Ryan LaMarche
 */
public final class ApplicationInformation {
    EnumAlgorithm algorithm;
    Node kioskLocation;

    public ApplicationInformation(EnumAlgorithm algorithm, String kioskLocation) {
        this.algorithm = algorithm;
        this.kioskLocation = Node.getNodeByID(kioskLocation);
    }

    /**
     * @author Ryan LaMarche.
     * @return EnumAlgorithm the current algorithm.
     */
    public EnumAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    /**
     * @author Ryan LaMarche.
     * @return Node at kiosk location.
     */
    public Node getKioskLocation() {
        return this.kioskLocation;
    }

}
