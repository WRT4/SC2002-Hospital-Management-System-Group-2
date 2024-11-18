package model;

import java.io.Serializable;

/**
 * Represents a generic request in the system.
 * Provides methods to accept or decline a request.
 * This interface extends Serializable, allowing requests to be serialized.
 * @author Hoo Jing Huan, Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public interface Request extends Serializable {

	/**
	 * Accepts the request, typically indicating that it has been approved or confirmed.
	 */
	public void acceptRequest();

	/**
	 * Declines the request, typically indicating that it has been rejected or refused.
	 */
	public void declineRequest();
}
