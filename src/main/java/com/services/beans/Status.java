package com.services.beans;

import java.io.Serializable;
/**
 * Status bean which will returned for the posted items to Cache
 * @author info4siva
 **/
public class Status implements Serializable {
    private int code;
    private String description;

    public enum  ResponseCodes {
        UNSET(999, "Status not available"),
        SUCCESS(201, "Item Add Operation completed successfully !"),
        SYSTEM_ERROR(501, "System error occurred while adding input values. Please try again"),
        USER_INPUT(502, "Please check your input values and try again");

        private int codeValue;
        private String codeDescription;

        ResponseCodes(int pCode, String pDescription) {
            codeValue = pCode;
            codeDescription = pDescription;
        }

        public int getCodeValue() {
            return codeValue;
        }
        public String getCodeDescription() {
            return codeDescription;
        }
    }

    public Status(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public Status(ResponseCodes pCode) {
        this.code = pCode.getCodeValue();
        this.description = pCode.getCodeDescription();
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return this.description;
    }

    public String toString() {
        return "Status: " + this.description + "(" + this.code + ")";
    }
}
