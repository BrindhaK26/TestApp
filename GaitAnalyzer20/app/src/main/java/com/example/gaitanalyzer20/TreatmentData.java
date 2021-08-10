package com.example.gaitanalyzer20;

public class TreatmentData {
    int treatmentDuration;
    String Diagnosis;
    String Surgery;
    String Precaution;
    String Physio;
    String PhysioDetails;
    TreatmentData(){

    }

    public TreatmentData(int treatmentDuration, String diagnosis, String surgery, String precaution, String physio, String physioDetails) {
        this.treatmentDuration = treatmentDuration;
        Diagnosis = diagnosis;
        Surgery = surgery;
        Precaution = precaution;
        Physio = physio;
        PhysioDetails = physioDetails;
    }

    public int getTreatmentDuration() {
        return treatmentDuration;
    }

    public String getDiagnosis() {
        return Diagnosis;
    }

    public String getSurgery() {
        return Surgery;
    }

    public String getPrecaution() {
        return Precaution;
    }

    public String getPhysio() {
        return Physio;
    }

    public String getPhysioDetails() {
        return PhysioDetails;
    }
}
