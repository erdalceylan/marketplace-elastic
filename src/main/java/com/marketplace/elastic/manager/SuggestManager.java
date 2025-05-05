package com.marketplace.elastic.manager;

import com.marketplace.elastic.dto.response.SuggestDTO;
import com.marketplace.elastic.mapper.SuggestMapper;
import com.marketplace.elastic.service.SuggestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuggestManager {

    private final SuggestService suggestService;
    private final SuggestMapper suggestMapper;

    public SuggestManager(
            SuggestService suggestService,
            SuggestMapper suggestMapper
    ){
        this.suggestService = suggestService;
        this.suggestMapper = suggestMapper;
    }

    public List<SuggestDTO> suggest(String q, Integer limit){
        return suggestService.suggest(q, limit).stream().map(suggestMapper::convert).toList();
    }
}
