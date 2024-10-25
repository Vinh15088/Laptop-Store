package com.LaptopWeb.controller;

import com.LaptopWeb.dto.request.IntrospectTokenRequest;
import com.LaptopWeb.dto.request.LoginRequest;
import com.LaptopWeb.dto.request.LogoutRequest;
import com.LaptopWeb.dto.request.RefreshTokenRequest;
import com.LaptopWeb.dto.response.ApiResponse;
import com.LaptopWeb.dto.response.AuthenticationResponse;
import com.LaptopWeb.entity.User;
import com.LaptopWeb.service.AuthenticationService;
import com.LaptopWeb.service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;


    @PostMapping("/login/local")
    public ApiResponse<?> login(@RequestBody LoginRequest loginRequest) {
        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>();


        AuthenticationResponse response = authenticationService.authenticationResponse(loginRequest);

        apiResponse.setSuccess(true);
        apiResponse.setContent(response);

        return apiResponse;
    }

    @GetMapping("/login/google")
    public ResponseEntity<?> loginGoogleAuth(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/v1/oauth2/authorization/google");

        return ResponseEntity.ok("Redirecting...");
    }

    @GetMapping("/login/facebook")
    public ResponseEntity<?> loginFacebookAuth(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/v1/oauth2/authorization/facebook");

        return ResponseEntity.ok("Redirecting...");
    }

    @GetMapping("/loginSuccess")
    public ResponseEntity<?> handleOAuth2LoginSuccess(OAuth2AuthenticationToken oAuth2AuthenticationToken) throws Exception {
        User user = userService.createUserByOAuth2(oAuth2AuthenticationToken);  // Tách logic theo provider bên trong service

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:3000/home"))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<?> introspect(@RequestBody IntrospectTokenRequest request) throws ParseException, JOSEException {
        var response = authenticationService.introspectTokenResponse(request);

        return ApiResponse.builder()
                .success(true)
                .content(response)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);

        return ApiResponse.builder()
                .success(true)
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<?> refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        var response = authenticationService.refreshToken(request);

        return ApiResponse.builder()
                .success(true)
                .content(response)
                .build();
    }

}
