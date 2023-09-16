package com.maniproject.newswave.utils.category;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceProvider1 implements Category {

    @Override
    public List<String> getCategories() {
        return List.of("business", "entertainment", "general", "health", "science", "sports", "technology");
    }
}

