package com.youprice.onion.repository.order;

import com.youprice.onion.dto.order.OrderDTO;
import com.youprice.onion.entity.order.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	// orderNum 중복확인
    Optional<Order> findByOrderNum(String orderNum);

	@Override
	@EntityGraph(attributePaths = "delivery")
	Optional<Order> findById(Long ID);

	// buyList
	@EntityGraph(attributePaths = "product")
	List<OrderDTO> findAllByMemberId(Long memberId);

	// sellList
	@EntityGraph(attributePaths = "product")
	List<OrderDTO> findAllByProductMemberId(Long memberId);

}
