package com.example.gaitanalyzer20;

public class LbData {
    int gait_speed_forward;
    int gait_speed_backward;

    int eyes_closed_forward;
    int eyes_closed_backward;
    int gait_level_surface;
    //int stride_length;
    //int angle;
    LbData(){}

    public LbData(int gait_speed_forward) {
        this.gait_speed_forward = gait_speed_forward;
    }

    public LbData(int gait_speed_forward, int gait_speed_backward) {
        this.gait_speed_forward = gait_speed_forward;
        this.gait_speed_backward = gait_speed_backward;
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

    public int getEyes_closed_forward() {
        return eyes_closed_forward;
    }

    public int getEyes_closed_backward() {
        return eyes_closed_backward;
    }

    public LbData(int gait_speed_forward, int gait_speed_backward, int gait_level_surface, int eyes_closed_forward) {
        this.gait_speed_forward = gait_speed_forward;
        this.gait_speed_backward = gait_speed_backward;
        this.gait_level_surface = gait_level_surface;
        this.eyes_closed_forward = eyes_closed_forward;
    }

    public LbData(int gait_speed_forward, int gait_speed_backward, int gait_level_surface) {
        this.gait_speed_forward = gait_speed_forward;
        this.gait_speed_backward = gait_speed_backward;
        this.gait_level_surface = gait_level_surface;
    }

    public LbData(int gait_speed_forward, int gait_speed_backward, int eyes_closed_forward, int eyes_closed_backward,int gait_level_surface) {
        this.gait_speed_forward = gait_speed_forward;
        this.gait_speed_backward = gait_speed_backward;
        this.gait_level_surface = gait_level_surface;
        this.eyes_closed_forward = eyes_closed_forward;
        this.eyes_closed_backward = eyes_closed_backward;
    }
}

