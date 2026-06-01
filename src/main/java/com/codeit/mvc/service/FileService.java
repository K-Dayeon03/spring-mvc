package com.codeit.mvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            // 이미지
            ".jpg", ".jpeg", ".png", ".gif", ".webp", ".bmp", ".svg",
            // 문서
            ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
            // 텍스트
            ".txt", ".md", ".csv", ".json"
    );

    private final Path uploadPath;

    public FileService(@Value("${blog.file-directory}") String uploadDir) {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(uploadPath);
            log.info("업로드 디렉토리 준비 완료: {}", uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("업로드 디렉토리 생성 실패: " + uploadPath, e);
        }
    }

    public String saveFile(MultipartFile file) {
        // cleanPath - null 체크 및 정규화(상대경로 등을 정리)
        String originFileName = StringUtils.cleanPath(
                file.getOriginalFilename() == null ? "unknown" : file.getOriginalFilename()
        );

        // 확장자만 추출하기 (우리가 허용하는 확장자인지를 검사하기 위해)
        int dotIndex = originFileName.lastIndexOf(".");
        String extension = (dotIndex >= 0) ? originFileName.substring(dotIndex).toLowerCase()
                : "";

        // 허용 확장자 검증
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: "+ extension);
        }

        // 랜덤 문자열을 UUID를 이용해서 생성 후 확장자를 붙여서 저장할 파일명 새롭게 생성
        String savedFileName = UUID.randomUUID().toString().replace("-", "") + extension;
        // 위에 준비한 uploadPath와 새로 생성한 파일명을 이어 붙인 Path 객체를 생성
        Path targetPath = uploadPath.resolve(savedFileName).normalize();

        try {
            // 지정한 경로로 실제 파일을 저장하는 핵심 메서드 (위에 작성한 건 유효성 검증일 뿐)
            file.transferTo(targetPath);
            log.info("파일 저장 완료: {} (원본: {}. 크기: {} bytes", savedFileName, originFileName, file.getSize());

            return savedFileName; // 파일 저장이 완료되면 새로운 파일명을 리턴 -> DB 저장
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + originFileName, e);
        }

    }

    public ResponseEntity<Resource> getImage(String fileName) {
        Path requested = uploadPath.resolve(fileName).normalize();

        if (!Files.exists(requested) || Files.isDirectory(requested)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Resource resource = new UrlResource(requested.toUri());
            // 파일의 Content-type이 무엇인지를 정확하게 알려줘야 img 태그가 제대로 화면에 렌더링을 할 수 있음.
            // "image/jpg", "text/html", "image/png", "application/json"
            String contentType = Files.probeContentType(requested);

            // 위에서 판단한 Content-Type이 무엇인지 확인이 되지 않을 때는
            // MediaType.APPLICATION_OCTET_STREAM: 가공되지 않은 순수한 이진 데이터(Binary Data) 스트림으로 취급
            // MediaType.parseMediaType(contentType): content Type 문자열을 Spring이 이해하고 다룰 수 있는
            // MediaType 객체로 변환해 주겠다.
            MediaType mediaType = (contentType == null)
                    ? MediaType.APPLICATION_OCTET_STREAM
                    : MediaType.parseMediaType(contentType);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);

        } catch (IOException e) {
            log.error("파일 응답 실패: fileName={}", fileName, e);
            return ResponseEntity.internalServerError().build();
        }

    }
}

