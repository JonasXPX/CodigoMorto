package me.jonasxpx.blocked;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Blocked extends JavaPlugin implements Listener{

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	
	@EventHandler
	public void commd(BlockPlaceEvent e){
		if(e.getBlock().getType() == Material.COMMAND){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onOpen(InventoryOpenEvent e){
		for(ItemStack i : e.getInventory().getContents()){
			if(i == null){
				return;
			}
			if(i.hasItemMeta()){
				if(i.getItemMeta().hasEnchants()){
					for(Enchantment en : i.getEnchantments().keySet()){
						if(i.getEnchantments().get(en) > en.getMaxLevel()){
							System.out.println("Enchant removido: " + en.getName() + " lv: " + i.getEnchantments().get(en));
							i.removeEnchantment(en);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onLoad(ChunkLoadEvent e){
		Chunk c = e.getChunk();
		for(BlockState b : c.getTileEntities()){
			if(b.getType() == Material.COMMAND){
				b.setType(Material.AIR);
			}
			if(b instanceof Chest){
				Chest chest = (Chest)b;
				for(ItemStack i : chest.getInventory().getContents()){
					if(i == null){
						return;
					}
					if(i.hasItemMeta()){
						if(i.getItemMeta().hasEnchants()){
							for(Enchantment en : i.getEnchantments().keySet()){
								if(i.getEnchantments().get(en) > en.getMaxLevel()){
									System.out.println("Enchant removido: " + en.getName() + " lv: " + i.getEnchantments().get(en));
									i.removeEnchantment(en);
								}
							}
						}
					}
				}
			}
		}
	}
}
