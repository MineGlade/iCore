package com.mineglade.icore.commands;

import com.mineglade.icore.ICore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.derkades.derkutils.bukkit.Chat;
import xyz.derkades.derkutils.bukkit.PlaceholderUtil.Placeholder;

public class VoteCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        Player player = (Player) sender;
        FileConfiguration config = ICore.instance.getConfig();

        if (args.length == 1 && args[0].equalsIgnoreCase("top")) {
            player.sendMessage("the /vote top command is not functional yet, please be patient.");
        }
        else {

            
            Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
            	final int voteCount;
            
            	try {
            		PreparedStatement statement = ICore.db.prepareStatement("SELECT `votes` FROM `votes` WHERE uuid=?", player.getUniqueId());
					ResultSet result = statement.executeQuery(); 
					
					if (result.next()) {
						voteCount = result.getInt("votes");
					} 
					else {
						voteCount = 0;
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
					return;
				}
            
            	Bukkit.getScheduler().runTask(ICore.instance, () -> {
            		Placeholder votes = new Placeholder("%votes%", (p) -> voteCount + "");
                    player.spigot().sendMessage(Chat.toComponentWithPlaceholders(config, "vote-response", player, votes));
            	});
            });
            
        }



        return true;
    }

}