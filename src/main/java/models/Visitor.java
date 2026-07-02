package models;

import java.time.LocalDate;

public class Visitor extends Person {
    private LocalDate firstTimeVisitDate;
    private int invitedById;
    private FollowUpStatus followUpStatus;


    public Visitor(int id, String firstName, String lastName, String email, String phoneNumber,
                   String residentialArea, String personRole, LocalDate firstTimeVisitDate,
                   int invitedById, FollowUpStatus followUpStatus) {

        super(id, firstName, lastName, email, phoneNumber, residentialArea, personRole);

        this.firstTimeVisitDate = firstTimeVisitDate;
        this.invitedById = invitedById;
        this.followUpStatus = followUpStatus != null ? followUpStatus : FollowUpStatus.PENDING;
    }

    public LocalDate getFirstTimeVisitDate() {
        return firstTimeVisitDate;
    }

    public void setFirstTimeVisitDate(LocalDate firstTimeVisitDate) {
        this.firstTimeVisitDate = firstTimeVisitDate;
    }

    public int getInvitedById() {
        return invitedById;
    }

    public void setInvitedById(int invitedById) {
        this.invitedById = invitedById;
    }

    public FollowUpStatus getFollowUpStatus() {
        return followUpStatus;
    }

    public void setFollowUpStatus(FollowUpStatus followUpStatus) {
        this.followUpStatus = followUpStatus;
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "id=" + getId() +
                ", name='" + getFullName() + '\'' +
                ", status=" + followUpStatus +
                ", visited=" + firstTimeVisitDate +
                '}';
    }
}