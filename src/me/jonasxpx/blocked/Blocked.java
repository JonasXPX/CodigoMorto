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

public class Blocked extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	/**
	 * Bloqueia a construção do bloco COMMAND
	 * @param event
	 */
	@EventHandler
	public void commd(BlockPlaceEvent event) {
		if (event.getBlock().getType() == Material.COMMAND) {
			event.setCancelled(true);
		}
	}

	
	/**
	 * Removo todo tipo de enchant editado quando um
	 * jogador abre qualquer tipo de inventario
	 * @param event
	 */
	@EventHandler
	public void onOpen(InventoryOpenEvent event) {
		for (ItemStack i : event.getInventory().getContents()) {
			if (i == null) {
				return;
			}
			if (i.hasItemMeta()) {
				if (i.getItemMeta().hasEnchants()) {
					for (Enchantment enhant : i.getEnchantments().keySet()) {
						if (i.getEnchantments().get(enhant) > enhant.getMaxLevel()) {
							System.out.println("Enchant removido: "	+ enhant.getName() + " lv: "+ i.getEnchantments().get(enhant));
							i.removeEnchantment(enhant);
						}
					}
				}
			}
		}
	}

	
	/**
	 *  Remove todos os itens no bau durante 
	 *  o carregamento de uma chunk
	 * @param event
	 */
	@EventHandler
	public void onLoad(ChunkLoadEvent event) {
		Chunk chunk = event.getChunk();
		for (BlockState bState : chunk.getTileEntities()) {
			if (bState.getType() == Material.COMMAND) {
				bState.setType(Material.AIR);
			}
			if (bState instanceof Chest) {
				Chest chest = (Chest) bState;
				for (ItemStack item : chest.getInventory().getContents()) {
					if (item == null) {
						return;
					}
					if (!item.hasItemMeta()) {
						return;
					}
					if (!item.getItemMeta().hasEnchants()) {
						return;
					}
					for (Enchantment enhant : item.getEnchantments().keySet()) {
						if (item.getEnchantments().get(enhant) > enhant.getMaxLevel()) {
							System.out.println("Enchant removido: "+ enhant.getName() + " lv: "+ item.getEnchantments().get(enhant));
							item.removeEnchantment(enhant);
						}
					}

				}
			}
		}
	}
}
