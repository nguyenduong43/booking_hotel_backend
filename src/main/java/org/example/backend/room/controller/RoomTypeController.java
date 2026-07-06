package org.example.backend.room.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.room.entity.RoomType;
import org.example.backend.room.repository.IRoomTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/room-types")
public class RoomTypeController {

    private final IRoomTypeRepository roomTypeRepository;

    @GetMapping
    public ResponseEntity<List<RoomType>> getAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        return ResponseEntity.ok(roomTypes);
    }
}
