package com.maniproject.newswave.utils.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceProvider1Test {

    private final CategoryServiceProvider1 categoryServiceProvider1 = new CategoryServiceProvider1();
    @Test
    void getCategories() {
        assertEquals(7, new CategoryServiceProvider1().getCategories().size());
        assertEquals("business", new CategoryServiceProvider1().getCategories().get(0));
        assertEquals("entertainment", new CategoryServiceProvider1().getCategories().get(1));
        assertEquals(new ArrayList<String>(Arrays.asList("business", "entertainment", "general", "health", "science", "sports", "technology")) , categoryServiceProvider1.getCategories());
    }
}