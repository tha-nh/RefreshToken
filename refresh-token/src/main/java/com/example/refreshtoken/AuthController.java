package com.example.refreshtoken;
import com.example.refreshtoken.JwtUtil;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtil jwt;
    public AuthController(JwtUtil j){this.jwt=j;}

    static Map<String,String> refreshStore = new HashMap<>();

    @PostMapping("/login")
    public Map<String,String> login(){
        String token = jwt.generateToken("user", 3*60*1000);
        String refresh = UUID.randomUUID().toString();
        refreshStore.put(refresh, "user");
        return Map.of("token",token,"refreshToken",refresh);
    }

    @PostMapping("/refresh")
    public Map<String,String> refresh(@RequestParam String refreshToken){
        if(!refreshStore.containsKey(refreshToken))
            return Map.of("error","invalid refresh token");

        String user = refreshStore.get(refreshToken);
        String newToken = jwt.generateToken(user, 3*60*1000);
        return Map.of("token",newToken);
    }
}
