/*
 * <Orbit Editor. An Open Source Text Editor>
 *
 * Copyright (C) 2018 Jordan Swann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package io.orbit.util;

import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tyler Swann on Friday March 23, 2018 at 15:33
 */
public class EventTargetObject implements ControllerEventTarget
{
    private Map<EventType, Collection<EventHandler>> handlers = new HashMap<>();

    @Override
    public Map<EventType, Collection<EventHandler>> getHandlers() { return handlers; }
}
