package com.travel.travel_project.api.post;

import com.travel.travel_project.common.Paging;
import com.travel.travel_project.domain.common.EntityType;
import com.travel.travel_project.domain.post.PostDTO;
import com.travel.travel_project.domain.post.PostEntity;
import com.travel.travel_project.domain.post.image.PostImageDTO;
import com.travel.travel_project.domain.post.image.PostImageEntity;
import com.travel.travel_project.domain.post.reply.ReplyDTO;
import com.travel.travel_project.domain.post.reply.ReplyEntity;
import com.travel.travel_project.domain.travel.image.TravelImageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/post")
@Api(tags = "게시글 관련 API")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

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
            @ApiResponse(code = 200, message = "게시글 리스트 조회 성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<Page<PostDTO>> findPostList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok(postService.findPostList(paramMap, paging.getPageRequest(paging.getPageNum(), paging.getSize())));
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
            @ApiResponse(code = 200, message = "게시글 상세 조회 성공", response = PostDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public ResponseEntity<PostDTO> findOnePost(@PathVariable Long idx) {
        return ResponseEntity.ok(postService.findOnePost(idx));
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
            @ApiResponse(code = 201, message = "게시글 등록 성공", response = PostDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<PostDTO> insertPost(@Valid @RequestBody PostEntity postEntity) {
        return ResponseEntity.created(URI.create("")).body(postService.insertPost(postEntity));
    }

    /**
     * <pre>
     * 1. MethodName : insertPostImage
     * 2. ClassName  : PostController.java
     * 3. Comment    : 게시글 이미지 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @ApiOperation(value = "게시글 이미지 저장", notes = "게시글 이미지를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "게시글 이미지 등록성공", response = TravelImageDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/{idx}/images", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PostImageDTO>> insertPostImage(@PathVariable Long idx, @RequestParam(value = "images") List<MultipartFile> fileName) {
        return ResponseEntity.created(URI.create("")).body(postService.insertPostImage(idx, fileName, PostImageEntity.builder().entityType(EntityType.POST).build()));
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
            @ApiResponse(code = 200, message = "게시글 수정 성공", response = PostDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long idx, @Valid @RequestBody PostEntity postEntity) {
        return ResponseEntity.ok(postService.updatePost(idx, postEntity));
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
            @ApiResponse(code = 204, message = "게시글 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deletePost(@PathVariable Long idx) {
        postService.deletePost(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : insertReply
     * 2. ClassName  : PostController.java
     * 3. Comment    : 게시글 댓글 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @ApiOperation(value = "게시글 댓글 등록", notes = "게시글 댓글 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "게시글 댓글 등록 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/{idx}/reply")
    public ResponseEntity<ReplyDTO> insertReply(@PathVariable Long idx, @Valid @RequestBody ReplyEntity replyEntity) {
        return ResponseEntity.created(URI.create("")).body(postService.insertReply(idx, replyEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateReply
     * 2. ClassName  : PostController.java
     * 3. Comment    : 게시글 댓글 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @ApiOperation(value = "게시글 댓글 수정", notes = "게시글 댓글 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "게시글 댓글 수정 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/reply")
    public ResponseEntity<ReplyDTO> updateReply(@PathVariable Long idx, @Valid @RequestBody ReplyEntity replyEntity) {
        return ResponseEntity.ok(postService.updateReply(idx, replyEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteReply
     * 2. ClassName  : PostController.java
     * 3. Comment    : 게시글 댓글 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @ApiOperation(value = "게시글 댓글 삭제", notes = "게시글 댓글 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "게시글 댓글 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}/reply")
    public ResponseEntity<Long> deleteReply(@PathVariable Long idx) {
        postService.deleteReply(idx);
        return ResponseEntity.noContent().build();
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
            @ApiResponse(code = 200, message = "인기 게시글 설정 성공", response = Boolean.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/toggle-popular")
    public ResponseEntity<Boolean> togglePopular(@PathVariable Long idx) {
        return ResponseEntity.ok(postService.togglePopular(idx));
    }
}
