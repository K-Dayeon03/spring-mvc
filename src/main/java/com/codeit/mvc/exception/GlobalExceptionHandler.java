package com.codeit.mvc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
* 전역 예외 처리 - @ControllerAdvice
* 모든 컨트롤러에서 던져진 예외를 한 곳에서 가로채 상황에 맞는 여러 페이지로 응답한다.
*  */
@ControllerAdvice
@Slf4j //로그
public class GlobalExceptionHandler {
    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(PostNotFoundException e,  Model model){
        log.warn("자원 없음:{}", e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(IllegalArgumentException.class) //클라이언트의 응답이 이상한 경우
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(IllegalArgumentException e, Model model){
        log.warn("잘못된 요청:{}", e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "error/400";
    }

    @ExceptionHandler(FileStorageException.class) //클라이언트의 응답이 이상한 경우
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleBadRequest(FileStorageException e, Model model){
        log.warn("서버 오류(파일 입출력):{}", e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "error/500";
    }

    //그 밖에 예상 못한 모든 에외
    @ExceptionHandler(Exception.class) //클라이언트의 응답이 이상한 경우
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleBadRequest(Exception e, Model model){
        log.error("서버 오류:{}", e.getMessage());
        model.addAttribute("message","서버에서 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
        return "error/500";
    }
}
