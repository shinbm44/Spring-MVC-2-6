package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    // 이 컨트롤러에서 IllegalArgumentException의 예외가 터지면,
    // @ExceptionHandler이 붙은 예외 메소드가 잡는다.
    // 그리고 해당 메소드의 로직이 호출된다.
    // 여기서 @RestController이기에 그대로 JSON 형태로 반환된다.

    // 그런데 200 응답인 정상적인 흐름으로 바뀐다. 여기서 만약 200이 아닌 예외상황으로 처리하고 싶다면
    // @ResponseStatus를 붙여주면 된다.
    // 이 경우 장점은 컨트롤러에서 발생한 에러가 서블릿 컨테이너까지 안올라간다.
    // 즉 ExceptionResolver에서 처리하고(해결) 끝

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    // @ExceptionHandler만 사용하려면 예외 처리 메서드의 매개변수로 예외 처리를 담당하는 클래스가 설정되면 ok
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    // Exception은 예외처리 상속에서 거의 최상위이고 위의 자식 계층이 처리 못하면
    // 부모계층으로 올라가면서 자식 계층의 에러가 처리되지 않는 것은 Exception이 잡고 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부오류");
    }


    @GetMapping("/api2/members/{id}")
    public MemberDto getMember (@PathVariable("id") String id) {

        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }

        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }





    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }

}
