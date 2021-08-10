package com.example.gaitanalyzer20;

public class NeckData {
    int head_turn_horizontal_forward;
    int head_turn_horizontal_backward;
    int head_turn_vertical_forward;
    int head_turn_vertical_backward;
    //int neck_angle;
    int eyes_closed_forward;
    int eyes_closed_backward;

    NeckData(){

    }

    public NeckData(int head_turn_horizontal_forward, int head_turn_horizontal_backward, int head_turn_vertical_forward, int head_turn_vertical_backward, int eyes_closed_forward, int eyes_closed_backward) {
        this.head_turn_horizontal_forward = head_turn_horizontal_forward;
        this.head_turn_horizontal_backward = head_turn_horizontal_backward;
        this.head_turn_vertical_forward = head_turn_vertical_forward;
        this.head_turn_vertical_backward = head_turn_vertical_backward;
        this.eyes_closed_forward = eyes_closed_forward;
        this.eyes_closed_backward = eyes_closed_backward;

    }

    public NeckData(int head_turn_horizontal_forward) {
        this.head_turn_horizontal_forward = head_turn_horizontal_forward;
    }

    public NeckData(int head_turn_horizontal_forward, int head_turn_horizontal_backward) {
        this.head_turn_horizontal_forward = head_turn_horizontal_forward;
        this.head_turn_horizontal_backward = head_turn_horizontal_backward;
    }

    public NeckData(int head_turn_horizontal_forward, int head_turn_horizontal_backward, int head_turn_vertical_forward) {
        this.head_turn_horizontal_forward = head_turn_horizontal_forward;
        this.head_turn_horizontal_backward = head_turn_horizontal_backward;
        this.head_turn_vertical_forward = head_turn_vertical_forward;
    }

    public NeckData(int head_turn_horizontal_forward, int head_turn_horizontal_backward, int head_turn_vertical_forward, int head_turn_vertical_backward) {
        this.head_turn_horizontal_forward = head_turn_horizontal_forward;
        this.head_turn_horizontal_backward = head_turn_horizontal_backward;
        this.head_turn_vertical_forward = head_turn_vertical_forward;
        this.head_turn_vertical_backward = head_turn_vertical_backward;
    }

    public NeckData(int head_turn_horizontal_forward, int head_turn_horizontal_backward, int head_turn_vertical_forward, int head_turn_vertical_backward, int eyes_closed_forward) {
        this.head_turn_horizontal_forward = head_turn_horizontal_forward;
        this.head_turn_horizontal_backward = head_turn_horizontal_backward;
        this.head_turn_vertical_forward = head_turn_vertical_forward;
        this.head_turn_vertical_backward = head_turn_vertical_backward;
        this.eyes_closed_forward = eyes_closed_forward;
    }

    public int getHead_turn_horizontal_forward() {
        return head_turn_horizontal_forward;
    }

    public int getHead_turn_horizontal_backward() {
        return head_turn_horizontal_backward;
    }

    public int getHead_turn_vertical_forward() {
        return head_turn_vertical_forward;
    }

    public int getHead_turn_vertical_backward() {
        return head_turn_vertical_backward;
    }

    public int getEyes_closed_forward() {
        return eyes_closed_forward;
    }

    public int getEyes_closed_backward() {
        return eyes_closed_backward;
    }
}
