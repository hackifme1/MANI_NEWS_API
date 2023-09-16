package com.maniproject.newswave.utils.category;

import java.util.List;

public class CategoryServiceProvider2 implements Category{

    @Override
    public List<String> getCategories() {
        return List.of("business", "entertainment", "environment", "food", "health", "politics", "science", "sports", "technology", "top", "tourism", "world");
    }
}