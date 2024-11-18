package model;

import java.io.Serializable;

public interface Request extends Serializable{
	public void acceptRequest();
	public void declineRequest();
}
