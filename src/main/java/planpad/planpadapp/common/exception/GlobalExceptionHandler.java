package planpad.planpadapp.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import planpad.planpadapp.dto.api.OnlyMessageResponseDto;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<OnlyMessageResponseDto> handleException(Exception e) {
        log.error("Unhandled Exception: ", e);
        return ResponseEntity.badRequest()
                .body(new OnlyMessageResponseDto("서버 오류가 발생했습니다."));
    }
}
