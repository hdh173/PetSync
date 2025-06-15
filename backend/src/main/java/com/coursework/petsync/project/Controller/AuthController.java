package com.coursework.petsync.project.Controller;

import com.coursework.petsync.project.DTO.Request.LoginRequest;
import com.coursework.petsync.project.DTO.Request.RegisterRequest;
import com.coursework.petsync.project.DTO.Response.LoginResponse;
import com.coursework.petsync.project.DTO.Response.PetsyncResponse;
import com.coursework.petsync.project.DTO.entity.User;
import com.coursework.petsync.project.Service.AuthService;
import com.coursework.petsync.utils.EmailUtil;
import com.coursework.petsync.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HDH
 * @version 1.0
 */
/**
 * 用户认证模块接口
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "用户认证模块", description = "提供用户注册与登录等接口")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/send-code")
    @Operation(
            summary = "发送邮箱验证码",
            description = "向指定邮箱发送注册验证码",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "验证码发送成功",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PetsyncResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "验证码发送失败",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PetsyncResponse.class)
                            )
                    )
            }
    )
    public PetsyncResponse<?> sendVerificationCode(@RequestParam String email) {
        boolean result = emailUtil.sendEmailVerificationCode(email);
        if (result) {
            return PetsyncResponse.success("验证码已发送");
        } else {
            return PetsyncResponse.status(400, "验证码发送失败，请检查邮箱格式或稍后再试", null);
        }
    }

    @PostMapping("/register")
    @Operation(
            summary = "用户注册",
            description = "注册新用户，需提供邮箱验证码",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "注册成功，返回新用户信息",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "注册失败，验证码错误或数据不合法",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PetsyncResponse.class)
                            )
                    )
            }
    )
    public PetsyncResponse<?> register(@RequestBody RegisterRequest request) {
        User user = new User(request.getEmail(), request.getUsername(), request.getPassword());
        user = authService.registerUser(user, request.getCode());
        if (user != null) {
            return PetsyncResponse.status(201, "用户成功创建", user);
        } else {
            return PetsyncResponse.status(400, "验证码错误或密码不符合要求", null);
        }
    }

    @PostMapping("/login")
    @Operation(
            summary = "用户登录",
            description = "用户登录，需邮箱/用户名 + 密码",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登录成功，返回用户信息与 Token",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "用户名或密码错误",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PetsyncResponse.class)
                            )
                    )
            }
    )
    public PetsyncResponse<?> login(@RequestBody LoginRequest loginRequest) {
        User user = authService.login(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
        if (user != null) {
            String accessToken = jwtUtil.generateToken(user.getId(), user.getRole());
            String refreshToken = jwtUtil.generateRefreshToken(user.getId());
            LoginResponse loginResponse = new LoginResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getRole(),
                    accessToken,
                    jwtUtil.getExpirationTime(),
                    refreshToken,
                    user.getAvatarUrl()
            );
            return PetsyncResponse.success(loginResponse);
        } else {
            return PetsyncResponse.status(400, "用户名或密码错误", null);
        }
    }
}