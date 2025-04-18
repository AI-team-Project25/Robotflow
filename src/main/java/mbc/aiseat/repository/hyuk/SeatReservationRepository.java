package mbc.aiseat.repository.hyuk;

import mbc.aiseat.entity.hyuk.SeatReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {
}
