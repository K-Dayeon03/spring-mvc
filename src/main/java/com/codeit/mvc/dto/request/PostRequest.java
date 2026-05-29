package com.codeit.mvc.dto.request;

import com.codeit.mvc.domain.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

// DTO: Data Transfer Object
// 데이터 전송 및 API 스펙 맞춤용 객체
// Entity는 데이터베이스와 굉장히 밀접한 연관을 가지고 있기 때문에
// 1. DB 스펙이 곧 입력값 스펙이 되는 건 좋지 않다.
// 2. 보안적인 문제가 발생할 수 있음. (응답을 줄 때도 DTO를 사용해서 꼭 필요한 정보만 화면단에 내려야 한다)
// 3. 화면단에서 요구하는 데이터가 Entity보다 더 적을수도 있고, 더 많을수도 있다.
@Setter @Getter
@ToString
public class PostRequest {

    private String title;
    private String content;
    private String author;
    private Category category;

}
