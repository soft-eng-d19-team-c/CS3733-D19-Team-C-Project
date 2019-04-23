package model;

import base.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.scene.control.agenda.Agenda;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingCalendar {
    private static class UserSession {
        public static Session getSession() {
            Configuration configuration = new Configuration().configure();
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            Session session = sessionFactory.openSession();
            return session;
        }
    }
//    @Entity
//    @Access(AccessType.FIELD)
//    @Table(name = "Appointment")
//    private class AppointmentEntity {
//        @Id
//        @GeneratedValue
//        private int id;
//        private Timestamp startTime;
//        private Timestamp endTime;
//        String description;
//        public int getId() { return id; }
//        public void setId(int id) { this.id = id; }
//        public String getDescription() { return description; }
//        public void setDescription(String description) { this.description = description; }
//        public Timestamp getStartTime() { return startTime; }
//        public void setStartTime(Timestamp startTime) { this.startTime = startTime; }
//        public Timestamp getEndTime() { return endTime; }
//        public void setEndTime(Timestamp endTime) { this.endTime = endTime; }
//    }
    public class Appointment extends Agenda.AppointmentImplLocal{
        private int id;
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
    }
    private static Session session = UserSession.getSession();

    public int addNewAppointment(Agenda.AppointmentImplLocal newAppointment){
//        AppointmentEntity appointmentEntity = new AppointmentEntity();
//        appointmentEntity.setStartTime(Timestamp.valueOf(newAppointment.getStartLocalDateTime()));
//        appointmentEntity.setEndTime(Timestamp.valueOf(newAppointment.getEndLocalDateTime()));
//        appointmentEntity.setDescription(newAppointment.getDescription());
//        session.beginTransaction();
//        session.save(appointmentEntity);
//        session.getTransaction().commit();
//        //returns generated id value
//        return appointmentEntity.getId();
        String sqlCmd = "insert into BOOKINGS (LOCATION, DESCRIPTION, DATETIMESTART, DATETIMEEND)  values (?,?,?,?)";
        ResultSet rs;
        int ID = -1;
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(sqlCmd, new String[] { "ID_COLUMN"} );
            ps.setString(1, newAppointment.getLocation());
            ps.setString(2, newAppointment.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(newAppointment.getStartLocalDateTime()));
            ps.setTimestamp(4, Timestamp.valueOf(newAppointment.getEndLocalDateTime()));
            boolean executed = ps.execute(); //returns a boolean
            System.out.println("New Appointment.insert " + executed);
            rs = ps.getGeneratedKeys();
            while(rs.next()) {
                ID = rs.getInt("ID");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return ID;
    }
    public List<Appointment> getAppointments(LocalDateTime startTime, LocalDateTime endTime){
        List<Appointment> result = FXCollections.observableArrayList();
        String hql = "SELECT * FROM BOOKINGS WHERE DATETIMESTART > ? AND DATETIMEEND < ?";
        ResultSet rs;
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(hql);
            ps.setTimestamp(1, Timestamp.valueOf(startTime));
            ps.setTimestamp(2, Timestamp.valueOf(endTime));
            rs = ps.executeQuery(hql);
            while(rs.next()) {
                Agenda.AppointmentImplLocal appointmentImplLocal= new Appointment()
                        .withLocation(rs.getString("LOCATION"))
                        .withDescription(rs.getString("DESCRIPTION"))
                        .withStartLocalDateTime(rs.getTimestamp("DATETIMESTART").toLocalDateTime())
                        .withEndLocalDateTime(rs.getTimestamp("DATETIMEEND").toLocalDateTime())
                        .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1"));
                Appointment appointment = (Appointment)appointmentImplLocal;
                appointment.setId(rs.getInt("ID"));
                result.add(appointment);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void deleteAppointment(int id){
        String dql = "DELETE FROM BOOKINGS WHERE ID = ?";
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement(dql);
            ps.setInt(1, id);
            boolean executed = ps.execute(); //returns a boolean
            System.out.println("delete" + id + "from Bookings" + executed);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
//        AppointmentEntity entity = (AppointmentEntity)session.get(AppointmentEntity.class,id);
//        session.beginTransaction();
//        session.delete(entity);
//        session.getTransaction().commit();
    }
    public void updateAppointment(Appointment newAppointment) {
//        AppointmentEntity entity = (AppointmentEntity)session.get(AppointmentEntity.class,newAppointment.getId());
//        entity.setStartTime(Timestamp.valueOf(newAppointment.getStartLocalDateTime()));
//        entity.setEndTime(Timestamp.valueOf(newAppointment.getEndLocalDateTime()));
//        entity.setDescription(newAppointment.getDescription());
//        if(entity==null)
//            System.out.println("NULL");
//        else{
//            System.out.println(entity.getId() + entity.getStartTime().toLocalDateTime().toLocalDate().toString());
//        }
//        System.out.println(newAppointment.toString());
//        try {
//            session.beginTransaction();
//            session.update(entity);
//            session.getTransaction().commit();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }
}
