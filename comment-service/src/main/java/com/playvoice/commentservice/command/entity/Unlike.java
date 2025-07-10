package com.playvoice.commentservice.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_comment_unlike")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unlike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
}