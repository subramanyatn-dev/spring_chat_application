package com.chatapp.entity;

import java.io.Serializable;
import java.util.Objects;

public class GroupMemberId implements Serializable {
    private Integer group;  // matches the group field in GroupMember
    private Integer user;   // matches the user field in GroupMember

    public GroupMemberId() {}

    public GroupMemberId(Integer group, Integer user) {
        this.group = group;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMemberId that = (GroupMemberId) o;
        return Objects.equals(group, that.group) &&
               Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, user);
    }
}