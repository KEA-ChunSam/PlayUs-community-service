package com.playus.communityservice.domain.post.specification;

import com.playus.communityservice.domain.post.dto.PostGetResponse;
import com.playus.communityservice.domain.post.dto.PostListResponse;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateResponse;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteRequest;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteResponse;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateRequest;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateResponse;
import com.playus.communityservice.domain.post.dto.presigned.PresignedUrlForSaveImageRequest;
import com.playus.communityservice.domain.post.dto.presigned.PresignedUrlForSaveImageResponse;
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
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
                                      "twpDate" : "2025-05-05"
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
    ResponseEntity<PostCreateResponse> createPost(@PathVariable("tag") TeamTag tag,
                                                  @Valid @Parameter(description = "게시글 생성 요청", required = true)
                                                  PostCreateRequest request, JwtUser user);

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
    ResponseEntity<PostDeleteResponse> deletePost(@Valid @Parameter(description = "게시글 삭제 요청", required = true)
                                                  PostDeleteRequest request, JwtUser user);

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
                                      "twpDate" : "2025-05-01"
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
    ResponseEntity<PostUpdateResponse> updatePost(@PathVariable("tag") TeamTag tag,
                                                  @Valid @Parameter(description = "게시글 생성 요청", required = true)
                                                  PostUpdateRequest request, JwtUser user);

    @Tag(name = "Post", description = "Presigned URL 발급 API")
    @Operation(
            summary = "Presigned URL 발급",
            description = "프론트에서 이미지를 직접 저장하기 위한 Presigned URL을 생성 및 반환합니다. (버킷에 저장할 때는 PUT으로)",
            security = @SecurityRequirement(name = "Access"),
            parameters = @Parameter(
                    name = "Access",
                    description = "JWT Access Token (쿠키)",
                    in = ParameterIn.COOKIE,
                    required = true,
                    example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            ),
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Presigned URL 발급 요청 예시",
                                    value = """
                                            {
                                              "imageFileName": "party_thumbnail.jpg"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "발급 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Presigned URL 발급 응답 예시",
                                    value = """
                                            {
                                              "presignedUrl": "http://presigned-url.com"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "이미지 파일명이 비어있을 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "이미지 파일명은 필수입니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    PresignedUrlForSaveImageResponse generatePresignedUrlForSaveImage(@Valid @Parameter(description = "Presigned URL 발급 요청", required = true)
                                                                      PresignedUrlForSaveImageRequest request);

    @Tag(name = "Get", description = "커뮤니티 글 조회")
    @Operation(
            summary = "커뮤니티 글 조회 API",
            description = "특정 게시물에 대한 자세한 정보를 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "postId",
                            in = ParameterIn.PATH,
                            description = "게시물 ID",
                            required = true,
                            example = "1"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "커뮤니티 글 조회 응답 예시",
                                    value = """
                                            {
                                            	"title":"오늘의 승리는 LG",
                                            	"date":"2025-03-20T00:00:00",
                                            	"writerNickname":"안타날릴",
                                            	"writerProfileImage":["profile.png"],
                                            	"activated":true,
                                            	"image":["image.png"],
                                            	"content":"오늘은 LG가 이겨서 너무 행복합니다.",
                                            	"comments":[
                                            		{
                                            		"writerNickname":"난리나리라",
                                            		"writerProfileImage":["profiles.png"],
                                            		"activated":true,
                                            		"content":"홈런터질때 정말 짜릿했어요",
                                            		"reComments":[
                                            			{
                                            			"writerNickname":"테스형",
                                            			"writerProfileImage":["profile_11.png"],
                                            			"activated":true,
                                            			"content":"인정입니다."
                                            			}
                                            		]
                                            		}
                                            	]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "게시물 ID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "게시물 ID는 1 이상이여야 합니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "게시물 ID로 게시물을 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "게시물이 존재하지 않습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<PostGetResponse> getPost(@PathVariable("postId") Long postId,
                                            JwtUser user);

    @Tag(name = "Get", description = "커뮤니티 글 목록 조회 API")
    @Operation(
            summary = "팀별 커뮤니티 글 목록 조회",
            description = "특정 구단 이름을 기준으로 게시글 목록을 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "teamName",
                            description = "구단 이름",
                            in = ParameterIn.PATH,
                            required = true,
                            example = "LG"
                    ),
                    @Parameter(
                            name = "page",
                            description = "페이지 번호 (0부터 시작)",
                            in = ParameterIn.QUERY,
                            required = false,
                            example = "0"
                    ),
                    @Parameter(
                            name = "size",
                            description = "페이지 크기 (기본 10개)",
                            in = ParameterIn.QUERY,
                            required = false,
                            example = "10"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "커뮤니티 글 목록 예시",
                                    value = """
                                        [
                                            {
                                                "postId": 1,
                                                "title": "오늘 경기 어땠나요?",
                                                "writerNickname": "LG팬",
                                                "createdDate": "2025-05-20",
                                                "image": ["LG.png"]
                                            },
                                            {
                                                "postId": 2,
                                                "title": "직관팟 모집",
                                                "writerNickname": "야구사랑",
                                                "createdDate": "2025-05-19",
                                                "image": ["boll.jpg"]
                                            }
                                        ]
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Enum 외 값 전달 시",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                        {
                                            "code": 400,
                                            "status": "BAD_REQUEST",
                                            "message": "존재하지 않는 구단입니다."
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 401,
                                              "status": "UNAUTHORIZED",
                                              "message": "유효하지 않은 토큰입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 내부 오류",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 500,
                                              "status": "INTERNAL_SERVER_ERROR",
                                              "message": "서버 내부 오류가 발생했습니다. 관리자에게 문의해 주세요."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<List<PostListResponse>> getPostsByTeam(@PathVariable("teamName") TeamTag teamName, JwtUser user,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size);
}


