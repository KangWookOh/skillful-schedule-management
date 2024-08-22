package com.sparta.schedulemanagement.Service.Reply;

import com.sparta.schedulemanagement.Dto.Reply.ReplyRequestDto;
import com.sparta.schedulemanagement.Dto.Reply.ReplyResponseDto;
import com.sparta.schedulemanagement.Entity.Reply;
import com.sparta.schedulemanagement.Entity.Schedule;
import com.sparta.schedulemanagement.Entity.User;
import com.sparta.schedulemanagement.Repository.ReplyRepository;
import com.sparta.schedulemanagement.Repository.ScheduleRepository;
import com.sparta.schedulemanagement.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository replyRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReplyResponseDto addReply(Long sid, ReplyRequestDto replyRequestDto) {
        Schedule schedule = scheduleRepository.findById(sid).orElseThrow(()-> new IllegalArgumentException("일정이 없습니다"));
        User owner = userRepository.findById(replyRequestDto.getOwnerId()).orElseThrow(()->new IllegalArgumentException("유저가 존재하지 않습니다. "));
        Reply reply = Reply.builder()
                .comment(replyRequestDto.getComment())
                .owner(owner)
                .schedule(schedule)
                .build();
        schedule.addReply(reply);
        replyRepository.save(reply);
        return ReplyResponseDto.from(reply);

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReplyResponseDto> getReplyById(Long rid) {
        Reply reply =replyRepository.findById(rid).orElseThrow(()->new IllegalArgumentException("댓글이 없습니다"));
        return Optional.of(ReplyResponseDto.from(reply));
    }

    @Override
    public List<ReplyResponseDto> getRepliesByScheduleId(Long sid) {
        Schedule schedule = scheduleRepository.findById(sid).orElseThrow(()->new IllegalArgumentException("일정을 찾을수 없습니다"));

        return schedule.getReplies().stream()
                .map(ReplyResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public ReplyResponseDto updateReply(Long rid, ReplyRequestDto replyRequestDto) {
        Reply reply =replyRepository.findById(rid).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을수 없습니다"));
        reply.replyUpdate(replyRequestDto);
        return ReplyResponseDto.from(replyRepository.save(reply));
    }

    @Override
    public void deleteReply(Long rid) {
        Reply reply = replyRepository.findById(rid).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을수 없습니다"));
        replyRepository.delete(reply);
    }
}
