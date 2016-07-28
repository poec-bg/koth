package models;

import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
public class Player extends Model {

    public String email;
    public String password;
    public String lastName;
    public String firstName;

}
