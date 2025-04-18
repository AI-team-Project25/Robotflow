package mbc.aiseat.dto.hyuk;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    private String seatNumber;
    private String userId;
    private String reservedTime;  // 예: "2025-04-17 13:00:00"
    private String expireTime;
}
