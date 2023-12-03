package com.cbnu.shop.service;

import com.cbnu.shop.domain.Category;
import com.cbnu.shop.domain.Item;
import com.cbnu.shop.domain.ItemDto;
import com.cbnu.shop.domain.SearchApiResponse;
import com.cbnu.shop.repository.CategoryRepository;
import com.cbnu.shop.repository.ItemRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverApiService {
    private final CategoryRepository categoryRepository;
    private static final Random random = new Random();
    String clientId = "xqqkRzFQ_OVBJ5CrXx_0";
    String clientSecret = "U_v1LV98cW";

    String text = null;
    public List<Item> crawler() throws JsonProcessingException, InterruptedException {
        List<Category> categories = categoryRepository.findAll();
        List<Item> itemList = new ArrayList<>();
        for (Category category : categories) {
            text = URLEncoder.encode(category.getName());
            String apiURL = "https://openapi.naver.com/v1/search/shop?query=" + text;
            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("X-Naver-Client-Id", clientId);
            requestHeaders.put("X-Naver-Client-Secret", clientSecret);
            String responseBody = get(apiURL,requestHeaders);

            ObjectMapper objectMapper = new ObjectMapper();
            SearchApiResponse searchApiResponse = objectMapper.readValue(responseBody, SearchApiResponse.class);

            for (ItemDto dto : searchApiResponse.getItems()) {
                log.info("itemDto:={}", dto.getCategory2());
                Item item = new Item();
                item.setItemName(dto.getTitle());  // title을 itemName에 매핑
                item.setPrice(new BigDecimal(dto.getLprice()));  // lprice를 price에 매핑
                item.setCategoryId(categoryRepository.findByCategoryName(dto.getCategory2()));  // category2를 category에 매핑

                // brand를 brand에 매핑
                item.setBrand(dto.getBrand());

                // releaseDate와 quantity 랜덤 값 설정
                item.setReleaseDate(generateRandomDate());
                item.setQuantity(generateRandomQuantity());
                itemList.add(item);
            }
            Thread.sleep(200);
        }

            return itemList;
    }


    private static String get(String apiURL, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiURL);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

    private static Date generateRandomDate() {
        // 랜덤으로 과거 10년 이내의 날짜 생성
        long tenYearsInMillis = 10L * 365 * 24 * 60 * 60 * 1000;
        long randomMillis = System.currentTimeMillis() - random.nextLong() % tenYearsInMillis;
        return new Date(randomMillis);
    }

    private static Long generateRandomQuantity() {
        // 랜덤으로 1에서 100까지의 수 생성
        return 1L + random.nextInt(100);
    }
}
