package com.viktoryi;

import lombok.Data;

@Data
public class Movie {
    private int rank;
    private String name;
    private float rating;
    private String url;
    private int releaseYear;
    private String description;
}
