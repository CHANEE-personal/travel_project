package com.travel.travel_project.api.post;

import com.travel.travel_project.common.Page;
import com.travel.travel_project.common.SearchCommon;
import com.travel.travel_project.domain.post.PostDTO;
import com.travel.travel_project.domain.post.PostEntity;
import com.travel.travel_project.exception.TravelException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.List;
import java.util.Map;

import static com.travel.travel_project.exception.ApiExceptionType.NOT_FOUND_POST;

@RestController
@RequestMapping("/api/post")
@Api(tags = "게시글 관련 API")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findPostList
     * 2. ClassName  : PostController.java
     * 3. Comment    : 게시글 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @ApiOperation(value = "게시글 리스트 조회", notes = "게시글 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "게시글 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public List<PostDTO> findPostList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        return this.postService.findPostList(searchCommon.searchCommon(page, paramMap));
    }

    /**
     * <pre>
     * 1. MethodName : findOnePost
     * 2. ClassName  : PostController.java
     * 3. Comment    : 게시글 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @ApiOperation(value = "게시글 상세 조회", notes = "게시글 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "게시글 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public PostDTO findOnePost(@PathVariable Long idx) {
        return postService.findOnePost(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertPost
     * 2. ClassName  : PostController.java
     * 3. Comment    : 게시글 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @ApiOperation(value = "게시글 등록", notes = "게시글 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "게시글 등록 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public PostDTO insertPost(@Valid @RequestBody PostEntity postEntity) {
        return postService.insertPost(postEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateNotice
     * 2. ClassName  : PostController.java
     * 3. Comment    : 게시글 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @ApiOperation(value = "게시글 수정", notes = "게시글 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "게시글 수정 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public PostDTO updatePost(@PathVariable Long idx, @Valid @RequestBody PostEntity postEntity) {
        if (postService.findOnePost(idx) == null) {
            throw new TravelException(NOT_FOUND_POST, new Throwable());
        } else {
            return postService.updatePost(postEntity);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deletePost
     * 2. ClassName  : PostController.java
     * 3. Comment    : 게시글 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @ApiOperation(value = "게시글 삭제", notes = "게시글 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "게시글 삭제 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public Long deletePost(@PathVariable Long idx) {
        return postService.deletePost(idx);
    }

    /**
     * <pre>
     * 1. MethodName : togglePopular
     * 2. ClassName  : PostController.java
     * 3. Comment    : 인기 게시글 설정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @ApiOperation(value = "게시글 고정글 설정", notes = "인기 게시글 설정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "인기 게시글 설정 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/toggle-popular")
    public Boolean togglePopular(@PathVariable Long idx) {
        return postService.togglePopular(idx);
    }
}
