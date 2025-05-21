package com.playus.communityservice.domain.post.specification;

import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateResponse;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteRequest;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteResponse;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateRequest;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateResponse;
import com.playus.communityservice.domain.post.enums.TeamTag;
import com.playus.communityservice.global.jwt.JwtUser;
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

public interface PostControllerSpecification {

    @Tag(name = "Post", description = "커뮤니티 글 작성  API")
    @Operation(
            summary = "커뮤니티 글 생성",
            description = "커뮤니티 글을 작성합니다.",
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
                                      "title": "오늘 경기 재밌었어요!",
                                      "image": ["post.jpg"],
                                      "content": "LG가 이길 줄 알았죠!",
                                      "jwpDate" : "2025-05-05"
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
                                    name = "커뮤니티 글 생성 응답 예시",
                                    value = """
                                    {
                                      "postId" : 1,
                                      "message" : "게시글 생성이 완료되었습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "게시글의 제목이 비어있을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 400,
                                                "statue" : "BAD_REQUEST"
                                                "message" : "게시글의 제목이 비어있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "게시글의 내용이 비어있을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 400,
                                                "statue" : "BAD_REQUEST"
                                                "message" : "게시글의 내용을 1~500자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),
    })
    ResponseEntity<PostCreateResponse> createPost(TeamTag tag, PostCreateRequest request, JwtUser user);

    @Tag(name = "Post", description = "커뮤니티 글 삭제 API")
    @Operation(
            summary = "커뮤니티 글 삭제",
            description = "작성자가 커뮤니티 글을 삭제합니다.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "커뮤니티 글 삭제 요청 예시",
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
                                        "success" : true,
                                        "message" : "게시물이 삭제되었습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 게시글을 찾을 수 없을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 404,
                                                "statue" : "NOT_FOUND"
                                                "message" : "해당 게시글을 찾을 수 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "해당 게시글의 작성자가 아닐 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 403,
                                                "statue" : "FORBIDDEN"
                                                "message" : "해당 게시글에 대한 권한이 없습니다."
                                            }
                                            """
                            )
                    )
            ),
    })
    ResponseEntity<PostDeleteResponse> deletePost(PostDeleteRequest request, JwtUser user);

    @Tag(name = "Post", description = "커뮤니티 글 수정 API")
    @Operation(
            summary = "커뮤니티 글 수정",
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
                                      "image": ["post.jpg"],
                                      "content": "긴장하면서 시청했네요."
                                      "postId": 1,
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
                                        "success" : true,
                                        "message" : "게시물이 수정되었습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 게시글을 찾을 수 없을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 404,
                                                "statue" : "NOT_FOUND"
                                                "message" : "해당 게시글을 찾을 수 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "해당 게시글의 작성자가 아닐 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 403,
                                                "statue" : "FORBIDDEN"
                                                "message" : "해당 게시글에 대한 권한이 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "게시글의 제목이 비어있을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 400,
                                                "statue" : "BAD_REQUEST"
                                                "message" : "게시글의 제목이 비어있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "게시글의 내용이 비어있을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 400,
                                                "statue" : "BAD_REQUEST"
                                                "message" : "게시글의 내용을 1~500자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),
    })
    ResponseEntity<PostUpdateResponse> updatePost(TeamTag tag, PostUpdateRequest request, JwtUser user);
}

