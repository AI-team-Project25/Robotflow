package mbc.aiseat.controller.hyuk;

import lombok.RequiredArgsConstructor;
import mbc.aiseat.entity.hyuk.SeatUnit;
import mbc.aiseat.repository.hyuk.SeatUnitRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SeatUnitController {

    private final SeatUnitRepository seatUnitRepository;

    // class{num}를 동적으로 처리하기 위한 메서드
    @GetMapping("/class{num}/seats")
    public Map<String, String> getSeatsByClass(@PathVariable("num") String num) {
        Long libraryId = Long.parseLong(num); // class1 -> 1, class2 -> 2 등으로 변환
        List<SeatUnit> seats = seatUnitRepository.findByLibrary_Id(libraryId); // 해당 도서관의 좌석 목록 조회

        System.out.println("좌석들 (Library ID: " + libraryId + "): " + seats);

        // "a1": "used" or "null" (available 값에 따라 상태 설정)
        return seats.stream()
                .collect(Collectors.toMap(
                        SeatUnit::getSeatNumber,
                        seat -> seat.isAvailable() ? "null" : "used" // available 값에 따라 상태 설정
                ));
    }
}
