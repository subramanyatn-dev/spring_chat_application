package com.chatapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.chatapp.entity.Group;
import com.chatapp.entity.Message;
import com.chatapp.entity.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    // Find direct messages between two users
    List<Message> findBySenderAndReceiverOrderByCreatedAtDesc(User sender, User receiver);
    
    // Find messages in a group
    List<Message> findByGroupOrderByCreatedAtDesc(Group group);
    
    // Find unread messages for a user
    List<Message> findByReceiverAndIsReadFalseOrderByCreatedAtDesc(User receiver);
    
    // Find messages paginated (for lazy loading in chat)
    Page<Message> findBySenderAndReceiver(User sender, User receiver, Pageable pageable);
    
    // Find group messages paginated
    Page<Message> findByGroup(Group group, Pageable pageable);
    
    // Custom query to get messages by user ID (for user joining/leaving)
    @Query("SELECT m FROM Message m WHERE m.sender.userId = ?1")
    List<Message> findMessagesBySenderId(Integer senderId);
    
    // Get messages between two users (in both directions) ordered by timestamp
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.userId = ?1 AND m.receiver.userId = ?2) OR " +
           "(m.sender.userId = ?2 AND m.receiver.userId = ?1) " +
           "ORDER BY m.createdAt ASC")
    List<Message> findMessagesBewtweenUsers(Integer userId1, Integer userId2);
    
    // Custom query to find latest messages for a user (both direct and group messages)
    @Query("SELECT m FROM Message m WHERE m.sender = :user OR m.receiver = :user OR m.group IN (SELECT gm.group FROM GroupMember gm WHERE gm.user = :user) ORDER BY m.createdAt DESC")
    Page<Message> findLatestMessagesForUser(User user, Pageable pageable);
}