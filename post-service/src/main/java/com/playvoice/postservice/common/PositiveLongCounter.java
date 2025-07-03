package com.playvoice.postservice.common;

import lombok.Getter;

@Getter
public class PositiveLongCounter {

    private Long count;

    public PositiveLongCounter(Long count) {
        if (count < 0) {
            throw new IllegalArgumentException("수가 음수가 될 수 없습니다.");
        }
        this.count = count;
    }

    public PositiveLongCounter() {
        this.count = 0L;
    }

    public void increase() {
        this.count++;
    }

    public void decrease() {
        if (this.count <= 0) {
            throw new IllegalArgumentException("수가 음수가 될 수 없습니다.");
        }
        this.count--;
    }
}
