package com.cbnu.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryCrawler {

    public MultiValueMap<Long, String> crawler() {
        int startCategoryId = 50000000;
        MultiValueMap<Long, String> categories = new LinkedMultiValueMap<>();

        try {
            for (int i = 1; i <= 9; i++) {
                String url = "https://search.shopping.naver.com/best/category/click?categoryCategoryId=" + startCategoryId +
                        "&categoryDemo=M02&categoryRootCategoryId=" + startCategoryId + "&chartRank=1&period=P1D";

                Connection conn = Jsoup.connect(url);
                Document document = conn.get();
                String subCategory = document.getElementsByClass("detailFilter_btn_detail__ESOWW").text();

                String[] subCategories = subCategory.split(" ");

                for (int k = 1; k < subCategories.length; k++) {
                    categories.add(Integer.toUnsignedLong(i), subCategories[k]);
                }

                startCategoryId++;
            }
        } catch (IOException e) {
            System.err.println("데이터 가져오기 오류: " + e.getMessage());
        }

        return categories;
    }

}

