package com.example.shopapp.services;

import com.example.shopapp.components.JwtTokenUntil;
import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserInfoDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.PermissionDenyException;
import com.example.shopapp.models.Role;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.RoleRepository;
import com.example.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUntil jwtTokenUntil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber=userDTO.getPhoneNumber();
        //Kiểm tra số điện thoại tồn tại hay chưa
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw  new DataIntegrityViolationException("số điện thoại đã tồn tại");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found with id = " + userDTO.getRoleId()));
        if(role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("bạn không thể đăng ký tài khoản admin");
        }
        //convert userDTO -> user
        User newUser=User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
//        Role role=roleRepository.findById(userDTO.getRoleId()).orElseThrow(()->new
//                DataNotFoundException("role not found")));

        newUser.setRole(role);

        // kiểm tra nếu có accountId thì ko cần password
        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId()==0){
            String password=userDTO.getPassword();
            String encodedPassword=passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
            // sẽ nói đến trong phần spring security

        }
        return userRepository.save(newUser);
    }

        // đăng nhập
    @Override
    public String login(String phoneNumber, String password) throws Exception {
        //Liên quan đến security => khó
        Optional<User>optionalUser= userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()){
            throw new DataNotFoundException("Invalid phoneNubber / passWord");
        }

        User existingUser=optionalUser.get();
        // check pass
        if(existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId()==0){
            if(!passwordEncoder.matches(password,existingUser.getPassword())){
                throw new BadCredentialsException("wrong phoneNumber or password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                phoneNumber,password,
                existingUser.getAuthorities()
                );
        // authenticate with java swing
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUntil.generateToken(existingUser);// muốn trả về token JWT
    }

    @Override
    public UserInfoDTO getUserInfoByPhoneNumber(String phoneNumber) {
        // Tìm người dùng theo số điện thoại, trả về Optional<User>
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);

        // Kiểm tra nếu người dùng không tồn tại, ném lỗi
        User user = userOptional.orElseThrow(() -> new RuntimeException("User not found"));

        // Trả về thông tin người dùng chỉ với các trường cần thiết
        return new UserInfoDTO(
                user.getFullName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getDateOfBirth()
        );
    }

}
