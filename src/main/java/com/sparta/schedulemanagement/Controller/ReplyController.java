package com.sparta.schedulemanagement.Controller;

import com.sparta.schedulemanagement.Dto.Reply.ReplyRequestDto;
import com.sparta.schedulemanagement.Dto.Reply.ReplyResponseDto;
import com.sparta.schedulemanagement.Service.Reply.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules/{sid}/replies")
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
    @PostMapping
    public ResponseEntity<ReplyResponseDto> createReply(@PathVariable Long sid, @RequestBody ReplyRequestDto replyRequestDto){
        ReplyResponseDto createReply = replyService.addReply(sid,replyRequestDto);
        log.info("createReply:{}", createReply);
        return ResponseEntity.ok().body(createReply);
    }

    /**
     * ID로 특정 댓글을 조회합니다.
     *
     * @param rid 댓글의 ID
     * @return 조회된 댓글 응답 DTO를 포함한 HTTP 상태 코드 200 (OK)
     */
    @GetMapping("/{rid}")
    public ResponseEntity<Optional<ReplyResponseDto>> getReplyById(@PathVariable Long rid){
        Optional<ReplyResponseDto> reply = replyService.getReplyById(rid);
        log.info("getReplyById:{}", reply);
        return ResponseEntity.ok().body(reply);
    }

    /**
     * 일정 ID로 모든 댓글을 조회합니다.
     *
     * @param sid 일정의 ID
     * @return 조회된 댓글 응답 DTO 목록을 포함한 HTTP 상태 코드 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<ReplyResponseDto>> getAllReply(@PathVariable Long sid){
        List<ReplyResponseDto> replise = replyService.getRepliesByScheduleId(sid);
        log.info("getAllReply:{}", replise);
        return ResponseEntity.ok().body(replise);
    }

    /**
     * 특정 댓글을 업데이트합니다.
     *
     * @param sid 댓글이 달릴 일정의 ID
     * @param rid 댓글의 ID
     * @param replyRequestDto 업데이트할 댓글 요청 DTO
     * @return 업데이트된 댓글 응답 DTO와 HTTP 상태 코드 200 (OK)
     */
    @PutMapping("/{rid}")
    public ResponseEntity<ReplyResponseDto> updateReply(@PathVariable Long sid,
                                                        @PathVariable Long rid,
                                                        @RequestBody ReplyRequestDto replyRequestDto){
        ReplyResponseDto updateReply =replyService.updateReply(rid, replyRequestDto);
        log.info("updateReply:{}", updateReply);
        return ResponseEntity.ok().body(updateReply);
    }

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param rid 댓글의 ID
     * @param sid 댓글이 달릴 일정의 ID
     * @return HTTP 상태 코드 204 (No Content)
     */
    @DeleteMapping("delete/{rid}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long rid,@PathVariable Long sid){
        replyService.deleteReply(rid);
        log.info("deleteReply:{}", rid);
        return ResponseEntity.noContent().build();
    }
}
