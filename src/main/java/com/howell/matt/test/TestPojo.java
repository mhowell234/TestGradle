package com.howell.matt.test;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonPropertyOrder({ "aString", "aBool", "anInt"})
public class TestPojo {

    /** String. */
    private String aString;

    /** Boolean. */
    private boolean aBool;

    /** Integer. */
    private int anInt;

    private List<String> strings;

    @Builder.Default
    private float aFloat = 1.34343434343F;
}
