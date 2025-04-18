package mbc.aiseat.entity.hyuk;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seat_reservation")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;

    private String userId;

    private LocalDateTime reservedTime;

    private LocalDateTime expireTime;
    private String status;
}
