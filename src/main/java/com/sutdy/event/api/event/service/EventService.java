package com.sutdy.event.api.event.service;


import com.sutdy.event.api.event.dto.request.EventSaveDto;
import com.sutdy.event.api.event.dto.response.EventDetailDto;
import com.sutdy.event.api.event.dto.response.EventOneDto;
import com.sutdy.event.api.event.entity.Event;
import com.sutdy.event.api.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventService {

    private final EventRepository eventRepository;

    //전체 조회 서비스
    public Map<String, Object> getEvents(int pageNo, String sort) {
        Pageable pageable = PageRequest.of(pageNo - 1, 4);
        Page<Event> eventPage = eventRepository.findEvents(pageable, sort);
        List<Event> events = eventPage.getContent();
        List<EventDetailDto> eventDetailDtoList = events.stream().map(EventDetailDto::new).collect(Collectors.toList());
        long totalElements = eventPage.getTotalElements(); //총 이벤트 개수
        Map<String, Object> map = new HashMap<>();
        map.put("events", eventDetailDtoList);
        map.put("totalCount", totalElements);
        return map;
    }

    //이벤트 등록
    public void
    saveEvent(EventSaveDto dto) {
        Event savedEvent = eventRepository.save(dto.toEntity());
//        return getEvents("date");
    }

    //이벤트 단일 조회
    public EventOneDto getEventDetail(Long id){
        Event foundEvent = eventRepository.findById(id).orElseThrow();

        return new EventOneDto(foundEvent);
    }

    //이벤트 삭제
    public void deleteEvent (Long id) {
        eventRepository.deleteById(id);
    }

    //이벤트 수정
    public void modifyEvent(Long id , EventSaveDto dto) {
        Event fooundEvent = eventRepository.findById(id).orElseThrow();
        fooundEvent.changeEvent(dto);

        eventRepository.save(fooundEvent);


    }

}
