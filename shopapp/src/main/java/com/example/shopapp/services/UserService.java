package com.example.shopapp.services;

import com.example.shopapp.components.JwtTokenUntil;
import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
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
    public User createUser(UserDTO userDTO)  throws DataNotFoundException{
        String phoneNumber=userDTO.getPhoneNumber();
        //Kiểm tra số điện thoại tồn tại hay chưa
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw  new DataIntegrityViolationException("phone number already exists");
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
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found with id = " + userDTO.getRoleId()));
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
}
