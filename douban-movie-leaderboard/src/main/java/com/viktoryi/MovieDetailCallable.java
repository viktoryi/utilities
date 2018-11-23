package com.viktoryi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.Callable;

public class MovieDetailCallable implements Callable<Movie> {

    private Movie movie;

    public MovieDetailCallable(Movie movie) {
        this.movie = movie;
    }

    public Movie call() throws Exception {
        Document doc = Jsoup.connect(movie.getUrl()).get();
        StringBuilder sb = new StringBuilder(doc.select("#content h1 .year").html());
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(0);
        movie.setReleaseYear(Integer.valueOf(sb.toString()));
        movie.setDescription(doc.select("#link-report span").first().attr("property", "v:summary").html());
        return movie;
    }
}
