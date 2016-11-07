package com.gmod.gianni.querbeer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gianni on 07/11/2016.
 */

public class Data {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("ibu")
    @Expose
    private String ibu;

    @SerializedName("abv")
    @Expose
    private String abv;

    @SerializedName("nameDisplay")
    @Expose
    private String nameDisplay;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("glass")
    @Expose
    private Glass glass;

    @SerializedName("style")
    @Expose
    private Style style;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameDisplay() {
        return nameDisplay;
    }

    public void setNameDisplay(String nameDisplay) {
        this.nameDisplay = nameDisplay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Glass getGlass() {
        return glass;
    }

    public void setGlass(Glass glass) {
        this.glass = glass;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public String getIbu() {
        return ibu;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }
}
