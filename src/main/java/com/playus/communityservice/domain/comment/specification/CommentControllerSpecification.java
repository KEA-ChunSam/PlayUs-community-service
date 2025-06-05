package com.playus.communityservice.domain.comment.specification;

import com.playus.communityservice.domain.comment.dto.comment_create.CommentCreateRequest;
import com.playus.communityservice.domain.comment.dto.comment_create.CommentCreateResponse;
import com.playus.communityservice.domain.comment.dto.comment_delete.CommentDeleteRequest;
import com.playus.communityservice.domain.comment.dto.comment_delete.CommentDeleteResponse;
import com.playus.communityservice.domain.comment.dto.comment_update.CommentUpdateRequest;
import com.playus.communityservice.domain.comment.dto.comment_update.CommentUpdateResponse;
import com.playus.communityservice.domain.common.security.JwtUser;
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
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

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
                                    name = "커뮤니티 댓글 생성 요청 예시",
                                    value = """
                                    {
                                      "postId": 1,
                                      "commentGroupId" : 2,
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
                                                "commentId" : 3,
                                                "commentGroupId" : 2,
                                                "message" : "댓글 작성이 완료되었습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "댓글의 내용이 비어있을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 400,
                                                "statue" : "BAD_REQUEST",
                                                "message" : "댓글의 내용을 1~100자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),
    })
    ResponseEntity<CommentCreateResponse> createComment(@Valid CommentCreateRequest request, JwtUser user);

    @Tag(name = "Comment", description = "커뮤니티 댓글 삭제 API")
    @Operation(
            summary = "커뮤니티 댓글 삭제",
            description = "작성자가 커뮤니티 댓글을 삭제합니다.",
            parameters = {
                    @Parameter(
                    name = "Access",
                    description = "JWT access token (쿠키)",
                    in = ParameterIn.COOKIE,
                    required = true,
                    example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            ),
                    @Parameter(
                    name = "commentId",
                    in = ParameterIn.PATH,
                    required = true,
                    description = "삭제할 댓글 ID",
                    example = "1"
            )},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "커뮤니티 댓글 삭제 요청 예시",
                                    value = """
                                            {
                                                "commentId":3,
                                                "commentGroupId" : 2
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
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 댓글을 찾을 수 없을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 404,
                                                "statue" : "NOT_FOUND",
                                                "message" : "해당 댓글을 찾을 수 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "해당 댓글의 작성자가 아닐 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 403,
                                                "statue" : "FORBIDDEN",
                                                "message" : "해당 댓글에 대한 권한이 없습니다."
                                            }
                                            """
                            )
                    )
            ),
    })
    ResponseEntity<CommentDeleteResponse> deleteComment(@Valid CommentDeleteRequest request,
                                                        @PathVariable("commentId") Long commentId,
                                                        JwtUser user);

    @Tag(name = "Comment", description = "커뮤니티 댓글 수정 API")
    @Operation(
            summary = "커뮤니티 댓글 수정",
            description = "작성자가 게시글을 수정합니다.",
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT access token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "commentId",
                            in = ParameterIn.PATH,
                            required = true,
                            description = "수정할 댓글 ID",
                            example = "1"
                    )},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "댓글/답글 수정 요청 예시",
                                    value = """
                                            {
                                                "commentId":3,
                                                "commentGroupId" : 2,
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
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 댓글을 찾을 수 없을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 404,
                                                "statue" : "NOT_FOUND",
                                                "message" : "해당 댓글을 찾을 수 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "해당 댓글의 작성자가 아닐 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 403,
                                                "statue" : "FORBIDDEN",
                                                "message" : "해당 댓글에 대한 권한이 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "댓글의 내용이 비어있을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 400,
                                                "statue" : "BAD_REQUEST",
                                                "message" : "댓글의 내용을 1~100자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),
    })
    ResponseEntity<CommentUpdateResponse> updateComment(@Valid CommentUpdateRequest request,
                                                        @PathVariable("commentId") Long commentId,
                                                        JwtUser user);
}
