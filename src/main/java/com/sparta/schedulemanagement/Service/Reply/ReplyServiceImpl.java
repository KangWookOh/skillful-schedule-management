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

    /**
     * 댓글을 추가하는 메서드입니다.
     * 일정 ID(sid)와 댓글 요청 DTO(replyRequestDto)를 받아서 댓글을 생성하고 저장합니다.
     * @param sid 일정 ID
     * @param replyRequestDto 댓글 요청 DTO
     * @return 생성된 댓글의 응답 DTO
     */
    @Override
    @Transactional
    public ReplyResponseDto addReply(Long sid, ReplyRequestDto replyRequestDto) {
        Schedule schedule = scheduleRepository.findById(sid)
                .orElseThrow(()-> new IllegalArgumentException("일정이 없습니다"));
        User owner = userRepository.findById(replyRequestDto.getOwnerId())
                .orElseThrow(()->new IllegalArgumentException("유저가 존재하지 않습니다. "));
        Reply reply = Reply.builder()
                .comment(replyRequestDto.getComment())
                .owner(owner)
                .schedule(schedule)
                .build();
        schedule.addReply(reply);
        replyRepository.save(reply);
        return ReplyResponseDto.from(reply);

    }

    /**
     * 댓글 ID로 특정 댓글을 조회하는 메서드입니다.
     * @param rid 댓글 ID
     * @return 댓글 응답 DTO(Optional)
     */

    @Override
    @Transactional(readOnly = true)
    public Optional<ReplyResponseDto> getReplyById(Long rid) {
        Reply reply =replyRepository.findById(rid)
                .orElseThrow(()->new IllegalArgumentException("댓글이 없습니다"));
        return Optional.of(ReplyResponseDto.from(reply));
    }

    /**
     * 일정 ID로 해당 일정에 달린 모든 댓글을 조회하는 메서드입니다.
     * @param sid 일정 ID
     * @return 댓글 응답 DTO 리스트
     */
    @Override
    public List<ReplyResponseDto> getRepliesByScheduleId(Long sid) {
        Schedule schedule = scheduleRepository.findById(sid)
                .orElseThrow(()->new IllegalArgumentException("일정을 찾을수 없습니다"));

        return schedule.getReplies().stream()
                .map(ReplyResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 댓글을 수정하는 메서드입니다.
     * 댓글 ID와 댓글 요청 DTO를 받아서 댓글을 수정하고 저장합니다.
     * @param rid 댓글 ID
     * @param replyRequestDto 댓글 요청 DTO
     * @return 수정된 댓글의 응답 DTO
     */
    @Override
    public ReplyResponseDto updateReply(Long rid, ReplyRequestDto replyRequestDto) {
        Reply reply =replyRepository.findById(rid)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을수 없습니다"));
        reply.replyUpdate(replyRequestDto);
        return ReplyResponseDto.from(replyRepository.save(reply));
    }

    /**
     * 댓글을 삭제하는 메서드입니다.
     * 댓글 ID로 해당 댓글을 조회하고 삭제합니다.
     * @param rid 댓글 ID
     */
    @Override
    public void deleteReply(Long rid) {
        Reply reply = replyRepository.findById(rid)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을수 없습니다"));
        replyRepository.delete(reply);
    }
}
