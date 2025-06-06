package com.example.shopapp.controllers;

import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserInfoDTO;
import com.example.shopapp.dtos.UserLoginDTO;
import com.example.shopapp.models.User;
import com.example.shopapp.services.IUserService;
import com.example.shopapp.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Password does not match");
            }
            userService.createUser(userDTO);
            return ResponseEntity.ok("register successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
        //kiểm tra thông tin đăng nhập và sinh token
        try{
            String token = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword());
            return ResponseEntity.ok(token);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    // lấy ra toong tin
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo() {
        try {
            // Lấy thông tin người dùng từ SecurityContext (giả sử token đã được xác thực trong filter)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Lấy đối tượng User từ authentication
            User user = (User) authentication.getPrincipal(); // Cần ép kiểu thành User, không phải String

            // Kiểm tra nếu phoneNumber không có giá trị
            String phoneNumber = user.getPhoneNumber();  // Lấy phone number từ User object

            if (phoneNumber == null || phoneNumber.isEmpty()) {
                return ResponseEntity.badRequest().body("Phone number not found in authentication.");
            }

            // Lấy thông tin người dùng từ service bằng phoneNumber
            UserInfoDTO userInfo = userService.getUserInfoByPhoneNumber(phoneNumber);

            // Trả về thông tin chỉ gồm 4 trường yêu cầu
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving user info: " + e.getMessage());
        }
    }
}
