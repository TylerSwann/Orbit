package io.orbit.ui;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * Created by Tyler Swann on Thursday March 08, 2018 at 14:12
 */
public class MUIStepDialogEvent extends Event
{
    public static final EventType<MUIStepDialogEvent> NEXT_STEP = new EventType<>(Event.ANY, "NEXT_STEP");
    public static final EventType<MUIStepDialogEvent> PREVIOUS_STEP = new EventType<>(Event.ANY, "PREVIOUS_STEP");
    public static final EventType<MUIStepDialogEvent> COMPLETION = new EventType<>(Event.ANY, "COMPLETION");


    public MUIStepDialogEvent(Object source, EventTarget target, EventType<? extends Event> eventType) { super(source, target, eventType); }
    @Override
    public MUIStepDialogEvent copyFor(Object newSource, EventTarget newTarget) { return (MUIStepDialogEvent) super.copyFor(newSource, newTarget); }
}
