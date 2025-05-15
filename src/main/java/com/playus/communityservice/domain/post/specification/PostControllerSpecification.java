package com.playus.communityservice.domain.post.specification;

import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateResponse;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteRequest;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteResponse;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateRequest;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Map;

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
                                      "jwpDate" : "2025-05-05"
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
                                      "postId" : 1,
                                      "message" : "게시물 생성이 완료되었습니다."
                                    }
                                    """
                            )
                    )
            )
    })
    ResponseEntity<PostCreateResponse> createPost(PostCreateRequest request);

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
                                    value = """
                                    {
                                        "success" : "true",
                                        "message" : "게시물이 삭제되었습니다."
                                    }
                                    """
                            )
                    )
            )
    })
    ResponseEntity<PostDeleteResponse> deletePost(PostDeleteRequest request);

    @Tag(name = "Post", description = "게시글 수정 API")
    @Operation(
            summary = "게시글 수정",
            description = "작성자가 게시글을 수정합니다.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "게시글 수정 요청 예시",
                                    value = """
                                    {
                                      "title": "오늘 경기 재밌었어요!",
                                      "image": "2",
                                      "content": "긴장하면서 시청했네요."
                                      "postId": 1
                                      "jwpDate" : "2025-05-01"
                                    }
                                    """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "수정 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "수정 응답 예시",
                                    value = """
                                    {
                                        "success" : "true",
                                        "message" : "게시물이 수정되었습니다."
                                    }
                                    """
                            )
                    )
            )
    })
    ResponseEntity<PostUpdateResponse> updatePost(PostUpdateRequest request);
}

