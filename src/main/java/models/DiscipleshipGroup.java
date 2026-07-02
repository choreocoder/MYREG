package models;

import models.GroupType;

public class DiscipleshipGroup {
    private int groupId;
    private String groupName;
    private String residentialArea;
    private int leaderId;
    private GroupType groupType;


    public DiscipleshipGroup(int groupId, String groupName, String residentialArea, int leaderId){
        this.groupId = groupId;
        this.groupName = groupName;
        this.residentialArea = residentialArea;
        this.leaderId = leaderId;
        this.groupType = GroupType.MIXED;

    }

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName(){
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getResidentialArea(){
        return residentialArea;
    }

    public void setResidentialArea(String residentialArea) {
        this.residentialArea = residentialArea;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    @Override
    public String toString() {
        return "DiscipleshipGroup{" +
                "id=" + groupId +
                ", name='" + groupName + '\'' +
                ", area='" + residentialArea + '\'' +
                ", type=" + groupType +
                '}';
    }
}
