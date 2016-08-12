package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Player extends Model {


    public String email;
    public String usrPassword;
    public String lastName;
    public String firstName;
    public boolean isSupprime = false;

    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    @CollectionTable(name = "PlayerRessource", joinColumns = @JoinColumn(name = "player_id"))
    public Map<String, Long> ressources;

    public Player() {
        ressources = new HashMap<>();
    }
}
