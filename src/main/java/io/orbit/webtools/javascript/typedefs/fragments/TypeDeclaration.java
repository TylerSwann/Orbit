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
package io.orbit.webtools.javascript.typedefs.fragments;

/**
 * Created By: Tyler Swann.
 * Date: Saturday, Nov 03, 2018
 * Time: 11:12 AM
 * Website: https://orbiteditor.com
 */
public class TypeDeclaration extends TypeNode
{
    private String kindString = "";
    private String originalName;
    private TypeFragment type;
    private Group[] groups = new Group[0];
    private Source[] sources = new Source[0];
    private TypeDeclaration[] children = new TypeDeclaration[0];
    private Signature[] signatures = new Signature[0];

    public String getKindString() {
        return kindString;
    }
    public void setKindString(String kindString) {
        this.kindString = kindString;
    }
    public String getOriginalName() {
        return originalName;
    }
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
    public TypeFragment getType() {
        return type;
    }
    public void setType(TypeFragment type) {
        this.type = type;
    }
    public Group[] getGroups() {
        return groups;
    }
    public void setGroups(Group[] groups) {
        this.groups = groups;
    }
    public Source[] getSources() {
        return sources;
    }
    public void setSources(Source[] sources) {
        this.sources = sources;
    }
    public TypeDeclaration[] getChildren() {
        return children;
    }
    public void setChildren(TypeDeclaration[] children) {
        this.children = children;
    }
    public Signature[] getSignatures() {
        return signatures;
    }
    public void setSignatures(Signature[] signatures) {
        this.signatures = signatures;
    }
}