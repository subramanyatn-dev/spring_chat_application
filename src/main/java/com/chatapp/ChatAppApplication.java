package com.chatapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.chatapp.entity.User;
import com.chatapp.repository.UserRepository;

import java.util.Optional;

@SpringBootApplication
public class ChatAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatAppApplication.class, args);
    }

    @Bean
    CommandLineRunner testDatabase(UserRepository userRepository) {
        return args -> {
            System.out.println("🚀 Starting Chat App...");
            System.out.println("📊 Testing database connection...");

            try {
                // Test database connection by counting users
                long userCount = userRepository.count();
                System.out.println("✅ Database connected successfully!");
                System.out.println("📈 Total users in database: " + userCount);

                // Look for specific test user using Optional
                Optional<User> userOptional = userRepository.findByUsername("chat_user");

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    System.out.println("✅ Found user: " + user.getUsername() + " - " + user.getDisplayName());
                    System.out.println("   User ID: " + user.getUserId());
                    System.out.println("   Created: " + user.getCreatedAt());
                } else {
                    System.out.println("💡 Creating a test user...");

                    // Create a test user if none exists
                    User newUser = new User();
                    newUser.setUsername("chat_user");
                    newUser.setPasswordHash("chat_password123");
                    newUser.setDisplayName("Chat Test User");

                    userRepository.save(newUser);
                    System.out.println("✅ Created test user: chat_user");

                    // Verify the user was created
                    userCount = userRepository.count();
                    System.out.println("📈 Total users now: " + userCount);
                }

            } catch (Exception e) {
                System.out.println("❌ Database connection failed: " + e.getMessage());
                System.out.println("💡 Check if MySQL is running and database 'chat_app' exists");
                e.printStackTrace();
            }
        };
    }
}