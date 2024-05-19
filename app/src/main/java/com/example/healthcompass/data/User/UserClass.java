package com.example.healthcompass.data.User;

public class UserClass {
    String name, username, password, gender;
    int age;
    float height, weight;
    String lastActivityDate;
    int currentStreak;
    int longestStreak;
    double totalDistance;
    Milestones milestones;

    public UserClass() {
        this.milestones = new Milestones();
    }

    public UserClass(String username, String password) {
        this.username = username;
        this.password = password;
        this.milestones = new Milestones();
    }

    public UserClass(String name, String username, String password, String gender, int age, float height, float weight) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.milestones = new Milestones();
    }

    // Getters and Setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(String lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Milestones getMilestones() {
        return milestones;
    }

    public void setMilestones(Milestones milestones) {
        this.milestones = milestones;
    }

    public static class Milestones {
        StreakMilestones streaks;
        WalkingDistanceMilestones walking;
        RunningDistanceMilestones running;

        public Milestones() {
            this.streaks = new StreakMilestones();
            this.walking = new WalkingDistanceMilestones();
            this.running = new RunningDistanceMilestones();
        }

        public StreakMilestones getStreaks() {
            return streaks;
        }

        public void setStreaks(StreakMilestones streaks) {
            this.streaks = streaks;
        }

        public WalkingDistanceMilestones getWalking() {
            return walking;
        }

        public void setWalking(WalkingDistanceMilestones walking) {
            this.walking = walking;
        }

        public RunningDistanceMilestones getRunning() {
            return running;
        }

        public void setRunning(RunningDistanceMilestones running) {
            this.running = running;
        }
    }

    public static class StreakMilestones {
        boolean days3Streak;
        boolean days7Streak;
        boolean days30Streak;

        public boolean isDays3Streak() {
            return days3Streak;
        }

        public void setDays3Streak(boolean days3Streak) {
            this.days3Streak = days3Streak;
        }

        public boolean isDays7Streak() {
            return days7Streak;
        }

        public void setDays7Streak(boolean days7Streak) {
            this.days7Streak = days7Streak;
        }

        public boolean isDays30Streak() {
            return days30Streak;
        }

        public void setDays30Streak(boolean days30Streak) {
            this.days30Streak = days30Streak;
        }
    }

    public static class WalkingDistanceMilestones {
        boolean km10;
        boolean km50;
        boolean km100;

        public boolean isKm10() {
            return km10;
        }

        public void setKm10(boolean km10) {
            this.km10 = km10;
        }

        public boolean isKm50() {
            return km50;
        }

        public void setKm50(boolean km50) {
            this.km50 = km50;
        }

        public boolean isKm100() {
            return km100;
        }

        public void setKm100(boolean km100) {
            this.km100 = km100;
        }
    }

    public static class RunningDistanceMilestones {
        boolean km5;
        boolean km25;
        boolean km50;

        public boolean isKm5() {
            return km5;
        }

        public void setKm5(boolean km5) {
            this.km5 = km5;
        }

        public boolean isKm25() {
            return km25;
        }

        public void setKm25(boolean km25) {
            this.km25 = km25;
        }

        public boolean isKm50() {
            return km50;
        }

        public void setKm50(boolean km50) {
            this.km50 = km50;
        }
    }
}
