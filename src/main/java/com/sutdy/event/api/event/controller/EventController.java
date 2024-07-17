package com.sutdy.event.api.event.controller;

import com.sutdy.event.api.auth.TokenProvider;
import com.sutdy.event.api.auth.TokenProvider.TokenUserInfo;
import com.sutdy.event.api.event.dto.request.EventSaveDto;
import com.sutdy.event.api.event.dto.response.EventOneDto;
import com.sutdy.event.api.event.repository.EventRepository;
import com.sutdy.event.api.event.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class EventController {

    private final EventService eventService;
    private final EventRepository eventRepository;

    // 전체 조회 요청
    @GetMapping("/page/{pageNo}")
    public ResponseEntity<?> getList(@RequestParam(required = false) String sort
                                     ,@PathVariable int pageNo,
                                     @AuthenticationPrincipal TokenUserInfo tokenInfo) throws InterruptedException
    {

        log.info("token user Id {}", tokenInfo);
        if (sort ==null){
            return ResponseEntity.badRequest().body("sort 파라미터가 없습니다.");
        }
        Map<String, Object> events = eventService.getEvents(pageNo, sort, tokenInfo.getUserId());

//        Thread.sleep(2000);
        return ResponseEntity.ok().body(events);
    }

    // 등록 요청
    @PostMapping
    public ResponseEntity<?> register(@RequestBody EventSaveDto dto,
    @AuthenticationPrincipal TokenUserInfo tokenInfo) { // JwtAuthFilter에서 시큐리티에 등록한 데이터
        eventService.saveEvent(dto, tokenInfo.getUserId());
        return ResponseEntity.ok().body("event Saved!");
    }

    // 단일 조회 요청
    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable Long eventId) {

        if (eventId == null || eventId < 1) {
            String errorMessage = "eventId가 정확하지 않습니다.";
            log.warn(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        EventOneDto eventDetail = eventService.getEventDetail(eventId);

        return ResponseEntity.ok().body(eventDetail);
    }



    //이벤트 삭제
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> delete(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);

        return ResponseEntity.ok().body("event deleted!");
    }


    @PatchMapping("{eventId}")
    public ResponseEntity<?> modify(@RequestBody EventSaveDto dto, @PathVariable Long eventId) {
        eventService.modifyEvent(eventId, dto);
        return ResponseEntity.ok().body("modify event success!");
    }
}
