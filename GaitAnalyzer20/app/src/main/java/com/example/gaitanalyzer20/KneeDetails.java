package com.example.gaitanalyzer20;

import java.time.LocalDate;

public class KneeDetails {
     int illness_duration;
     String side;
     String injury;
     int severity;
     String walking_difficulty;
     String walking_aid;
     String doctor_consulted;
     //String image_path;
//gait scores for each param
    LocalDate currentDate;

 KneeDetails(){

 }

    public KneeDetails(int illness_duration, String side, String injury, int severity, String walking_difficulty, String walking_aid, String doctor_consulted) {
        this.illness_duration = illness_duration;
        this.side = side;
        this.injury = injury;
        this.severity = severity;
        this.walking_difficulty = walking_difficulty;
        this.walking_aid = walking_aid;
        this.doctor_consulted = doctor_consulted;
        //this.image_path = image_path;
    }

    public int getIllness_duration() {
        return illness_duration;
    }

    public String getSide() {
        return side;
    }

    public String getInjury() {
        return injury;
    }

    public int getSeverity() {
        return severity;
    }

    public String getWalking_difficulty() {
        return walking_difficulty;
    }

    public String getWalking_aid() {
        return walking_aid;
    }

    public String getDoctor_consulted() {
        return doctor_consulted;
    }
}
