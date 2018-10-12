package com.kaval.android.inflow.Enums;

public enum TaskState {

    TODO, IN_PROGRESS, DONE;

    public static TaskState getFromPosition(int position) {
        switch (position) {
            case 0:
                return TODO;
            case 1:
                return TODO;
            case 2:
                return TODO;
            default:
                throw new IllegalArgumentException("wrong position for enum: " + position);
        }
    }
}
