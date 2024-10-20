package com.LaptopWeb.service;

import com.LaptopWeb.dto.request.CreateUserRequest;
import com.LaptopWeb.dto.request.UpdateUserRequest;
import com.LaptopWeb.entity.Role;
import com.LaptopWeb.entity.User;
import com.LaptopWeb.exception.AppException;
import com.LaptopWeb.exception.ErrorApp;
import com.LaptopWeb.mapper.UserMapper;
import com.LaptopWeb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableMethodSecurity
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    public User createUser(CreateUserRequest request) throws Exception{
        Role role = roleService.getRole("USER");

        User user = userMapper.toUser(request);

        if(userRepository.existsByUsername(user.getUsername())) throw new AppException(ErrorApp.USERNAME_EXISTED);
        if(userRepository.existsByEmail(user.getEmail())) throw new AppException(ErrorApp.EMAIL_EXISTED);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(role);

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.claims['data']['id']")
    public User getById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorApp.USER_NOTFOUND));

        return  user;
    }

    public User getByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorApp.USER_NOTFOUND));

        return user;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<User> getPageUser(Integer pageNum, Integer size, String sortField, String keyWord) {
        Sort sort = sortField != null ? Sort.by(sortField).ascending() : Sort.unsorted();
        Pageable pageable = PageRequest.of(pageNum, size, sort);

        if(keyWord == null) {
            return userRepository.findAll(pageable);
        } else {
            return userRepository.findAllUser(keyWord, pageable);
        }

    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.claims['data']['id']")
    public User updateUser(Integer id, UpdateUserRequest request) {
        User user = getById(id);

        User user1 = userMapper.toUserUpdate(request);

        user1.setId(user.getId());
        user1.setUsername(user.getUsername());
        user1.setPassword(user.getPassword());
        user1.setRole(user.getRole());

        return userRepository.save(user1);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.claims['data']['id']")
    public User changePassword(Integer id, String password) {
        User user = getById(id);

        String encodePassword = passwordEncoder.encode(password);

        user.setPassword(encodePassword);

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Integer id) {
        User user = getById(id);

        userRepository.deleteById(id);
    }
}
