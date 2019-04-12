package base;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.util.Duration;

/**
 * Used to monitor whether a user has remained idle for a specified period of time.
 * This is useful for session timeouts.
 * @author Ryan LaMarche
 */
public final class IdleMonitor {
    private final Timeline idleTimeline;
    private final EventHandler<Event> userEventHandler;

    /**
     * Constructor for IdleMonitor
     * @author Ryan LaMarche
     * @param idleTime How long to wait before running the notifier.
     * @param notifier The thing to do when it times out.
     * @param startMonitoring Whether or not to start the monitor.
     */
    public IdleMonitor(Duration idleTime, Runnable notifier, boolean startMonitoring) {
        idleTimeline = new Timeline(new KeyFrame(idleTime, e -> notifier.run()));
        idleTimeline.setCycleCount(Animation.INDEFINITE);

        userEventHandler = e -> notIdle() ;

        if (startMonitoring) {
            startMonitoring();
        }
    }

    /**
     * Overloads constructor and does not start monitoring.
     * @author Ryan LaMarche
     * @param idleTime
     * @param notifier
     */
    public IdleMonitor(Duration idleTime, Runnable notifier) {
        this(idleTime, notifier, false);
    }

    /**
     * Adds the event listener to the scene
     * @author Ryan LaMarche
     * @param scene
     * @param eventType
     */
    public void register(Scene scene, EventType<? extends Event> eventType) {
        scene.addEventFilter(eventType, userEventHandler);
    }

    /**
     * Adds the event listener to a node
     * @author Ryan LaMarche
     * @param node
     * @param eventType
     */
    public void register(Node node, EventType<? extends Event> eventType) {
        node.addEventFilter(eventType, userEventHandler);
    }

    /**
     * Removes the event listener from a scene
     * @author Ryan LaMarche
     * @param scene
     * @param eventType
     */
    public void unregister(Scene scene, EventType<? extends Event> eventType) {
        scene.removeEventFilter(eventType, userEventHandler);
    }

    /**
     * Removes the event listener from a node
     * @author Ryan LaMarche
     * @param node
     * @param eventType
     */
    public void unregister(Node node, EventType<? extends Event> eventType) {
        node.removeEventFilter(eventType, userEventHandler);
    }

    /**
     * Starts the idle monitor timeline from the beginning if it is still running
     * @author Ryan LaMarche
     */
    public void notIdle() {
        if (idleTimeline.getStatus() == Animation.Status.RUNNING) {
            idleTimeline.playFromStart();
        }
    }

    /**
     * Starts the idle monitor timeline from the beginning
     * @author Ryan LaMarche
     */
    public void startMonitoring() {
        idleTimeline.playFromStart();
    }

    /**
     * Stops the idle monitor timeline
     * @author Ryan LaMarche
     */
    public void stopMonitoring() {
        idleTimeline.stop();
    }
}