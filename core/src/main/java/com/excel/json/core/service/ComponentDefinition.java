package com.excel.json.core.service;

import java.util.List;

public class ComponentDefinition {
    private String name;
    private String title;
    private String description;
    private String resourceSupertype;
    private String inputType;
    private boolean hasVariations;

    private String fieldLabel;

    private boolean isHidden;

    private boolean hasShowHideLogic;

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceSupertype() {
        return resourceSupertype;
    }

    public void setResourceSupertype(String resourceSupertype) {
        this.resourceSupertype = resourceSupertype;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public boolean isHasVariations() {
        return hasVariations;
    }

    public void setHasVariations(boolean hasVariations) {
        this.hasVariations = hasVariations;
    }

    public void setRequiredFields(List<String> requiredFields) {
    }

    public void setLabel(String fieldLabel) {
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public boolean isHasShowHideLogic() {
        return hasShowHideLogic;
    }

    public void setHasShowHideLogic(boolean hasShowHideLogic) {
        this.hasShowHideLogic = hasShowHideLogic;
    }



}
