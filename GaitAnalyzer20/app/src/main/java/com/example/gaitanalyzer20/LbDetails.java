package com.example.gaitanalyzer20;

public class LbDetails {
    int illness;
    String injury;
    String radiate_pain_in_limbs;
    int severity;
    String walking_difficulty;
    String walking_aid;
    String Doctor_seen;
    String image_path;
    ///taking only gait scores

    LbDetails(){

    }

    public LbDetails(int illness, String injury, String radiate_pain_in_limbs, int severity, String walking_difficulty, String walking_aid, String doctor_seen,String image_path) {
        this.illness = illness;
        this.injury = injury;
        this.radiate_pain_in_limbs = radiate_pain_in_limbs;
        this.severity = severity;
        this.walking_difficulty = walking_difficulty;
        this.walking_aid = walking_aid;
        this.Doctor_seen = doctor_seen;
        this.image_path= image_path;
    }

    public int getIllness() {
        return illness;
    }

    public String getInjury() {
        return injury;
    }

    public String getRadiate_pain_in_limbs() {
        return radiate_pain_in_limbs;
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

    public String getDoctor_seen() {
        return Doctor_seen;
    }
}

