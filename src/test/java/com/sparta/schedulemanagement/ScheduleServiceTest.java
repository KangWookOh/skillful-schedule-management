//package com.sparta.schedulemanagement;
//import java.time.LocalDateTime;
//
//import com.sparta.schedulemanagement.dto.schedule.ScheduleRequestDto;
//import com.sparta.schedulemanagement.dto.schedule.ScheduleResponseDto;
//import com.sparta.schedulemanagement.entity.Schedule;
//import com.sparta.schedulemanagement.entity.User;
//import com.sparta.schedulemanagement.repository.ScheduleRepository;
//import com.sparta.schedulemanagement.repository.UserRepository;
//import com.sparta.schedulemanagement.service.schedule.ScheduleServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//
//public class ScheduleServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private ScheduleRepository scheduleRepository;
//
//    @InjectMocks
//    private ScheduleServiceImpl scheduleService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateScheduleWithFromMethod() {
//        // Given
//        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto();
//        scheduleRequestDto.setTitle("Test Schedule");
//        scheduleRequestDto.setContents("This is a test schedule.");
//        scheduleRequestDto.setOwnerId(1L);
//
//        User user = new User();
//        user.setUid(1L);
//
//        when(userRepository.findByIdOrElseThrow(anyLong())).thenReturn(user);
//        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // When
//        LocalDateTime beforeCreate = LocalDateTime.now();
//        long startTime = System.nanoTime();
//        ScheduleResponseDto responseDto = scheduleService.createSchedule(scheduleRequestDto);
//        long endTime = System.nanoTime();
//        LocalDateTime afterCreation = LocalDateTime.now();
//
//        // Then
//        assertNotNull(responseDto);
//        System.out.println("Execution time for testCreateScheduleWithFromMethod: " + (endTime - startTime) + " nanoseconds");
//    }
//    @Test
//    void testCreateScheduleWithOfMethod() {
//        // Given
//        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto();
//        scheduleRequestDto.setTitle("Test Schedule");
//        scheduleRequestDto.setContents("This is a test schedule.");
//        scheduleRequestDto.setOwnerId(1L);
//
//        User user = new User();
//        user.setUid(1L);
//
//        for(int i=0; i< 100000; i++){
//        when(userRepository.findByIdOrElseThrow(anyLong())).thenReturn(user);
//        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // When
//        LocalDateTime beforeCreation = LocalDateTime.now();
//        long startTime = System.nanoTime();
//        ScheduleResponseDto responseDto = scheduleService.createSchedule(scheduleRequestDto);
//        long endTime = System.nanoTime();
//        LocalDateTime afterCreation = LocalDateTime.now();
//
//        // Then
//        assertNotNull(responseDto);
//        System.out.println("Execution time for testCreateScheduleWithOfMethod: " + (endTime - startTime) + " nanoseconds");
//        }
//    }
//
//}
