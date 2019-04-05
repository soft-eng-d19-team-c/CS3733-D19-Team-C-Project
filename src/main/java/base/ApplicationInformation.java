package base;

/**
 * This is a class that will be used to store volatile information
 * about the currently running application. Think about it like a "temporary" database
 * where we can change configuration for the app while it's running.
 * @author Ryan LaMarche
 */
public final class ApplicationInformation {
    EnumAlgorithm algorithm;

    public ApplicationInformation() {
        this.algorithm = EnumAlgorithm.ASTAR;
    }

    public EnumAlgorithm getAlgorithm() {
        return this.algorithm;
    }

}
