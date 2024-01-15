package sample;

import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO {

    private DatabaseConnection databaseConnection;

    public ChatDAO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void sendGroupMessage(ChatMessage message) {
        try (Connection connectDB = databaseConnection.getConnection();
             PreparedStatement statement = connectDB.prepareStatement(
                     "INSERT INTO ChatMessages (sender_id, group_id, message_text) VALUES (?, ?, ?)")) {
            statement.setInt(1, message.getSenderId());
            statement.setInt(2, message.getGroupId());
            statement.setString(3, message.getMessageText());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ChatMessage> loadChatHistory(int groupId) {
        List<ChatMessage> groupChatHistory = new ArrayList<>();

        try (Connection connectDB = databaseConnection.getConnection();
             PreparedStatement statement = connectDB.prepareStatement(
                     "SELECT * FROM ChatMessages WHERE group_id = ?")) {
            statement.setInt(1, groupId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ChatMessage message = new ChatMessage();
                    message.setMessageId(resultSet.getInt("message_id"));
                    message.setSenderId(resultSet.getInt("sender_id"));
                    message.setGroupId(resultSet.getInt("group_id"));
                    message.setMessageText(resultSet.getString("message_text"));
                    message.setTimestamp(resultSet.getTimestamp("timestamp"));

                    groupChatHistory.add(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupChatHistory;
    }
}
