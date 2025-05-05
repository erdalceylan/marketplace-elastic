package com.marketplace.elastic.controller;

import com.marketplace.elastic.dto.response.SuggestDTO;
import com.marketplace.elastic.manager.SuggestManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suggests")
public class SuggestController {


    private final SuggestManager suggestManager;

    public SuggestController(SuggestManager suggestManager) {
        this.suggestManager = suggestManager;
    }

    @GetMapping("")
    public ResponseEntity<List<SuggestDTO>> aggregate(@RequestParam("q") String q, @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {

        return ResponseEntity.ok(suggestManager.suggest(q, limit));
    }
}
