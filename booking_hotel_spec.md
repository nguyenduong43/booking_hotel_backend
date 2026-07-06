# Booking Hotel Web – Đặt phòng và sử dụng dịch vụ khách sạn

---

## 1. Bối cảnh và lý do xây dựng
Hiện nay, quy trình quản lý và đặt phòng tại nhiều khách sạn, homestay quy mô vừa và nhỏ vẫn còn thủ công và gặp nhiều bất cập:
*   **Về phía khách hàng:** Người thuê thường phải liên hệ hỏi phòng qua điện thoại hoặc tin nhắn trên các mạng xã hội (Zalo, Messenger, Fanpage). Thông tin về giá cả, hình ảnh phòng và tiện ích bị phân tán, khiến khách hàng mất nhiều thời gian chờ đợi phản hồi và khó có cái nhìn tổng quan để so sánh.
*   **Về phía quản lý:** Chủ khách sạn hoặc nhân viên lễ tân phải theo dõi lịch trống bằng sổ sách truyền thống hoặc các file Excel rời rạc. Việc quản lý thủ công này rất dễ dẫn đến sai sót, nhầm lẫn trạng thái phòng, gây ra tình trạng "overbooking" (đặt trùng ngày) và khó khăn trong việc thống kê doanh thu.
*   **Thiếu hụt trải nghiệm thông minh:** Khách hàng chưa có công cụ để chủ động tìm kiếm phòng theo các tiêu chí cá nhân hóa (khoảng giá, view, số lượng người) một cách nhanh chóng. Các câu hỏi lặp đi lặp lại về chính sách (giờ check-in, hủy phòng) làm tốn nhiều nguồn lực của bộ phận chăm sóc khách hàng.

Dự án Hệ thống Đặt phòng Khách sạn (Hotel Booking Web) ra đời nhằm giải quyết triệt để các vấn đề trên bằng cách cung cấp một nền tảng web thống nhất. Tại đây, khách hàng có thể chủ động tìm kiếm, xem hình ảnh trực quan và đặt phòng 24/7; đồng thời cung cấp cho quản trị viên một hệ thống quản lý tập trung, tự động hóa cập nhật trạng thái đơn hàng và được hỗ trợ bởi AI để tối ưu hóa trải nghiệm người dùng.

---

## 2. Mục tiêu dự án

### 2.1. Mục tiêu tổng quát
Xây dựng một hệ thống website đặt phòng khách sạn trực tuyến, liền mạch và thông minh; giúp khách hàng dễ dàng tìm kiếm, đặt phòng dựa trên nhu cầu cá nhân hóa, đồng thời hỗ trợ tối đa cho quản trị viên trong việc số hóa quy trình quản lý vận hành, tối ưu hóa doanh thu và nâng cao trải nghiệm chăm sóc khách hàng nhờ công nghệ AI.

### 2.2. Mục tiêu cụ thể
*   **Đối với Khách hàng (User):**
    *   Cho phép đăng ký, đăng nhập và quản lý thông tin tài khoản cá nhân bảo mật (Sử dụng JWT/Spring Security).
    *   Hỗ trợ tìm kiếm, lọc phòng theo thời gian thực (ngày check-in/out, số lượng khách, khoảng giá, loại phòng).
    *   Xem chi tiết thông tin phòng (hình ảnh, giá, mô tả, các tiện ích đi kèm).
    *   Thực hiện luồng đặt phòng cốt lõi (Booking Core): Chọn phòng, kiểm tra lịch trống (tránh overbooking), tính tổng tiền và tạo đơn đặt phòng với trạng thái "Chờ xác nhận" (Hình thức thanh toán khi nhận phòng để tối ưu thời gian code).
    *   Xem lại lịch sử đặt phòng và trạng thái các đơn hàng đã đặt.
*   **Đối với Quản trị viên (Admin):**
    *   Quản lý danh mục phòng (CRUD): Thêm, sửa, xóa, cập nhật trạng thái phòng (Trống, Đã đặt, Đang dọn dẹp/Bảo trì).
    *   Quản lý đơn đặt phòng: Tiếp nhận, duyệt đơn, hủy đơn hoặc cập nhật trạng thái khi khách trả phòng (Check-out).
    *   Dashboard thống kê cơ bản: Xem báo cáo tổng quan về số lượng đơn đặt phòng thành công và doanh thu theo tháng dưới dạng biểu đồ trực quan.
*   **Tích hợp AI (Lựa chọn 1 trong các tính năng khả thi):**
    *   Tích hợp thành công AI Chatbot (Trợ lý ảo) hỗ trợ giải đáp tự động 24/7 các câu hỏi thường gặp của khách hàng về chính sách khách sạn bằng ngôn ngữ tự nhiên (Sử dụng LLM API như Gemini/OpenAI).

### 2.3. Mục tiêu mở rộng
*   **Nâng cao tính năng AI:**
    *   Phát triển tính năng Smart Search (Tìm kiếm thông minh): Cho phép khách hàng tìm phòng bằng cách nhập một câu yêu cầu tự do (VD: "Tìm phòng view biển cho gia đình 4 người giá tầm 1.5 triệu"), AI tự bóc tách dữ liệu để truy vấn SQL.
    *   Tích hợp AI phân tích dữ liệu và tự động Tóm tắt đánh giá (Review Summarization) của các khách hàng trước để hiển thị ưu/nhược điểm ngắn gọn cho từng phòng.
*   **Mở rộng nghiệp vụ hệ thống:**
    *   Tích hợp cổng thanh toán trực tuyến (VNPay, Momo, hoặc Stripe) để tự động hóa quy trình đặt cọc và thanh toán.
    *   Phát triển mô hình quản lý chuỗi nhiều khách sạn/homestay ở các chi nhánh/địa điểm khác nhau.
    *   Xây dựng hệ thống tự động gửi email/SMS thông báo (Xác nhận đặt phòng thành công, nhắc nhở giờ check-in, cảm ơn sau khi check-out).
    *   Áp dụng thuật toán "Dynamic Pricing" (Giá động) tự động điều chỉnh giá phòng tăng/giảm theo mùa cao điểm hoặc ngày lễ.

---

## 3. Phạm vi dự án

### 3.1. Trong phạm vi
*   **Quy mô mô hình:** Chỉ hỗ trợ quản lý đặt phòng cho một khách sạn hoặc một cơ sở homestay duy nhất (Single-tenant).
*   **Nghiệp vụ đặt phòng:** Hỗ trợ luồng cơ bản (chọn phòng, chọn ngày Check-in/Check-out, nhập số lượng khách) và kiểm tra tính khả dụng để tránh đặt trùng ngày (overbooking).
*   **Thanh toán:** Chỉ hỗ trợ phương thức "Thanh toán khi nhận phòng" (Pay at hotel) để tập trung thời gian phát triển luồng logic cốt lõi.
*   **Vai trò người dùng:** Hệ thống có 2 phân quyền chính là Khách hàng (User) và Quản lý khách sạn (Admin). Chưa có vai trò Super Admin quản lý toàn hệ thống chuỗi.
*   **Quản lý trạng thái đơn:** Admin duyệt đơn, hủy đơn và cập nhật trạng thái phòng hoàn toàn thủ công thông qua trang quản trị.
*   **Tích hợp AI:** Triển khai một tính năng AI duy nhất để tối ưu thời gian (Ví dụ: AI Chatbot giải đáp thông tin quy định khách sạn bằng văn bản).

### 3.2. Ngoài phạm vi
*   **Sản phẩm mở rộng:** Các dịch vụ du lịch đi kèm ngoài lưu trú (Đặt vé máy bay, tour du lịch, thuê xe, spa, nhà hàng).
*   **Thanh toán trực tuyến:** Tích hợp các cổng thanh toán điện tử (VNPay, MoMo, Stripe, thẻ tín dụng).
*   **Mở rộng quy mô:** Quản lý chuỗi nhiều khách sạn/homestay ở các địa điểm khác nhau (Multi-tenant).
*   **Đa nền tảng:** Ứng dụng di động (Native Mobile App cho iOS/Android).
*   **Thông báo phức tạp:** Tích hợp gửi thông báo qua Email, SMS OTP, Zalo ZNS (Khách hàng sẽ theo dõi trạng thái đơn trực tiếp trên website).
*   **AI phức tạp:** Các tính năng yêu cầu huấn luyện mô hình sâu (Deep Learning) hoặc tốn kém tài nguyên như AI dự đoán giá phòng (Dynamic Pricing), AI nhận diện khuôn mặt lúc check-in.

---

## 4. Đối tượng người dùng và nhu cầu

### 4.1. Quản lý / Lễ tân khách sạn (Admin)
*   **Nhân khẩu:** Chủ khách sạn, quản lý hoặc nhân viên lễ tân, từ 22-50 tuổi, sử dụng thành thạo máy tính và các trình duyệt web cơ bản.
*   **Nhu cầu chính:**
    *   Thêm, sửa, xóa và cập nhật thông tin phòng (hình ảnh, giá cả, trạng thái bảo trì) một cách nhanh chóng.
    *   Xem toàn bộ danh sách đơn đặt phòng tập trung tại một nơi duy nhất.
    *   Phê duyệt, hủy hoặc cập nhật trạng thái đơn đặt phòng (Chờ xác nhận, Đã nhận phòng, Đã trả phòng) dễ dàng.
    *   Theo dõi thống kê cơ bản về số lượng đơn và doanh thu theo thời gian thực.
*   **Nỗi đau hiện tại:** Phải tổng hợp tin nhắn đặt phòng từ nhiều nguồn (Zalo, Facebook, gọi điện); kiểm tra lịch trống bằng sổ sách hoặc file Excel thủ công dễ dẫn đến sai sót và đặt trùng phòng (overbooking); tốn quá nhiều thời gian để trả lời các câu hỏi lặp đi lặp lại của khách hàng về chính sách khách sạn.

### 4.2. Khách hàng (User)
*   **Nhân khẩu:** Khách du lịch, người đi công tác, từ 18-55 tuổi, có thói quen sử dụng điện thoại thông minh, máy tính và chuộng việc tự tra cứu dịch vụ trực tuyến.
*   **Nhu cầu chính:**
    *   Tìm kiếm phòng trống linh hoạt theo ngày check-in/check-out, số lượng người và khoảng giá.
    *   Xem thông tin chi tiết, trực quan về phòng (hình ảnh, tiện ích, quy định) trước khi quyết định.
    *   Đặt phòng nhanh chóng với vài thao tác cơ bản và biết ngay kết quả phòng có khả dụng hay không.
    *   Tương tác với AI Chatbot để được giải đáp thắc mắc ngay lập tức 24/7 (VD: hỏi về giờ nhận phòng, có cho mang thú cưng không, có bãi đỗ xe không) thay vì phải gọi điện chờ đợi.
    *   Xem lại lịch sử các đơn đặt phòng của cá nhân và trạng thái hiện tại của đơn.

---

## 5. Luồng nghiệp vụ chính

### 5.1. Luồng xác thực và thiết lập dữ liệu phòng (Admin)
1.  Admin (Quản trị viên) đăng nhập vào hệ thống quản trị bằng tài khoản được cấp sẵn.
2.  Truy cập module Quản lý phòng, nhấn "Thêm phòng mới".
3.  Nhập các thông tin cơ bản: Tên phòng (VD: P101), Loại phòng (Standard/Deluxe), Giá mỗi đêm, Mô tả, Tiện ích và tải lên hình ảnh.
4.  Đặt trạng thái ban đầu của phòng là "Đang trống".
5.  Khách hàng (User) truy cập website, thực hiện đăng ký tài khoản (qua email/mật khẩu) và đăng nhập để bắt đầu sử dụng dịch vụ.

### 5.2. Luồng tìm kiếm và hỗ trợ tự động (Khách hàng)
1.  Khách hàng vào trang chủ, nhập các tiêu chí vào thanh tìm kiếm: Ngày Check-in, Ngày Check-out, Số lượng khách.
2.  Hệ thống truy vấn cơ sở dữ liệu, loại trừ những phòng đã có người đặt trong khoảng thời gian đó và trả về danh sách các phòng khả dụng.
3.  *(Tích hợp AI)* Trong quá trình tìm hiểu, khách hàng có thể mở khung chat AI Chatbot ở góc màn hình để hỏi bằng ngôn ngữ tự nhiên (VD: "Tôi check-in lúc 12h đêm được không?" hoặc "Khách sạn có bãi đỗ xe ô tô không?"). AI sẽ trả lời ngay lập tức dựa trên dữ liệu quy định của khách sạn.

### 5.3. Luồng đặt phòng (Khách hàng)
1.  Khách hàng click vào một phòng ưng ý từ danh sách tìm kiếm để xem chi tiết.
2.  Xem thông tin, kiểm tra lại tổng tiền (Hệ thống tự động tính: Giá phòng x Số đêm).
3.  Nhấn "Đặt phòng", kiểm tra lại thông tin cá nhân (Tên, Số điện thoại - tự động điền nếu đã đăng nhập).
4.  Xác nhận phương thức "Thanh toán khi nhận phòng".
5.  Nhấn "Xác nhận đặt". Hệ thống ghi nhận đơn đặt phòng vào cơ sở dữ liệu với trạng thái "Chờ xác nhận" và lưu lại thời gian tạo đơn. Khách hàng xem lại đơn trong mục Lịch sử đặt phòng.

### 5.4. Luồng xử lý và quản lý đơn đặt phòng (Admin)
1.  Admin mở trang quản trị, truy cập module Quản lý đơn đặt phòng.
2.  Nhìn thấy danh sách các đơn hàng, phân loại theo trạng thái (Mới nhất, Chờ xác nhận, Đã xác nhận, Đã hủy).
3.  Click vào một đơn hàng mới, xem chi tiết thông tin: Khách hàng nào đặt, đặt phòng nào, thời gian lưu trú.
4.  Thao tác xử lý: Nhấn "Xác nhận" để duyệt đơn (lúc này hệ thống đánh dấu phòng đó đã được đặt trong khoảng thời gian tương ứng) hoặc "Hủy đơn" nếu xảy ra sự cố.
5.  Khi khách đến lưu trú và rời đi, Admin tiếp tục cập nhật trạng thái đơn thành "Đã nhận phòng" và cuối cùng là "Đã trả phòng" để giải phóng phòng trống cho các lượt đặt sau. Khách hàng theo dõi được tiến trình này trên tài khoản của mình.
