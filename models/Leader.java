package models;

public class Leader extends Person {
    private LeaderTier leaderTier;



    public Leader(int id, String firstName, String lastName, String email,
                  String phoneNumber, String residentialArea, String personRole,
                  LeaderTier leaderTier) {
        super(id, firstName, lastName, email, phoneNumber, residentialArea, personRole);
        this.leaderTier = leaderTier != null ? leaderTier : LeaderTier.TRAINEE; // Safe default
    }


    public LeaderTier getLeaderTier() {
        return leaderTier;
    }


    public void setLeaderTier(LeaderTier leaderTier) {
        this.leaderTier = leaderTier;
    }

    @Override
    public String toString() {
        return "Leader{" +
                ", id=" + getId() +
                ", name='" + getFirstName() + " " + getLastName() + '\'' +
                ", tier=" + leaderTier +
                '}';
    }
}