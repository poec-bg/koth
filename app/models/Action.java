package models;

import models.types.ActionPossible;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Action extends Model {

    @ManyToOne
    public Player player;

    @ManyToOne
    public Zone zone;

    @Enumerated(EnumType.STRING)
    public ActionPossible action;

    public Date date;

}
