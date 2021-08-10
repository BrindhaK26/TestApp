package com.example.gaitanalyzer20;

public class NeckDetails {
    int illness_duration;
    String pain_radiation;
    String injury;
    int severity;
    String stiff_neck;
    String headache_numbness_tingling;
    String doctor_consulted;
    String image_path;

    //gait scores for all params


    NeckDetails(){

    }

    public NeckDetails(int illness_duration, String pain_radiation, String injury, int severity, String stiff_neck, String headache_numbness_tingling, String doctor_consulted, String image_path) {
        this.illness_duration = illness_duration;
        this.pain_radiation = pain_radiation;
        this.injury = injury;
        this.severity = severity;
        this.stiff_neck = stiff_neck;
        this.headache_numbness_tingling = headache_numbness_tingling;
        this.doctor_consulted = doctor_consulted;
        this.image_path = image_path;
    }

    public NeckDetails(int illness_duration, String pain_radiation, String injury, int severity, String stiff_neck, String headache_numbness_tingling, String doctor_consulted) {
        this.illness_duration = illness_duration;
        this.pain_radiation = pain_radiation;
        this.injury = injury;
        this.severity = severity;
        this.stiff_neck = stiff_neck;
        this.headache_numbness_tingling = headache_numbness_tingling;
        this.doctor_consulted = doctor_consulted;

    }




    public int getIllness_duration() {
        return illness_duration;
    }

    public String getPain_radiation() {
        return pain_radiation;
    }

    public String getInjury() {
        return injury;
    }

    public int getSeverity() {
        return severity;
    }

    public String getStiff_neck() {
        return stiff_neck;
    }

    public String getHeadache_numbness_tingling() {
        return headache_numbness_tingling;
    }

    public String getDoctor_consulted() {
        return doctor_consulted;
    }
}
