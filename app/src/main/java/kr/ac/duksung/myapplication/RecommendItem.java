package kr.ac.duksung.myapplication;

import java.util.List;

public class RecommendItem {
    private User user1;
    private User user2;
    private User user3;

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public User getUser3() {
        return user3;
    }

    public static class User {
        private String username;
        private List<Integer> numbers;

        public String getUsername() {
            return username;
        }

        public List<Integer> getNumbers() {
            return numbers;
        }
    }
}
