package com.kaval.android.inflow;

import com.kaval.android.inflow.Enums.TaskState;

import java.util.ArrayList;
import java.util.List;

public class Event {

    public static class ModeChangeTaskStateEvent {
        List<TaskState> possibleStates = new ArrayList<>();

        public ModeChangeTaskStateEvent(List<TaskState> states) {
            possibleStates.addAll(states);
        }
    }

    public static class MoveCurrentSelectedItemEvent {
        TaskState state;

        public MoveCurrentSelectedItemEvent(TaskState state) {
            this.state = state;
        }
    }


    public static class ChangeSelectedTabEvent {
        TaskState taskState;

        public ChangeSelectedTabEvent(TaskState taskState) {
            this.taskState = taskState;
        }
    }

    public static class DeleteCurrentSelectedItemEvent {
    }
}
