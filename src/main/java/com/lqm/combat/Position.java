package com.lqm.combat;

/**
 * Vị trí trong bản đồ game. Dùng Java 17 record để có equality, hashCode, toString miễn phí.
 */
public record Position(double x, double y) {}