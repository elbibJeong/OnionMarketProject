package com.youprice.onion.service.order.impl;

import com.youprice.onion.dto.order.WishListDTO;
import com.youprice.onion.entity.member.Member;
import com.youprice.onion.entity.order.Wish;
import com.youprice.onion.entity.product.Product;
import com.youprice.onion.repository.chat.ChatroomRepository;
import com.youprice.onion.repository.member.MemberRepository;
import com.youprice.onion.repository.order.WishRepository;
import com.youprice.onion.repository.product.ProductRepository;
import com.youprice.onion.service.order.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	private final ChatroomRepository chatroomRepository;

	// 찜 리스트 조회
	@Override
	public Page<WishListDTO> getWishList(Long memberId, Pageable pageable) {
		return wishRepository.findAllByMemberId(memberId, pageable).map(wish -> {
			int chatroomListSize = chatroomRepository.countByProductId(wish.getId());
			int wishListSize = wishRepository.countByProductId(wish.getId());
			return new WishListDTO(wish, chatroomListSize, wishListSize);
		});
	}

	// 찜 등록
	@Override
	public void addWish(Long memberId, Long productId) {

		Member member = memberRepository.findById(memberId).orElse(null);
		Product product = productRepository.findById(productId).orElse(null);

		wishRepository.save(new Wish(member, product));
	}

	// 찜 삭제
	@Override
	public void removeWish(Long wishId) {
		wishRepository.deleteById(wishId);
	}

}
