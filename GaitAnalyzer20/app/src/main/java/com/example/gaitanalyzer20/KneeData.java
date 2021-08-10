package com.example.gaitanalyzer20;

public class KneeData {
    int gait_speed_forward;
    int gait_speed_backward;
    int gait_level_surface;
    int total;

    KneeData(){

    }

    public KneeData(int gait_speed_forward, int gait_speed_backward) {
        this.gait_speed_forward = gait_speed_forward;
        this.gait_speed_backward = gait_speed_backward;
    }

    public KneeData(int gait_speed_forward) {
        this.gait_speed_forward = gait_speed_forward;
    }


    public KneeData(int gait_speed_forward, int gait_speed_backward, int gait_level_surface) {
        this.gait_speed_forward = gait_speed_forward;
        this.gait_speed_backward = gait_speed_backward;
        this.gait_level_surface = gait_level_surface;

    }

    public KneeData(int gait_speed_forward, int gait_speed_backward, int gait_level_surface, int total) {
        this.gait_speed_forward = gait_speed_forward;
        this.gait_speed_backward = gait_speed_backward;
        this.gait_level_surface = gait_level_surface;
        this.total = total;
    }
    public void getTotal(){
        this.total = this.gait_speed_backward+this.gait_speed_forward+this.gait_level_surface;
    }

    public int getGait_speed_forward() {
        return gait_speed_forward;
    }

    public int getGait_speed_backward() {
        return gait_speed_backward;
    }

    public int getGait_level_surface() {
        return gait_level_surface;
    }



    public void setGait_speed_forward(int gait_speed_forward) {
        this.gait_speed_forward = gait_speed_forward;
    }

    public void setGait_speed_backward(int gait_speed_backward) {
        this.gait_speed_backward = gait_speed_backward;
    }

    public void setGait_level_surface(int gait_level_surface) {
        this.gait_level_surface = gait_level_surface;
    }


}
