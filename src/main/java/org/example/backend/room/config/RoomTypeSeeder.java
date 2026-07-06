package org.example.backend.room.config;

import lombok.RequiredArgsConstructor;
import org.example.backend.room.entity.Room;
import org.example.backend.room.entity.RoomType;
import org.example.backend.room.enums.RoomStatus;
import org.example.backend.room.repository.IRoomRepository;
import org.example.backend.room.repository.IRoomTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RoomTypeSeeder implements CommandLineRunner {

    private final IRoomTypeRepository roomTypeRepository;
    private final IRoomRepository roomRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roomTypeRepository.count() == 0) {
            RoomType juniorSuite = RoomType.builder()
                    .name("Phòng Suite Nhỏ")
                    .pricePerNight(1200000.0)
                    .maxGuests(2)
                    .size("38 m²")
                    .image("/images/junior_suite.png")
                    .description("Không gian vườn tược xanh mát lý tưởng cho các cặp đôi tìm kiếm sự yên bình giữa lòng phố thị.")
                    .amenities("Wifi, Tivi, Điều hòa, Minibar, Bồn tắm")
                    .build();

            RoomType deluxeSuite = RoomType.builder()
                    .name("Phòng Suite Cao Cấp")
                    .pricePerNight(1800000.0)
                    .maxGuests(2)
                    .size("45 m²")
                    .image("/images/deluxe_suite.png")
                    .description("Không gian tinh tế, tối giản với ban công lớn ngắm toàn cảnh Hà Nội rực rỡ sắc màu về đêm.")
                    .amenities("Wifi, Tivi, Điều hòa, Minibar, Bồn tắm, Ban công")
                    .build();

            RoomType executivePremier = RoomType.builder()
                    .name("Phòng Suite Hoàng Gia")
                    .pricePerNight(2500000.0)
                    .maxGuests(3)
                    .size("60 m²")
                    .image("/images/executive_suite.png")
                    .description("Phân khúc cao cấp được trang bị phòng khách riêng biệt và nội thất cẩm thạch sang trọng tinh xảo.")
                    .amenities("Wifi, Tivi, Điều hòa, Minibar, Bồn tắm, Ban công, Phòng khách riêng")
                    .build();

            RoomType familyStudio = RoomType.builder()
                    .name("Phòng Căn Hộ Gia Đình")
                    .pricePerNight(3000000.0)
                    .maxGuests(4)
                    .size("70 m²")
                    .image("/images/family_studio.png")
                    .description("Căn hộ ấm cúng đầy đủ tiện ích bếp ăn cho những chuyến đi dài ngày của cả gia đình.")
                    .amenities("Wifi, Tivi, Điều hòa, Minibar, Bồn tắm, Bếp ăn riêng, Máy giặt")
                    .build();

            RoomType aravelleGrandSuite = RoomType.builder()
                    .name("Phòng Thượng Hạng Aravelle")
                    .pricePerNight(4200000.0)
                    .maxGuests(4)
                    .size("95 m²")
                    .image("/images/grand_suite.png")
                    .description("Kiệt tác nghỉ dưỡng với view panorama vô cực và các tiện ích xa hoa bậc nhất dành cho khách VIP.")
                    .amenities("Wifi, Tivi, Điều hòa, Minibar, Bồn tắm, Ban công panorama, Quầy bar mini")
                    .build();

            RoomType presidentialSuite = RoomType.builder()
                    .name("Phòng Tổng Thống")
                    .pricePerNight(8500000.0)
                    .maxGuests(6)
                    .size("150 m²")
                    .image("/images/presidential_suite.png")
                    .description("Trải nghiệm đỉnh cao của quyền quý với quản gia riêng, thang máy bảo mật và phòng họp thượng đỉnh.")
                    .amenities("Wifi, Tivi, Điều hòa, Minibar, Bồn tắm, Quản gia riêng, Thang máy bảo mật, Phòng họp")
                    .build();

            roomTypeRepository.saveAll(Arrays.asList(
                    juniorSuite, deluxeSuite, executivePremier, familyStudio, aravelleGrandSuite, presidentialSuite
            ));
            System.out.println("Room Types Seeded successfully!");
        }

        if (roomRepository.count() == 0) {
            // Fetch RoomTypes from DB to make sure they have IDs
            RoomType juniorSuite = roomTypeRepository.findAll().stream().filter(t -> t.getName().contains("Nhỏ")).findFirst().orElse(null);
            RoomType deluxeSuite = roomTypeRepository.findAll().stream().filter(t -> t.getName().contains("Cao Cấp")).findFirst().orElse(null);
            RoomType executivePremier = roomTypeRepository.findAll().stream().filter(t -> t.getName().contains("Hoàng Gia")).findFirst().orElse(null);
            RoomType familyStudio = roomTypeRepository.findAll().stream().filter(t -> t.getName().contains("Gia Đình")).findFirst().orElse(null);
            RoomType aravelleGrandSuite = roomTypeRepository.findAll().stream().filter(t -> t.getName().contains("Thượng Hạng")).findFirst().orElse(null);
            RoomType presidentialSuite = roomTypeRepository.findAll().stream().filter(t -> t.getName().contains("Tổng Thống")).findFirst().orElse(null);

            if (juniorSuite != null) {
                roomRepository.save(Room.builder().roomNumber("101").roomType(juniorSuite).view("Hướng Vườn").status(RoomStatus.AVAILABLE).build());
                roomRepository.save(Room.builder().roomNumber("102").roomType(juniorSuite).view("Hướng Thành Phố").status(RoomStatus.AVAILABLE).build());
            }
            if (deluxeSuite != null) {
                roomRepository.save(Room.builder().roomNumber("201").roomType(deluxeSuite).view("Hướng Thành Phố").status(RoomStatus.AVAILABLE).build());
                roomRepository.save(Room.builder().roomNumber("202").roomType(deluxeSuite).view("Hướng Hồ Tây").status(RoomStatus.AVAILABLE).build());
            }
            if (executivePremier != null) {
                roomRepository.save(Room.builder().roomNumber("301").roomType(executivePremier).view("Hướng Tầng Cao").status(RoomStatus.AVAILABLE).build());
                roomRepository.save(Room.builder().roomNumber("302").roomType(executivePremier).view("Hướng Hồ Tây").status(RoomStatus.AVAILABLE).build());
            }
            if (familyStudio != null) {
                roomRepository.save(Room.builder().roomNumber("401").roomType(familyStudio).view("Hướng Thành Phố").status(RoomStatus.AVAILABLE).build());
                roomRepository.save(Room.builder().roomNumber("402").roomType(familyStudio).view("Hướng Vườn").status(RoomStatus.AVAILABLE).build());
            }
            if (aravelleGrandSuite != null) {
                roomRepository.save(Room.builder().roomNumber("501").roomType(aravelleGrandSuite).view("Hướng Toàn Cảnh").status(RoomStatus.AVAILABLE).build());
                roomRepository.save(Room.builder().roomNumber("502").roomType(aravelleGrandSuite).view("Hướng Hồ Tây").status(RoomStatus.AVAILABLE).build());
            }
            if (presidentialSuite != null) {
                roomRepository.save(Room.builder().roomNumber("601").roomType(presidentialSuite).view("Hướng Hồ Tây").status(RoomStatus.AVAILABLE).build());
                roomRepository.save(Room.builder().roomNumber("602").roomType(presidentialSuite).view("Hướng Toàn Cảnh").status(RoomStatus.AVAILABLE).build());
            }
            System.out.println("Rooms Seeded successfully!");
        }
    }
}
