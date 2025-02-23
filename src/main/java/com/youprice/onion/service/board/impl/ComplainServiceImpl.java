package com.youprice.onion.service.board.impl;

import com.youprice.onion.dto.board.ComplainDTO;
import com.youprice.onion.dto.board.ComplainFormDTO;
import com.youprice.onion.entity.board.Complain;
import com.youprice.onion.entity.chat.Chatroom;
import com.youprice.onion.entity.member.Member;
import com.youprice.onion.entity.product.Product;
import com.youprice.onion.repository.board.ComplainRepository;
import com.youprice.onion.repository.chat.ChatroomRepository;
import com.youprice.onion.repository.member.MemberRepository;
import com.youprice.onion.repository.product.ProductRepository;
import com.youprice.onion.service.board.ComplainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComplainServiceImpl implements ComplainService {

    private final ComplainRepository complainRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ChatroomRepository chatroomRepository;

    @Override
    public ComplainDTO findComplainDTO(Long complainId) {
        return complainRepository.findById(complainId).map(ComplainDTO::new).orElse(null);
    }

    @Override
    @Transactional
    public void saveComplain(ComplainFormDTO form) {
        Member member = memberRepository.findById(form.getMemberId()).orElse(null);
        Product product = null;
        Chatroom chatroom = null;

        if(form.getProductId() == null){
            product = null;
        } else{
            product = productRepository.findById(form.getProductId()).orElse(null);
        }
        if(form.getChatroomId() == null){
            chatroom = null;
        } else {
           chatroom = chatroomRepository.findById(form.getChatroomId()).orElse(null);
        }
        Complain complain = new Complain(member,product,chatroom,form.getComplainType(), form.getComplainContent(),"처리대기");
        complainRepository.save(complain);
    }

    public void modifyStatus(Long complainId, int select){
        Complain complain = complainRepository.findById(complainId).orElse(null);

        if(select==0) {
            complain.updateStatus("처리완료");
        } else{
            complain.updateStatus("접수취소");
        }
    }

    @Override
    public Page<ComplainDTO> findAll(Pageable pageable) {
        return complainRepository.findAll(pageable).map(ComplainDTO::new);
    }
}
