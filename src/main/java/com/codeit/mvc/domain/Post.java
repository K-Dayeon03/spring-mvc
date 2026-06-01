package com.codeit.mvc.domain;

import lombok.*;

import java.time.LocalDateTime;

// Entity: DB와 Java 객체를 연결해 주는 가장 핵심적 개념
// DB 테이블과 1:1로 매칭되는 필드를 선언
@Getter
//@Setter <- Entity에서 Setter는 권장하지 않습니다. 필요한 필드에만 선택적으로 생성합니다.
@AllArgsConstructor
@ToString
public class Post {

    @Setter
    private Long id;
    private String title;
    private String content;
    private String author;
    private Category category;
    private int viewCount;
    private String thumbnailPath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Post() {
        this.viewCount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // class단에 @Builder를 붙이면 모든 필드를 초기화 할 수 있는 Builder 패턴 완성
    // 생성자에 @Builder를 붙이면 생성자로 전달되는 매개값만 초기화하는 Builder 패턴 완성 -> 이 방식을 선호.
    @Builder
    public Post(String title, String content, String author, Category category, String thumbnailPath) {
        this();
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
        this.thumbnailPath = thumbnailPath;
    }

    // Lombok의 setter는 완전 기본형, 커스텀을 원한다면 직접 setter를 구축하세요.
    public void setViewCount() {
        this.viewCount++;
    }


}
