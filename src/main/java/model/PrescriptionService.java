package model;

import base.Database;
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
    private String userSubmittedBy; // ID of the doctor requesting the drug
    private String UserCompletedBy; // ID of nurse of doctor the receives the drug from the pharmacy
    private String drug; // name of drug and amount
    private Timestamp dateTimeSubmitted;
    private Timestamp timeDelivered;

    public PrescriptionService(int id, String patientID, String userSubmittedBy, String UserCompletedBy, String drug, Timestamp dateTimeSubmitted, Timestamp timeDelivered) {
        this.ID = id;
        this.patientID = patientID;
        this.userSubmittedBy = userSubmittedBy;
        this.UserCompletedBy = UserCompletedBy;
        this.drug = drug;
        this.dateTimeSubmitted = dateTimeSubmitted;
        this.timeDelivered = timeDelivered;
    }

    public PrescriptionService(String patientID, String userSubmittedBy, String drug) {
        this.patientID = patientID;
        this.userSubmittedBy = userSubmittedBy;
        this.drug = drug;
        Date date = new Date();
        this.dateTimeSubmitted = new Timestamp(date.getTime());
    }

    public void drugDelivered(String UserCompletedBy){
        this.UserCompletedBy = UserCompletedBy;
        Date date = new Date();
        this.timeDelivered = new Timestamp(date.getTime());
    }

    @SuppressWarnings("Duplicates")
    public boolean insert(){

        boolean executed = false;

        String sqlCmd = "insert into PRESCRIPTIONREQUESTS (PATIENTID, USERSUBMITTEDBY, DRUG, DATETIMESUBMITTED) values (?,?,?,?)";
//        java.sql.Date sqlSubmitDate = new java.sql.Date(dateTimeSubmitted.getTime()); //because ps.setDate takes an sql.date, not a util.date
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlCmd);
            ps.setString(1, patientID);
            ps.setString(2, userSubmittedBy);
            ps.setString(3, drug);
            ps.setTimestamp(4, dateTimeSubmitted);
            executed = ps.execute(); //returns a boolean
            System.out.println("PrescriptionService.insert " + executed);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return executed;

    }

    //Returns an observable list of all SANITATIONREQUESTS for JavaFX's sake
    @SuppressWarnings("Duplicates")
    public static ObservableList<PrescriptionService> getAllPrescriptionServices() {

        ObservableList<PrescriptionService> requests =  FXCollections.observableArrayList();

        try {
            Statement stmt = Database.getConnection().createStatement();
            String str = "SELECT * FROM PRESCRIPTIONREQUESTS";
            ResultSet rs = stmt.executeQuery(str);
            while(rs.next()) {
//                System.out.println("PrescriptionService.getAllPrescriptionServices");
                int ID = rs.getInt("ID");
//                System.out.println(ID);
                String patientID = rs.getString("patientID");
                String userSubmittedBy =  rs.getString("userSubmittedBy");
                String UserCompletedBy = rs.getString("UserCompletedBy");
                String drug = rs.getString("drug");
                Timestamp dateTimeSubmitted = rs.getTimestamp("dateTimeSubmitted");
                Timestamp timeDelivered = rs.getTimestamp("dateTimecompleted");
                PrescriptionService prescriptionService = new PrescriptionService(ID, patientID, userSubmittedBy, UserCompletedBy, drug, dateTimeSubmitted, timeDelivered);
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
        return userSubmittedBy;
    }

    public String getResolverID() {
        return UserCompletedBy;
    }

    public String getDrug() {
        return drug;
    }

    public Date getTimeOrdered() {
        return dateTimeSubmitted;
    }

    public Date getTimeDelivered() {
        return timeDelivered;
    }

    @SuppressWarnings("Duplicates")
    public boolean resolve() {
        String str = "UPDATE PRESCRIPTIONREQUESTS SET USERCOMPLETEDBY = ?, DATETIMECOMPLETED = ? WHERE ID = ?";
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(str);
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
