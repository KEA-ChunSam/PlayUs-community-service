package com.playus.communityservice.domain.post.specification;

import com.playus.communityservice.domain.post.dto.diary_view.DiaryGetResponse;
import com.playus.communityservice.domain.post.dto.diary_view.DiaryListResponse;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateRequest;
import com.playus.communityservice.domain.post.dto.post_create.PostCreateResponse;
import com.playus.communityservice.domain.post.dto.post_delete.PostDeleteResponse;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateRequest;
import com.playus.communityservice.domain.post.dto.post_update.PostUpdateResponse;
import com.playus.communityservice.domain.post.enums.TeamTag;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface LiveMatchDiaryControllerSpecification {

    @Tag(name = "Post", description = "직관일지 작성 API")
    @Operation(
            summary = "직관일지 생성",
            description = "직관일지를 작성합니다.",
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
                                    name = "직관일지 생성 요청 예시",
                                    value = """
                                    {
                                      "title": "LG가 이긴 날",
                                      "image": "post.jpg",
                                      "content": "연장까지 가서 정말 숨죽이고 지켜봤던 날이었다.",
                                      "twpDate" : "2025-05-05",
                                      "isSecret": true
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
                                    name = "직관일지 생성 응답 예시",
                                    value = """
                                    {
                                      "postId" : 1,
                                      "message" : "직관일지 생성이 완료되었습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관일지의 제목이 비어있을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 400,
                                                "statue" : "BAD_REQUEST"
                                                "message" : "직관일지의 제목이 비어있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관일지의 내용이 비어있을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 400,
                                                "statue" : "BAD_REQUEST"
                                                "message" : "직관일지의 내용을 1~500자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),
    })
    ResponseEntity<PostCreateResponse> createPost(@PathVariable("tag") TeamTag tag,
                                                  @Valid @Parameter(description = "직관일지 생성 요청", required = true)
                                                  PostCreateRequest request, JwtUser user);

    @Tag(name = "Post", description = "직관일지 삭제 API")
    @Operation(
            summary = "직관일지 삭제",
            description = "작성자가 직관일지를 삭제합니다.",
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT access token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                    name = "postId",
                    in = ParameterIn.PATH,
                    required = true,
                    description = "삭제할 직관일지 ID",
                    example = "1"
            )}
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
                                        "message" : "직관일지가 삭제되었습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 직관일지를 찾을 수 없을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 404,
                                                "statue" : "NOT_FOUND"
                                                "message" : "해당 직관일지를 찾을 수 없습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<PostDeleteResponse> deletePost(@PathVariable("postId") Long postId, JwtUser user);

    @Tag(name = "Post", description = "직관일지 수정 API")
    @Operation(
            summary = "직관일지 수정",
            description = "작성자가 직관일지를 수정합니다.",
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT access token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                    name = "postId",
                    in = ParameterIn.PATH,
                    required = true,
                    description = "수정할 직관일지 ID",
                    example = "1"
            )},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "직관일지 수정 요청 예시",
                                    value = """
                                    {
                                      "postId": 1,
                                      "title": "비 오는 날에 만끽하는 승리",
                                      "image": "post.jpg",
                                      "content": "우취될까봐 조마조마 했지만, 역시 KIA의 승!",
                                      "twpDate" : "2025-05-01",
                                      "isSecret": true
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
                                        "message" : "직관일지가 수정되었습니다."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "해당 직관일지를 찾을 수 없을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 404,
                                                "statue" : "NOT_FOUND"
                                                "message" : "해당 직관일지를 찾을 수 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관일지의 제목이 비어있을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 400,
                                                "statue" : "BAD_REQUEST"
                                                "message" : "직관일지의 제목이 비어있습니다!"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "직관일지의 내용이 비어있을 때",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code" : 400,
                                                "statue" : "BAD_REQUEST"
                                                "message" : "직관일지의 내용을 1~500자 이내로 작성해 주세요!"
                                            }
                                            """
                            )
                    )
            ),
    })
    ResponseEntity<PostUpdateResponse> updatePost(@PathVariable("tag") TeamTag tag,
                                                  @PathVariable("postId") Long postId,
                                                  @Valid @Parameter(description = "직관일지 생성 요청", required = true)
                                                  PostUpdateRequest request, JwtUser user);

    @Tag(name = "Get", description = "직관일지 조회")
    @Operation(
            summary = "직관일지 조회 API",
            description = "특정 직관일지에 대한 자세한 정보를 조회합니다.",
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
                            description = "직관일지 ID",
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
                                    name = "직관일지 조회 응답 예시",
                                    value = """
                                            {
                                            	"team":"KIA",
                                             	"title":"아쉽게 패배한 날",
                                             	"date":"2025-03-20",
                                             	"image":"LG_vs_KIA.png",
                                             	"content":"마지막에 1점 차이로 역전을 당한 날..."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "postID가 1 미만일 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "postID는 1 이상이여야 합니다!"
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
                    responseCode = "404", description = "postID로 직관일지를 찾을 수 없는 경우 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 404,
                                              "status": "NOT_FOUND",
                                              "message": "직관일지가 존재하지 않습니다!"
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
    ResponseEntity<DiaryGetResponse> getMyDiary(@PathVariable("tag") TeamTag tag,
                                                @PathVariable("postId") Long postId,
                                                JwtUser user);

    @Tag(name = "Get", description = "나의 직관일지 목록 조회 API")
    @Operation(
            summary = "나의 직관일지 목록 조회",
            description = "나의 직관일지 목록을 조회합니다.",
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
                                                "title":"비 오는 날에 만끽하는 승리",
                                                "thumbnail":"post.jpg",
                                                "date":"2025-05-01",
                                                "team":"KIA"
                                            },
                                            {
                                                "title":"마지막 역전으로 이긴 날",
                                                "thumbnail":"example.jpg",
                                                "date":"2025-03-20",
                                                "team":"LG"
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
    ResponseEntity<List<DiaryListResponse>> getMyDiaries(JwtUser user,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size);
}


