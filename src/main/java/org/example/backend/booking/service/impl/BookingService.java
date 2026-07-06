package org.example.backend.booking.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.backend.booking.dto.BookingRequest;
import org.example.backend.booking.dto.BookingResponse;
import org.example.backend.booking.dto.SingleBookingDto;
import org.example.backend.booking.entity.Booking;
import org.example.backend.booking.enums.BookingStatus;
import org.example.backend.booking.repository.IBookingRepository;
import org.example.backend.booking.service.IBookingService;
import org.example.backend.room.entity.Room;
import org.example.backend.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    private final IBookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request, User user) {
        LocalDate checkIn = request.getCheckInDate();
        LocalDate checkOut = request.getCheckOutDate();

        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new IllegalArgumentException("Ngày trả phòng phải sau ngày nhận phòng!");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày nhận phòng không được ở quá khứ!");
        }

        int quantity = request.getQuantity();

        // 1. Find all available rooms during the target period
        List<Room> availableRooms = bookingRepository.findAvailableRooms(request.getRoomTypeId(), checkIn, checkOut);

        if (availableRooms.size() < quantity) {
            throw new IllegalArgumentException("Không đủ phòng trống thuộc hạng phòng yêu cầu cho khoảng thời gian này! Hiện chỉ còn " 
                    + availableRooms.size() + " phòng.");
        }

        // 2. Query last check-out dates for these rooms to prioritize recently vacated ones
        List<Long> roomIds = availableRooms.stream().map(Room::getId).toList();
        List<Object[]> lastCheckoutResults = bookingRepository.findLastCheckOutDatesForRooms(roomIds, checkIn);
        
        Map<Long, LocalDate> roomLastCheckoutMap = new HashMap<>();
        for (Object[] result : lastCheckoutResults) {
            Long rId = (Long) result[0];
            LocalDate lastCO = (LocalDate) result[1];
            roomLastCheckoutMap.put(rId, lastCO);
        }

        // Sort rooms: recently checked out first, never booked goes last
        availableRooms.sort((r1, r2) -> {
            LocalDate d1 = roomLastCheckoutMap.getOrDefault(r1.getId(), LocalDate.of(1970, 1, 1));
            LocalDate d2 = roomLastCheckoutMap.getOrDefault(r2.getId(), LocalDate.of(1970, 1, 1));
            return d2.compareTo(d1);
        });

        List<Room> selectedRooms = new ArrayList<>();

        if (quantity == 1) {
            selectedRooms.add(availableRooms.get(0));
        } else {
            // Priority: adjacent rooms on the same floor
            List<Room> adjacentList = findAdjacentRooms(availableRooms, quantity);
            if (adjacentList != null) {
                selectedRooms = adjacentList;
            } else {
                // Fallback: take the first N available rooms from the priority-sorted list
                for (int i = 0; i < quantity; i++) {
                    selectedRooms.add(availableRooms.get(i));
                }
            }
        }

        // 3. Create bookings for each selected room
        String bookingGroupCode = "BK-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 900 + 100);
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        List<SingleBookingDto> singleBookings = new ArrayList<>();

        for (Room room : selectedRooms) {
            double totalPrice = room.getRoomType().getPricePerNight() * days;
            double depositAmount = totalPrice * 0.20; // 20% deposit

            Booking booking = Booking.builder()
                    .user(user)
                    .room(room)
                    .bookingGroupCode(bookingGroupCode)
                    .customerName(request.getCustomerName())
                    .customerEmail(request.getCustomerEmail())
                    .customerPhone(request.getCustomerPhone())
                    .checkInDate(checkIn)
                    .checkOutDate(checkOut)
                    .numberOfGuests(request.getNumberOfGuests())
                    .totalPrice(totalPrice)
                    .depositAmount(depositAmount)
                    .status(BookingStatus.CONFIRMED)
                    .build();

            Booking savedBooking = bookingRepository.save(booking);

            singleBookings.add(SingleBookingDto.builder()
                    .id(savedBooking.getId())
                    .roomNumber(room.getRoomNumber())
                    .roomTypeName(room.getRoomType().getName())
                    .checkInDate(checkIn)
                    .checkOutDate(checkOut)
                    .totalPrice(totalPrice)
                    .status(savedBooking.getStatus())
                    .build());
        }

        return BookingResponse.builder()
                .bookingGroupCode(bookingGroupCode)
                .bookings(singleBookings)
                .build();
    }

    private List<Room> findAdjacentRooms(List<Room> rooms, int quantity) {
        // Group rooms by floor
        Map<String, List<Room>> floorMap = new HashMap<>();
        for (Room r : rooms) {
            String floor = getFloorFromRoomNumber(r.getRoomNumber());
            floorMap.computeIfAbsent(floor, k -> new ArrayList<>()).add(r);
        }

        // Sort rooms within each floor by roomNumber (numeric order)
        for (Map.Entry<String, List<Room>> entry : floorMap.entrySet()) {
            entry.getValue().sort((r1, r2) -> compareRoomNumbers(r1.getRoomNumber(), r2.getRoomNumber()));
        }

        List<Room> bestSelection = null;
        int minSpan = Integer.MAX_VALUE;

        for (Map.Entry<String, List<Room>> entry : floorMap.entrySet()) {
            List<Room> floorRooms = entry.getValue();
            if (floorRooms.size() < quantity) {
                continue;
            }

            // Find N rooms with minimal span
            for (int i = 0; i <= floorRooms.size() - quantity; i++) {
                List<Room> candidate = floorRooms.subList(i, i + quantity);
                Integer firstNum = tryParseInt(candidate.get(0).getRoomNumber());
                Integer lastNum = tryParseInt(candidate.get(quantity - 1).getRoomNumber());

                if (firstNum != null && lastNum != null) {
                    int span = lastNum - firstNum;
                    if (span < minSpan) {
                        minSpan = span;
                        bestSelection = new ArrayList<>(candidate);
                    }
                } else {
                    if (bestSelection == null) {
                        bestSelection = new ArrayList<>(candidate);
                        minSpan = 9999;
                    }
                }
            }
        }

        return bestSelection;
    }

    private String getFloorFromRoomNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.isEmpty()) {
            return "unknown";
        }
        if (roomNumber.matches("\\d+")) {
            if (roomNumber.length() > 2) {
                return roomNumber.substring(0, roomNumber.length() - 2);
            }
            return "1";
        }
        return roomNumber.substring(0, 1);
    }

    private int compareRoomNumbers(String rn1, String rn2) {
        Integer n1 = tryParseInt(rn1);
        Integer n2 = tryParseInt(rn2);
        if (n1 != null && n2 != null) {
            return n1.compareTo(n2);
        }
        return rn1.compareTo(rn2);
    }

    private Integer tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
