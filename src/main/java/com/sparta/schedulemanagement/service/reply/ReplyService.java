package com.sparta.schedulemanagement.service.reply;

import com.sparta.schedulemanagement.dto.reply.ReplyRequestDto;
import com.sparta.schedulemanagement.dto.reply.ReplyResponseDto;

import java.util.List;
import java.util.Optional;

public interface ReplyService {
    ReplyResponseDto addReply(Long sid,ReplyRequestDto replyRequestDto);
    Optional<ReplyResponseDto> getReply(Long rid);
    List<ReplyResponseDto> getReplies(Long sid);
    ReplyResponseDto updateReply(Long rid,ReplyRequestDto replyRequestDto);
    void deleteReply(Long rid);
}
