package com.youprice.onion.dto.product;

import com.youprice.onion.entity.product.Category;
import com.youprice.onion.entity.product.ProductProgress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateDTO {

    private String townName; //동네이름
    private Long townId; //동네번호
    private Long categoryId; //카테고리번호

    private String subject; //제목
    private String content; //내용
    private int price; //상품가격
    private String representativeImage; //대표이미지
    private LocalDateTime updateDate; //수정일
    private Boolean payStatus; //페이현황

    private LocalDateTime auctionDeadline; //경매기한
    private Boolean auctionStatus; //경매현황

    private List<MultipartFile> productImageName;//이미지 이름

}
