package models;

public class Leader extends Person {
    private LeaderTier leaderTier;



    public Leader(int id, String firstName, String lastName, String email,
                  String phoneNumber, String residentialArea, String personRole) {
        super(firstName, lastName, email, phoneNumber, residentialArea, personRole);
        this.leaderTier = LeaderTier.TRAINEE; // Safe default
    }


    public LeaderTier getLeaderTier() {
        return leaderTier;
    }


    public void setLeaderTier(LeaderTier leaderTier) {
        this.leaderTier = leaderTier;
    }
}