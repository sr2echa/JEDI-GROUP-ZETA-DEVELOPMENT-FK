package com.flipfit.dao.impl;

import com.flipfit.bean.Admin;
import com.flipfit.bean.Customer;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.Role;
import com.flipfit.bean.User;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Class GymUserDAOImpl.
 * Implementation of GymUserDAO interface for managing user data operations.
 *
 * @author Zeta
 * @ClassName  "GymUserDAOImpl"
 */
public class GymUserDAOImpl implements GymUserDAO {

    /**
     * Register user.
     * Inserts a new user into the database along with role-specific details.
     *
     * @param user the user to register
     * @return true, if successful
     */
    @Override
    public boolean registerUser(User user) {
        Connection conn = DBConnection.getConnection();
        String sqlUser = "INSERT INTO User (userId, name, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUser)) {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getPassword());
                pstmt.setString(5, user.getRole().toString());
                pstmt.executeUpdate();

                if (user.getRole() == Role.GYM_OWNER) {
                    String sqlOwner = "INSERT INTO GymOwner (userId, panNumber, isApproved, gstNumber, aadharNumber, location) VALUES (?, ?, ?, ?, ?, ?)";
                    GymOwner owner = (GymOwner) user;
                    try (PreparedStatement pstmtOwner = conn.prepareStatement(sqlOwner)) {
                        pstmtOwner.setString(1, owner.getUserId());
                        pstmtOwner.setString(2, owner.getPanNumber());
                        pstmtOwner.setBoolean(3, owner.isApproved());
                        pstmtOwner.setString(4, owner.getGstNumber());
                        pstmtOwner.setString(5, owner.getAadharNumber());
                        pstmtOwner.setString(6, owner.getLocation());
                        pstmtOwner.executeUpdate();
                    }
                } else if (user.getRole() == Role.CUSTOMER) {
                    String sqlCustomer = "INSERT INTO Customer (userId) VALUES (?)";
                    try (PreparedStatement pstmtCustomer = conn.prepareStatement(sqlCustomer)) {
                        pstmtCustomer.setString(1, user.getUserId());
                        pstmtCustomer.executeUpdate();
                    }
                }

                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Login user.
     * Authenticates a user and retrieves their profile from the database.
     *
     * @param username the username
     * @param password the password
     * @return the user object if credentials are valid, null otherwise
     */
    @Override
    public User loginUser(String username, String password) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM User WHERE userId = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String roleStr = rs.getString("role");
                    Role role = Role.valueOf(roleStr);
                    User user;
                    if (role == Role.GYM_OWNER) {
                        GymOwner owner = new GymOwner();
                        // Set userId before calling loadGymOwnerDetails
                        owner.setUserId(rs.getString("userId"));
                        owner.setName(rs.getString("name"));
                        owner.setEmail(rs.getString("email"));
                        owner.setPassword(rs.getString("password"));
                        owner.setRole(role);
                        loadGymOwnerDetails(owner);
                        return owner;
                    } else if (role == Role.CUSTOMER) {
                        user = new Customer();
                    } else {
                        // Admin or other
                        user = new Admin();
                    }
                    user.setUserId(rs.getString("userId"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(role);
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load gym owner details.
     * Fetches additional gym owner specific information from the database.
     *
     * @param owner the gym owner object to populate with details
     */
    private void loadGymOwnerDetails(GymOwner owner) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM GymOwner WHERE userId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, owner.getUserId());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    owner.setPanNumber(rs.getString("panNumber"));
                    owner.setApproved(rs.getBoolean("isApproved"));
                    owner.setGstNumber(rs.getString("gstNumber"));
                    owner.setAadharNumber(rs.getString("aadharNumber"));
                    owner.setLocation(rs.getString("location"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Change password.
     * Updates the user's password after verifying the old password.
     *
     * @param username the username
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return true, if successful
     */
    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Connection conn = DBConnection.getConnection();
        String sql = "UPDATE User SET password = ? WHERE userId = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.setString(3, oldPassword);
            int updated = pstmt.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the user.
     * Retrieves a user by their user ID.
     *
     * @param userId the user id
     * @return the user object, or null if not found
     */
    @Override
    public User getUser(String userId) {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM User WHERE userId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all users.
     * Retrieves all users from the database.
     *
     * @return the list of all users
     */
    @Override
    public java.util.List<User> getAllUsers() {
        java.util.List<User> users = new java.util.ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM User";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Map user.
     * Maps a ResultSet row to a User object with the appropriate role type.
     *
     * @param rs the result set
     * @return the user object
     * @throws SQLException the SQL exception
     */
    private User mapUser(ResultSet rs) throws SQLException {
        String roleStr = rs.getString("role");
        Role role = Role.valueOf(roleStr);
        User user;
        if (role == Role.GYM_OWNER) {
            user = new GymOwner();
            user.setUserId(rs.getString("userId"));
            loadGymOwnerDetails((GymOwner) user);
        } else if (role == Role.CUSTOMER) {
            user = new Customer();
            user.setUserId(rs.getString("userId"));
        } else if (role == Role.ADMIN) {
            user = new Admin();
            user.setUserId(rs.getString("userId"));
        } else {
            user = new Customer(); // Default fallback
            user.setUserId(rs.getString("userId"));
        }
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(role);
        return user;
    }
}
