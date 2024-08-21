package com.sparta.schedulemanagement.Service.Reply;

import com.sparta.schedulemanagement.Dto.Reply.ReplyRequestDto;
import com.sparta.schedulemanagement.Dto.Reply.ReplyResponseDto;
import com.sparta.schedulemanagement.Dto.Schedule.ScheduleResponseDto;

import java.util.List;
import java.util.Optional;

public interface ReplyService {
    ReplyResponseDto addReply(Long sid,ReplyRequestDto replyRequestDto);
    Optional<ReplyResponseDto> getReplyById(Long rid);
    List<ReplyResponseDto> getRepliesByScheduleId(Long sid);
    ReplyResponseDto updateReply(Long rid,ReplyRequestDto replyRequestDto);
    void deleteReply(Long rid);
}
