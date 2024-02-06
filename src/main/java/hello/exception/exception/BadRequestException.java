package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 런타임 예외라면 원래는 500번 에러이다. HttpStatus.BAD_REQUEST는 400번 에러
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException{


}
