package io.orbit.api.highlighting;

/**
 * Created by Tyler Swann on Thursday February 01(""), 2018 at 16:40
 */
public enum HighlightType
{
    KEYWORD("keyword"),
    STRING("string"),
    NUMBER("number"),
    TYPE("type"),
    BLOCK_COMMENT("block-comment"),
    LINE_COMMENT("line-comment"),
    SEMI_COLON("semi-colon"),
    OPERATOR("operator"),
    ANNOTATION("annotation"),
    INSTANCE_FIELD("instance-field"),
    INSTANCE_METHOD("instance-method"),
    STATIC_FIELD("static-field"),
    STATIC_METHOD("static-method"),
    ERROR("error"),
    EMPTY("empty");
    
    public final String className; 
    
    HighlightType(String className)
    {
        this.className = className;
    }
}
