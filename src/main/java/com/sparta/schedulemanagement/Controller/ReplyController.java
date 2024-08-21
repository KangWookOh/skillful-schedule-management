package com.sparta.schedulemanagement.Controller;

import com.sparta.schedulemanagement.Dto.Reply.ReplyRequestDto;
import com.sparta.schedulemanagement.Dto.Reply.ReplyResponseDto;
import com.sparta.schedulemanagement.Service.Reply.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules/{sid}/replies")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<ReplyResponseDto> createReply(@PathVariable Long sid , @RequestBody ReplyRequestDto replyRequestDto){
        ReplyResponseDto createReply = replyService.addReply(sid, replyRequestDto);
        return ResponseEntity.status(201).body(createReply);
    }
    @GetMapping("/{rid}")
    public ResponseEntity<Optional<ReplyResponseDto>> getReplyById(@PathVariable Long rid){
        Optional<ReplyResponseDto> reply = replyService.getReplyById(rid);
        return ResponseEntity.ok(reply);
    }
    @GetMapping
    public ResponseEntity<List<ReplyResponseDto>> getAllReply(@PathVariable Long sid){
        List<ReplyResponseDto> replise = replyService.getRepliesByScheduleId(sid);
        return ResponseEntity.ok(replise);
    }
    @PutMapping("/{rid}")
    public ResponseEntity<ReplyResponseDto> updateReply(@PathVariable Long sid,
                                                        @PathVariable Long rid,
                                                        @RequestBody ReplyRequestDto replyRequestDto){
        ReplyResponseDto updateReply =replyService.updateReply(rid, replyRequestDto);
        return ResponseEntity.ok(updateReply);
    }

    @DeleteMapping("delete/{rid}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long rid,@PathVariable Long sid){
        replyService.deleteReply(rid);
        return ResponseEntity.noContent().build();
    }
}
