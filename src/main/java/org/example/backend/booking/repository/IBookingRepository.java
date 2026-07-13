package org.example.backend.booking.repository;

import org.example.backend.booking.entity.Booking;
import org.example.backend.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IBookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT r FROM Room r WHERE r.roomType.id = :roomTypeId " +
           "AND r.id NOT IN (SELECT b.room.id FROM Booking b WHERE b.status <> org.example.backend.booking.enums.BookingStatus.CANCELLED " +
           "AND b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate)")
    List<Room> findAvailableRooms(@Param("roomTypeId") Long roomTypeId,
                                  @Param("checkInDate") LocalDate checkInDate,
                                  @Param("checkOutDate") LocalDate checkOutDate);

    @Query("SELECT b.room.id, MAX(b.checkOutDate) FROM Booking b WHERE b.room.id IN :roomIds " +
           "AND b.status = org.example.backend.booking.enums.BookingStatus.CHECKED_OUT " +
           "AND b.checkOutDate <= :checkInDate GROUP BY b.room.id")
    List<Object[]> findLastCheckOutDatesForRooms(@Param("roomIds") List<Long> roomIds,
                                                 @Param("checkInDate") LocalDate checkInDate);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId ORDER BY b.createdAt DESC")
    List<Booking> findByUserId(@Param("userId") Long userId);

    List<Booking> findTop5ByOrderByCreatedAtDesc();
}
