package pl.edu.agh.student.retrofit;

/**
 * Created by mkolbusz on 10.11.17.
 */

public class Student {
    Integer id;
    String firstname;
    String lastname;
    String album;
    Double note;

    public Student(Integer id, String firstname, String lastname, String album, Double note) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.album = album;
        this.note = note;
    }

    public Student(String firstname, String lastname, String album, Double note) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.album = album;
        this.note = note;
    }
}
