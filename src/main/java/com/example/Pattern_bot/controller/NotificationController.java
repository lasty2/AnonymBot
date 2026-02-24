package com.example.Pattern_bot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.Pattern_bot.service.otherService.NotificationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;


    //    @Operation(
//            summary = "Отправка уведомления всем пользователям",
//            description = "Можно отправить текстовое сообщение и/или фотографию",
//            requestBody = @RequestBody(
//                    content = @Content(
//                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
//                            schema = @Schema(type = "object",
//                                    example = "{\"text\": \"Сообщение\", \"file\": \"photo.jpg\"}")
//                    )
//            ),
//            responses = {
//                    @ApiResponse(responseCode = "204", description = "Сообщения успешно отправлены")
//            }
//    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> sendNotification(
            @Parameter(description = "Текст сообщения", required = false)
            @RequestParam(value = "text", required = false) String text,
            @Parameter(description = "Файл изображения", required = false, content = @Content(schema = @Schema(type = "string", format = "binary")))
            @RequestPart(value = "photo", required = false) MultipartFile file
    ) {
        log.info("text: {}, file {}", text ,file);
        notificationService.notificationAll(text, file);
        return ResponseEntity.noContent().build();
    }
}