package com.example.demo1.Services;

import com.example.demo1.Entities.News;
import com.example.demo1.Repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public News createNews(News news) {
        news.setTimeOfCreation(LocalDateTime.now());
        return newsRepository.save(news);
    }

}
