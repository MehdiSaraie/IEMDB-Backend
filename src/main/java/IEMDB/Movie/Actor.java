package IEMDB.Movie;

import java.util.ArrayList;
import java.util.List;

public class Actor {
    private Integer id;
    private String name;
    private String birthDate;
    private String nationality;

    public Actor(Integer id, String name, String birthDate, String nationality) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor that = (Actor) o;
        return id.equals(that.id);
    }

    public Integer getId() {
        return this.id;
    }

//    public String getName() {
//        return this.name;
//    }
//    public String getBirthDate() {
//        return this.birthDate;
//    }
//    public String getNationality() {
//        return this.nationality;
//    }
//
//    public void updateActorInfo(Actor actor) {
//        this.name = actor.getName();
//        this.birthDate = actor.getBirthDate();
//        this.nationality = actor.getNationality();
//    }

}