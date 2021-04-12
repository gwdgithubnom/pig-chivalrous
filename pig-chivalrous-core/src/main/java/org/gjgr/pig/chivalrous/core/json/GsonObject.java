package org.gjgr.pig.chivalrous.core.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LinkedTreeMap;
import java.io.Serializable;

import java.util.Map;
import java.util.Set;

/**
 * JSON对象<br>
 * 例：<br>
 *
 * <pre>
 * newJson = new JsonObject().put(&quot;Json&quot;, &quot;Hello, World!&quot;).toString();
 * </pre>
 *
 * @author looly
 */
public class GsonObject extends JsonElement implements Serializable {

    protected static final long serialVersionUID = 1024L;
    protected final LinkedTreeMap<String, JsonElement> members =
        new LinkedTreeMap<String, JsonElement>();

    /**
     * Creates a deep copy of this element and all its children
     * @since 2.8.2
     */
    @Override
    public GsonObject deepCopy() {
        GsonObject result = new GsonObject();
        for (Map.Entry<String, JsonElement> entry : members.entrySet()) {
            result.add(entry.getKey(), entry.getValue().deepCopy());
        }
        return result;
    }

    @Override
    public boolean isJsonObject() {
        return true;
    }

    @Override
    public JsonObject getAsJsonObject() {
        JsonObject jsonObject =  new JsonObject();
        jsonObject.entrySet().addAll(members.entrySet());
        return jsonObject;
    }
    /**
     * Adds a member, which is a name-value pair, to self. The name must be a String, but the value
     * can be an arbitrary JsonElement, thereby allowing you to build a full tree of JsonElements
     * rooted at this node.
     *
     * @param property name of the member.
     * @param value the member object.
     */
    public void add(String property, JsonElement value) {
        if (value == null) {
            value = JsonNull.INSTANCE;
        }
        members.put(property, value);
    }

    /**
     * Removes the {@code property} from this {@link GsonObject}.
     *
     * @param property name of the member that should be removed.
     * @return the {@link JsonElement} object that is being removed.
     * @since 1.3
     */
    public JsonElement remove(String property) {
        return members.remove(property);
    }

    /**
     * Convenience method to add a primitive member. The specified value is converted to a
     * JsonPrimitive of String.
     *
     * @param property name of the member.
     * @param value the string value associated with the member.
     */
    public void addProperty(String property, String value) {
        add(property, createJsonElement(value));
    }

    /**
     * Convenience method to add a primitive member. The specified value is converted to a
     * JsonPrimitive of Number.
     *
     * @param property name of the member.
     * @param value the number value associated with the member.
     */
    public void addProperty(String property, Number value) {
        add(property, createJsonElement(value));
    }

    /**
     * Convenience method to add a boolean member. The specified value is converted to a
     * JsonPrimitive of Boolean.
     *
     * @param property name of the member.
     * @param value the number value associated with the member.
     */
    public void addProperty(String property, Boolean value) {
        add(property, createJsonElement(value));
    }

    /**
     * Convenience method to add a char member. The specified value is converted to a
     * JsonPrimitive of Character.
     *
     * @param property name of the member.
     * @param value the number value associated with the member.
     */
    public void addProperty(String property, Character value) {
        add(property, createJsonElement(value));
    }

    /**
     * Creates the proper {@link JsonElement} object from the given {@code value} object.
     *
     * @param value the object to generate the {@link JsonElement} for
     * @return a {@link JsonPrimitive} if the {@code value} is not null, otherwise a {@link JsonNull}
     */
    private JsonElement createJsonElement(Object value) {
        return value == null ? JsonNull.INSTANCE : JsonCommand.jsonPrimitive(value);
    }

    /**
     * Returns a set of members of this object. The set is ordered, and the order is in which the
     * elements were added.
     *
     * @return a set of members of this object.
     */
    public Set<Map.Entry<String, JsonElement>> entrySet() {
        return members.entrySet();
    }

    /**
     * Returns a set of members key values.
     *
     * @return a set of member keys as Strings
     * @since 2.8.1
     */
    public Set<String> keySet() {
        return members.keySet();
    }

    /**
     * Returns the number of key/value pairs in the object.
     *
     * @return the number of key/value pairs in the object.
     */
    public int size() {
        return members.size();
    }

    /**
     * Convenience method to check if a member with the specified name is present in this object.
     *
     * @param memberName name of the member that is being checked for presence.
     * @return true if there is a member with the specified name, false otherwise.
     */
    public boolean has(String memberName) {
        return members.containsKey(memberName);
    }

    /**
     * Returns the member with the specified name.
     *
     * @param memberName name of the member that is being requested.
     * @return the member matching the name. Null if no such member exists.
     */
    public JsonElement get(String memberName) {
        return members.get(memberName);
    }

    /**
     * Convenience method to get the specified member as a JsonPrimitive element.
     *
     * @param memberName name of the member being requested.
     * @return the JsonPrimitive corresponding to the specified member.
     */
    public JsonPrimitive getAsJsonPrimitive(String memberName) {
        return (JsonPrimitive) members.get(memberName);
    }

    /**
     * Convenience method to get the specified member as a JsonArray.
     *
     * @param memberName name of the member being requested.
     * @return the JsonArray corresponding to the specified member.
     */
    public JsonArray getAsJsonArray(String memberName) {
        return (JsonArray) members.get(memberName);
    }

    /**
     * Convenience method to get the specified member as a JsonObject.
     *
     * @param memberName name of the member being requested.
     * @return the JsonObject corresponding to the specified member.
     */
    public GsonObject getAsJsonObject(String memberName) {
        return (GsonObject) members.get(memberName);
    }

    public String getAsString(String memberName){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsString();
        }else{
            return null;
        }
    }

    public String getAsString(String memberName,String defaultValue){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsString();
        }else{
            return defaultValue;
        }
    }

    public Number getAsNumber(String memberName){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsNumber();
        }else{
            return null;
        }
    }

    public Number getAsNumber(String memberName,Number defaultValue){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsNumber();
        }else{
            return defaultValue;
        }
    }

    public Integer getAsInt(String memberName){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsInt();
        }else{
            return null;
        }
    }

    public int getAsInt(String memberName,int defaultValue){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsInt();
        }else{
            return defaultValue;
        }
    }

    public Boolean getAsBoolean(String memberName){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsBoolean();
        }else{
            return null;
        }
    }

    public boolean getAsBoolean(String memberName,boolean defaultValue){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsBoolean();
        }else{
            return defaultValue;
        }
    }

    public Long getAsLong(String memberName,Long defaultValue){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsLong();
        }else{
            return null;
        }
    }

    public long getAsLong(String memberName,long defaultValue){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsLong();
        }else{
            return defaultValue;
        }
    }

    public short getAsShort(String memberName,short defaultValue){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsShort();
        }else{
            return defaultValue;
        }
    }

    public Short getAsShort(String memberName){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsShort();
        }else{
            return null;
        }
    }

    public double getAsDouble(String memberName,double defaultValue){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsDouble();
        }else{
            return defaultValue;
        }
    }

    public Double getAsDouble(String memberName){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsDouble();
        }else{
            return null;
        }
    }

    public Float getAsFloat(String memberName,Float defaultValue){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsFloat();
        }else{
            return null;
        }
    }


    public float getAsFloat(String memberName,float defaultValue){
        if(members.containsKey(memberName)){
            return members.get(memberName).getAsFloat();
        }else{
            return defaultValue;
        }
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof GsonObject
            && ((GsonObject) o).members.equals(members));
    }

    @Override
    public int hashCode() {
        return members.hashCode();
    }

    public JsonElement getElement(String key, Object defaultValue) {
        JsonElement obj = this.get(key);
        return obj.isJsonNull() ? JsonCommand.jsonPrimitive(defaultValue) : obj;
    }

}
