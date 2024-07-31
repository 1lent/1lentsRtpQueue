package com.brodi.rtpqueue1;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RTPQueueCommand implements CommandExecutor {
    private Main main;

    public RTPQueueCommand(Main main) {
        this.main = main;



    }



    List<UUID> playersInQueue = new ArrayList<>(); // this queue will at max contain 2 entries, you don't need a List


    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player player)) return false; // use jdk 17 for cool instant instanceOf casting
        if (playersInQueue.contains(player.getUniqueId())) {
            playersInQueue.remove(player.getUniqueId());
            player.sendMessage(ChatColor.RED + "You were removed from the queue.");
            return true;
        }


        playersInQueue.add(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "You were added to the queue.");
        if (playersInQueue.size() == 2) {
            Location loc = getRandomLocation();
            Player player1 = Bukkit.getPlayer(playersInQueue.get(0));
            Player player2 = Bukkit.getPlayer(playersInQueue.get(1));
            World world = loc.getWorld();
            int x = (int) loc.getX();
            int y = (int) loc.getY();
            int z = (int) loc.getZ();

            if (world.getBiomeProvider().getBiome(world, x, y, z) != Biome.OCEAN)  {
                player1.sendMessage(ChatColor.GREEN + "[i] Success");
                player2.sendMessage(ChatColor.GREEN + "[i] Success");
                player1.sendMessage(ChatColor.YELLOW + "You are being teleported"); // possibly switch to async teleports - this will pause the main thread
                player2.sendMessage(ChatColor.YELLOW + "You are being teleported");
                player1.teleport(loc);
                player2.teleport(loc);
                playersInQueue.remove(player1.getUniqueId());
                playersInQueue.remove(player2.getUniqueId());
            } else player1.sendMessage(ChatColor.RED + "[i] RtpQueue doesnt want you to spawn in an ocean");
            player2.sendMessage(ChatColor.RED + "[i] RtpQueue doesnt want you to spawn in an ocean");

        }
        return true; // don't return false, it will give the player the feeling that they misspelled the command
    }
    public Location getRandomLocation() {

        Random randomSource = new Random();
        World hopeFullyExistingDefaultWorld = Bukkit.getWorld("world"); // bad idea - rather make world names configurable!
        int zMin = main.getConfig().getInt("zMin");
        int zMax = main.getConfig().getInt("zMax");
        int xMin = main.getConfig().getInt("xMin");
        int xMax = main.getConfig().getInt("xMax");
        int randomX = randomSource.nextInt(xMax - xMin + 1) + xMin;
        int randomZ = randomSource.nextInt(zMax - zMin + 1) + zMin;
        int highestY = hopeFullyExistingDefaultWorld.getHighestBlockYAt(randomX, randomZ) + 3;
        return new Location(hopeFullyExistingDefaultWorld, randomX, highestY, randomZ).add(0, 2, 0);
    }}
