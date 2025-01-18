package net.modekh.itemguess.handlers;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.*;;

public class ItemGuessDatabase {
    private final Connection connection;

    public ItemGuessDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS itemguess (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "score INTEGER NOT NULL DEFAULT 0, " +
                    "item TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void addPlayerData(Player player) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO itemguess (uuid, score, item) " +
                        "VALUES (?, 0, '')")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPlayerScore(Player player) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT score FROM itemguess WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0; // initial score for all
    }

    public boolean addItemToGuess(Player player, String itemId) {
        if (getMaterial(itemId) == null) {
            return false;
        }

        try (PreparedStatement statement = connection.prepareStatement("UPDATE itemguess " +
                "SET item = ? WHERE uuid = ?")) {
            statement.setString(1, itemId);
            statement.setString(2, player.getUniqueId().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean guessItem(Player opponent, String itemId) {
        String opponentItemId = getPlayerItem(opponent);

        if (getMaterial(itemId) == null || opponentItemId == null || opponentItemId.isEmpty()) {
            return false;
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM itemguess WHERE uuid = ? AND item = ?")) {
            statement.setString(1, opponent.getUniqueId().toString());
            statement.setString(2, itemId);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getPlayerItem(Player player) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT item FROM itemguess WHERE uuid = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("item");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    public boolean isItemGuessed(Player player) {
        return getPlayerItem(player).equals("guessed");
    }

    public void resetDatabase() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM itemguess");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Material getMaterial(String itemId) {
        itemId = "minecraft:" + itemId;
        return Material.matchMaterial(itemId);
    }
}
