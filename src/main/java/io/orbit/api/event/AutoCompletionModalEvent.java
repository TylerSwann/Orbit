package io.orbit.api.event;

import io.orbit.api.Nullable;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import java.util.Optional;
import io.orbit.api.autocompletion.AutoCompletionOption;
/**
 * Created by Tyler Swann on Thursday June 07, 2018 at 16:51
 */
public class AutoCompletionModalEvent extends Event
{
    public static final EventType<AutoCompletionModalEvent> OPTION_WAS_SELECTED = new EventType<>(Event.ANY, "OPTION_WAS_SELECTED2");

    public final Optional<AutoCompletionOption> selectedOption;

    public AutoCompletionModalEvent(EventType<? extends Event> eventType, Object source, EventTarget target, @Nullable AutoCompletionOption selectedOption)
    {
        super(source, target, eventType);
        this.selectedOption = Optional.ofNullable(selectedOption);
    }

    @Override
    public AutoCompletionModalEvent copyFor(Object newSource, EventTarget newTarget) { return (AutoCompletionModalEvent) super.copyFor(newSource, newTarget); }
}
