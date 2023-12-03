package com.cbnu.shop.controller;

import com.cbnu.shop.domain.Item;
import com.cbnu.shop.repository.CategoryRepository;
import com.cbnu.shop.repository.ItemRepository;
import com.cbnu.shop.repository.MemberRepository;
import com.cbnu.shop.domain.Member;
import com.cbnu.shop.service.CategoryCrawler;
import com.cbnu.shop.service.NaverApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShopController {
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final NaverApiService apiService;
    private final CategoryCrawler categoryCrawler;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/members")
    public String findAllMember(Model model,
                                @RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "age", required = false) Long age) {
        List<Member> members = memberRepository.findAll(name, age);
        model.addAttribute("members", members);
        return "members";
    }

    @GetMapping("/crawling")
    public void crawling() {
        MultiValueMap<Long, String> categories = categoryCrawler.crawler();
        memberRepository.saveCategories(categories);
    }

    @GetMapping("/itemCrawler")
    public String itemCrawling(Model model) throws JsonProcessingException, InterruptedException {
        List<Item> items = apiService.crawler();
        for (Item item : items) {
            itemRepository.saveItem(item);
        }
        model.addAttribute("items", items);
        return "item";
    }
}
