package com.youprice.onion.service.board;

import com.youprice.onion.dto.board.InquiryDTO;
import com.youprice.onion.dto.board.InquiryFormDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InquiryService {
    InquiryDTO findInquiryDTO(Long inquiryId);
    Long saveInquiry(InquiryFormDTO inquiryFormDTO);
    void updateInquiry(Long inquiryId, InquiryFormDTO form);
    void deleteInquiry(InquiryDTO inquiryDTO);

    Page<InquiryDTO> findAll(Pageable pageable);
    Page<InquiryDTO> getSearchList(String field, String word, Pageable pageable);
}
