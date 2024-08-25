package com.sparta.schedulemanagement.controller.reply;

import com.sparta.schedulemanagement.common.response.ApiResponse;
import com.sparta.schedulemanagement.common.util.LoggerUtil;
import com.sparta.schedulemanagement.dto.reply.ReplyRequestDto;
import com.sparta.schedulemanagement.dto.reply.ReplyResponseDto;
import com.sparta.schedulemanagement.service.reply.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/schedules/")
@RequiredArgsConstructor
@Slf4j
public class ReplyController {
    private final ReplyService replyService;

    /**
     * 새로운 댓글을 생성합니다.
     *
     * @param sid 댓글이 달릴 일정의 ID
     * @param replyRequestDto 댓글 요청 DTO
     * @return 조회된 댓글 응답 DTO를 포함한 HTTP 상태 코드 200 (OK)
     */
    @PostMapping("{sid}/replies")
    public ResponseEntity<ApiResponse<ReplyResponseDto>> createReply(@PathVariable long sid, @RequestBody ReplyRequestDto replyRequestDto){

        try {
            ReplyResponseDto createReply = replyService.addReply(sid, replyRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(createReply));
        } catch (Exception e) {
            LoggerUtil.logError("댓글 생성 실패: {}", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("REPLY_CREATION_FAILED","댓글 생성 실패: " + e.getMessage()));
        }
    }

    /**
     * ID로 특정 댓글을 조회합니다.
     *
     * @param rid 댓글의 ID
     * @return 조회된 댓글 응답 DTO를 포함한 HTTP 상태 코드 200 (OK)
     */
    @GetMapping("/replies/{rid}")
    public ResponseEntity<ApiResponse<ReplyResponseDto>> getReply(@PathVariable long rid){
        try {
            ReplyResponseDto reply = replyService.getReply(rid)
                    .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));
            return ResponseEntity.ok(ApiResponse.success(reply));
        }
        catch (NoSuchElementException e){
            LoggerUtil.logError("댓글 조회 실패: {}", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("REPLY_NOT_FOUND", e.getMessage()));
        }

    }

    /**
     * 일정 ID로 모든 댓글을 조회합니다.
     *
     * @param sid 일정의 ID
     * @return 조회된 댓글 응답 DTO 목록을 포함한 HTTP 상태 코드 200 (OK)
     */
    @GetMapping("/{sid}/replies")
    public ResponseEntity<ApiResponse<List<ReplyResponseDto>>> getReplies(@PathVariable long sid){
        try {
            List<ReplyResponseDto> replies = replyService.getReplies(sid);
            return ResponseEntity.ok(ApiResponse.success(replies));
        } catch (Exception e) {
            LoggerUtil.logError("댓글 목록 조회 실패: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("REPLIES_RETRIEVAL_FAILED", "댓글 목록 조회 중 오류가 발생했습니다."));
        }
    }

    /**
     * 특정 댓글을 업데이트합니다.
     *
     * @param sid 댓글이 달릴 일정의 ID
     * @param rid 댓글의 ID
     * @param replyRequestDto 업데이트할 댓글 요청 DTO
     * @return 업데이트된 댓글 응답 DTO와 HTTP 상태 코드 200 (OK)
     */
    @PutMapping("/{sid}/replies/{rid}")
    public ResponseEntity<ApiResponse<ReplyResponseDto>> updateReply(@PathVariable long sid,
                                                        @PathVariable long rid,
                                                        @RequestBody ReplyRequestDto replyRequestDto){
        try {
            ReplyResponseDto updateReply = replyService.updateReply(rid, replyRequestDto);
            return ResponseEntity.ok(ApiResponse.success(updateReply));
        } catch (Exception e) {
            LoggerUtil.logError("댓글 업데이트", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("댓글 업데이트 실패", e.getMessage()));
        }
    }

    /**
     * 특정 댓글을 삭제합니다.
     * @param rid 댓글의 ID
     * @param sid 댓글이 달릴 일정의 ID
     * @return HTTP 상태 코드 204 (No Content)
     */
    @DeleteMapping("/{sid}/replies/{rid}")
    public ResponseEntity<ApiResponse<Void>> deleteReply(@PathVariable long rid,@PathVariable long sid){
        try {
            replyService.deleteReply(rid);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            LoggerUtil.logError("댓글 삭제", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("댓글 삭제 실패", e.getMessage()));
        }
    }
}
