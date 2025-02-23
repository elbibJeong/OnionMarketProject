package com.youprice.onion.controller.board;

import com.youprice.onion.dto.board.*;
import com.youprice.onion.dto.member.MemberDTO;
import com.youprice.onion.dto.member.SessionDTO;
import com.youprice.onion.dto.order.OrderDTO;
import com.youprice.onion.dto.product.ProductDTO;
import com.youprice.onion.security.auth.LoginUser;
import com.youprice.onion.service.board.ReviewImageService;
import com.youprice.onion.service.board.ReviewService;
import com.youprice.onion.service.member.MemberService;
import com.youprice.onion.service.order.OrderService;
import com.youprice.onion.service.product.ProductImageService;
import com.youprice.onion.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;
    private final OrderService orderService;
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final MemberService memberService;

    @GetMapping("/created/{orderId}")
    public String createdForm(@PathVariable("orderId") Long orderId, Model model,
                              @LoginUser SessionDTO sessionDTO) {
        OrderDTO orderDTO = orderService.getOrderDTO(orderId);
        if(sessionDTO != null){
            model.addAttribute("sessionDTO", sessionDTO);
        }
        ProductDTO productDTO = productService.getProductDTO(orderDTO.getProductId());
        Long salesId = productDTO.getMemberId();
        MemberDTO salesDTO = reviewService.getSalesUserName(orderDTO);
//      List<ProductImageDTO> productImage = productImageService.getProductImage(productDTO.getProductId());

        model.addAttribute("orderDTO", orderDTO);
        model.addAttribute("salesDTO", salesDTO);
//      model.addAttribute("productImage", productImage.get(0));
        model.addAttribute("productDTO", productDTO);
        return "board/reviewForm";
    }

    @PostMapping("/created/{orderId}")
    public String created(@Valid @ModelAttribute("form") ReviewFormDTO form, BindingResult bindingResult,
                          Model model, @LoginUser SessionDTO sessionDTO, @RequestParam("reviewImageName") List<MultipartFile> reviewImageName) throws IOException {
        MemberDTO salesDTO = memberService.getMemberDTO(form.getSalesId());
        if(bindingResult.hasErrors()){
            model.addAttribute("sessionDTO",sessionDTO);
            model.addAttribute("salesDTO",salesDTO);
            return "board/reviewForm";
        }
        /*int point = reviewService.saveReview(form, reviewImageName);
        return "redirect:/review/complete?point=" + point;*/
        reviewService.saveReview(form, reviewImageName);
        return "redirect:/review/complete";
    }
    @GetMapping("/complete")
    public String complete(){
        //model.addAttribute("point", point);
        return "board/reviewComplete";
    }

    @GetMapping("/list")
    public String reviewLists(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                Model model, @LoginUser SessionDTO sessionDTO) {
        Page<ReviewDTO> reviewList = reviewService.findAll(pageable);
        if(sessionDTO != null){
            model.addAttribute("sessionDTO", sessionDTO);
        }
        int pageNumber = reviewList.getPageable().getPageNumber();
        int totalPages = reviewList.getTotalPages();
        int pageBlock = 5;
        int startBlockPage = ((pageNumber)/pageBlock)*pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        endBlockPage = totalPages < endBlockPage ? totalPages : endBlockPage;

        model.addAttribute("startBlockPage", startBlockPage);
        model.addAttribute("endBlockPage", endBlockPage);
        model.addAttribute("reviewList", reviewList);

        return "board/reviewList";
    }
    // 이미지출력
//    @ResponseBody
//    @GetMapping("/images/{filename}")
//    public Resource image(@PathVariable String filename) throws MalformedURLException {
//        return new UrlResource("file:" + reviewService.filePath()+filename);
//    }
    // 특정회원이 받은 후기 목록
    @GetMapping("/list/{salesId}")
    public String userReviewList(@PathVariable Long salesId, Model model, @LoginUser SessionDTO sessionDTO,
                                 @PageableDefault(size = 7) Pageable pageable){
        Page<ReviewDTO> userReviewList = reviewService.userReviewList(salesId, pageable);

        if(sessionDTO != null){
            model.addAttribute("sessionDTO", sessionDTO);
        }
        int pageNumber = userReviewList.getPageable().getPageNumber();
        int totalPages = userReviewList.getTotalPages();
        int pageBlock = 5;
        int startBlockPage = ((pageNumber)/pageBlock)*pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        endBlockPage = totalPages < endBlockPage ? totalPages : endBlockPage;

        model.addAttribute("startBlockPage", startBlockPage);
        model.addAttribute("endBlockPage", endBlockPage);
        model.addAttribute("userReviewList",userReviewList);

        return "board/userReview";
    }

    // 수정 화면
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model,@LoginUser SessionDTO sessionDTO){
        ReviewDTO reviewDTO = reviewService.findReviewDTO(id);
        List<ReviewImageDTO> imageList = reviewImageService.findByReviewId(id);

        if(sessionDTO != null){
            model.addAttribute("sessionDTO", sessionDTO);
        }
        model.addAttribute("reviewDTO",reviewDTO);
        model.addAttribute("imageList",imageList);
        return "board/reviewUpdate";
    }
    // 첨부사진 개별 삭제
    @GetMapping("/images/delete/{id}/{reviewId}")
    public String imageDelete(@PathVariable("id") Long imageId, Long reviewId){
        reviewImageService.deleteImage(imageId);
        return "redirect:/review/update/{reviewId}";
    }
    // 수정
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @Valid @ModelAttribute("form") ReviewUpdateDTO form,
                                                BindingResult bindingResult, Model model) throws IOException {
        List<ReviewImageDTO> imageList = reviewImageService.findByReviewId(id);
        if(bindingResult.hasErrors()){
            model.addAttribute("imageList",imageList);
            return "board/reviewUpdate";
        }
        reviewService.updateReview(id, form);
        return "redirect:/review/list";
    }

    // 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        ReviewDTO reviewDTO = reviewService.findReviewDTO(id);
        reviewService.deleteReview(reviewDTO);

        return "redirect:/review/list";
    }

}