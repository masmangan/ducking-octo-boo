package org.freerealm.xmlwrapper;

/**
 *
 * @author Deniz ARIKAN
 */
public class TagData {

    private String id;
    private String className;
    private String xMLConverterName;

    public TagData(String id, String className, String xMLConverterName) {
        this.id = id;
        this.className = className;
        this.xMLConverterName = xMLConverterName;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getXMLConverterName() {
        return xMLConverterName;
    }
}
