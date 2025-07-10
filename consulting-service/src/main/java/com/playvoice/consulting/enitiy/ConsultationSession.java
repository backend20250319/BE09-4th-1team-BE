package com.playvoice.consulting.enitiy;

import com.playvoice.consulting.dto.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;
    private String userId;
    private String managerId;
    private LocalDateTime localDateTime;
    private LocalDate consultationDate;
    private LocalDateTime reservationTime;
    private LocalDateTime cancelTime;
    private String consultationText;
    private Long review;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;
}
