package com.seko0716.es.plugin.pipeline.utils;

public class Job<L, R> {
    private final L left;
    private final R Right;

    public Job(L left, R right) {
        this.left = left;
        Right = right;
    }

    public static <L, R> Job<L, R> of(L left, R right) {
        return new Job<>(left, right);
    }

    public L getJobDetail() {
        return left;
    }

    public R getTrigger() {
        return Right;
    }
}
