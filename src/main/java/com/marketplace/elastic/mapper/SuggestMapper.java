package com.marketplace.elastic.mapper;

import com.marketplace.elastic.document.Suggest;
import com.marketplace.elastic.dto.response.SuggestDTO;
import org.springframework.stereotype.Service;

@Service
public class SuggestMapper {
    public SuggestDTO convert(Suggest suggest){
        SuggestDTO suggestDTO = new SuggestDTO();
        suggestDTO.setId(suggest.getId());
        suggestDTO.setText(suggest.getText());
        suggestDTO.setType(suggest.getType());
        suggestDTO.setRefId(suggest.getRefId());
        return suggestDTO;
    }
}
