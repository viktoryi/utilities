package com.viktoryi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MovieLeaderboard {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        List<Movie> list = new ArrayList<>();
        List<Future<Movie>> futures = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            int index = 25 * i;
            String url = "https://movie.douban.com/top250?start=%s";
            Document doc = Jsoup.connect(String.format(url, String.valueOf(index))).get();
            Elements newsHeadlines = doc.select(".grid_view li");

            for (Element element : newsHeadlines) {
                Movie movie = new Movie();
                movie.setRank(Integer.valueOf(element.select(".pic em").html()));
                movie.setName(element.select(".info .hd a span").first().html());
                movie.setRating(Float.valueOf(element.select(".info .bd .star .rating_num").html()));
                movie.setUrl(element.select(".pic a").attr("href"));
                Future<Movie> future = service.submit(new MovieDetailCallable(movie));
                futures.add(future);
            }
        }

        for (Future<Movie> future : futures) {
            list.add(future.get());
        }

        service.shutdown();

        list.forEach(System.out::println);
    }

}
