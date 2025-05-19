package com.playus.communityservice.domain.comment.specification;

import com.playus.communityservice.domain.comment.dto.comment_create.CommentCreateRequest;
import com.playus.communityservice.domain.comment.dto.comment_create.CommentCreateResponse;
import com.playus.communityservice.domain.comment.dto.comment_delete.CommentDeleteRequest;
import com.playus.communityservice.domain.comment.dto.comment_delete.CommentDeleteResponse;
import com.playus.communityservice.domain.comment.dto.comment_update.CommentUpdateRequest;
import com.playus.communityservice.domain.comment.dto.comment_update.CommentUpdateResponse;
import com.playus.communityservice.global.config.jwt.JwtUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface CommentControllerSpecification {

    @Tag(name = "Comment", description = "커뮤니티 댓글 작성 API")
    @Operation(
            summary = "커뮤니티 댓글 생성",
            description = "커뮤니티 댓글을 작성합니다.",
            security = @SecurityRequirement(name = "AccessCookie"),
            parameters = @Parameter(
                    name = "Access",
                    description = "JWT access token (쿠키)",
                    in = ParameterIn.COOKIE,
                    required = true,
                    example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            ),
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "커뮤니티 글 생성 요청 예시",
                                    value = """
                                    {
                                      "postId": 1,
                                      "image": "1",
                                      "content": "오늘 경기 재미있었어요"
                                    }
                                    """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "생성 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "커뮤니티 댓글 생성 응답 예시",
                                    value = """
                                            {
                                                "commentId" : 1,
                                                "message" : "댓글 작성이 완료되었습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<CommentCreateResponse> createComment(CommentCreateRequest request, JwtUser user);

    @Tag(name = "Comment", description = "커뮤니티 댓글 삭제 API")
    @Operation(
            summary = "커뮤니티 댓글 삭제",
            description = "작성자가 커뮤니티 댓글을 삭제합니다.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "커뮤니티 댓글 삭제 요청 예시",
                                    value = """
                                            {
                                                "commentId" : 1
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
                                                "success" : true,
                                                "message" : "게시물이 삭제되었습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<CommentDeleteResponse> deleteComment(CommentDeleteRequest request, JwtUser user);

    @Tag(name = "Comment", description = "커뮤니티 댓글 수정 API")
    @Operation(
            summary = "커뮤니티 댓글 수정",
            description = "작성자가 게시글을 수정합니다.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "댓글/답글 수정 요청 예시",
                                    value = """
                                            {
                                                "commentId" : 1,
                                                "content" : "아슬아슬하게 이겼네요!"
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
                                                "success" : true,
                                                "message" : "댓글이 수정되었습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<CommentUpdateResponse> updateComment(CommentUpdateRequest request, JwtUser user);
}
