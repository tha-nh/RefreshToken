package com.example.refreshtoken;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SampleDataController {

    private final JwtUtil jwtUtil;

    public SampleDataController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private static final String[] NAMES = {
            "Document", "Project", "Report", "Invoice", "Customer",
            "Task", "File", "Contract", "Product", "Employee"
    };

    private static final String[] STATUS = {"ACTIVE", "INACTIVE", "PENDING"};

    private static final String[] CONTENT_SAMPLES = {
            "This item contains detailed operational information includingsssss workflow steps, revision logs, related attachments, and internal notes. It is generated as part of the system dataset to illustrate how long text fields behave in real application environments.",
            "The description provides context for the generated object, offering extensive metadata, processing comments, and simulated user-generated content. This helps developers validate pagination, UI rendering, and backend transformation logic.",
            "Here you can find a long-form example text intended to test data handling, formatting, and serialization. This includes simulated descriptions that reflect real-world business objects with multiple attributes and dynamic content.",
            "This sample content demonstrates how larger text bodies are handled within REST responses. It includes realistic wording, mixed formatting patterns, and structured sentences intended to resemble enterprise system documentation.",
            "This is a generated placeholder text that mimics paragraph-style business content. It is useful for validating API payload performance, UI wrapping logic, and database behavior under longer string conditions."
    };

    Random random = new Random();

    @GetMapping("/data")
    public Map<String, Object> getData(@RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing Bearer token");
        }

        String token = authHeader.substring(7);

        try {
            String user = jwtUtil.validate(token);

            List<Map<String, Object>> list = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {

                String itemName = NAMES[random.nextInt(NAMES.length)] + " #" + i;

                String content = CONTENT_SAMPLES[random.nextInt(CONTENT_SAMPLES.length)];

                Map<String, Object> item = Map.of(
                        "id", i,
                        "name", itemName,
                        "status", STATUS[random.nextInt(STATUS.length)],
                        "price", 10 + random.nextInt(990),
                        "createdAt", LocalDateTime.now().minusDays(random.nextInt(30)).toString(),
                        "content", content,
                        "createdBy", user
                );

                list.add(item);
            }

            return Map.of("total", list.size(), "data", list);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
    }
}
