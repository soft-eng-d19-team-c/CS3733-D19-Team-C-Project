package model;

import base.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Date;

public class PrescriptionService {
    // This should only be used by doctors, patients will never be ordering their own prescriptions or will they
    // have to recieve them while they are at the hospital. Doctors or other people will request a prescription
    // then the prescription will be delivered to the floor a patient is on.

    private int id;
    private String patientID; // unique id of the patient
    private String requesterID; // id of the doctor requesting the drug
    private String resolverID; // id of nurse of doctor the receives the drug from the pharmacy
    private String drug; // name of drug and amount
    private Date timeOrdered;
    private Date timeDelivered;

    public PrescriptionService(int id, String patientID, String requesterID, String resolverID, String drug, Date timeOrdered, Date timeDelivered) {
        this.id = id;
        this.patientID = patientID;
        this.requesterID = requesterID;
        this.resolverID = resolverID;
        this.drug = drug;
        this.timeOrdered = timeOrdered;
        this.timeDelivered = timeDelivered;
    }

    public PrescriptionService(String patientID, String requesterID, String drug) {
        this.patientID = patientID;
        this.requesterID = requesterID;
        this.drug = drug;
        this.timeOrdered = new Date();
    }

    public void drugDelivered(String resolverID){
        this.resolverID = resolverID;
        this.timeDelivered = new Date();
    }

    @SuppressWarnings("Duplicates")
    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into PRESCRIPTIONSERVICE (PATIENTID, REQUESTERID, DRUG, TIMEORDERED) values (?,?,?,?)";
        java.sql.Date sqlSubmitDate = new java.sql.Date(timeOrdered.getTime()); //because ps.setDate takes an sql.date, not a util.date

        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, patientID);
            ps.setObject(2, requesterID);
            ps.setString(3, drug);
            ps.setDate(4, sqlSubmitDate);
            executed = ps.execute(); //returns a boolean
            System.out.println("PrescriptionService.insert " + executed);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }

    //Returns an observable list of all ServiceRequests for JavaFX's sake
    @SuppressWarnings("Duplicates")
    public static ObservableList<PrescriptionService> getAllPrescriptionServices() {

        ObservableList<PrescriptionService> requests =  FXCollections.observableArrayList();

        try {
            Statement stmt = Main.database.getConnection().createStatement();
            String str = "SELECT * FROM PRESCRIPTIONSERVICE";
            ResultSet rs = stmt.executeQuery(str);
            while(rs.next()) {
                System.out.println("PrescriptionService.getAllPrescriptionServices");
                int ID = rs.getInt("ID");
                String patientID = rs.getString("patientID");
                String requesterID =  rs.getString("requesterID");
                String resolverID = rs.getString("resolverID");
                String drug = rs.getString("drug");
                Date timeOrdered = rs.getDate("timeOrdered");
                Date timeDelivered = rs.getDate("timeDelivered");
                PrescriptionService prescriptionService = new PrescriptionService(ID, patientID, requesterID, resolverID, drug, timeOrdered, timeDelivered);
                requests.add(prescriptionService);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

}
