package io.orbit.api.event;

import io.orbit.api.autocompletion.AutoCompletionBase;
import io.orbit.api.Nullable;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import java.util.Optional;

/**
 * Created by Tyler Swann on Wednesday March 21, 2018 at 13:36
 */
@Deprecated
public class AutoCompletionEvent extends Event
{

    public static final EventType<AutoCompletionEvent> OPTION_WAS_SELECTED = new EventType<>(Event.ANY, "OPTION_WAS_SELECTED");
    public static final EventType<AutoCompletionEvent> SUB_OPTION_WAS_SELECTED = new EventType<>(Event.ANY, "SUB_OPTION_WAS_SELECTED");

    public final Optional<? extends AutoCompletionBase> selectedOption;

    public AutoCompletionEvent(@Nullable AutoCompletionBase selectedOption, Object source, EventTarget target, EventType<? extends Event> eventType)
    {
        super(source, target, eventType);
        this.selectedOption = Optional.ofNullable(selectedOption);
    }

    @Override
    public AutoCompletionEvent copyFor(Object newSource, EventTarget newTarget) { return (AutoCompletionEvent) super.copyFor(newSource, newTarget); }
}
