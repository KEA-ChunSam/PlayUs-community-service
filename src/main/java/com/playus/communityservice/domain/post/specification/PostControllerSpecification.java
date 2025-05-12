package com.playus.communityservice.domain.post.specification;

import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface PostControllerSpecification {

    @Tag(name = "Post", description = "게시글 생성 API")
    @Operation(
            summary = "게시글 생성",
            description = "게시글을 작성합니다.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "게시글 생성 요청 예시",
                                    value = """
                                    {
                                      "title": "오늘 경기 재밌었어요!",
                                      "image": "1",
                                      "content": "LG가 이길 줄 알았죠!"
                                    }
                                    """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "생성 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "게시글 생성 응답 예시",
                                    value = """
                                    {
                                      "ok"
                                    }
                                    """
                            )
                    )
            )
    })
    ResponseEntity<String> createPost(PostCreateRequest request);
    @Tag(name = "Post", description = "게시글 삭제 API")
    @Operation(
            summary = "게시글 삭제",
            description = "작성자가 게시글을 삭제합니다.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "게시글 삭제 요청 예시",
                                    value = """
                                    {
                                      "postId": 1
                                    }
                                    """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "삭제 응답 예시",
                                    value = "{}"
                            )
                    )
            )
    })
    ResponseEntity<String> deletePost(PostDeleteRequest request);
}
