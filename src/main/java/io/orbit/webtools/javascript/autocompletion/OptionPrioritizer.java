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
package io.orbit.webtools.javascript.autocompletion;

import io.orbit.webtools.javascript.typedefs.parsing.Interface;
import io.orbit.webtools.javascript.typedefs.parsing.Method;
import io.orbit.webtools.javascript.typedefs.parsing.Scope;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By: Tyler Swann.
 * Date: Thursday, Nov 15, 2018
 * Time: 5:25 PM
 * Website: https://orbiteditor.com
 */
public class OptionPrioritizer
{
    private static Map<String, Map<String, Integer>> priorityMap = new HashMap<>();

    private OptionPrioritizer() { }

    static {
        priorityMap.put("Document", new HashMap<>());
        priorityMap.get("Document").put("getElementById", 100);
        priorityMap.get("Document").put("getElementsByClassName", 100);
        priorityMap.get("Document").put("getElementsByName", 100);
        priorityMap.get("Document").put("getElementsByTagName", 100);
        priorityMap.get("Document").put("addEventListener", 90);
        priorityMap.get("Document").put("removeEventListener", 90);
    }

    public static void prioritize(Scope scope)
    {
        for (String key : priorityMap.keySet())
        {
            Interface type = scope.getInterfaces().get(key);
            Map<String, Integer> methodPriorityMap = priorityMap.get(key);
            for (String methodKey : methodPriorityMap.keySet())
            {
                Method method = getMethod(methodKey, type.getMethods());
                if (method == null)
                    continue;
                int priority = methodPriorityMap.get(methodKey);
                method.setPriority(priority);
            }

        }
    }

    private static Method getMethod(String name, List<Method> methods)
    {
        for (Method method : methods)
        {
            if (method.getName().equals(name))
                return method;
        }
        return null;
    }
}