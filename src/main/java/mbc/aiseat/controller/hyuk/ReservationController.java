//package mbc.aiseat.controller.hyuk;
//
//import lombok.RequiredArgsConstructor;
//import mbc.aiseat.dto.hyuk.ReservationRequest;
//import mbc.aiseat.entity.hyuk.SeatReservation;
//import mbc.aiseat.repository.hyuk.SeatReservationRepository;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//@RestController
//@RequestMapping("/api/hyuk")
//@RequiredArgsConstructor
//public class ReservationController {
//
//    private final SeatReservationRepository repository;
//
//    @PostMapping("/reserve")
//    public String reserve(@RequestBody ReservationRequest request) {
//        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//            SeatReservation reservation = SeatReservation.builder()
//                    .seatNumber(request.getSeatNumber())
//                    .userId(request.getUserId())
//                    .reservedTime(LocalDateTime.parse(request.getReservedTime(), formatter))
//                    .expireTime(LocalDateTime.parse(request.getExpireTime(), formatter))
//                    .status("reserved")
//                    .build();
//
//            repository.save(reservation);
//            return "예약 완료";
//        } catch (Exception e) {
//            return "예약 실패: " + e.getMessage();
//        }
//    }
//}
