package controller;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.AppointmentRequest;
import model.Status;

public class RequestController {
	public static AppointmentRequest findRequest(ArrayList<AppointmentRequest> requests, boolean isPatient, Scanner scanner) {
		AppointmentRequest request = null;
		while (true) {
			try {
				request = null;
				int requestID;
				System.out.println("Enter requestID or -1 to exit: ");
				requestID = scanner.nextInt();
				if (requestID == -1) return null;
				int i = 0;
				for (i = 0; i < requests.size(); i++) {
		            if (requests.get(i).getRequestID() == requestID) {
		                request = requests.get(i);
		                break;
		            }
		        }
				if (request == null) throw new RuntimeException("RequestID does not exist! ");
				if (request.getStatus() != Status.PENDING) {
					if (request.getStatus() == Status.CANCELLED) {
						throw new RuntimeException("Request already cancelled!");
					}
					else if (request.getStatus() == Status.ACCEPTED) {
						if (isPatient) throw new RuntimeException("Request already accepted! Cancel appointment instead!");
						throw new RuntimeException("Request already accepted!");
					}
					else if (request.getStatus() == Status.DECLINED) {
						throw new RuntimeException("Request already declined!");
					}
				}
				break;
			}
			catch (InputMismatchException e) {
				System.out.println("Wrong input type! Try Again!");
				scanner.nextLine();
				continue;
			}
			catch (RuntimeException e) {
				System.out.println(e.getMessage());
				scanner.nextLine();
				continue;
			}
			catch (Exception e) {
				System.out.println("Error! Try Again!");
				scanner.nextLine();
				continue;
			}
		}
		return request;
	}
}
