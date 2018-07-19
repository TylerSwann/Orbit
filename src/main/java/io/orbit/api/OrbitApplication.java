package io.orbit.api;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

/**
 * Created by Tyler Swann on Thursday July 19, 2018 at 15:07
 */
public final class OrbitApplication
{
    private OrbitApplication() {}

    public static final ObservableValue<Stage> PRIMARY_STAGE = new SimpleObjectProperty<>();
}
