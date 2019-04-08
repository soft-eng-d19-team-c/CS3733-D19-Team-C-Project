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

    private int ID;
    private String patientID; // unique ID of the patient
    private String requesterID; // ID of the doctor requesting the drug
    private String resolverID; // ID of nurse of doctor the receives the drug from the pharmacy
    private String drug; // name of drug and amount
    private Timestamp timeOrdered;
    private Timestamp timeDelivered;

    public PrescriptionService(int id, String patientID, String requesterID, String resolverID, String drug, Timestamp timeOrdered, Timestamp timeDelivered) {
        this.ID = id;
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
        Date date = new Date();
        this.timeOrdered = new Timestamp(date.getTime());
    }

    public void drugDelivered(String resolverID){
        this.resolverID = resolverID;
        Date date = new Date();
        this.timeDelivered = new Timestamp(date.getTime());
    }

    @SuppressWarnings("Duplicates")
    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into PRESCRIPTIONSERVICE (PATIENTID, REQUESTERID, DRUG, TIMEORDERED) values (?,?,?,?)";
//        java.sql.Date sqlSubmitDate = new java.sql.Date(timeOrdered.getTime()); //because ps.setDate takes an sql.date, not a util.date
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, patientID);
            ps.setObject(2, requesterID);
            ps.setString(3, drug);
            ps.setTimestamp(4, timeOrdered);
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
//                System.out.println("PrescriptionService.getAllPrescriptionServices");
                int ID = rs.getInt("ID");
//                System.out.println(ID);
                String patientID = rs.getString("patientID");
                String requesterID =  rs.getString("requesterID");
                String resolverID = rs.getString("resolverID");
                String drug = rs.getString("drug");
                Timestamp timeOrdered = rs.getTimestamp("timeOrdered");
                Timestamp timeDelivered = rs.getTimestamp("timeDelivered");
                PrescriptionService prescriptionService = new PrescriptionService(ID, patientID, requesterID, resolverID, drug, timeOrdered, timeDelivered);
                requests.add(prescriptionService);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public int getID() {
        return ID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getRequesterID() {
        return requesterID;
    }

    public String getResolverID() {
        return resolverID;
    }

    public String getDrug() {
        return drug;
    }

    public Date getTimeOrdered() {
        return timeOrdered;
    }

    public Date getTimeDelivered() {
        return timeDelivered;
    }

    @SuppressWarnings("Duplicates")
    public boolean resolve() {
        String str = "UPDATE PRESCRIPTIONSERVICE SET RESOLVERID = ?, TIMEDELIVERED = ? WHERE ID = ?";
        try {
            PreparedStatement ps = Main.database.getConnection().prepareStatement(str);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            ps.setString(1, Main.user.getUsername());
            ps.setTimestamp(2, ts);
            ps.setInt(3, this.getID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}