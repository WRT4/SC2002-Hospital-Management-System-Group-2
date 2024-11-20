package enums;

/**
 * Represents the various statuses that can be assigned to actions, requests, or processes 
 * in the Hospital Management System (HMS).
 * Used to track and manage the lifecycle of requests, appointments, or other operations.
 * @author Hoo Jing Huan, Lee Kuan Rong Shane, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public enum Status {

    /** Indicates that an action or request has been accepted. */
    ACCEPTED,

    /** Indicates that an action or request has been declined. */
    DECLINED,

    /** Indicates that an action or request is pending and awaiting review or action. */
    PENDING,

    /** Indicates that an action or request has been confirmed. */
    CONFIRMED,

    /** Indicates that an action or request has been completed successfully. */
    COMPLETED,

    /** Indicates that an action or request has been cancelled. */
    CANCELLED,

    /** Indicates that an action or request has been approved. */
    APPROVED,

    /** Indicates that an item (e.g., medication) has been dispensed. */
    DISPENSED,
}
