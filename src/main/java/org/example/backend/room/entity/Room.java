package org.example.backend.room.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.room.enums.RoomStatus;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true, length = 50)
    private String roomNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Column(name = "room_view", length = 100)
    private String view;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RoomStatus status;
}
