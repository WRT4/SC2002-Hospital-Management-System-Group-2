package model;

import java.util.ArrayList;

public class MedicalRecord {
    private String patientID;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String bloodType;
    private ArrayList<String> diagnoses;
    private ArrayList<String> prescriptions;
    private String phoneNumber;
    private String email;
    private int age;
    
    
    public MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String phoneNumber, String email, String bloodType) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bloodType = bloodType;
        this.diagnoses = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
    }
    public MedicalRecord(String patientID, String name) {
        this(patientID, name, "", "", "", "", "");
    }

    public MedicalRecord() {
    	this("","","","","","","");
    }

    public void addDiagnosis(String diagnosis, Doctor doctor) {
        if (doctor != null){
            diagnoses.add(diagnosis);
            System.out.println("Diagnosis added by Dr. " + doctor.getName());
        }
        else{
            System.out.println("Only a doctor can add a diagnosis.");
        }
    }
    
    public void addPrescription(String prescription, Doctor doctor) {
        if (doctor != null){
            prescriptions.add(prescription);
            System.out.println("Prescription added by Dr. " + doctor.getName());
        }
        else{
            System.out.println("Only a doctor can add a prescription.");
        }
    }

    public void addPrescription(String prescription) {
        prescriptions.add(prescription);
    }

    public String getPhonenumber (){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail (){
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void printMedicalRecord() {
        System.out.println("Name: " + name + " | ID: " + patientID + " | Phone number: " + phoneNumber + " | Email Address: " + email +
        "\nDate Of Birth: " + dateOfBirth + " | Gender: " + gender + " | Blood Type: " + bloodType + " | Age: " + age +
        "\nDiagnoses: " + diagnoses +
        "\nPrescriptions: " + prescriptions);
    }

	public void setDateOfBirth(String dob) {
		this.dateOfBirth = dob;
	}

	public String getDateOfBirth() {
		return this.dateOfBirth;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getName() {
		return name;
	}

    public void setGender(String gender){
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    
    public void viewContactInfo() {
		System.out.println("Current contact info: ");
		System.out.println("Name: " + getName());
		System.out.println("Email: " + getEmail());
		System.out.println("Date of Birth: " + getDateOfBirth());
		System.out.println("Phone Number: " + getPhoneNumber());
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}

