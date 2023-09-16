package com.maniproject.newswave.utils.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceProvider2Test {

    private final CategoryServiceProvider2 categoryServiceProvider2 = new CategoryServiceProvider2();
    @Test
    void getCategories() {
        assertEquals(7, new CategoryServiceProvider1().getCategories().size());
        assertEquals("business", new CategoryServiceProvider1().getCategories().get(0));
        assertEquals("entertainment", new CategoryServiceProvider1().getCategories().get(1));
        assertEquals(new ArrayList<String>(Arrays.asList("business", "entertainment", "environment", "food", "health", "politics", "science", "sports", "technology", "top", "tourism", "world")) , categoryServiceProvider2.getCategories());
    }
}