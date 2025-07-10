package com.playvoice.commentservice.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_comment_like", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "comment_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
}
