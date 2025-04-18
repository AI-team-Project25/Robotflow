package mbc.aiseat.controller.kwj;

import lombok.extern.log4j.Log4j2;
import mbc.aiseat.dto.kwj.SeatDto;
import mbc.aiseat.entity.kwj.Seat;
import mbc.aiseat.service.kwj.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/reserve")
    public List<Seat> getReservedSeats() {
        return seatService.getReservedSeats();
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveSeat(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestBody SeatDto seatDto) {
        try {
            String username = userDetails.getUsername();  // 로그인된 사용자 이름
            String seatName = seatDto.getSeatName();
            seatService.reserveSeat(username,seatName);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/reserved-seat/{username}")
    public ResponseEntity<?> getReservedSeatByUser(@PathVariable String username) {
        Seat reservedSeat = seatService.getReservedSeatByUser(username);
        if (reservedSeat != null) {
            // 예약된 좌석이 있을 경우
            return ResponseEntity.ok(reservedSeat); // Seat 객체 반환
        } else {
            // 예약된 좌석이 없을 경우
            return ResponseEntity.ok().body(null); // null 반환
        }
    }

    @PostMapping("/update-reservation")
    public ResponseEntity<Map<String, Object>> updateReservation(@RequestBody SeatDto seatDto) {
        boolean success = seatService.updateReservation(seatDto.getSeatName(), seatDto.getReservedBy());
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("success", true);
            response.put("message", "좌석 변경 성공!");
        } else {
            response.put("success", false);
            response.put("message", "좌석 변경 실패");
        }
        return ResponseEntity.ok(response);
    }

    // 예약 취소
    @PostMapping("/cancel-reservation")
    public ResponseEntity<Map<String, Object>> cancelReservation(@RequestBody SeatDto seatDto) {
        String username = seatDto.getReservedBy();
        boolean success = seatService.cancelReservation(username);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("success", true);
            response.put("message", "예약이 성공적으로 취소되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "예약 취소 실패: 해당 사용자의 예약을 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(response);
    }






}
