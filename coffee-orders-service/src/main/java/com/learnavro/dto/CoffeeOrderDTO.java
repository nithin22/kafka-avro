package com.learnavro.dto;

import com.learnavro.domain.generated.PickUp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoffeeOrderDTO {
    private String id;
    private String name;
    private String nickName;
    private StoreDto store;
    private List<OrderLineItemDTO>orderLineItems;
    private PickUp pickUp;
    private String status;
    private LocalDateTime orderedTime;



}
