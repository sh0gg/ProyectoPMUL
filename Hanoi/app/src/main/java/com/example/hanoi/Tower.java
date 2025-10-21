package com.example.hanoi;

import java.util.Stack;

public class Tower {
    private Stack<Integer> stack;
    private final int capacity;

    public Tower(int capacity) {
        this.capacity = capacity;
        this.stack = new Stack<>();
    }

    public boolean push(int piece) {
        if (stack.size() >= capacity) return false;
        if (!stack.isEmpty() && stack.peek() <= piece) return false;
        stack.push(piece);
        return true;
    }

    public Integer pop() {
        if (stack.isEmpty()) return null;
        return stack.pop();
    }

    public Integer peek() {
        if (stack.isEmpty()) return null;
        return stack.peek();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }

    public Stack<Integer> getStack() {
        return stack;
    }
}
