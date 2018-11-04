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
package io.orbit.webtools.javascript.typedefs;

/**
 * Created By: Tyler Swann.
 * Date: Friday, Nov 02, 2018
 * Time: 3:31 PM
 * Website: https://orbiteditor.com
 */
public class Flag
{
    private boolean isOptional = false;
    private boolean isRest = false;
    private boolean isExported = false;

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public boolean isRest() {
        return isRest;
    }

    public void setRest(boolean rest) {
        isRest = rest;
    }

    public boolean isExported() {
        return isExported;
    }

    public void setExported(boolean exported) {
        isExported = exported;
    }
}