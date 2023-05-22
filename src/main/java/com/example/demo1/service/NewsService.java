package com.example.demo1.service;

import com.example.demo1.entity.News;
import com.example.demo1.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public News createNews(News news) {
        news.setTimeOfCreation(LocalDateTime.now());
        return newsRepository.save(news);
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public void reverseNews(List<News> allNews) {
        Collections.reverse(allNews);
    }

    public List<News> paginateNews(Integer page, Integer newsLimitOnSinglePage, Integer size, List<News> allNews) {
        List<News> newsOnRequestedPage;
        if (size > newsLimitOnSinglePage * (page + 1)) {
            newsOnRequestedPage = allNews.subList(newsLimitOnSinglePage * page, newsLimitOnSinglePage * (page + 1));
        } else {
            newsOnRequestedPage = allNews.subList(newsLimitOnSinglePage * page, size);
        }
        return newsOnRequestedPage;
    }


}
