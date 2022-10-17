package com.youprice.onion.dto.product;

import com.youprice.onion.entity.product.Product;
import com.youprice.onion.entity.product.ProductProgress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductSellListDTO {

    private Long productId;
    private String subject; //제목
    private String content; //내용
    private int price; //상품가격
    private LocalDateTime date; //등록시간
    private ProductProgress productProgress; //판매상태 Reserved,tradings,soldout 예약중,거래중,판매완료
    private String payStatus; //페이현황
    private String blindStatus; //블라인드현황

	private String townName;
	private String categoryName;

    public ProductSellListDTO(Product product) {

		// product
        productId = product.getId();
        subject = product.getSubject();
        content = product.getContent();
		date = product.getUpdateDate() != null ? product.getUpdateDate() : product.getUploadDate();
        price = product.getPrice();
        productProgress = product.getProductProgress();
        payStatus = product.getPayStatus();
        blindStatus = product.getBlindStatus();

		townName = product.getTown().getCoordinate().getTownName();
//		categoryName = product.getProductCategoryList().getCategoryName();
    }
}
