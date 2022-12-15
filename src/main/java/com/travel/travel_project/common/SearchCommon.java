package com.travel.travel_project.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.travel.travel_project.common.StringUtil.getInt;
import static com.travel.travel_project.common.StringUtil.getString;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchCommon {
    /**
     * <pre>
     * 1. MethodName : searchCommon
     * 2. ClassName  : SearchCommon.java
     * 3. Comment    : 페이징 및 검색 조건 공통
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 08. 08.
     * </pre>
     */
    public Map<String, Object> searchCommon(Page page, Map<String, Object> paramMap) {
        Map<String, Object> searchMap = new HashMap<>();

        // 페이징 처리
        page.setPageNum(getInt(page.getPageNum(), 1));
        page.setSize(getInt(page.getSize(), 10));

        // 검색 조건
        searchMap.put("searchType", getString(paramMap.get("searchType"), ""));
        searchMap.put("searchKeyword", getString(paramMap.get("searchKeyword"), ""));
        searchMap.put("jpaStartPage", getInt(page.getStartPage(), 0));
        searchMap.put("startPage", getInt(page.getPageNum(), 1));
        searchMap.put("size", getInt(page.getSize(), 10));

        return searchMap;
    }
}