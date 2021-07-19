package com.codenjoy.dojo.snake.client;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Lee {

    private final List<LPoint> deltas = Arrays.asList(
            LPoint.of(0, -1), LPoint.of(-1, 0),
            LPoint.of(1, 0), LPoint.of(0, 1));
    private final int width;
    private final int height;
    private final int[][] board;
    private final static int EMPTY = 0;
    private final static int OBSTACLE = -9; // any non-positive number
    private final static int START = -1;    // any non-positive number

    public Lee(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new int[height][width];
    }

    private int get(LPoint p) {
        return board[p.y][p.x];
    }

    private void set(LPoint p, int val) {
        board[p.y][p.x] = val;
    }

    private boolean isOnBoard(LPoint p) {
        return p.x >= 0 && p.y >= 0 && p.x < width && p.y < height;
    }

    private boolean isUnvisited(LPoint p) {
        return get(p) == EMPTY;
    }

    private Stream<LPoint> neighbours(LPoint point) {
        return deltas.stream()
                .map(d -> d.move(point))
                .filter(this::isOnBoard);
    }

    private Set<LPoint> neighboursUnvisited(LPoint point) {
        return neighbours(point)
                .filter(this::isUnvisited)
                .collect(Collectors.toSet());
    }

    public List<LPoint> neighboursByValue(LPoint point, int val) {
        return neighbours(point)
                .filter(p -> get(p) == val)
                .collect(Collectors.toList());
    }


    private List<LPoint> neighboursByPositiveValue(LPoint point) {
        return neighbours(point)
                .filter(p -> get(p) >= EMPTY)
                .collect(Collectors.toList());
    }

    private void initializeBoard(List<LPoint> obstacles) {
        IntStream.range(0, width).boxed().flatMap(x ->
                IntStream.range(0, height).mapToObj(y ->
                        LPoint.of(x, y)
                )).forEach(p -> set(p, EMPTY));
        obstacles.forEach(p -> set(p, OBSTACLE));
    }

    public Optional<List<LPoint>> trace(LPoint start, LPoint finish, List<LPoint> obstacles) {
        initializeBoard(obstacles);
        boolean found = false;
        set(start, START);
        Set<LPoint> curr = new HashSet<>();
        curr.add(start);
        int[] counter = {0}; // int counter located in the STACK, int[] counter - in the HEAP
        // wave dissemination
        while (!(curr.isEmpty() || found)) {
            counter[0]++;
            Set<LPoint> next = curr.stream()
                    .map(this::neighboursUnvisited)  // Stream<Set<LPoint>>
                    .flatMap(Collection::stream)        // Stream<LPoint>
                    .collect(Collectors.toSet());
            next.forEach(p -> set(p, counter[0])); // inside lambda we can refer to HEAP ONLY
            found = next.contains(finish);
            //  printMe(Collections.emptyList());
            curr.clear();
            curr.addAll(next);
        }

        if (!found) {

            return Optional.empty();
        }

        return returnPath(start, finish, counter);
    }

    public Optional<List<LPoint>> returnPath(LPoint start, LPoint finish, int[] counter) {
        LinkedList<LPoint> path = new LinkedList<>();
        path.add(finish);
        set(start, 0);
        LPoint curr_p = finish;
        while (counter[0] > 0) {
            counter[0]--;
            LPoint prev_p = neighboursByValue(curr_p, counter[0]).get(0);// pick any point
            path.addFirst(prev_p);
            curr_p = prev_p;
        }
        return Optional.of(path);
    }


}
